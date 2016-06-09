/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.html;

import com.greenpepper.Example;
import com.greenpepper.TextExample;
import com.greenpepper.document.Document;
import com.greenpepper.repository.DocumentBuilder;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.log.GreenPepperLogger;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>HtmlDocumentBuilder class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HtmlDocumentBuilder implements DocumentBuilder
{
    
    private static final Logger logger = GreenPepperLogger.getLogger(HtmlDocumentBuilder.class);
    
    
    private final List<String> tags;
    private final List<HtmlContentFilter> filters = new ArrayList<HtmlContentFilter>();

    /**
     * <p>tables.</p>
     *
     * @return a {@link com.greenpepper.html.HtmlDocumentBuilder} object.
     */
    public static HtmlDocumentBuilder tables()
    {
        return new HtmlDocumentBuilder( "table", "tr", "td th" );
    }

    /**
     * <p>tablesAndLists.</p>
     *
     * @return a {@link com.greenpepper.html.HtmlDocumentBuilder} object.
     */
    public static HtmlDocumentBuilder tablesAndLists()
    {
        return new HtmlDocumentBuilder( "table ul ol", "table>tr ol>li ul>li", "tr>td tr>th li>span li>b li>i li>u li>em" ).addFilter( new BulletListFilter() );
    }

    /**
     * <p>Constructor for HtmlDocumentBuilder.</p>
     *
     * @param tags a {@link java.lang.String} object.
     */
    public HtmlDocumentBuilder( String... tags )
    {
        this( Arrays.asList( tags ) );
    }

    /**
     * <p>Constructor for HtmlDocumentBuilder.</p>
     *
     * @param tags a {@link java.util.List} object.
     */
    public HtmlDocumentBuilder( List<String> tags )
    {
        if (tags.isEmpty()) throw new IllegalArgumentException( "Specify at least a tag" );
        this.tags = new ArrayList<String>( tags );
    }

    /** {@inheritDoc} */
    public Document build( Reader reader ) throws IOException
    {
        String html = IOUtil.readContent( reader );
        html = injectCss(html);
        Example example = parse( html );
        if (example == null) example = new TextExample( html );
        return Document.html( example, name( html ), externalLink( html ) );
    }

    /**
     * <p>parse.</p>
     *
     * @param html a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example parse( String html )
    {
        String text = removeComments( html );

		return doParse(text);
    }

    private String injectCss(String html) {
        Pattern pattern = Pattern.compile("(.*)(<body\\s*[^>]*>)(.*)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher bodyMatcher = pattern.matcher(html);
        if (bodyMatcher.matches()) {
            StringBuilder cssStyle = new StringBuilder(bodyMatcher.group(2));
            cssStyle.append("<style type=\"text/css\">");
            try {
                InputStream gpReportCssStream = getClass().getResourceAsStream("greenpepper-reports.css");
                InputStreamReader inputStreamReader = new InputStreamReader(gpReportCssStream);
                cssStyle.append(IOUtil.readContent(inputStreamReader));
                inputStreamReader.close();
                cssStyle.append("</style>");
                
                String modified = bodyMatcher.group(1) + cssStyle + bodyMatcher.group(3);
                logger.trace("Html with Injected CSS\n {}", modified);
                return modified;
            } catch (IOException e) {
                throw new RuntimeException("Couldn't inject the report css in the document ",e);
            }
        } else {
            return html;
        }
    }
    
	private Example doParse(String text) {

		if (pathologicalCase(text)) return null;

		Matcher match = match( text );
		if (!match.find()) return null;

		Example child = hasChild( match ) ? childParser( match ).doParse( content( match ) ) : null;
		Example sibling = hasSibling( match ) ? doParse( tail( match ) ) : null;

		return new HtmlExample(
			lead( match ),
			startTag( match ),
			tag( match ),
			content( match ),
			endTag( match ),
			tail( match ),
			childTags( match ),
			child,
			sibling );
	}

	/**
    * Most regex implementations today do not build a DFA / NFA --
    * especially those that offer backreferences (which are not "regular" at all).
    *
    * And because they do NOT build DFAs and NFAs, it's very simple to construct pathological cases
    * - e.g., "((a*)(a*))+b" can take exponentially long to decide that
    * aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaac is not in the language
    * using the matching techniques commonly in use.
    *
    *    Ori Berger
    */
    private boolean pathologicalCase(String text)
    {
        String tag = toRegex( CollectionUtil.first( tags ));
        String regex = String.format( "(?is)(<\\s*(%s)\\s*.*?>)", tag );
        Matcher match  = compile(regex).matcher(text);

        if (match.find())
        {
            regex = String.format( "(?is)(.*?)(<\\s*/\\s*(%s)\\s*.*?>)", tag );
            return !compile(regex).matcher(text).find(match.end());
        }

        return true;
    }

    private Matcher match( String text )
    {
        return compile( firstTag( tags ) ).matcher( text );
    }

    private boolean hasSibling( Matcher match )
    {
        return tail( match ).matches( firstTag( tags ) );
    }

    private HtmlDocumentBuilder childParser( Matcher match )
    {
        return new HtmlDocumentBuilder( childTags( match ) ).addAllFilters( filters );
    }

    private boolean hasChild( Matcher match )
    {
        return !childTags( match ).isEmpty() && content( match ).matches( firstTag( childTags( match ) ) );
    }

    private List<String> childTags( Matcher matcher )
    {
        List<String> moreTags = new ArrayList<String>();
        for (int i = 1; i < tags.size(); i++)
        {
            moreTags.add( stripSelector( tag( matcher ), tags.get( i ) ) );
        }
        return moreTags;
    }

    private String firstTag( List<String> tags )
    {
        return elementPattern( toRegex( CollectionUtil.first( tags ) ) );
    }

    private String tail( Matcher matcher )
    {
        return matcher.group( 6 );
    }

    private String endTag( Matcher matcher )
    {
        return matcher.group( 5 );
    }

    private String content( Matcher matcher )
    {
        String content = matcher.group( 4 );
        for (int i = filters.size() - 1; i >= 0; i--)
        {
            HtmlContentFilter filter = filters.get( i );
            if (filter.handles( tag( matcher ) )) return filter.process( content );
        }
        return content;
    }

    private String tag( Matcher matcher )
    {
        return matcher.group( 3 ).toLowerCase();
    }

    private String startTag( Matcher matcher )
    {
        return matcher.group( 2 );
    }

    private String lead( Matcher matcher )
    {
        return matcher.group( 1 );
    }

    private Pattern compile( String regex )
    {
        return Pattern.compile( regex );
    }

    private String removeComments( String html )
    {
        return html.replaceAll( "(?is)<!--(.*?)-->", "" );
    }

    private String stripSelector( String selector, String tag )
    {
        return tag.replaceAll( selector + ">", "" );
    }

    private String elementPattern( String t )
    {
        return String.format( "(?is)(.*?)(<\\s*(%s)\\s*.*?>)(.*?)(<\\s*/\\s*\\3\\s*>)(.*)", t );
    }

    private String toRegex( String tags )
    {
        return tags.replaceAll( " ", "|" );
    }

	private String name( String content )
	{
		return meta( content, "title" );
	}

	private String externalLink( String content )
	{
		return meta( content, "external-link" );
	}

	private String meta( String content, String name )
	{
		Matcher matcher = compile(String.format( "<meta name=\"%s\" content=\"(.*)\"/>" , name )).matcher( content );

		return matcher.find() ? matcher.group( 1 ) : null;
	}

    /**
     * <p>useTags.</p>
     *
     * @param tags a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.html.HtmlDocumentBuilder} object.
     */
    public HtmlDocumentBuilder useTags( String... tags )
    {
        this.tags.clear();
        this.tags.addAll( Arrays.asList( tags ) );
        return this;
    }

    /**
     * <p>addAllFilters.</p>
     *
     * @param filters a {@link java.util.List} object.
     * @return a {@link com.greenpepper.html.HtmlDocumentBuilder} object.
     */
    public HtmlDocumentBuilder addAllFilters( List<HtmlContentFilter> filters )
    {
        this.filters.addAll( filters );
        return this;
    }

    /**
     * <p>addFilter.</p>
     *
     * @param filter a {@link com.greenpepper.html.HtmlContentFilter} object.
     * @return a {@link com.greenpepper.html.HtmlDocumentBuilder} object.
     */
    public HtmlDocumentBuilder addFilter( HtmlContentFilter... filter )
    {
        return addAllFilters( Arrays.asList( filter ) );
    }
}
