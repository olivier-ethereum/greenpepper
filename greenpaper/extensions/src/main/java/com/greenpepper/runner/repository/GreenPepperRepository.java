package com.greenpepper.runner.repository;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.document.Document;
import com.greenpepper.document.SpecificationListener;
import com.greenpepper.report.XmlReport;
import com.greenpepper.repository.DocumentNotFoundException;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.UnsupportedDocumentException;
import com.greenpepper.util.ClassUtils;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExceptionImposter;
import com.greenpepper.util.StringUtil;
import com.greenpepper.util.URIUtil;

public class GreenPepperRepository implements DocumentRepository
{
	private URI root;
	private String handler;
	private String sut;
	private boolean includeStyle;
	private Boolean implemented;
	private boolean postExecutionResult;
	private String username = "";
	private String password = "";

	public GreenPepperRepository(String... args) throws Exception
    {
        if (args.length == 0) throw new IllegalArgumentException("No root specified");
        this.root = URI.create(URIUtil.raw(args[0]));
		
		String includeAtt = URIUtil.getAttribute(root, "includeStyle");
        includeStyle = includeAtt == null || Boolean.valueOf(includeAtt);

        String implementedAtt = URIUtil.getAttribute(root, "implemented");
        if(implementedAtt != null)
        	implemented = Boolean.parseBoolean(implementedAtt);

    	handler = URIUtil.getAttribute(root, "handler");
    	if(handler == null) throw new IllegalArgumentException("No handler specified");

        sut = URIUtil.getAttribute(root, "sut");
    	if(sut == null) throw new IllegalArgumentException("No sut specified");

		String postExecutionResultAtt = URIUtil.getAttribute(root, "postExecutionResult");
		if(postExecutionResultAtt != null)
			postExecutionResult = Boolean.parseBoolean(postExecutionResultAtt);
    	
		if (args.length == 3)
		{
			username = args[1];
			password = args[2];
		}
    }

	public void setDocumentAsImplemeted(String location) throws Exception
	{	
		throw new UnsupportedOperationException("Not supported");
	}

    public List<String> listDocuments(String uri) throws Exception
    {
        List<String> documentsURI = new ArrayList<String>();

        String repoUID = getPath(uri);
    	if (repoUID == null) throw new UnsupportedDocumentException("Missing repo UID");

    	List<List<String>> definitions = downloadSpecificationsDefinitions(repoUID);
        for(List<String> definition : definitions)
        {
        	String docName = repoUID + "/" + definition.get(4);
        	documentsURI.add(docName);
        }
        
    	return documentsURI;
    }

    private String getPath(String uri)
    {
        return URI.create(URIUtil.raw(uri)).getPath();
    }

	@SuppressWarnings("unchecked")
    private List<List<String>> downloadSpecificationsDefinitions(String repoUID) throws Exception
    {
		Object callResult = getXmlRpcClient().execute(handler + ".getListOfSpecificationLocations", CollectionUtil.toVector(repoUID, sut) );
		List<List<String>> definitions = (List<List<String>>) callResult;
        checkForErrors(definitions);
        return definitions;
    }

	public List<Object> listDocumentsInHierarchy() throws Exception
	{
		throw new UnsupportedOperationException("Hierarchy not supported");
	}
	
    public Document loadDocument(String location) throws Exception
	{
        List<String> definition = getDefinition(location);

		DocumentRepository repository = getRepository(definition);
		
    	String specLocation = definition.get(4) + (implemented != null ? "?implemented="+implemented : "");

        Document document = repository.loadDocument(specLocation);

		if (postExecutionResult)
		{
			document.setSpecificationListener(new PostExecutionResultSpecificationListener(definition, document));
		}

		return document;
	}

	private DocumentRepository getRepository(List<String> definition)
			throws Exception
	{
		Class klass = ClassUtils.loadClass(definition.get(0)) ;
		Constructor<?> constructor = klass.getConstructor( String[].class );
		return (DocumentRepository)constructor.newInstance(new Object[]{args(definition)});
	}

    private List<String> getDefinitionFor(List<List<String>> definitions, String location) throws DocumentNotFoundException
    {
        for(List<String> def : definitions)
        {
            if (def.get(4).equals(location)) return def;
        }
        throw new DocumentNotFoundException(location);
    }

	private List<String> getDefinition(String location) throws Exception
	{
		String path = getPath(location);
		String[] parts  = path.split("/", 2);
		String repoUID = parts[0];
		if (parts.length == 1) throw new DocumentNotFoundException(location);

		List<List<String>> definitions = downloadSpecificationsDefinitions(repoUID);
		return getDefinitionFor(definitions, parts[1]);
	}

	private XmlRpcClient getXmlRpcClient()
			throws MalformedURLException
	{
	    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(root.getScheme() + "://" + root.getAuthority() + root.getPath()));
        XmlRpcClient xmlrpc = new XmlRpcClient();
        xmlrpc.setConfig(config);
        xmlrpc.setTypeFactory(new CompatTypeFactory(xmlrpc));
		return xmlrpc;
	}

	private String[] args(List<String> definition)
	{
		String[] args = new String[3];
		args[0] = includeStyle ? definition.get(1) : withNoStyle(definition.get(1));
		args[1] = StringUtil.isEmpty(username) ? definition.get(2) : username;
		args[2] = StringUtil.isEmpty(password) ? definition.get(3) : password;
		return args;
	}

    private String withNoStyle(String location)
    {
    	URI uri = URI.create(URIUtil.raw(location));
    	StringBuilder sb = new StringBuilder();
    	if(uri.getScheme() != null) sb.append(uri.getScheme()).append("://");
    	if(uri.getAuthority() != null) sb.append(uri.getAuthority());
    	if(uri.getPath() != null) sb.append(uri.getPath());
    	
    	String query = uri.getQuery();
    	if(query == null) query = "?includeStyle=false";
    	else query += "&includeStyle=false";
    	sb.append("?").append(query);
    	
    	if(uri.getFragment() != null) sb.append("#").append(uri.getFragment());
    	
    	return sb.toString();
    }

	@SuppressWarnings("unchecked")
    private void checkForErrors(Object xmlRpcResponse) throws Exception
    {
        if (xmlRpcResponse instanceof List)
        {
            List temp = (List)xmlRpcResponse;
            if(!temp.isEmpty())
            {
                checkErrors(temp.get(0));
            }
        }
        else if (xmlRpcResponse instanceof Hashtable)
        {
            Hashtable<String, ?> table = (Hashtable<String, ?>)xmlRpcResponse;
            if(!table.isEmpty())
            {
                checkForErrors(table.get("<exception>"));
            }
        }
        else
        {
        	checkErrors(xmlRpcResponse);
        }
    }
    
    private void checkErrors(Object object) throws Exception
    {
        if (object instanceof Exception)
        {
            throw (Exception)object;
        }
        
        if (object instanceof String)
        {
            String msg = (String)object;
            if (!StringUtil.isEmpty(msg) && msg.indexOf("<exception>") > -1)
                throw new Exception(msg.replace("<exception>", ""));
        }
    }

	private final class PostExecutionResultSpecificationListener
			implements SpecificationListener
	{
		private final List<String> definitionRef;
		private final Document documentRef;

		private PostExecutionResultSpecificationListener(List<String> definitionRef, Document documentRef)
		{
			this.definitionRef = definitionRef;
			this.documentRef = documentRef;
		}

		public void exampleDone(Example table, Statistics statistics) {}

		public void specificationDone(Example spec, Statistics statistics)
		{
			try
			{
				String[] args1 = args(definitionRef);
				URI location = URI.create(URIUtil.raw(definitionRef.get(1)));
				Vector args = CollectionUtil.toVector(args1[1], args1[2], 
													  CollectionUtil.toVector(location.getFragment(),
																			  definitionRef.get(4),
																			  sut,
																			  XmlReport.toXml(documentRef)));

				String msg = (String)getXmlRpcClient().execute( handler + ".saveExecutionResult", args);

                if(!("<success>".equals(msg)))
                {
                    throw new Exception(msg);
                }
			}
			catch (XmlRpcException e)
			{
				// Old server / incompatible method ? 
				if (e.getMessage().indexOf(NoSuchMethodException.class.getName()) == -1)
				{
					// @todo : Log ? Critical ? Do we want the test execution to fail if we can't post the result back ?
					throw ExceptionImposter.imposterize(e);
				}
			}
			catch (Exception e)
			{
				// @todo : Log ? Critical ? Do we want the test execution to fail if we can't post the result back ?
				throw ExceptionImposter.imposterize(e);
			}
		}
	}
}