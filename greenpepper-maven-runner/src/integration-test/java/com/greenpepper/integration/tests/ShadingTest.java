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
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

public class ShadingTest {

    @Test
    @Ignore
    public void testCommonsCodecsConflicts() throws IOException {
        Properties info = new Properties();
        info.load(getClass().getResourceAsStream("/info.properties"));
        String shadedJar = info.getProperty("shaded.jar");
        String issue24POM = StringUtils.join(new String[] {info.getProperty("it.tests.folder"), "test-issue24", "pom.xml"}, File.separator);
        String issue24TestFile = StringUtils.join(new String[] {info.getProperty("it.tests.folder"), "test-issue24", "src", "test", "specs", "right.html"}, File.separator);
        URL cwd = getClass().getResource(".");
        File cwdFile = FileUtils.toFile(cwd);

        String line = String.format("java -jar %s -v -p %s %s %s", shadedJar, issue24POM, issue24TestFile,
                StringUtils.join(new String[] {cwdFile.getAbsolutePath(), "testCommonsCodecsConflicts.html"}, File.separator));
        CommandLine command = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(out, err));
        executor.execute(command);
    }

}
