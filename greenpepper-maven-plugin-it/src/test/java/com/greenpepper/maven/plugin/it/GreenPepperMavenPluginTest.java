package com.greenpepper.maven.plugin.it;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class GreenPepperMavenPluginTest extends TestCase {

    private void testLaunchingMaven(File testBasedir, ArrayList<String> cliOptions) throws IOException, VerificationException {
        Verifier verifier = new Verifier(testBasedir.getAbsolutePath());
        verifier.deleteArtifact("dummy", "dummy", "4.0", "jar");
        verifier.setCliOptions(cliOptions);
        verifier.executeGoal("integration-test");
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    public void testGreenpepperPlugin() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-gp-resources");
        testLaunchingMaven(testDir, new ArrayList<String>());
    }


    public void testLaunchingOutsideOfProjectRoot() throws VerificationException, IOException {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-gp-resources");
        testDir = testDir.getParentFile();
        ArrayList<String> cliOptions = new ArrayList<String>() {{
            add("-f");
            add("test-gp-resources/pom.xml");
        }};
        testLaunchingMaven(testDir, cliOptions);
    }

}
