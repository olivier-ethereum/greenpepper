package com.greenpepper.maven.plugin;

import com.greenpepper.maven.plugin.spy.impl.JavaFixtureGenerator;
import com.greenpepper.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.net.URL;

public class FixtureGeneratorMojoTest  extends AbstractMojoTestCase {

    private FixtureGeneratorMojo mojo;

    public void testGenerateFixture() throws Exception {
        URL pomPath = FixtureGeneratorMojoTest.class.getResource("pom-genfixture.xml");
        mojo = (FixtureGeneratorMojo) lookupMojo("generate-fixtures", URIUtil.decoded(pomPath.getPath()));
        mojo.fixtureGeneratorClass = JavaFixtureGenerator.class.getName();
        mojo.specification = loadSpecification();
        mojo.execute();
    }


    private File loadSpecification() {
        URL resource = getClass().getResource("specs/CollectionOfValuesSample.html");
        return FileUtils.toFile(resource);
    }

}