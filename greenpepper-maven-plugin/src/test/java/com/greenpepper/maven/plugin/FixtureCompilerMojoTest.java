package com.greenpepper.maven.plugin;

import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.unitils.inject.util.InjectionUtils;


public class FixtureCompilerMojoTest extends AbstractMojoTestCase {
    
    private FixtureCompilerMojo mojo;
    
    public void testShouldCompileOneFile() throws Exception {
        URL pomPath = SpecificationDownloaderMojoTest.class.getResource("pom-compiler.xml");
        
        mojo = (FixtureCompilerMojo) lookupMojo("compile", FileUtils.toFile(pomPath).getAbsolutePath());
        // Let's put the default values
        InjectionUtils.injectInto("javac", mojo, "compilerId");
        mojo.execute();
        URL resource = getClass().getResource("/fixture-classes/c/gp/fixt/HelloFixture.class");
        assertNotNull(resource);
    }

}
