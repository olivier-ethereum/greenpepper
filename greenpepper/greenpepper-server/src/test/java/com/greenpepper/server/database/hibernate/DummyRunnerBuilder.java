package com.greenpepper.server.database.hibernate;

import java.util.Properties;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;
import com.greenpepper.server.runner.spi.DefaultRunnerBuilder;

public class DummyRunnerBuilder implements DefaultRunnerBuilder {

    @Override
    public String getRunnerName() {
        return "Dummy Runner";
    }

    @Override
    public void buildAndRegisterRunner(SystemUnderTestDao systemUnderTestDao, Properties properties) {
        Runner runner = new Runner();
        runner.setName(getRunnerName());
        runner.setEnvironmentType(systemUnderTestDao.getEnvironmentTypeByName("JAVA"));
        try {
            systemUnderTestDao.create(runner);
        } catch (GreenPepperServerException e) {
            throw new RuntimeException(e);
        }

    }

}
