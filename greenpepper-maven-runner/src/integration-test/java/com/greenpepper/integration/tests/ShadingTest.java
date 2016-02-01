package com.greenpepper.integration.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ShadingTest {

    private SetupContent setupContent;

    @Before
    public void setup() throws IOException, VerificationException {
        setupContent = new SetupContent();
        setupContent.invoke();
    }

    @After
    public void tearDown() throws VerificationException {
        setupContent.shutdown();
    }

    @Test
    public void testCommonsCodecsConflicts() throws IOException, VerificationException {
        SetupContent setupContent = new SetupContent().invoke();
        String shadedJar = setupContent.getShadedJar();
        String issue24POM = setupContent.getIssue24POM();
        String issue24TestFile = setupContent.getIssue24TestFile();
        File cwdFile = setupContent.getCwdFile();
        

        String line = String.format("java -jar %s -v -p %s %s %s", shadedJar, issue24POM, issue24TestFile,
                StringUtils.join(new String[] {cwdFile.getAbsolutePath(), "testCommonsCodecsConflicts.html"}, File.separator));
        CommandLine command = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(out, err));
        executor.execute(command);
    }

    @Test
    public void shouldNotOverwriteInputFile() throws IOException, VerificationException {
        SetupContent setupContent = new SetupContent().invoke();
        String shadedJar = setupContent.getShadedJar();
        String issue24POM = setupContent.getIssue24POM();
        String issue24TestFile = setupContent.getIssue24TestFile();
        File specFile = new File(issue24TestFile);
        File specBackupFile = new File(issue24TestFile + ".orig");
        FileUtils.copyFile(specFile, specBackupFile);

        String line = String.format("java -jar %s -v -p %s %s", shadedJar, issue24POM, issue24TestFile);
        CommandLine command = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(specFile.getParentFile());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(out, err));
        executor.execute(command);

        assertTrue("The specification should not be overriden by the output", FileUtils.contentEquals(specFile,specBackupFile));
    }

    private class SetupContent {
        private String shadedJar;
        private String issue24POM;
        private String issue24TestFile;
        private File cwdFile;

        public String getShadedJar() {
            return shadedJar;
        }

        public String getIssue24POM() {
            return issue24POM;
        }

        public String getIssue24TestFile() {
            return issue24TestFile;
        }

        public File getCwdFile() {
            return cwdFile;
        }

        public SetupContent invoke() throws IOException, VerificationException {
            Properties info = new Properties();
            info.load(getClass().getResourceAsStream("/info.properties"));
            shadedJar = info.getProperty("shaded.jar");
            issue24POM = StringUtils.join(new String[]{info.getProperty("it.tests.folder"), "test-issue24", "pom.xml"}, File.separator);
            issue24TestFile = StringUtils.join(new String[]{info.getProperty("it.tests.folder"), "test-issue24", "src", "test", "specs", "right.html"}, File.separator);
            URL cwd = getClass().getResource(".");
            cwdFile = FileUtils.toFile(cwd);

            Verifier verifier = new Verifier(new File(issue24POM).getParent());
            verifier.executeGoal("integration-test");
            verifier.verifyErrorFreeLog();
            verifier.resetStreams();
            return this;
        }

        public void shutdown() throws VerificationException {
            Verifier verifier = new Verifier(new File(issue24POM).getParent());
            verifier.executeGoal("clean");
            verifier.verifyErrorFreeLog();
            verifier.resetStreams();
        }
    }
}
