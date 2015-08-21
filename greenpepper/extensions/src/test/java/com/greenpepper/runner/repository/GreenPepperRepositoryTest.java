package com.greenpepper.runner.repository;

import static com.greenpepper.util.CollectionUtil.toVector;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.document.Document;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.util.TestCase;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GreenPepperRepositoryTest extends TestCase
{
    private static WebServer ws;
	private Mockery context = new JUnit4Mockery();
    private Handler handler;
    private String dummySpec;

    public static Test suite() {
        return new TestSetup(new TestSuite(GreenPepperRepositoryTest.class)) {
            @Override
            protected void setUp() throws Exception
            {
                ws = new WebServer( 9005 );
                XmlRpcServer rpcServer = ws.getXmlRpcServer();
                Map<String,String> handlersMap = new HashMap<String, String>();
                handlersMap.put("greenpepper1", MockHandler.class.getName());
                PropertyHandlerMapping phm = new PropertyHandlerMapping();
                phm.load(Thread.currentThread().getContextClassLoader(), handlersMap);
                rpcServer.setHandlerMapping(phm);
                ws.start();
            }

            @Override
            protected void tearDown() throws Exception
            {
                ws.shutdown();
            }
        };
    }

    protected void setUp()
    {
        handler = context.mock( Handler.class );
        MockHandler.jmockHandler = handler;

        dummySpec = TestStringSpecifications.SimpleAlternateCalculatorTest;
    }

    @SuppressWarnings("unchecked")
	public void testCanProvideAListOfSpecificationsGivenASutAndARepository() throws Exception
    {
    	final Vector<String> page1 = pageDefinition(1);
    	final Vector<String> page2 = pageDefinition(2);
    	final Vector<String> page3 = pageDefinition(3);
        List<String> expected = toVector( "REPO/PAGE 1", "REPO/PAGE 2", "REPO/PAGE 3" );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1, page2, page3)));
		}});

        DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT");
        List<String> actual = repo.listDocuments( "REPO" );

        assertEquals( expected, actual );
    }

    @SuppressWarnings("unchecked")
	public void testCanDownloadPageContentFromConfluence() throws Exception
    {
    	final Vector<String> page1 = confPageDefinition();
    	final Vector<?> expected = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected);
			will(returnValue(dummySpec));
		}});

        DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT");
        Document document = repo.loadDocument( "REPO/PAGE TITLE" );
        assertEquals("html", document.getType());
        assertSpecification( dummySpec, document );
    }

    @SuppressWarnings("unchecked")
	public void testhandlesImplementedVersionAttribute() throws Exception
    {
    	final Vector<String> page1 = confPageDefinition();
    	final Vector<?> expected = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.FALSE  );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected);
			will(returnValue(dummySpec));
		}});

        DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?implemented=false&handler=greenpepper1&sut=SUT");
        Document document = repo.loadDocument( "REPO/PAGE TITLE" );
        assertEquals("html", document.getType());
        assertSpecification( dummySpec, document );
    }

	@SuppressWarnings("unchecked")
	public void testHandlesPostBackExecutionResult() throws Exception
	{
		final Vector<String> page1 = confPageDefinition();
		final Vector<?> expected1 = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE  );
		final Vector<?> expected2 = toVector( "SPACE%20KEY", "PAGE TITLE", "SUT", TestStringSpecifications.SimpleAlternateCalculatorXmlReport );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected1);
			will(returnValue(dummySpec));
			one(handler).saveExecutionResult("user", "pwd", expected2);
			will(returnValue("<success>"));
		}});

		DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT&postExecutionResult=true");
		Document document = repo.loadDocument( "REPO/PAGE TITLE" );
		assertEquals("html", document.getType());
		assertSpecification( dummySpec, document );

		document.done();
	}

	@SuppressWarnings("unchecked")
	public void testHandlesNoSuchMethodPostBackExecutionResultQuietly() throws Exception
	{
		final Vector<String> page1 = confPageDefinition();
		final Vector<?> expected1 = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE  );

		final RuntimeException noSuchMethodException = new RuntimeException(new NoSuchMethodException());
		final Vector<?> expected2 = toVector( "SPACE%20KEY", "PAGE TITLE", "SUT", TestStringSpecifications.SimpleAlternateCalculatorXmlReport );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected1);
			will(returnValue(dummySpec));
			one(handler).saveExecutionResult("user", "pwd", expected2);
			will(throwException(noSuchMethodException));
		}});

		DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT&postExecutionResult=true");
		Document document = repo.loadDocument( "REPO/PAGE TITLE" );
		assertEquals("html", document.getType());
		assertSpecification( dummySpec, document );

		document.done();
	}

	@SuppressWarnings("unchecked")
	public void testHandlesOtherXmlRpcExceptionPostBackExecutionResult() throws Exception
	{
		final Vector<String> page1 = confPageDefinition();
		final Vector<?> expected1 = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE  );

		final XmlRpcException noSuchMethodException = new XmlRpcException(0, "junit");
		final Vector<?> expected2 = toVector( "SPACE%20KEY", "PAGE TITLE", "SUT", TestStringSpecifications.SimpleAlternateCalculatorXmlReport );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected1);
			will(returnValue(dummySpec));
			one(handler).saveExecutionResult("user", "pwd", expected2);
			will(throwException( noSuchMethodException ));

		}});

		DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT&postExecutionResult=true");
		Document document = repo.loadDocument( "REPO/PAGE TITLE" );
		assertEquals("html", document.getType());
		assertSpecification( dummySpec, document );

		try
		{
			document.done();

			fail();
		}
		catch (Exception e)
		{
			// ok
		}
	}

	@SuppressWarnings("unchecked")
	public void testHandlesUnsuccessfullPostBackExecutionResult() throws Exception
	{
		final Vector<String> page1 = confPageDefinition();
		final Vector<?> expected1 = toVector( "SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE  );

		final Vector<?> expected2 = toVector( "SPACE%20KEY", "PAGE TITLE", "SUT", TestStringSpecifications.SimpleAlternateCalculatorXmlReport );

		context.checking(new Expectations()
		{{
			one(handler).getListOfSpecificationLocations("REPO", "SUT");
			will(returnValue(toVector(page1)));
			one(handler).getRenderedSpecification("user", "pwd", expected1);
			will(returnValue(dummySpec));
			one(handler).saveExecutionResult("user", "pwd", expected2);
			will(returnValue("<failure>"));
		}});

		DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1&sut=SUT&postExecutionResult=true");
		Document document = repo.loadDocument( "REPO/PAGE TITLE" );
		assertEquals("html", document.getType());
		assertSpecification( dummySpec, document );

		try
		{
			document.done();

			fail();
		}
		catch (Exception e)
		{
			// ok
		}
	}

    public void testComplainsIfArgumentsAreMissing() throws Exception
    {
        try
        {
            DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1");
            repo.listDocuments( "REPO" );
            fail();
        }
        catch (IllegalArgumentException expected)
        {
            assertTrue(true);
        }
        
        try
        {
            DocumentRepository repo = new GreenPepperRepository("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1");
            repo.listDocuments( "http://localhost:9005/rpc/xmlrpc?sut=SUT&includeStyle=true" );
            fail();
        }
        catch (IllegalArgumentException expected)
        {
            assertTrue(true);
        }
    }

    private void assertSpecification( String expectedSpec, Document actualDoc)
    {
        assertNotNull( actualDoc );
        StringWriter buffer = new StringWriter();
        actualDoc.print( new PrintWriter( buffer ) );
        assertEquals( expectedSpec, buffer.toString() );
    }
    
    private Vector<String> confPageDefinition()
    {
    	Vector<String> def = new Vector<String>();
    	def.add(AtlassianRepository.class.getName());
    	def.add("http://localhost:9005/rpc/xmlrpc?handler=greenpepper1#SPACE%20KEY");
    	def.add("user");
    	def.add("pwd");
    	def.add("PAGE TITLE");
    	
    	return def;
    }
    
    private Vector<String> pageDefinition(int identifier)
    {
    	Vector<String> def = new Vector<String>();
    	def.add("repoClass");
    	def.add("testUrl"+identifier);
    	def.add("user"+identifier);
    	def.add("pwd"+identifier);
    	def.add("PAGE "+identifier);
    	
    	return def;
    }
    
    public static interface Handler
    {
        Vector<Vector<String>> getListOfSpecificationLocations(String repoUID, String sutName);
        String getRenderedSpecification(String username, String password, Vector<?> args);
		String saveExecutionResult(String username, String password, Vector<?> args);
    }
    
    public static class MockHandler implements Handler
    {

        public static Handler jmockHandler;
        
        
        @Override
        public Vector<Vector<String>> getListOfSpecificationLocations(String repoUID, String sutName) {
            return jmockHandler.getListOfSpecificationLocations(repoUID, sutName);
        }

        @Override
        public String getRenderedSpecification(String username, String password, Vector<?> args) {
            return jmockHandler.getRenderedSpecification(username, password, args);
        }

        @Override
        public String saveExecutionResult(String username, String password, Vector<?> args) {
            return jmockHandler.saveExecutionResult(username, password, args);
        }
    }
}
