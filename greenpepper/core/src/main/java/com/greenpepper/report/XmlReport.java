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

package com.greenpepper.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.greenpepper.Statistics;
import com.greenpepper.TimeStatistics;
import com.greenpepper.util.BOMUtil;
import com.greenpepper.util.ExceptionImposter;
import com.greenpepper.util.ExceptionUtils;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.StringUtil;

/**
 * <p>XmlReport class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class XmlReport implements Report
{
    private static final String DOCUMENTS = "documents";
    private static final String GLOBAL_EXCEPTION = "global-exception";
    private static final String DOCUMENT = "document";
	private static final String DOCUMENT_NAME = "name";
	private static final String DOCUMENT_EXTERNAL_LINK = "external-link";
    private static final String STATISTICS = "statistics";
	private static final String TIME = "time-statistics";
	private static final String EXECUTION_TIME = "execution";
	private static final String TOTAL_TIME = "total";
    private static final String RESULTS = "results";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String ERROR = "error";
    private static final String IGNORED = "ignored";
	private static final String SECTIONS = "sections";
	private static final String SECTION = "section";

    private static final String ANNOTATION = "annotation";

    private static final DocumentBuilderFactory documentFactoryBuilder = DocumentBuilderFactory.newInstance();
	private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

	private Document dom;
    private Element root;
    private String name;
    private com.greenpepper.document.Document document;

    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.report.XmlReport} object.
     */
    public static XmlReport newInstance( String name )
    {
        return new XmlReport( name );
    }

    /**
     * <p>Constructor for XmlReport.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public XmlReport( String name )
    {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>getType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType()
    {
        return "xml";
    }

    private DocumentBuilder newDocumentBuilder()
    {
        try
        {
            return documentFactoryBuilder.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw ExceptionImposter.imposterize( e );
        }
    }

    private XmlReport( InputSource source ) throws Exception
    {
        dom = newDocumentBuilder().parse( source );
        root = dom.getDocumentElement();
    }

    /**
     * <p>parse.</p>
     *
     * @param content a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.report.XmlReport} object.
     * @throws java.lang.Exception if any.
     */
    public static XmlReport parse( String content ) throws Exception
    {
        return parse( new StringReader( content ) );
    }

    /**
     * <p>parse.</p>
     *
     * @param in a {@link java.io.Reader} object.
     * @return a {@link com.greenpepper.report.XmlReport} object.
     * @throws java.lang.Exception if any.
     */
    public static XmlReport parse( Reader in ) throws Exception
    {
        return parse( new InputSource( in ) );
    }

    /**
     * <p>parse.</p>
     *
     * @param is a {@link java.io.InputStream} object.
     * @return a {@link com.greenpepper.report.XmlReport} object.
     * @throws java.lang.Exception if any.
     */
    public static XmlReport parse( InputStream is ) throws Exception
    {
        return parse( new InputSource( is ) );
    }

	/**
	 * <p>parse.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 * @return a {@link com.greenpepper.report.XmlReport} object.
	 * @throws java.lang.Exception if any.
	 */
	public static XmlReport parse( File file ) throws Exception
	{
		Reader in = BOMUtil.newReader( file );

		try
		{
			return parse( in );
		}
		finally
		{
			IOUtil.closeQuietly( in );
		}
	}

    /** {@inheritDoc} */
    public void renderException( Throwable throwable )
    {
        createEmptyDocument();
        String msg = ExceptionUtils.stackTrace( throwable, "\n", 10 );
        addTextValue( root, GLOBAL_EXCEPTION, msg, true );
    }

    private void createEmptyDocument()
    {
        dom = newDocumentBuilder().newDocument();
        root = dom.createElement( DOCUMENTS );
        dom.appendChild( root );
    }

    /** {@inheritDoc} */
    public void generate( com.greenpepper.document.Document document )
    {

        this.document = document;

        createEmptyDocument();
        Statistics compiler = document.getStatistics();
        Element element = dom.createElement( DOCUMENT );
        root.appendChild( element );

		if (!StringUtil.isEmpty(document.getName()))
		{
			addTextValue( element, DOCUMENT_NAME, document.getName(), true);
		}

		if (!StringUtil.isEmpty(document.getExternalLink()))
		{
			addTextValue( element, DOCUMENT_EXTERNAL_LINK, document.getExternalLink(), true);
		}

		if (document.getSections() != null && document.getSections().length > 0)
		{
			Element sections = dom.createElement( SECTIONS );
			root.appendChild(sections);
			
			for (String section : document.getSections())
			{
				addTextValue( sections, SECTION, section, true);
			}
		}

		Element time = dom.createElement( TIME );
		element.appendChild( time );

        Element stats = dom.createElement( STATISTICS );
        element.appendChild( stats );

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter( buffer );
        document.print( writer );
        writer.flush();

		addNumberValue( time, EXECUTION_TIME, document.getTimeStatistics().getExecution() );
		addNumberValue( time, TOTAL_TIME, document.getTimeStatistics().getTotal() );
        addTextValue( element, RESULTS, buffer.toString(), true );
        addNumberValue( stats, SUCCESS, compiler.rightCount() );
        addNumberValue( stats, FAILURE, compiler.wrongCount() );
        addNumberValue( stats, ERROR, compiler.exceptionCount() );
        addNumberValue( stats, IGNORED, compiler.ignoredCount() );
    }

    /** {@inheritDoc} */
    @Override
    public String getDocumentUri() {
        return document == null ? null : document.getUri();
    }

    /**
     * <p>getResults.</p>
     *
     * @param index a int.
     * @return a {@link java.lang.String} object.
     */
    public String getResults( int index )
    {
        return getTextValue( RESULTS, index );
    }

    /**
     * <p>getGlobalException.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getGlobalException()
    {
        return getTextValue( GLOBAL_EXCEPTION, 0 );
    }

    /**
     * <p>getSuccess.</p>
     *
     * @param index a int.
     * @return a int.
     */
    public int getSuccess( int index )
    {
        return getIntValue( SUCCESS, index );
    }

    /**
     * <p>getFailure.</p>
     *
     * @param index a int.
     * @return a int.
     */
    public int getFailure( int index )
    {
        return getIntValue( FAILURE, index );
    }

    /**
     * <p>getError.</p>
     *
     * @param index a int.
     * @return a int.
     */
    public int getError( int index )
    {
        return getIntValue( ERROR, index );
    }

    /**
     * <p>getIgnored.</p>
     *
     * @param index a int.
     * @return a int.
     */
    public int getIgnored( int index )
    {
        return getIntValue( IGNORED, index );
    }

    /**
     * <p>getAnnotation.</p>
     *
     * @param index a int.
     * @return a int.
     */
    public int getAnnotation( int index )
    {
        return getIntValue( ANNOTATION, index );
    }

	/**
	 * <p>getSections.</p>
	 *
	 * @param index a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getSections( int index )
	{
		return getTextValue( SECTION, index );
	}

	/**
	 * <p>getExecutionTime.</p>
	 *
	 * @param index a int.
	 * @return a long.
	 */
	public long getExecutionTime( int index )
	{
		return getLongValue( EXECUTION_TIME, index );
	}

	/**
	 * <p>getTotalTime.</p>
	 *
	 * @param index a int.
	 * @return a long.
	 */
	public long getTotalTime( int index )
	{
		return getLongValue( TOTAL_TIME, index );
	}

	/**
	 * <p>getDocumentName.</p>
	 *
	 * @param index a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getDocumentName( int index )
	{
		return getTextValue( DOCUMENT_NAME, index );
	}

	/**
	 * <p>getDocumentExternalLink.</p>
	 *
	 * @param index a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getDocumentExternalLink( int index )
	{
		return getTextValue(DOCUMENT_EXTERNAL_LINK, index );
	}

    /** {@inheritDoc} */
    public void printTo( Writer out ) throws IOException
    {
		try
		{
			transformerFactory.newTransformer().transform(new DOMSource(dom), new StreamResult(out));
		}
		catch (TransformerException ex)
		{
			throw new IOException(ex.getMessage());
		}
	}

    /**
     * <p>parse.</p>
     *
     * @param source a {@link org.xml.sax.InputSource} object.
     * @return a {@link com.greenpepper.report.XmlReport} object.
     * @throws java.lang.Exception if any.
     */
    public static XmlReport parse( InputSource source ) throws Exception
    {
        return new XmlReport( source );
    }

	/**
	 * <p>toXml.</p>
	 *
	 * @param document a {@link com.greenpepper.document.Document} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.io.IOException if any.
	 */
	public static String toXml(com.greenpepper.document.Document document) throws IOException
	{
		StringWriter sw = new StringWriter();

		XmlReport xmlReport = XmlReport.newInstance("");
		xmlReport.generate( document );
		xmlReport.printTo( sw );

		return sw.toString();
	}

	/**
	 * <p>toStatistics.</p>
	 *
	 * @return a {@link com.greenpepper.Statistics} object.
	 */
	public Statistics toStatistics()
	{
		return new Statistics(getSuccess(0), getFailure(0), getError(0), getIgnored(0));
	}

	/**
	 * <p>toTimeStatistics.</p>
	 *
	 * @return a {@link com.greenpepper.TimeStatistics} object.
	 */
	public TimeStatistics toTimeStatistics()
	{
		return new TimeStatistics(getTotalTime(0), getExecutionTime(0));
	}

    private String getTextValue( String tagName, int index )
    {
        String textVal = null;
        NodeList nl = root.getElementsByTagName( tagName );
        if (nl != null && nl.getLength() > 0 && index < nl.getLength())
        {
            Element el = (Element) nl.item( index );
            Node node = el.getFirstChild();
            if (node != null)
            {
                textVal = node.getNodeValue();
            }
        }

        return textVal;
    }

    private void addTextValue( Element parent, String tagName, String value, boolean cdata )
    {
        Element element = dom.createElement( tagName );
        Text eleValue = cdata ? dom.createCDATASection( value ) : dom.createTextNode( value );
        element.appendChild( eleValue );
        parent.appendChild( element );
    }

    private int getIntValue( String tagName, int index )
    {
        return Integer.parseInt( getTextValue( tagName, index ) );
    }

	private long getLongValue( String tagName, int index )
	{
		String value = getTextValue( tagName, index );
		return value == null ? 0 : Long.parseLong( value );
	}

    private void addNumberValue( Element parent, String tagName, Number value )
    {
        Element element = dom.createElement( tagName );
        Text eleValue = dom.createTextNode( String.valueOf( value ) );
        element.appendChild( eleValue );
        parent.appendChild( element );
    }
}
