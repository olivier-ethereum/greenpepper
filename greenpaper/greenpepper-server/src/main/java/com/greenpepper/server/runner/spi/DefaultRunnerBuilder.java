package com.greenpepper.server.runner.spi;

import java.util.Properties;

import com.greenpepper.server.domain.dao.SystemUnderTestDao;


public interface DefaultRunnerBuilder {

    String getRunnerName();

    void buildAndRegisterRunner(SystemUnderTestDao systemUnderTestDao, Properties properties);
}
