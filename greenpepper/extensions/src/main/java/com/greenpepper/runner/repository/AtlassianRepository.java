package com.greenpepper.runner.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.document.Document;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.repository.DocumentNotFoundException;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.RepositoryException;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.URIUtil;

public class AtlassianRepository implements DocumentRepository
{
    public static final String PAGE_NOT_FOUND = "Page Not Found !";

    public static final String INSUFFICIENT_PRIVILEGES = "INSUFFICIENT PRIVILEGES !";

    public static final String SESSION_INVALID = "Session Invalid !";

    public static final String PARAMETERS_MISSING = "Parameters Missing, expecting:[SpaceKey, PageTitle, IncludeStyle] !";

    private static final Logger logger = LoggerFactory.getLogger(AtlassianRepository.class); 

    private final URI root;
	private String handler;
	private boolean includeStyle;
	private String username = "";
	private String password = "";

	public AtlassianRepository(String... args) throws Exception
	{
		this.root = URI.create(URIUtil.raw(args[0]));

		String includeAtt = URIUtil.getAttribute(root, "includeStyle");
        includeStyle = includeAtt == null ? true : Boolean.valueOf(includeAtt);

    	handler = URIUtil.getAttribute(root, "handler");
    	if(handler == null) throw new IllegalArgumentException("Missing handler");

		if(args.length == 3)
		{
			username = args[1];
			password = args[2];
		}
	}

	public Document loadDocument(String location) throws Exception
	{
        String spec = retrieveSpecification(URI.create(URIUtil.raw(location)));
        logger.trace("Page retrieved from the repository for location '{}'\n{}", location,spec);
        // check if there is an error in the page
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(spec);
        Elements select = jsoupDoc.select("#conf_actionError_Msg");
        for (Element element : select) {
            if (element.hasText()) {
                if ( StringUtils.equals(element.text(), PAGE_NOT_FOUND)) {
                    throw new DocumentNotFoundException(location);
                } else if (StringUtils.equals(element.text(), PARAMETERS_MISSING)){
                    throw new RepositoryException(PARAMETERS_MISSING);
                } else if (StringUtils.equals(element.text(), SESSION_INVALID)){
                    throw new RepositoryException(SESSION_INVALID);
                } else if (StringUtils.equals(element.text(), INSUFFICIENT_PRIVILEGES)){
                    throw new RepositoryException(INSUFFICIENT_PRIVILEGES);
                }
            }
        }
        return loadHtmlDocument( spec );
	}

	public void setDocumentAsImplemeted(String location) throws Exception
	{
    	Vector<?> args = CollectionUtil.toVector( username , password, args(URI.create(URIUtil.raw(location))));
        XmlRpcClient xmlrpc = new XmlRpcClient( root.getScheme() + "://" + root.getAuthority() + root.getPath() );
        String msg = (String)xmlrpc.execute( new XmlRpcRequest( handler + ".setSpecificationAsImplemented", args ) );
        
        if(!("<success>".equals(msg))) throw new Exception(msg);
	}

    public List<String> listDocuments(String uri)
    {
        return new ArrayList<String>();
    }

	@SuppressWarnings("unchecked")
	public List<Object> listDocumentsInHierarchy() throws Exception
	{
    	Vector<?> args = CollectionUtil.toVector( username , password, CollectionUtil.toVector(root.getFragment()));
        XmlRpcClient xmlrpc = new XmlRpcClient( root.getScheme() + "://" + root.getAuthority() + root.getPath() );
        return (Vector<Object>)xmlrpc.execute( new XmlRpcRequest( handler + ".getSpecificationHierarchy", args ) );
	}

    private String retrieveSpecification(URI location) throws Exception
    {
    	Vector<?> args = CollectionUtil.toVector( username , password, args(location));
        XmlRpcClient xmlrpc = new XmlRpcClient( root.getScheme() + "://" + root.getAuthority() + root.getPath() );
        String renderedSpecificationResponse = (String) xmlrpc.execute( new XmlRpcRequest( handler + ".getRenderedSpecification", args ) );
        return renderedSpecificationResponse;
    }

	private Document loadHtmlDocument( String content ) throws IOException
    {
        Reader reader = new StringReader( content );
        try
        {
            return HtmlDocumentBuilder.tablesAndLists().build( reader );
        }
        finally
        {
            IOUtil.closeQuietly( reader );
        }
    }

    private Vector<Object> args(URI location)
    {
    	String[] locationArgs = location.getPath().split("/");
        Vector<Object> args = new Vector<Object>();
	    args.add( root.getFragment() );
	    
        for(int i = 0; i < locationArgs.length ; i++)
        {
	        args.add( locationArgs[i] );
        }

        args.add( includeStyle );
        
        String implemented = URIUtil.getAttribute(location, "implemented");
        args.add( implemented == null ? true : Boolean.valueOf(implemented) );

        return args;
    }
}
