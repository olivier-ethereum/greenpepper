package com.greenpepper.agent.server;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.runner.Main;
import com.greenpepper.runner.repository.AtlassianRepository;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.ClasspathSet;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.RepositoryTypeClass;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.URIUtil;
import junit.framework.TestCase;

public class AgentTest extends TestCase
{
	private static final boolean IS_OS_LINUX = System.getProperty("os.name").indexOf("Linux") != -1;
	private int CURRENT_PORT = 18887;

	private Mockery context = new JUnit4Mockery();
	private WebServer fakeServer;
    private Handler handler;

	public void setUp()
	{
		CURRENT_PORT++;
		fakeServer = new WebServer(CURRENT_PORT);
        handler = context.mock( Handler.class );
        fakeServer.addHandler( "greenpepper1", handler );
        fakeServer.start();
		
		Agent.main(new String[0]);
	}
	
	public void tearDown()
	{
		Agent.shutdown();
		fakeServer.shutdown();
		//Really not proud of that code. Had to put a 5 seconds delay to avoid JVM_bind exception. Anyone has a better idea?
		try {
			Thread.sleep(5000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testCanExecuteASpecificationInJava() throws Exception
	{
    	final Vector<?> expected = CollectionUtil.toVector( "SPACE", "My Test", Boolean.FALSE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			oneOf(handler).getRenderedSpecification("", "", expected);
			will(returnValue(testJavaContent()));
		}});

        XmlRpcClient xmlrpc = new XmlRpcClient("http://localhost:7777");
        Vector params = CollectionUtil.toVector(getJavaRunner().marshallize(), getJavaSystemUnderTest().marshallize(), getJavaSpecification().marshallize(), true, "", "en");
        Vector<Object> execParams = (Vector<Object>)xmlrpc.execute("greenpepper-agent1.execute", params);
		Execution execution = XmlRpcDataMarshaller.toExecution(execParams);
		assertEquals(2, execution.getSuccess());
		assertEquals(0, execution.getErrors());
		assertEquals(0, execution.getIgnored());
		assertEquals(1, execution.getFailures());
		assertNull(execution.getExecutionErrorId());
	}
	
	@SuppressWarnings("unchecked")
	public void testCanExecuteASpecificationInDotnet() throws Exception
	{
		final Vector<?> expected = CollectionUtil.toVector( "SPACE", "My%20Test", Boolean.FALSE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			oneOf(handler).getRenderedSpecification("", "", expected);
			will(returnValue(testDotnetContent()));

		}});

        XmlRpcClient xmlrpc = new XmlRpcClient("http://localhost:7777");
        Vector params = CollectionUtil.toVector(getDotnetRunner().marshallize(), getDotnetSystemUnderTest().marshallize(), getDotnetSpecification().marshallize(), true, "", "en");
        Vector<Object> execParams = (Vector<Object>)xmlrpc.execute("greenpepper-agent1.execute", params);
		Execution execution = XmlRpcDataMarshaller.toExecution(execParams);
		assertEquals(2, execution.getSuccess());
		assertEquals(0, execution.getErrors());
		assertEquals(0, execution.getIgnored());
		assertEquals(1, execution.getFailures());
		assertNull(execution.getExecutionErrorId());
	}
	
	private Runner getJavaRunner()
	{
		Runner runner = Runner.newInstance("My Runner");
		runner.setMainClass(Main.class.getName());
		runner.setEnvironmentType(EnvironmentType.newInstance("JAVA"));
		runner.setCmdLineTemplate("java -mx252m -cp ${classpaths} ${mainClass} ${inputPath} ${outputPath} -l ${locale} -r ${repository} --xml");
		ClasspathSet classPaths = new ClasspathSet();
		classPaths.add(getPathJava("greenpepper-core.jar"));
		classPaths.add(getPathJava("greenpepper-extensions-java.jar"));
		classPaths.add(getPathJava("commons-codec-1.3.jar"));
		classPaths.add(getPathJava("xmlrpc-2.0.1.jar"));
		runner.setClasspaths(classPaths);
		return runner;
	}

	private Runner getDotnetRunner()
	{
		Runner runner = Runner.newInstance("My Runner");
		runner.setMainClass(Main.class.getName());
		runner.setEnvironmentType(EnvironmentType.newInstance(".NET"));
		runner.setCmdLineTemplate((IS_OS_LINUX ? "mono " : "") + getPathDotNet("GreenPepper.exe") + " ${inputPath} ${outputPath} -a ${classpaths} -r ${repository} --xml");
		ClasspathSet classPaths = new ClasspathSet();
		classPaths.add(getPathDotNet("CookComputing.XmlRpc.dll"));
		classPaths.add(getPathDotNet("GreenPepper.Core.dll"));
		classPaths.add(getPathDotNet("GreenPepper.Extensions.dll"));
		runner.setClasspaths(classPaths);
		return runner;
	}
	
	private Specification getJavaSpecification() throws GreenPepperServerException
	{
		Repository repository = Repository.newInstance("My Repository");
		repository.setBaseTestUrl("http://localhost:"+CURRENT_PORT+"/rpc/xmlrpc?handler=greenpepper1#SPACE");
		RepositoryType type = RepositoryType.newInstance("JIRA");
		type.setTestUrlFormat("%s/%s");
		Set<RepositoryTypeClass> classes = new HashSet<RepositoryTypeClass>();
		classes.add(RepositoryTypeClass.newInstance(type, EnvironmentType.newInstance("JAVA"), AtlassianRepository.class.getName()));
		type.setRepositoryTypeClasses(classes);
		repository.setType(type);
		repository.setProject(Project.newInstance("My Project"));
		
		Specification specification = Specification.newInstance("My Test");
		repository.addSpecification(specification);
		return specification;
	}
	
	private Specification getDotnetSpecification() throws GreenPepperServerException
	{
		Repository repository = Repository.newInstance("My Repository");
		repository.setBaseTestUrl("http://localhost:"+CURRENT_PORT+"/rpc/xmlrpc?handler=greenpepper1#SPACE");
		RepositoryType type = RepositoryType.newInstance("JIRA");
		type.setTestUrlFormat("%s/%s");
		Set<RepositoryTypeClass> classes = new HashSet<RepositoryTypeClass>();
		classes.add(RepositoryTypeClass.newInstance(type, EnvironmentType.newInstance(".NET"), "GreenPepper.Repositories.AtlassianRepository"));
		type.setRepositoryTypeClasses(classes);
		repository.setType(type);
		repository.setProject(Project.newInstance("My Project"));
		
		Specification specification = Specification.newInstance("My Test");
		repository.addSpecification(specification);
		return specification;
	}
	
	private SystemUnderTest getJavaSystemUnderTest()
	{
		SystemUnderTest sut = SystemUnderTest.newInstance("My Sut");
		ClasspathSet classPaths = new ClasspathSet();
		classPaths.add(getPathJava("greenpepper-core-fixtures.jar"));
		classPaths.add(getPathJava("greenpepper-core-tests.jar"));
		sut.setProject(Project.newInstance("My Project"));
		sut.setFixtureClasspaths(classPaths);
		return sut;
	}
	
	private SystemUnderTest getDotnetSystemUnderTest()
	{
		SystemUnderTest sut = SystemUnderTest.newInstance("My Sut");
		ClasspathSet classPaths = new ClasspathSet();
		classPaths.add(getPathDotNet("GreenPepper.Fixtures.dll"));
		classPaths.add(getPathDotNet("GreenPepper.Core.Tests.dll"));
		sut.setProject(Project.newInstance("My Project"));
		sut.setFixtureClasspaths(classPaths);
		return sut;
	}

	private String getPathJava(String fileName)
	{
		return URIUtil.decoded(new File(AgentTest.class.getResource("/runners/java/" + fileName).getPath()).getAbsolutePath());
	}

	private String getPathDotNet(String fileName)
	{
		String path = "/runners/dotnet/" + (IS_OS_LINUX ? "mono/" : "");
		return URIUtil.decoded(new File(AgentTest.class.getResource(path + fileName).getPath()).getAbsolutePath());
	}

    private String testJavaContent()
    {
        return "<html><table border='1' cellspacing='0'>" +
               "<tr><td>Rule for</td><td colspan='3'>com.greenpepper.interpreter.CellAnnotationFixture</td></tr>" +
               "<tr><td>comparisonValue</td><td>returnedValue</td><td>annotation?</td></tr>" +
               "<tr><td>1</td><td>2</td><td>wrong</td></tr>" +
               "<tr><td>2</td><td>2</td><td>right</td></tr>" +
               "<tr><td>2</td><td>3</td><td>right</td></tr>" +
               "</table></html>";
    }

    private String testDotnetContent()
    {
        return "<html><table border='1' cellspacing='0'>" +
               "<tr><td>Rule for</td><td colspan='3'>GreenPepper.Interpreters.CellAnnotationFixture</td></tr>" +
               "<tr><td>comparisonValue</td><td>returnedValue</td><td>annotation?</td></tr>" +
               "<tr><td>1</td><td>2</td><td>wrong</td></tr>" +
               "<tr><td>2</td><td>2</td><td>right</td></tr>" +
               "<tr><td>2</td><td>3</td><td>right</td></tr>" +
               "</table></html>";
    }

    public static class MyFixture {
        public int a;
        public int b;

        public MyFixture() {}

        public int sum() {
            return a+b;
        }

    }

    public static interface Handler
    {
        String getRenderedSpecification(String username, String password, Vector<?> args);
    }
}