package com.greenpepper.maven.plugin;

import com.greenpepper.maven.plugin.spy.impl.JavaFixtureGenerator;
import com.greenpepper.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.replace;
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
        File srcDir = new File(FileUtils.toFile(currentPackage), "src");
        FileUtils.forceMkdir(srcDir);
        return srcDir;
    }

    private File loadSpecification(String name) {
        URL resource = getClass().getResource("specs/" + name);
        return FileUtils.toFile(resource);
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
        String javaclassSrc = FileUtils.readFileToString(fixtureFile);
        String moddedSrc = replace(javaclassSrc, "throw new UnsupportedOperationException(\"Not yet implemented!\");", " return null;");
        writeStringToFile(fixtureFile, moddedSrc);

        mojo.specification = loadSpecification("right1.html");
        mojo.execute();

        fixtureFile = new File(srcDir, "com/greenpepper/maven/plugin/EchoFixture.java");
        assertTrue(fixtureFile.exists());
        String actual = FileUtils.readFileToString(fixtureFile);
        assertThat(actual, containsString("thatTheAnswerIs"));
        assertThat(actual, containsString("echo"));
        assertThat(actual, containsString("return null;"));
        assertEquals(1, countMatches(actual,"public EchoFixture() {"));
    }
}