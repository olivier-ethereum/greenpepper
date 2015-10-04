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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.xmlrpc.WebServer;
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

    public void setUp() throws Exception
    {
        super.setUp();
        URL pomPath = SpecificationRunnerMojoTest.class.getResource( "pom-runner.xml");
        mojo = (SpecificationRunnerMojo) lookupMojo( "run", URIUtil.decoded(pomPath.getPath()) );
        mojo.classpathElements = new ArrayList<String>( );
        String core = dependency( "greenpepper-core.jar" ).getAbsolutePath();
        mojo.classpathElements.add( core );
        mojo.classpathElements.add( dependency( "guice-1.0.jar" ).getAbsolutePath());
        
        mojo.pluginDependencies = new ArrayList<Artifact>();
        mojo.pluginDependencies.add( new DependencyArtifact( "commons-codec", dependency( "commons-codec-1.3.jar" )) );
        mojo.pluginDependencies.add( new DependencyArtifact( "xmlrpc", dependency( "xmlrpc-2.0.1.jar" )) ); 
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

    public void testShouldFailIfSingleTestLaunchedAndDefaultRepositoryNotSet() throws MojoFailureException, URISyntaxException {
        createLocalRepository( "repo1" );
        createLocalRepository( "repo2" );
        mojo.testSpecification = "right.html";
        try {
            mojo.execute();
            fail("No default repository set, the test should not run");
        } catch (MojoExecutionException e) {
            assertEquals("A default repository should be set when using '-Dgp.test='. Use '-Dgp.repo=' or specify it in the pom.xml", e.getMessage());
        } 
    }
    
    public void testShouldConsiderASingleRepositoryDefinedAsDefault() throws URISyntaxException, MojoExecutionException, MojoFailureException {
        createLocalRepository( "repoSingle" );
        mojo.testSpecification = "right.html";
        mojo.execute();
        assertReport( "right.html","repoSingle" );
    }
    
    public void testShouldSupportDefaultRepositoryDefinition() throws Exception{
        createLocalRepository( "repo1" );
        createLocalRepository( "repo2" ).setDefault(true);
        mojo.testSpecification = "right.html";
        mojo.execute();
        assertReport( "right.html", "repo2" );
    }
    public void testShouldSupportSelectedRepositoryDefinition() throws Exception{
        createLocalRepository( "repo1" );
        createLocalRepository( "repo2" );
        mojo.testSpecification = "right.html";
        mojo.selectedRepository = "repo2";
        mojo.execute();
        assertReport( "right.html", "repo2" );
    }
    
    public void testShouldOnlyRunTheSpecifiedGPTest() throws Exception{
        createLocalRepository( "repoSingle" ).addTest("wrong.html");
        mojo.testSpecification = "right.html";
        mojo.execute();
        assertEquals(1, mojo.statistics.totalCount());
        assertReport( "right.html","repoSingle" );
    }
    
    public void testShouldSupportOutputFileOnSingleTestRun() throws Exception {
        createLocalRepository( "repoSingle" ).addTest("wrong.html");
        mojo.testSpecification = "right.html";
        mojo.testSpecificationOutput = "target/greenpepper-reports/rightCustomOutput.html";
        mojo.execute();
        assertEquals(1, mojo.statistics.totalCount());
        assertReport( "rightCustomOutput.html","" );
    }
    
    public void testShouldUseSelectedRepositoryPrior() throws Exception {
        createLocalRepository( "repo1" ).setDefault(true);
        createLocalRepository( "repo2" );
        mojo.testSpecification = "right.html";
        mojo.selectedRepository = "repo2";
        mojo.execute();
        assertEquals(1, mojo.statistics.totalCount());
        assertReport( "right.html","repo2" );
    }

    public void testShouldFailIfSelectedRepositoryNotInTheList() throws Exception {
        createLocalRepository( "repo1" ).setDefault(true);
        createLocalRepository( "repo2" );
        mojo.testSpecification = "right.html";
        mojo.selectedRepository = "repo3";
        try {
            mojo.execute();
            fail("Should have thrown an exception");
        } catch (MojoExecutionException e) {
            assertEquals("Repository 'repo3' not found in the list of repository.", e.getMessage());
        }
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

    private File reportFileFor(String input, String repoName)
    {
        return new File( new File(mojo.reportsDirectory, repoName), URIUtil.flatten(input) );
    }

    private File spec(String name) throws URISyntaxException
    {
        return new File( URIUtil.decoded(SpecificationRunnerMojoTest.class.getResource( name ).getPath()) );
    }

    private void assertReport( String reportName ) {
        assertReport(reportName, "repo");
    }
    
    private void assertReport( String reportName, String repoName ) {
        File out = reportFileFor( reportName, repoName );
        assertTrue(out.getPath() + " doesn't exist",  out.exists() );
        long length = out.length();
        out.delete();
        assertTrue(out.getPath() + " should not be empty",  length > 0 );
    }

    private void startWebServer()
    {
        ws = new WebServer( 9005 );
        handler = new Mock( Handler.class );
        ws.addHandler( "greenpepper1", handler.proxy() );
        ws.start();
    }

    private void stopWebServer()
    {
        if(ws != null) ws.shutdown();
    }

    public static interface Handler {

        String getRenderedSpecification(String username, String password, Vector<Object> args);
    }
}