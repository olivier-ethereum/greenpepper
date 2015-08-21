package com.greenpepper.runner.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.greenpepper.document.Document;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.URIUtil;

public class AtlassianRepository implements DocumentRepository
{
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
        return loadHtmlDocument( spec );
	}

	public void setDocumentAsImplemeted(String location) throws Exception
	{
    	Vector<Object> args2 = args(URI.create(URIUtil.raw(location)));
    	String specific = ".setSpecificationAsImplemented";

    	Object response = makeXMLRPCCall(args2, specific);
        String msg = (String)response;
        
        if(!("<success>".equals(msg))) throw new Exception(msg);
	}


    public List<String> listDocuments(String uri)
    {
        return new ArrayList<String>();
    }

	@SuppressWarnings("unchecked")
	public List<Object> listDocumentsInHierarchy() throws Exception
	{
    	Vector<String> otherArgs = CollectionUtil.toVector(root.getFragment());
    	Object response = makeXMLRPCCall(otherArgs, ".getSpecificationHierarchy");
        return (List<Object>)response;
	}

    private String retrieveSpecification(URI location) throws Exception
    {
    	Vector<Object> args2 = args(location);
    	Object response = makeXMLRPCCall(args2, ".getRenderedSpecification");
        return (String) response;
    }

    private Object makeXMLRPCCall(Vector<?> options, String specificHandler) throws MalformedURLException, XmlRpcException {
        Vector<?> args = CollectionUtil.toVector( username , password, options);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(root.getScheme() + "://" + root.getAuthority() + root.getPath()));
        XmlRpcClient xmlrpc = new XmlRpcClient();
        xmlrpc.setConfig(config);
        xmlrpc.setTypeFactory(new CompatTypeFactory(xmlrpc));
        Object response = xmlrpc.execute( handler + specificHandler, args );
        return response;
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
