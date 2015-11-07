package com.greenpepper.maven.plugin.it;

import java.io.File;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class GreenPepperMavenPluginTest 
    extends TestCase
{
    public void testGreenpepperPlugin()
            throws Exception
        {
            // Check in your dummy Maven project in /src/test/resources/...
            // The testdir is computed from the location of this
            // file.
            File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/test-gp-resources" );
     
            Verifier verifier;
     
            /*
             * We must first make sure that any artifact created
             * by this test has been removed from the local
             * repository. Failing to do this could cause
             * unstable test results. Fortunately, the verifier
             * makes it easy to do this.
             */
            verifier = new Verifier( testDir.getAbsolutePath() );
            
            verifier.deleteArtifact( "dummy", "dummy", "4.0", "jar" );
     
            /*
             * The Command Line Options (CLI) are passed to the
             * verifier as a list. This is handy for things like
             * redefining the local repository if needed. In
             * this case, we use the -N flag so that Maven won't
             * recurse. We are only installing the parent pom to
             * the local repo here.
             */
            //verifier.setMavenDebug(true);
            verifier.executeGoal( "integration-test" );
            
            /*
             * This is the simplest way to check a build
             * succeeded. It is also the simplest way to create
             * an IT test: make the build pass when the test
             * should pass, and make the build fail when the
             * test should fail. There are other methods
             * supported by the verifier. They can be seen here:
             * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
             */
            verifier.verifyErrorFreeLog();
     
            
            /*
             * Reset the streams before executing the verifier
             * again.
             */
            verifier.resetStreams();
     
            /*
             * The verifier also supports beanshell scripts for
             * verification of more complex scenarios. There are
             * plenty of examples in the core-it tests here:
             * http://svn.apache.org/repos/asf/maven/core-integration-testing/trunk
             */
        }
}
