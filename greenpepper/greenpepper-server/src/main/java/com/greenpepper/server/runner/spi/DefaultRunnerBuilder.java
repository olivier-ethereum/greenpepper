package com.greenpepper.server.runner.spi;

import java.util.Properties;

import com.greenpepper.server.domain.dao.SystemUnderTestDao;


/**
 * <p>DefaultRunnerBuilder interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface DefaultRunnerBuilder {

    /**
     * <p>getRunnerName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getRunnerName();

    /**
     * <p>buildAndRegisterRunner.</p>
     *
     * @param systemUnderTestDao a {@link com.greenpepper.server.domain.dao.SystemUnderTestDao} object.
     * @param properties a {@link java.util.Properties} object.
     */
    void buildAndRegisterRunner(SystemUnderTestDao systemUnderTestDao, Properties properties);
}
