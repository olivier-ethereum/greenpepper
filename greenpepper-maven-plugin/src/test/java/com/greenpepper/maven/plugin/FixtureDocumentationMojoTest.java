package com.greenpepper.maven.plugin;

import com.greenpepper.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.io.FileUtils.toFile;

public class FixtureDocumentationMojoTest extends AbstractMojoTestCase {


    private FixtureDocumentationMojo mojo;
    private File docDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        URL pomPath = FixtureDocumentationMojoTest.class.getResource("pom-doc.xml");
        mojo = (FixtureDocumentationMojo) lookupMojo("documentation", URIUtil.decoded(pomPath.getPath()));
        docDir = getFixtureDocDir();
        mojo.documentationDirectory = docDir;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        File docDir = getFixtureDocDir();
        FileUtils.forceDelete(docDir);
    }

    private File getFixtureDocDir() throws IOException {
        URL currentPackage = getClass().getResource(".");
        File docTarget = new File(toFile(currentPackage), "docTarget");
        FileUtils.forceMkdir(docTarget);
        return docTarget;
    }


    public void testGenerateDocumentation() throws Exception {
        mojo.packagesToScan = new String[]{ "com.greenpepper.fixtures.application.bank" };
        mojo.execute();

        assertTrue(new File(docDir, "fixtures.xml").exists());
    }

}