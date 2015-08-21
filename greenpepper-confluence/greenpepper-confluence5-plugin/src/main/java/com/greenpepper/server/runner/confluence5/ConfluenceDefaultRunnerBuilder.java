package com.greenpepper.server.runner.confluence5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.GreenPepperCore;
import com.greenpepper.runner.Main;
import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.ClasspathSet;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;
import com.greenpepper.server.runner.spi.DefaultRunnerBuilder;


public class ConfluenceDefaultRunnerBuilder implements DefaultRunnerBuilder {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceDefaultRunnerBuilder.class);


    @Override
    public String getRunnerName() {
        return "GPCore JAVA v." + GreenPepperServer.VERSION + " (with DemoSpace Fixtures)";
    }

    @Override
    public void buildAndRegisterRunner(SystemUnderTestDao systemUnderTestDao, Properties properties) {
        String confluenceHome = properties.getProperty("confluence.home", null);
        if (confluenceHome != null) {
            File pluginsCacheDir = new File(confluenceHome, "plugins-data");
            insertJavaRunnerFromPackagedJar(pluginsCacheDir, systemUnderTestDao);
        }
    }

    private void insertJavaRunnerFromPackagedJar(File dir, SystemUnderTestDao dao) {
        ClasspathSet paths = new ClasspathSet();
        try {
            String clientjar = "greenpepper-client-" + GreenPepperCore.VERSION + "-complete.jar";
            extractJar(dir, paths, clientjar);
            String demoFixtureJar = "greenpepper-confluence-demo-" + GreenPepperCore.VERSION + "-fixtures.jar";
            extractJar(dir, paths, demoFixtureJar);
            createJavaRunner(dao, paths);
        } catch (Exception e) {
            LOGGER.error("Runner registration failed: ", e);
        }
    }

    private void extractJar(File dir, ClasspathSet paths, String clientjar) throws FileNotFoundException, IOException {
        LOGGER.debug("Try to extract the client jar file ({}) from our classpath", clientjar);
        InputStream clientjarStream = getClass().getClassLoader().getResourceAsStream(clientjar);
        if (clientjarStream != null) {
            if (dir.isFile()) {
                dir.delete();
            }
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            File destinationFile = new File(dir, clientjar);
            FileOutputStream jarFileOS = new FileOutputStream(destinationFile);
            int copiedBytes = IOUtils.copy(clientjarStream, jarFileOS);
            LOGGER.debug("Copied {} bytes to {}", copiedBytes, destinationFile);

            paths.add(destinationFile.getCanonicalPath());
        } else {
            throw new FileNotFoundException(String.format("%s not found in the classpath.", clientjar));
        }
    }

    private void createJavaRunner(SystemUnderTestDao sutDao, ClasspathSet classpaths) throws IOException, GreenPepperServerException {

        LOGGER.info(String.format("Registrating Runner: %s ", getRunnerName()));
        Runner runner = Runner.newInstance(getRunnerName());
        runner.setCmdLineTemplate("java -mx252m -cp ${classpaths} ${mainClass} ${inputPath} ${outputPath} " + "-l ${locale} -r ${repository} -f ${fixtureFactory} --xml");
        runner.setMainClass(Main.class.getName());
        runner.setClasspaths(classpaths);
        runner.setEnvironmentType(sutDao.getEnvironmentTypeByName("JAVA"));
        sutDao.create(runner);
    }

}
