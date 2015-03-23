package com.greenpepper.server.runner.confluence5;


import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.greenpepper.GreenPepperCore;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ConfluenceDefaultRunnerBuilderTest {

    private static final String clientJarFileName = "greenpepper-client-" + GreenPepperCore.VERSION + "-complete.jar";
    private static final String demoJarFileName = "greenpepper-confluence-demo-" + GreenPepperCore.VERSION + "-fixtures.jar";

    @TestedObject
    private ConfluenceDefaultRunnerBuilder builder;

    @Mock
    private SystemUnderTestDao systemUnderTestDao;

    private Properties properties = new Properties();

    @Before
    public void setUp() throws Exception {
        String home = FileUtils.toFile(getClass().getResource("/")).getCanonicalPath();
        properties.setProperty("confluence.home", home);
        File testclassesDir = FileUtils.toFile(getClass().getResource("/"));
        File testFakeJarfile = new File(testclassesDir, clientJarFileName);
        FileUtils.writeStringToFile(testFakeJarfile, "dummy content", "UTF-8");
        File testFakeDemoJarfile = new File(testclassesDir, demoJarFileName);
        FileUtils.writeStringToFile(testFakeDemoJarfile, "dummy demo content", "UTF-8");
    }

    @After
    public void cleanup() {
        FileUtils.deleteQuietly(new File(properties.getProperty("confluence.home") + "/plugins-data"));
        File testclassesDir = FileUtils.toFile(getClass().getResource("/"));
        FileUtils.deleteQuietly(new File(testclassesDir, clientJarFileName));
        FileUtils.deleteQuietly(new File(testclassesDir, demoJarFileName));
        properties.clear();
    }

    @Test
    public void getRunnerNameShouldNotBeNull() {
        assertNotNull(builder.getRunnerName());
    }

    @Test
    public void shouldNotBuildRunnerIfConfluenceHomeNotSet() {
        properties.clear();
        builder.buildAndRegisterRunner(systemUnderTestDao, properties);
    }

    @Test
    public void shouldCreateAndRegisterRunner() throws GreenPepperServerException, IOException {

        expect(systemUnderTestDao.getEnvironmentTypeByName("JAVA")).andReturn(new EnvironmentType());
        Capture<Runner> runnerCapture = new Capture<Runner>();

        expect(systemUnderTestDao.create(capture(runnerCapture))).andReturn(new Runner());

        replay(systemUnderTestDao);
        builder.buildAndRegisterRunner(systemUnderTestDao, properties);

        verify(systemUnderTestDao);

        Runner value = runnerCapture.getValue();
        assertNotNull(value);
        assertNotNull(value.getCmdLineTemplate());
        assertNotNull(value.getMainClass());
        assertEquals(2, value.getClasspaths().size());
    }
}
