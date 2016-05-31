package com.greenpepper.maven.plugin;

import com.greenpepper.runner.repository.AtlassianRepository;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.hamcrest.CoreMatchers;
import org.junit.After;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.net.URL;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

public class SpecificationNavigatorMojoTest  extends AbstractMojoTestCase {

    private SpecificationNavigatorMojo mojo;

    @After
    protected void tearDown()
            throws Exception
    {
        // required
        super.tearDown();
        cleanIndexFiles();
    }

    private void cleanIndexFiles() {
        File outputFolder = getOutputFolder();
        if (outputFolder.exists()) {
            for (File file : outputFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isFile() && name.endsWith(".index");
                }
            })) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    private DocumentNode docNodeHierarchy() {
        DocumentNode hierachy = new DocumentNode("ROOT");
        hierachy.setIsExecutable(false);
        hierachy.setCanBeImplemented(false);

        DocumentNode page = new DocumentNode("PAGE");
        page.setCanBeImplemented(false);
        page.setIsExecutable(false);
        hierachy.addChildren(page);

        DocumentNode page2 = new DocumentNode("PAGE Executable");
        page2.setCanBeImplemented(true);
        page2.setIsExecutable(true);
        hierachy.addChildren(page2);

        DocumentNode subpage = new DocumentNode("SUBPAGE IMPLEMENTED");
        subpage.setCanBeImplemented(false);
        subpage.setIsExecutable(true);
        page2.addChildren(subpage);

        return hierachy;
    }

    private Repository createMockRepository() throws Exception {
        return createMockRepository("repo");
    }

    private Repository createMockRepository(String reponame) throws Exception {
        Repository repo = getMockRepositoryNoexpectServiceCall(reponame);
        expect(repo.retrieveDocumentHierarchy()).andReturn(docNodeHierarchy()).anyTimes();
        return repo;
    }

    private Repository getMockRepositoryNoexpectServiceCall(String reponame) {
        Repository repo = createMock(Repository.class);
        expect(repo.getProjectName()).andReturn("PROJECT").anyTimes();
        expect(repo.getSystemUnderTest()).andReturn("SUTNAME").anyTimes();
        expect(repo.getName()).andReturn(reponame).anyTimes();
        expect(repo.getType()).andReturn(AtlassianRepository.class.getName()).anyTimes();
        expect(repo.getRoot()).andReturn("http://localhost:19005/rpc/xmlrpc?includeStyle=true&handler=greenpepper1#SPACE").anyTimes();
        mojo.addRepository(repo);
        return repo;
    }

    private File getOutputFolder(){
        return FileUtils.toFile(getClass().getResource("."));
    }

    public void testSelectARepoShouldFailIfNoMatchingFound() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        replay(createMockRepository("repo"));
        replay(createMockRepository("repo1"));
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
        createMockRepository("repo1");
        createMockRepository("repo");
        mojo.selectedRepository = "repo";
        mojo.specOutputDirectory = getOutputFolder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mojo.setPrintWriter(new PrintWriter(outputStream));

        replay(mojo.repositories.get(0),mojo.repositories.get(1));
        mojo.execute();

        String out = outputStream.toString();
        assertThat(out, containsString("PAGE Executable"));
        assertThat(out, containsString("SUBPAGE IMPLEMENTED"));
    }

    @SuppressWarnings("unchecked")
    public void testShouldPrintTheSpecs() throws Exception {
        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mojo.setPrintWriter(new PrintWriter(outputStream));
        mojo.specOutputDirectory = getOutputFolder();
        replay(createMockRepository());

        mojo.execute();

        String out = outputStream.toString();
        assertThat(out, containsString("PAGE Executable"));
        assertThat(out, containsString("SUBPAGE IMPLEMENTED"));
    }

    @SuppressWarnings("unchecked")
    public void testWeShouldUseTheIndexFileWhenExists() throws Exception {

        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        Repository repo = getMockRepositoryNoexpectServiceCall("repo");
        expect(repo.retrieveDocumentHierarchy()).andReturn(docNodeHierarchy()).once();
        replay(repo);
        mojo.specOutputDirectory = getOutputFolder();

        mojo.execute();
        // The second time we call execute, the Mock retrieveDocumentHierarchy method should not be called
        mojo.execute();
        verify(repo);
    }

    public void testWeShouldSendAllPageIfFilterIsEmpty() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();

        mojo.execute();

        assertThat(outputStream.toString(), containsString("[0001]"));
        assertThat(outputStream.toString(), containsString("[0002]"));
        outputStream.reset();
        cleanIndexFiles();

        mojo.specFilter = null;
        mojo.execute();
        assertThat(outputStream.toString(), containsString("[0001]"));
        assertThat(outputStream.toString(), containsString("[0002]"));
    }

    public void testWeShouldBeAbleToSendOnlyImplementedPages() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="[I]";

        mojo.execute();

        String output = outputStream.toString();
        assertThat(output, containsString("[SUBPAGE IMPLEMENTED]"));
        assertThat(output, CoreMatchers.not(containsString("[PAGE Executable]")));
        outputStream.reset();
        cleanIndexFiles();
    }

    private ByteArrayOutputStream prepareTestWithMock() throws Exception {
        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-tree.xml");
        mojo = (SpecificationNavigatorMojo) lookupMojo("tree", URIUtil.decoded(pomPath.getPath()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mojo.setPrintWriter(new PrintWriter(outputStream));
        mojo.specOutputDirectory = getOutputFolder();
        mojo.specFilter = "";

        Repository repo = createMockRepository();
        replay(repo);
        return outputStream;
    }

    public void testWeShouldBeAbleToSendOnlyNonImplementedPages() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="[!I]";

        mojo.execute();

        assertThat(outputStream.toString(), containsString("[PAGE Executable]"));
        assertThat(outputStream.toString(), CoreMatchers.not(containsString("[SUBPAGE IMPLEMENTED]")));
        outputStream.reset();
    }

    public void testWeShouldBeAbleToFilterOnSubStringUPPERCASE() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="CUTABL";

        mojo.execute();

        String actual = outputStream.toString();
        assertThat(actual, containsString("[PAGE Executable]"));
        assertThat(actual, CoreMatchers.not(containsString("[SUBPAGE IMPLEMENTED]")));
        outputStream.reset();
    }

    public void testWeShouldBeAbleToFilterOnSubStringlowercase() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="plemente";

        mojo.execute();

        String actual = outputStream.toString();
        assertThat(actual, CoreMatchers.not(containsString("[PAGE Executable]")));
        assertThat(actual, containsString("[SUBPAGE IMPLEMENTED]"));
        outputStream.reset();
    }

    public void testWeShouldBeAbleToFilterOnRegexp() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="[RE].*PAGE .+";

        mojo.execute();

        String actual = outputStream.toString();
        assertThat(actual, containsString("[PAGE Executable]"));
        assertThat(actual, containsString("[SUBPAGE IMPLEMENTED]"));
        outputStream.reset();
        cleanIndexFiles();
    }

    public void testWeShouldBeAbleToFilterOnCombineEverything() throws Exception {
        ByteArrayOutputStream outputStream = prepareTestWithMock();
        mojo.specFilter="[!I][RE].*cutab.+";
        mojo.execute();

        String actual = outputStream.toString();
        assertThat(actual, containsString("[PAGE Executable]"));
        assertThat(actual, CoreMatchers.not(containsString("[SUBPAGE IMPLEMENTED]")));
        outputStream.reset();

        mojo.specFilter="[I][RE].*cutab.+";
        mojo.execute();
        actual = outputStream.toString();
        assertThat(actual, CoreMatchers.not(containsString("[PAGE Executable]")));
        assertThat(actual, CoreMatchers.not(containsString("[SUBPAGE IMPLEMENTED]")));
        outputStream.reset();


        mojo.specFilter="[I]page";
        mojo.execute();
        actual = outputStream.toString();
        assertThat(actual, CoreMatchers.not(containsString("[PAGE Executable]")));
        assertThat(actual, containsString("[SUBPAGE IMPLEMENTED]"));
        outputStream.reset();
    }
}