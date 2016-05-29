package com.greenpepper.runner.repository;

import static com.greenpepper.util.CollectionUtil.toVector;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import com.greenpepper.server.domain.DocumentNode;
import org.apache.xmlrpc.WebServer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.document.Document;
import com.greenpepper.repository.DocumentNotFoundException;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.RepositoryException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.junit.*;


public class AtlassianRepositoryTest {
    private static WebServer ws;
	private Mockery context = new JUnit4Mockery();
    private Handler handler;
    private DocumentRepository repo;

    @BeforeClass
    public static void createWebServer() {
        ws = new WebServer( 19005 );
        ws.start();
    }

    @AfterClass
    public static void shutTheWebServerDown() {
        ws.shutdown();
    }

    @Before
    public void setUp()
    {
        handler = context.mock( Handler.class );
        ws.removeHandler( "greenpepper1" );
        ws.addHandler( "greenpepper1", handler );
    }

    @Test
    public void testCannotProvideListOfSpecifications() throws Exception
    {
        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        assertTrue( repo.listDocuments( "PAGE" ).isEmpty() );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSpecificationsHierarchy() throws Exception {

        expectsForSpecsificationHierarchy();

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        List<Object> hierarchy = repo.getSpecificationsHierarchy("PROJECT", "SUTNAME");
        DocumentNode documentNode = DocumentNode.toDocumentNode(hierarchy);
        Iterator<DocumentNode> documentNodeIterator = DocumentNode.traverser.preOrderTraversal(documentNode).iterator();
        DocumentNode node  = documentNodeIterator.next();
        assertEquals("ROOT", node.getTitle());
        node  = documentNodeIterator.next();
        assertEquals("PAGE", node.getTitle());
        node  = documentNodeIterator.next();
        assertEquals("PAGE Executable", node.getTitle());
        node  = documentNodeIterator.next();
        assertEquals("SUBPAGE IMPLEMENTED", node.getTitle());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void shouldFailIfTheSpaceIsNotFound() throws Exception {

        expectsForSpecsificationHierarchy();

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KO KEY");
        repo.getSpecificationsHierarchy("PROJECT", "SUTNAME");
    }

    private void expectsForSpecsificationHierarchy() {
        context.checking(new Expectations()
        {{
            atLeast(1).of(handler).getSystemUnderTestsOfProject("PROJECT");
            Vector<Vector<?>> suts = systemUnderTests();
            will(returnValue(suts));
            exactly(1).of(handler).getAllSpecificationRepositories();
            Vector<Vector<?>> specsRepos = specificationRepositories();
            will(returnValue(specsRepos));
            atLeast(1).of(handler).getSpecificationHierarchy(specsRepos.get(0), suts.get(0));
            will(returnValue(specHierarchy()));
        }});
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RepositoryException.class)
    public void shouldFailIfTheSUTIsNotFound() throws Exception {

        context.checking(new Expectations()
        {{
            atLeast(1).of(handler).getSystemUnderTestsOfProject("PROJECT");
            Vector<Vector<?>> suts = systemUnderTests();
            will(returnValue(suts));
        }});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        repo.getSpecificationsHierarchy("PROJECT", "SUTNAME WRONG");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProvideAHierarchyListOfSpecifications() throws Exception
    {
    	final Vector<?> expected1 = toVector( "SPACE KEY" );
    	final Vector<?> expected2 = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			atLeast(1).of(handler).getSpecificationHierarchy("", "", expected1);
			will(returnValue(hierarchy()));
			atLeast(1).of(handler).getRenderedSpecification("", "", expected2);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        List<Object> docs = repo.listDocumentsInHierarchy();
        Hashtable<?,?> pageBranch = (Hashtable<?,?>)docs.get(2);
        repo.loadDocument((String)pageBranch.keySet().iterator().next());
    }

    @Test
    public void testCanDowloadPageContentFromAConfluenceServer() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        Document spec = repo.loadDocument( "PAGE" );

        assertSpecification( spec );
    }

    @Test
    public void testCanDowloadPageContentFromAJiraServer() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "PROJECT ID", "ISSUE KEY", Boolean.FALSE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#PROJECT ID");
        Document spec = repo.loadDocument( "ISSUE KEY" );

        assertSpecification( spec );
    }

    @Test
    public void testImplementedIsPassedInTheArgumenents() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.FALSE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.loadDocument( "PAGE?implemented=false" );
    }

    @Test
    public void testStyleDefaultsToTrueAndImplementedDefaultsToTrue() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.loadDocument( "PAGE" );
    }

    private void assertSpecification( Document doc )
    {
        assertNotNull( doc );
        StringWriter buffer = new StringWriter();
        doc.print( new PrintWriter( buffer ) );
        org.jsoup.nodes.Document expectedDoc = Jsoup.parse(specification());
        expectedDoc.outputSettings().escapeMode(Entities.EscapeMode.xhtml).prettyPrint(false);

        Element expected = expectedDoc.body();

        org.jsoup.nodes.Document resultDoc = Jsoup.parse(buffer.toString());
        Element result = resultDoc.body();
        result.select("style:first-of-type").remove();
        resultDoc.outputSettings().escapeMode(Entities.EscapeMode.xhtml).prettyPrint(false);

        Assert.assertEquals( expected.outerHtml(), result.outerHtml() );
    }

    @Test
    public void testComplainsIfArgumentsAreMissing() throws Exception
    {
        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?includeStyle=true");
            repo.loadDocument( "ISSUE KEY?" );
            fail();
        }
        catch (Exception e)
        {
            assertThat( e, is( IllegalArgumentException.class ) );
        }
    }

    @Test
    public void testWeCanSetASpecificationAsImplemented() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).setSpecificationAsImplemented("", "", expected);
			will(returnValue("<success>"));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.setDocumentAsImplemeted( "PAGE" );
    }

    @Test
    public void testExceptionIsThrownIfReturnedValueFromSettingAsImplementedDiffersFromSuccess()
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).setSpecificationAsImplemented("", "", expected);
			will(returnValue("exception"));
		}});

        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
			repo.setDocumentAsImplemeted( "PAGE" );
			fail();
		} 
        catch (Exception e)
        {
			Assert.assertEquals("exception", e.getMessage());
		}
    }


    @Test
    public void testShouldThrowDocumentNotFoundExceptionOnLoadUnexistingDocument()
    {
        @SuppressWarnings("unchecked")
        final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

        context.checking(new Expectations()
        {{
            one(handler).getRenderedSpecification("", "", expected);
            will(returnValue(error(AtlassianRepository.PAGE_NOT_FOUND)));
        }});

        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
            repo.loadDocument("PAGE" );
            fail("Should have thrown an exception");
        } 
        catch (Exception e)
        {
            assertTrue(e instanceof DocumentNotFoundException);
            Assert.assertEquals("Document not found PAGE", e.getMessage());
        }
    }

    @Test
    public void testShouldThrowRepositoryExceptionOnLoadDocumentWithUnsufficientPrivile()
    {
        @SuppressWarnings("unchecked")
        final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

        context.checking(new Expectations()
        {{
            one(handler).getRenderedSpecification("", "", expected);
            will(returnValue(error(AtlassianRepository.INSUFFICIENT_PRIVILEGES)));
        }});

        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
            repo.loadDocument("PAGE" );
            fail("Should have thrown an exception");
        } 
        catch (Exception e)
        {
            assertTrue(e instanceof RepositoryException);
            Assert.assertEquals(AtlassianRepository.INSUFFICIENT_PRIVILEGES, e.getMessage());
        }
    }

    @Test
    public void testShouldThrowRepositoryExceptionOnLoadDocumentWithSessionInvalid()
    {
        @SuppressWarnings("unchecked")
        final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

        context.checking(new Expectations()
        {{
            one(handler).getRenderedSpecification("", "", expected);
            will(returnValue(error(AtlassianRepository.SESSION_INVALID)));
        }});

        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
            repo.loadDocument("PAGE" );
            fail("Should have thrown an exception");
        } 
        catch (Exception e)
        {
            assertTrue(e instanceof RepositoryException);
            Assert.assertEquals(AtlassianRepository.SESSION_INVALID, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Vector<Vector<?>> specificationRepositories() {
        Vector<Vector<?>> repos = new Vector<Vector<?>>();
        repos.add(new Vector(Arrays.asList("REPO NAME", "Confluence-SPACE KEY" )));
        repos.add(new Vector(Arrays.asList("SUTNAME 2", "Confluence-SPACE 2 KEY" )));
        return repos;
    }

    @SuppressWarnings("unchecked")
    private Vector<Vector<?>> systemUnderTests() {
        Vector<Vector<?>> suts = new Vector<Vector<?>>();
        suts.add(new Vector(Collections.singletonList("SUTNAME")));
        suts.add(new Vector(Collections.singletonList("SUTNAME 1")));
        return suts;
    }

	private List<Object> hierarchy()
    {
    	List<Object> hierachy = new Vector<Object>();
    	hierachy.add("1");
    	hierachy.add("HOME");
    	
    	Hashtable<String,Vector<Object>> pageBranch = new Hashtable<String,Vector<Object>>();
    	Vector<Object> page = new Vector<Object>();
    	page.add("PAGE");
    	page.add(new Hashtable<String,Vector<?>>());
    	pageBranch.put("PAGE", page);
    	
    	hierachy.add(pageBranch);
    	
    	return hierachy;
    }

    private Vector<Object> specHierarchy()
    {
        Vector<Object> hierachy = new Vector<Object>();
        hierachy.add("ROOT");
        hierachy.add(Boolean.FALSE);
        hierachy.add(Boolean.FALSE);
        Hashtable<String,Vector<Object>> pageBranch = new Hashtable<String,Vector<Object>>();
        hierachy.add(pageBranch);

        Vector<Object> page = new Vector<Object>();
        page.add("PAGE");
        page.add(Boolean.FALSE);
        page.add(Boolean.FALSE);
        page.add(new Hashtable<String,Vector<?>>());
        pageBranch.put("PAGE", page);

        Vector<Object> page2 = new Vector<Object>();
        page2.add("PAGE Executable");
        page2.add(Boolean.TRUE);
        page2.add(Boolean.FALSE);
        Hashtable<String, Vector<?>> subPageBranch = new Hashtable<String, Vector<?>>();
        page2.add(subPageBranch);

        Vector<Object> subpage = new Vector<Object>();
        subpage.add("SUBPAGE IMPLEMENTED");
        subpage.add(Boolean.TRUE);
        subpage.add(Boolean.TRUE);
        subpage.add(new Hashtable<String,Vector<?>>());
        subPageBranch.put("SUBPAGE IMPLEMENTED", subpage);
        pageBranch.put("PAGE Executable", page2);

        return hierachy;
    }

    private String specification()
    {
        return "<html><table border='1' cellspacing='0'>" +
               "<tr><td colspan='3'>My Fixture</td></tr>" +
               "<tr><td>a</td><td>b</td><td>sum()</td></tr>" +
               "<tr><td>1</td><td>2</td><td>3</td></tr>" +
               "<tr><td>2</td><td>3</td><td>15</td></tr>" +
               "<tr><td>2</td><td>3</td><td>a</td></tr>" +
               "</table></html>";
    }

    private String error(String errorText)
    {

        return "<html>" +
                "  <table style=\"text-align:center; border:1px solid #cc0000; border-spacing:0px; background-color:#ffcccc; padding:0px; margin:5px; width:100%;\">" +
                "    <tr style=\"display:none\"><td>Comment</td></tr>" +
                "    <tr><td id=\"conf_actionError_Msg\" style=\"color:#cc0000; font-size:12px; font-family:Arial, sans-serif; text-align:center; font-weight:bold;\">" + errorText + "</td></tr>" +
                "  </table>" +
                "</html>";
    }

    
    public interface Handler
    {
        String getRenderedSpecification(String username, String password, Vector<?> args);
        Vector<?> getSpecificationHierarchy(String username, String password, Vector<?> args);
        String setSpecificationAsImplemented(String username, String password, Vector<?> args);
        Vector<?> getSystemUnderTestsOfProject(String projectName);
        Vector<?> getAllSpecificationRepositories();
        Vector<?> getSpecificationHierarchy(Vector<?> repository,Vector<?> sut);
    }
}