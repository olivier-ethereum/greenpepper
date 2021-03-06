package com.greenpepper.maven.plugin;

import com.greenpepper.document.Document;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.maven.plugin.spy.impl.JavaFixtureGenerator;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class FixtureGeneratorMojoTest  extends AbstractMojoTestCase {

    private FixtureGeneratorMojo mojo;
    private File srcDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        URL pomPath = FixtureGeneratorMojoTest.class.getResource("pom-genfixture.xml");
        mojo = (FixtureGeneratorMojo) lookupMojo("generate-fixtures", URIUtil.decoded(pomPath.getPath()));
        mojo.fixtureGeneratorClass = JavaFixtureGenerator.class.getName();
        srcDir = getFixtureSrcDir();
        mojo.setBasedir(srcDir.getParentFile());
        mojo.setFixtureSourceDirectory(srcDir);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        File srcDir = getFixtureSrcDir();
        FileUtils.forceDelete(srcDir);
    }

    private File getFixtureSrcDir() throws IOException {
        URL currentPackage = getClass().getResource(".");
        File srcDir = new File(toFile(currentPackage), "src");
        FileUtils.forceMkdir(srcDir);
        return srcDir;
    }

    private File loadSpecification(String name) {
        URL resource = getClass().getResource("specs/" + name);
        return toFile(resource);
    }

    public void testGenerateFixture() throws Exception {
        mojo.specification = loadSpecification("CollectionOfValuesSample.html");
        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/application/phonebook/PhoneBookFixture.java").exists());
    }

    public void testGenerateMultipleFixtures() throws Exception {
        mojo.specification = loadSpecification("multifixture.html");
        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/BankFixture.java").exists());
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/ShopFixture.java").exists());
    }

    public void testGenerateFixtureWithPackageInName() throws Exception {
        mojo.specification = loadSpecification("right.html");
        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java").exists());
    }

    public void testGenerateFixtureAndUpdateIt() throws Exception {
        mojo.specification = loadSpecification("right.html");
        mojo.execute();

        File fixtureFile = new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        assertTrue(fixtureFile.exists());
        String javaclassSrc = readFileToString(fixtureFile);
        String moddedSrc = replace(javaclassSrc, "throw new UnsupportedOperationException(\"Not yet implemented!\");", "return null;");
        writeStringToFile(fixtureFile, moddedSrc);

        mojo.specification = loadSpecification("right1.html");
        mojo.execute();

        fixtureFile = new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        assertTrue(fixtureFile.exists());
        String actual = readFileToString(fixtureFile);
        assertThat(actual, containsString("thatTheAnswerIs"));
        assertThat(actual, containsString("echo"));
        assertThat(actual, containsString("return null;"));
        assertEquals("We don't generate a default constructor",  0, countMatches(actual,"public EchoFixture() {"));
        assertEquals("We generate a constructor with parameter",  1, countMatches(actual,"public EchoFixture(String param1) {"));


    }

    public void testShouldNotReformatTheCode() throws MojoFailureException, MojoExecutionException, IOException {
        mojo.specification = loadSpecification("right.html");
        mojo.execute();
        File fixtureFile = new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        assertTrue(fixtureFile.exists());
        String javaclassSrc = readFileToString(fixtureFile);
        assertThat(javaclassSrc, containsString("\tpublic String echo"));

        // let's reformat the code
        writeStringToFile(fixtureFile, javaclassSrc.replaceAll("\t", "  "));

        mojo.specification = loadSpecification("right1.html");
        mojo.execute();

        javaclassSrc = readFileToString(fixtureFile);
        assertEquals("We should not have a tab for pre existing methods",  0, countMatches(javaclassSrc,"\tpublic String echo"));
    }

    public void testShouldFindTheSourceInARandomPackage() throws MojoFailureException, MojoExecutionException, IOException {
        mojo.specification = loadSpecification("right.html");
        mojo.execute();
        File fixtureFile = new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        assertTrue(fixtureFile.exists());
        String previousContent = readFileToString(fixtureFile);

        mojo.specification = loadSpecification("multifixture-multiimport.html");
        mojo.execute();

        fixtureFile = getFile(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        String newContent = readFileToString(fixtureFile);
        assertThat(previousContent, not(equalTo(newContent)));
        assertTrue(getFile(srcDir, "BankFixture.java").exists());
        assertTrue(getFile(srcDir, "ShopFixture.java").exists());

        mojo.specification = loadSpecification("multifixture.html");
        mojo.execute();
        assertTrue(getFile(srcDir, "com/greenpepper/samples/fixture/BankFixture.java").exists());
        assertTrue(getFile(srcDir, "com/greenpepper/samples/fixture/ShopFixture.java").exists());
    }

    public void testShouldFindMethodsInSupertype() throws MojoFailureException, MojoExecutionException, IOException {
        mojo.specification = loadSpecification("supertype.html");
        copyExistingSrcToTestingSrc();

        String previousContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/ShopFixture.java"));

        mojo.execute();

        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/ShopFixture.java"));

        assertThat(previousContent, equalTo(newContent));

    }

    private void copyExistingSrcToTestingSrc() throws IOException {
        URL currentPackage = getClass().getResource(".");
        File existingSrc = new File(toFile(currentPackage), "existingSrc");
        FileUtils.copyDirectory(existingSrc, srcDir);
    }

    public void testShouldDetectCollectionProviderAnnotation() throws Exception {
        copyExistingSrcToTestingSrc();
        String previousContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookFixture.java"));
        mojo.specification = loadSpecification("CollectionOfValuesWithoutImport.html");
        JavaFixtureGenerator javaFixtureGenerator = new JavaFixtureGenerator();
        javaFixtureGenerator.defaultPackage = "com.greenpepper.samples.fixture";
        mojo.fixtureGenerator = javaFixtureGenerator;

        mojo.execute();

        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookFixture.java"));

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookFixture.java").exists());
        assertThat(previousContent, equalTo(newContent));
    }


    public void testShouldGenerateCollectionProviderAnnotation() throws Exception {
        mojo.specification = loadSpecification("CollectionOfValuesWithoutImport.html");
        JavaFixtureGenerator javaFixtureGenerator = new JavaFixtureGenerator();
        javaFixtureGenerator.defaultPackage = "com.greenpepper.samples.fixture";
        mojo.fixtureGenerator = javaFixtureGenerator;

        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookFixture.java").exists());
        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookFixture.java"));
        assertThat(newContent, containsString("Collection"));
        assertThat(newContent, containsString("PhoneBookFixture.PhoneBookEntriesItem"));
    }

    public void testShouldDetectQueryMethod() throws Exception {
        copyExistingSrcToTestingSrc();
        String previousContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithQueryMethodFixture.java"));
        mojo.specification = loadSpecification("query-method.html");

        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/WithQueryMethodFixture.java").exists());
        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithQueryMethodFixture.java"));
        assertThat(previousContent, equalTo(newContent));
    }

    public void testShouldUseTheUniqueImportAsPackage() throws Exception {
        mojo.specification = loadSpecification("dowith-setup.html");

        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java").exists());
        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java"));
        assertThat(newContent, containsString("package com.greenpepper.samples.fixture;"));
    }

    public void testShouldGenerateEnterRowAnnotation() throws Exception {
        mojo.specification = loadSpecification("dowith-setup.html");

        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java").exists());
        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java"));
        assertThat(newContent, containsString("@EnterRow"));
        assertThat(newContent, containsString("phoneBookEntries"));
    }

    public void testShouldDetectEnterRowMethod() throws Exception {
        copyExistingSrcToTestingSrc();
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethod2Fixture.java").exists());
        String previousContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethod2Fixture.java"));
        mojo.specification = loadSpecification("dowith-setup2.html");

        mojo.execute();

        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethod2Fixture.java"));
        assertThat(newContent, equalTo(previousContent));
    }


    public void testShouldDetectEnterRowAnnotation() throws Exception {
        copyExistingSrcToTestingSrc();
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java").exists());
        String previousContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java"));
        mojo.specification = loadSpecification("dowith-setup.html");

        mojo.execute();

        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/WithEnterRowMethodFixture.java"));
        assertThat(newContent, equalTo(previousContent));
    }

    public void testShouldGenerateEnterRowAnnotationInSetup() throws Exception {
        mojo.specification = loadSpecification("setup.html");

        mojo.execute();

        File fixture = getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupFixture.java");
        assertTrue(fixture.exists());
        String newContent = readFileToString(fixture);
        assertThat(newContent, containsString("@EnterRow"));
        assertThat(newContent, containsString("public void enterRow()"));
    }

    public void testShouldGenerateCollectionProviderAnnotationInListof() throws Exception {
        mojo.specification = loadSpecification("list.html");

        mojo.execute();

        File fixture = getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookListFixture.java");
        assertTrue(fixture.exists());
        String newContent = readFileToString(fixture);
        assertThat(newContent, containsString("@CollectionProvider"));
        assertThat(newContent, containsString("public Collection<"));
        assertThat(newContent, containsString("query()"));
    }

    public void testShouldDetectEnterRowAnnotationAndMethodOnSetups() throws Exception {
        copyExistingSrcToTestingSrc();
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithAnnotationFixture.java").exists());
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithMethodFixture.java").exists());
        String previousContentWithMethod = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithMethodFixture.java"));
        String previousContentWithAnnotation = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithAnnotationFixture.java"));
        mojo.specification = loadSpecification("setup.html");

        mojo.execute();

        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithMethodFixture.java"));
        String newContentWithAnnotation = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookSetupWithAnnotationFixture.java"));
        assertThat(newContent, equalTo(previousContentWithMethod));
        assertThat(newContentWithAnnotation, equalTo(previousContentWithAnnotation));
    }

    public void testShouldDetectCollectionAnnotationAndMethodOnSetups() throws Exception {
        copyExistingSrcToTestingSrc();
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithAnnotationFixture.java").exists());
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithMethodFixture.java").exists());
        String previousContentWithAnnotation = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithAnnotationFixture.java"));
        String previousContentWithMethod = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithMethodFixture.java"));
        mojo.specification = loadSpecification("list.html");

        mojo.execute();

        String newContentWithAnnotation = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithAnnotationFixture.java"));
        String newContent = readFileToString(getFile(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithMethodFixture.java"));
        assertThat(newContent, equalTo(previousContentWithMethod));
        assertThat(newContentWithAnnotation, equalTo(previousContentWithAnnotation));
    }

    public void testShouldGenerateFixtureWithSpecialChars() throws Exception {

        String specName = "special-chars-infixture";
        Repository repo = createMockRepository("repo", specName);
        mojo.repositories = Collections.singletonList(repo);

        mojo.specificationName = specName;

        mojo.execute();

        File fixture = new File(srcDir, "com/greenpepper/samples/application/phonebook/PhoneBookFixture.java");
        assertTrue(fixture.exists());
    }

    public void testShouldGenerateFixtureFromRepository() throws Exception {

        String specName = "list";
        Repository repo = createMockRepository("repo", specName);
        mojo.repositories = Collections.singletonList(repo);

        mojo.specificationName = specName;

        mojo.execute();

        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithAnnotationFixture.java").exists());
        assertTrue(new File(srcDir, "com/greenpepper/samples/fixture/PhoneBookListWithMethodFixture.java").exists());
    }


    private Repository createMockRepository(String reponame, String specName) throws Exception {
        Repository repo = createMock(Repository.class);
        expect(repo.getName()).andReturn(reponame).anyTimes();

        DocumentRepository documentRepository = createMock(DocumentRepository.class);
        File specification = loadSpecification(specName + ".html");
        Document doc = HtmlDocumentBuilder.tablesAndLists().build(new FileReader(specification));
        expect(documentRepository.loadDocument(specName)).andReturn(doc);

        expect(repo.getDocumentRepository()).andReturn(documentRepository).anyTimes();

        replay(repo,documentRepository);
        return repo;
    }


}