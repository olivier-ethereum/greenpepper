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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.greenpepper.AbstractExample;
import com.greenpepper.Example;
import com.greenpepper.Text;
import com.greenpepper.annotation.Annotation;
import com.greenpepper.util.CollectionUtil;

/**
 * <p>HtmlExample class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HtmlExample extends AbstractExample implements Text
{
    private String lead;
    private String startTag;
    private String text;
    private String endTag;
    private String tail;
    private String tag;
    private List<String> childTags;
    private Example sibling;
    private Example child;

    private final Map<String, String> styles = new LinkedHashMap<String, String>();
    private final Set<String> cssClasses = new TreeSet<String>();

    /**
     * <p>Constructor for HtmlExample.</p>
     *
     * @param lead a {@link java.lang.String} object.
     * @param startTag a {@link java.lang.String} object.
     * @param tag a {@link java.lang.String} object.
     * @param content a {@link java.lang.String} object.
     * @param endTag a {@link java.lang.String} object.
     * @param tail a {@link java.lang.String} object.
     * @param childTags a {@link java.util.List} object.
     * @param child a {@link com.greenpepper.Example} object.
     * @param sibling a {@link com.greenpepper.Example} object.
     */
    public HtmlExample( String lead,
                        String startTag,
                        String tag,
                        String content,
                        String endTag,
                        String tail,
                        List<String> childTags,
                        Example child,
                        Example sibling )
    {
        this.tag = tag;
        this.tail = tail;
        this.endTag = endTag;
        this.startTag = startTag;
        this.lead = lead;
        this.text = content;
        this.childTags = childTags;
        this.child = child;
        this.sibling = sibling;
    }

    /**
     * <p>firstChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example firstChild()
    {
        return child;
    }

    /**
     * <p>nextSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example nextSibling()
    {
        return sibling;
    }

    /** {@inheritDoc} */
    public void print( PrintWriter out )
    {
        out.write( lead );
        printStartTag( out );
        if (child != null) child.print( out );
        else out.write( text );
        out.write( endTag );
        if (sibling != null) sibling.print( out );
        else out.write( tail );
    }

    // Should we return null or empty string when we have children?
    /**
     * <p>getContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getContent()
    {
        String content = normalizeLineBreaks( text );
        content = removeNonLineBreaks( content );
        content = condenseWhitespace( content );
        content = decodeMarkup( content );
        return content.trim();
    }

    private String firstPattern(String tags) {
        Scanner scanner = new Scanner(tags);
        try {
            String first = scanner.next();
            return first;
        } finally {
            scanner.close();
        }
    }

    private HtmlExample createSpecification( String tag, List<String> moreTags )
    {
        return new HtmlExample( "", start( tag ), tag, "", end( tag ), "", moreTags, null, null );
    }

    private String start( String tag )
    {
        return String.format( "<%s>", tag );
    }

    private String end( String tag )
    {
        return String.format( "</%s>", tag );
    }

    /**
     * <p>addChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example addChild()
    {
        if (hasChild())
        {
            return child.addSibling();
        }
        else
        {
            if (childTags.isEmpty()) throw new IllegalStateException( "No child tag" );
            List<String> moreTags = new ArrayList<String>( childTags );
            String childTag = firstPattern( CollectionUtil.shift( moreTags ) );
            child = createSpecification( childTag, moreTags );
            return child;
        }
    }

    /**
     * <p>addSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example addSibling()
    {
        if (hasSibling())
        {
            return sibling.addSibling();
        }
        else
        {
            sibling = createSpecification( tag, childTags );
            return sibling;
        }
    }

    private String condenseWhitespace( String s )
    {
        // non breaking space is decimal character 160 (hex A0)
        return s.replace( (char) 160, ' ' ).replaceAll( "&nbsp;", " " ).replaceAll( "\\s+", " " );
    }

    private String decodeMarkup( String s )
    {
        return new HtmlEntitiesDecoder( s ).decode();
    }

    /** {@inheritDoc} */
    public void annotate( Annotation annotation )
    {
        annotation.writeDown( this );
    }

    private String normalizeLineBreaks( String s )
    {
        return s.replaceAll( "<\\s*br(\\s+.*?)*>", "<br/>" );
    }

    private void printStartTag( PrintWriter out )
    {
        out.write( startTag.substring( 0, startTag.length() - 1 ) );
        if (!styles.isEmpty()) out.write( String.format( " style=\"%s\" ", inlineStyle() ) );
        if (!cssClasses.isEmpty()) out.write( String.format( " class=\"%s\" ", StringUtils.join(cssClasses, " ")));
        out.write( ">" );
    }

    private String inlineStyle()
    {
        StringBuilder style = new StringBuilder();
        for (String attr : styles.keySet())
        {
            style.append( String.format( "%s: %s; ", attr, styles.get( attr ) ) );
        }
        return style.toString();
    }

    private String removeNonLineBreaks( String s )
    {
        return s.replaceAll( "<" + not( "br/>" ) + ">", "" );
    }

    private String not( String regex )
    {
        return String.format( "(?!%s).*?", regex );
    }

    /** {@inheritDoc} */
    @Override
    public void setCssClasses(String... classes) {
        Collections.addAll(cssClasses, classes);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getCssClasses() {
        return cssClasses.toArray(new String[cssClasses.size()]);
    }

    /** {@inheritDoc} */
    public void setStyle( String property, String value )
    {
        styles.put( property, value );
    }

    /** {@inheritDoc} */
    public String getStyle( String property)
    {
        return styles.get( property);
    }

    /** {@inheritDoc} */
    public void setContent( String content )
    {
        text = content;
    }
}
