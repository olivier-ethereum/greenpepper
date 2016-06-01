package com.greenpepper.runner.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
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

/**
 * <p>AtlassianRepository class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AtlassianRepository implements DocumentRepository
{
    /** Constant <code>PAGE_NOT_FOUND="Page Not Found !"</code> */
    static final String PAGE_NOT_FOUND = "Page Not Found !";

    /** Constant <code>INSUFFICIENT_PRIVILEGES="INSUFFICIENT PRIVILEGES !"</code> */
    static final String INSUFFICIENT_PRIVILEGES = "INSUFFICIENT PRIVILEGES !";

    /** Constant <code>SESSION_INVALID="Session Invalid !"</code> */
    static final String SESSION_INVALID = "Session Invalid !";

    /** Constant <code>PARAMETERS_MISSING="Parameters Missing, expecting:[SpaceKey"{trunked}</code> */
    private static final String PARAMETERS_MISSING = "Parameters Missing, expecting:[SpaceKey, PageTitle, IncludeStyle] !";

    private static final Logger logger = LoggerFactory.getLogger(AtlassianRepository.class);
    private static final int SUTNAME_INDEX = 0;
    private static final int REPOSITORY_UID_INDEX = 1;
    private static final String CONFLUENCE = "Confluence-";

    private final URI root;
	private String handler;
	private boolean includeStyle;
	private String username = "";
	private String password = "";


    private List<Object> selectedRepository;
    private XmlRpcClient xmlRpcClient;

    /**
	 * <p>Constructor for AtlassianRepository.</p>
	 *
	 * @param args a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
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

	/** {@inheritDoc} */
	public Document loadDocument(String location) throws Exception
	{
        String spec = retrieveSpecification(URI.create(URIUtil.raw(location)));
        logger.trace("Page retrieved from the repository for location '{}'\n{}", location,spec);
        // check if there is an error in the page
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(spec);
        jsoupDoc.outputSettings().prettyPrint(false);
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
        return loadHtmlDocument( jsoupDoc );
	}

	/** {@inheritDoc} */
	public void setDocumentAsImplemeted(String location) throws Exception
	{
    	Vector<?> args = CollectionUtil.toVector( username , password, args(URI.create(URIUtil.raw(location))));
        XmlRpcClient xmlrpc = getXmlRpcClient();
        String msg = (String)xmlrpc.execute( new XmlRpcRequest( handler + ".setSpecificationAsImplemented", args ) );
        
        if(!("<success>".equals(msg))) throw new Exception(msg);
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getSpecificationsHierarchy(String project, String systemUnderTest) throws Exception {
        XmlRpcClient xmlrpc = getXmlRpcClient();

        @SuppressWarnings("unchecked")
        List<List<Object>> sutList = (List<List<Object>>) xmlrpc.execute(new XmlRpcRequest(handler + ".getSystemUnderTestsOfProject", toArgs(project)));

        List<Object> selectedSUT = null;
        for (List<Object> sut : sutList) {
            String SutName = (String)sut.get(SUTNAME_INDEX);
            if (StringUtils.equals(SutName, systemUnderTest)) {
                selectedSUT = sut;
                break;
            }
        }
        if (selectedSUT == null) {
            throw new RepositoryException(String.format("SUT %s not found in the project %s", systemUnderTest, project));
        }

        return (List<Object>) xmlrpc.execute(
                new XmlRpcRequest(handler + ".getSpecificationHierarchy", toArgs(getSelectedRepository(), selectedSUT)));

    }

    /** {@inheritDoc} */
    public List<String> listDocuments(String uri)
    {
        return new ArrayList<String>();
    }

	/**
	 * <p>listDocumentsInHierarchy.</p>
	 *
	 * @return a {@link java.util.List} object.
	 * @throws java.lang.Exception if any.
	 */
	@SuppressWarnings("unchecked")
	public List<Object> listDocumentsInHierarchy() throws Exception
	{
    	Vector<?> args = CollectionUtil.toVector( username , password, CollectionUtil.toVector(getRepositoryName()));
        XmlRpcClient xmlrpc = getXmlRpcClient();
        return (Vector<Object>)xmlrpc.execute( new XmlRpcRequest( handler + ".getSpecificationHierarchy", args ) );
	}

    private String retrieveSpecification(URI location) throws Exception
    {
    	Vector<?> args = CollectionUtil.toVector( username , password, args(location));
        XmlRpcClient xmlrpc = getXmlRpcClient();
        return (String) xmlrpc.execute( new XmlRpcRequest( handler + ".getRenderedSpecification", args ) );
    }

	private Document loadHtmlDocument(org.jsoup.nodes.Document content ) throws IOException
    {
        Element head = content.head();
        // I will put an encoding UTF8 per default
        if (head.select("meta[charset]").isEmpty() && head.select("meta[http-equiv=Content-Type]").isEmpty() ) {
            head.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>");
        }

        // I will also remove any <base> node from the HEAD
        head.select("base").remove();

        Reader reader = new StringReader( content.outerHtml() );
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
    	final String[] locationArgs = location.getPath().split("/");
        final String implemented = URIUtil.getAttribute(location, "implemented");

        ArrayList<Object> args = new ArrayList<Object>(){{
            add(getRepositoryName());
            addAll(Arrays.asList(locationArgs));
            add(includeStyle);
            add(implemented == null ? true : Boolean.valueOf(implemented));
            }};
        return toArgs(args.toArray());
    }

    private Vector<Object> toArgs(Object... options) {
        Vector<Object> args = new Vector<Object>();
        Collections.addAll(args, options);
        return args;
    }

    private XmlRpcClient getXmlRpcClient() throws MalformedURLException {
        if (xmlRpcClient == null) {
            xmlRpcClient = new XmlRpcClient( root.getScheme() + "://" + root.getAuthority() + root.getPath() );
        }
        return xmlRpcClient;
    }

    private List<Object> getSelectedRepository() throws IOException, XmlRpcException, RepositoryException {
        if (selectedRepository == null) {
            @SuppressWarnings("unchecked")
            List<List<Object>> repoList = (List<List<Object>>) getXmlRpcClient()
                    .execute(new XmlRpcRequest(handler + ".getAllSpecificationRepositories", toArgs()));
            for (List<Object> repo : repoList) {
                if (StringUtils.equals(CONFLUENCE + getRepositoryName(), (String) repo.get(REPOSITORY_UID_INDEX))){
                    selectedRepository = repo;
                    break;
                }
            }
            if (selectedRepository == null) {
                throw new RepositoryException(String.format("SpecificationRepository %s not found", getRepositoryName()));
            }
        }
        return selectedRepository;
    }

    private String getRepositoryName() {
        return root.getFragment();
    }

}
