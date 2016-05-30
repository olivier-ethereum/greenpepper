package com.greenpepper.maven.plugin;

import com.greenpepper.runner.repository.AtlassianRepository;
import com.greenpepper.util.URIUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.xmlrpc.WebServer;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

public class SpecificationNavigatorMojoTest  extends AbstractMojoTestCase {

    private SpecificationNavigatorMojo mojo;
    private WebServer ws;
    private Handler handler;

    @Before
    protected void setUp() throws Exception {
        // required
        super.setUp();
        startWebServer();
    }

    @After
    protected void tearDown()
            throws Exception
    {
        // required
        super.tearDown();
        stopWebServer();
    }

    @SuppressWarnings("unchecked")
    public void testShouldFailIfNoSUT() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        createAtlassianRepository("repo");
        expect(handler.getSystemUnderTestsOfProject("PROJECT")).andReturn(new Vector());

        replay(handler);
        try {
            mojo.execute();
            fail("No exception thrown");
        } catch (MojoExecutionException e) {
            // ok
        }
    }


    public void testhouldFailIfNoRepo() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        try {
            mojo.execute();
            fail("No exception thrown");
        } catch (MojoExecutionException e) {
            // ok
        }
    }

    public void testSelectARepoShouldFailIfNoMatchingFound() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        createAtlassianRepository("repo");
        createAtlassianRepository("repo1");
        mojo.selectedRepository = "repo2";
        try {
            mojo.execute();
            fail("No exception thrown");
        } catch (MojoExecutionException e) {
            // ok
        }
    }

    @SuppressWarnings("unchecked")
    public void testSelectARepo() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        createAtlassianRepository("repo1");
        createAtlassianRepository("repo");
        mojo.selectedRepository = "repo";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mojo.setPrintWriter(new PrintWriter(outputStream));

        Vector systemUnderTests = (Vector) systemUnderTests();
        expect(handler.getSystemUnderTestsOfProject("PROJECT")).andReturn(systemUnderTests);
        Vector specificationRepositories = (Vector) specificationRepositories();
        expect(handler.getAllSpecificationRepositories()).andReturn(specificationRepositories);
        expect(handler.getSpecificationHierarchy((Vector)specificationRepositories.get(0), (Vector)systemUnderTests.get(0)))
                .andReturn((Vector)specHierarchy());

        replay(handler);

        mojo.execute();
        verify(handler);

        String out = outputStream.toString();
        assertThat(out, containsString("PAGE Executable"));
        assertThat(out, containsString("SUBPAGE IMPLEMENTED"));
    }

    @SuppressWarnings("unchecked")
    public void testShouldPrintTheSpecs() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        createAtlassianRepository("repo");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mojo.setPrintWriter(new PrintWriter(outputStream));

        Vector systemUnderTests = (Vector) systemUnderTests();
        expect(handler.getSystemUnderTestsOfProject("PROJECT")).andReturn(systemUnderTests);
        Vector specificationRepositories = (Vector) specificationRepositories();
        expect(handler.getAllSpecificationRepositories()).andReturn(specificationRepositories);
        expect(handler.getSpecificationHierarchy((Vector)specificationRepositories.get(0), (Vector)systemUnderTests.get(0)))
            .andReturn((Vector)specHierarchy());

        replay(handler);
        mojo.execute();
        verify(handler);

        String out = outputStream.toString();
        assertThat(out, containsString("PAGE Executable"));
        assertThat(out, containsString("SUBPAGE IMPLEMENTED"));
    }


    @SuppressWarnings("unchecked")
    private Vector<Vector<?>> specificationRepositories() {
        Vector<Vector<?>> repos = new Vector<Vector<?>>();
        repos.add(new Vector(Arrays.asList("REPO NAME", "Confluence-SPACE" )));
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

    private Repository createAtlassianRepository(String name) {
        Repository repository = new Repository();
        repository.setName(name);
        repository.setType( AtlassianRepository.class.getName() );
        repository.setRoot("http://localhost:19005/rpc/xmlrpc?includeStyle=true&handler=greenpepper1#SPACE");
        repository.setProjectName("PROJECT");
        repository.setSystemUnderTest("SUTNAME");
        mojo.addRepository(repository);
        return repository;
    }


    private void startWebServer() {
        ws = new WebServer(19005);
        handler = createMock(Handler.class);
        ws.addHandler("greenpepper1", handler);
        ws.start();
    }

    private void stopWebServer() {
        if (ws != null) ws.shutdown();
    }

    public interface Handler {

        Vector<?> getSystemUnderTestsOfProject(String projectName);
        Vector<?> getAllSpecificationRepositories();
        Vector<?> getSpecificationHierarchy(Vector<?> repository,Vector<?> sut);
    }

}