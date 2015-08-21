/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.maven.plugin;

import static com.greenpepper.util.CollectionUtil.toVector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import org.jmock.Mock;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.stub.ReturnStub;

import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.runner.repository.AtlassianRepository;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.URIUtil;

public class SpecificationRunnerMojoTest extends AbstractMojoTestCase
{
    private SpecificationRunnerMojo mojo;
    private WebServer ws;
    private Mock handler;

    protected void tearDown() throws Exception
    {
        stopWebServer();
    }

    @SuppressWarnings("unchecked")
    public void setUp() throws Exception
    {
        super.setUp();
        URL pomPath = SpecificationRunnerMojoTest.class.getResource( "pom-runner.xml");
        mojo = (SpecificationRunnerMojo) lookupMojo( "run", URIUtil.decoded(pomPath.getPath()) );
        mojo.classpathElements = new ArrayList( );
        String core = dependency( "greenpepper-core.jar" ).getAbsolutePath();
        mojo.classpathElements.add( core );
        mojo.classpathElements.add( dependency( "guice-1.0.jar" ).getAbsolutePath());
        
        mojo.pluginDependencies = new ArrayList<Artifact>();
        mojo.pluginDependencies.add( new DependencyArtifact( "commons-codec", dependency( "commons-codec-1.3.jar" )) );
        mojo.pluginDependencies.add( new DependencyArtifact( "xmlrpc-common", dependency( "xmlrpc-common-3.1.2.jar" )) );
        mojo.pluginDependencies.add( new DependencyArtifact( "xmlrpc-client", dependency( "xmlrpc-client-3.1.2.jar" )) ); 
        File extension = dependency( "greenpepper-extensions-java.jar" );
        mojo.pluginDependencies.add( new DependencyArtifact( "greenpepper-extensions-java", extension  ));

		assertEquals("en", mojo.locale);
		assertEquals(MySelector.class.getName(), mojo.selector);
		assertTrue(mojo.debug);
    }

    private Repository createLocalRepository(String name) throws URISyntaxException {
        Repository repository = new Repository();
        repository.setName(name);
        repository.setType( FileSystemRepository.class.getName() );
        repository.setRoot(localPath());
        mojo.addRepository(repository);
        return repository;
    }

    private String localPath() throws URISyntaxException {
        return localDir().getAbsolutePath();
    }

    private File localDir() throws URISyntaxException {
        return spec("spec.html").getParentFile();
    }

    private File dependency(String name) throws URISyntaxException
    {
        return new File( classesOutputDir(), name );
    }

    private File classesOutputDir()
        throws URISyntaxException
    {
        return localDir().getParentFile().getParentFile().getParentFile().getParentFile();
    }

    public void testCanRunASingleFileSpecification() throws Exception
    {
        createLocalRepository( "repo" ).addTest( "right.html" );
        mojo.execute();
        
        assertReport( "right.html" );
    }

    public void testShouldSupportSpecifyingCustomSystemUnderDevelopmentSuchAsGuice() throws Exception
    {
        createLocalRepository( "repo" ).addTest( "guice.html" );
        mojo.systemUnderDevelopment = "com.greenpepper.extensions.guice.GuiceSystemUnderDevelopment";
        mojo.execute();

        assertReport( "guice.html" );
    }

    public void testCanRunASuiteOfSpecifications() throws Exception
    {
        createLocalRepository( "repo" ).addSuite( "/" );
        try
        {
            mojo.execute();
        }
        catch (MojoFailureException ignored)
        {
        }
        assertReport( "right.html" );
        assertReport( "wrong.html" );
    }

    public void testSupportsMultipleRepositories() throws Exception
    {
        createLocalRepository( "repo" ).addTest( "right.html" );
        createLocalRepository( "repo" ).addTest( "wrong.html" );

        try
        {
            mojo.execute();
        }
        catch (MojoFailureException ignored)
        {
        }
        assertReport( "right.html" );
        assertReport( "wrong.html" );
    }

    @SuppressWarnings("unchecked")
	public void testShouldSupportCustomRepositoriesSuchAsConfluence() throws Exception
    {
        startWebServer();
    	Vector<?> expected = toVector( "SPACE", "PAGE", Boolean.TRUE, Boolean.TRUE  );
        String right = IOUtil.readContent( spec( "right.html" ) );
        handler.expects( new InvokeOnceMatcher( ) ).method( "getRenderedSpecification" ).with( eq( "" ), eq( "" ), eq( expected ) ).will( new ReturnStub( right ) );

        createAtlassianRepository( "repo" ).addTest("PAGE");
        mojo.execute();

        handler.verify();
        assertReport( "PAGE.html" );
    }

    private Repository createAtlassianRepository(String name) {
        Repository repository = new Repository();
        repository.setName(name);
        repository.setType( AtlassianRepository.class.getName() );
        repository.setRoot("http://localhost:9005/rpc/xmlrpc?includeStyle=true&handler=greenpepper1#SPACE");
        mojo.addRepository(repository);
        return repository;
    }
    
    private Constraint eq(Object o)
    {
    	return new IsEqual(o);
    }

    public void testShouldMakeBuildFailIfThereWereTestFailures() throws Exception
    {
        createLocalRepository("repo").addTest( "wrong.html" );
        try
        {
            mojo.execute();
            fail();
        }
        catch (MojoFailureException expected)
        {
            assertTrue( true );
        }
        assertReport( "wrong.html" );
    }

    public void testShouldMakeBuildFailIfSomeTestsCouldNotBeRun() throws Exception
    {
        createLocalRepository("repo").addTest( "no_such_file.html" );
        try
        {
            mojo.execute();
            fail();
        }
        catch (MojoExecutionException expected)
        {
            assertTrue( true );
        }
    }

    private File reportFileFor(String input)
    {
        return new File( new File(mojo.reportsDirectory, "repo"), URIUtil.flatten(input) );
    }

    private File spec(String name) throws URISyntaxException
    {
        return new File( URIUtil.decoded(SpecificationRunnerMojoTest.class.getResource( name ).getPath()) );
    }

    private void assertReport( String reportName ) {
        File out = reportFileFor( reportName );
        assertTrue( out.exists() );
        long length = out.length();
        out.delete();
        assertTrue( length > 0 );
    }

    private void startWebServer() throws XmlRpcException, IOException
    {
        ws = new WebServer( 9005 );
        XmlRpcServer rpcServer = ws.getXmlRpcServer();
        Map<String,String> handlersMap = new HashMap<String, String>();
        handlersMap.put("greenpepper1", MockHandler.class.getName());
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.load(Thread.currentThread().getContextClassLoader(), handlersMap);
        rpcServer.setHandlerMapping(phm);
        ws.start();
        handler = new Mock(Handler.class);
        MockHandler.jmockhandler =  (Handler) handler.proxy();
    }

    private void stopWebServer()
    {
        if(ws != null) ws.shutdown();
    }

    public static interface Handler {

        String getRenderedSpecification(String username, String password, Vector<Object> args);
    }
    
    public static class MockHandler implements Handler {
        public static Handler jmockhandler;
        
        @Override
        public String getRenderedSpecification(String username, String password, Vector<Object> args) {
            return jmockhandler.getRenderedSpecification(username, password, args);
        }
    }
}