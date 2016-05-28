/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.confluence;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.dialect.HSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.BootstrapManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.plugin.StateAware;
import com.atlassian.spring.container.ContainerManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.GreenPepperServerServiceImpl;
import com.greenpepper.server.configuration.DefaultServerProperties;
import com.greenpepper.server.database.hibernate.BootstrapData;
import com.greenpepper.server.database.hibernate.DefaultRunners;
import com.greenpepper.server.database.hibernate.HibernateSessionService;
import com.greenpepper.server.domain.dao.DocumentDao;
import com.greenpepper.server.domain.dao.ProjectDao;
import com.greenpepper.server.domain.dao.RepositoryDao;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateDocumentDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateProjectDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateRepositoryDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemUnderTestDao;
import com.greenpepper.server.license.Authorizer;
import com.greenpepper.server.license.OpenSourceAuthorizer;
import com.greenpepper.server.rpc.xmlrpc.GreenPepperXmlRpcServer;

/**
 * <p>GreenPepperServerConfigurationActivator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperServerConfigurationActivator implements StateAware {

    private static final Logger log = LoggerFactory.getLogger(GreenPepperServerConfigurationActivator.class);

    private final BandanaContext bandanaContext = new ConfluenceBandanaContext("_GREENPEPPER");

    private BandanaManager bandanaManager;
    
    private BootstrapManager bootstrapManager;
    
    private SettingsManager settingsManager;

    private GreenPepperServerConfiguration configuration;

    private HibernateSessionService hibernateSessionService;

    private boolean isPluginEnabled = false;

    private boolean isServerStarted = false;


    /**
     * <p>enabled.</p>
     */
    public void enabled() {
        log.info("Enabling GreenPepper Plugin");
        isPluginEnabled = true;

    }

    /**
     * <p>disabled.</p>
     */
    public void disabled() {
        log.info("Disabling GreenPepper Plugin");
        isPluginEnabled = false;
        shutdown();
    }

    /**
     * <p>Setter for the field <code>bandanaManager</code>.</p>
     *
     * @param bandanaManager a {@link com.atlassian.bandana.BandanaManager} object.
     */
    public void setBandanaManager(BandanaManager bandanaManager) {
        this.bandanaManager = bandanaManager;
    }

    /**
     * <p>Setter for the field <code>bootstrapManager</code>.</p>
     *
     * @param bootstrapManager a {@link com.atlassian.confluence.setup.BootstrapManager} object.
     */
    public void setBootstrapManager(BootstrapManager bootstrapManager) {
        this.bootstrapManager = bootstrapManager;
    }

    /**
     * <p>Setter for the field <code>settingsManager</code>.</p>
     *
     * @param settingsManager a {@link com.atlassian.confluence.setup.settings.SettingsManager} object.
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    /**
     * <p>isReady.</p>
     *
     * @return a boolean.
     */
    public boolean isReady() {
        return isPluginEnabled && hibernateSessionService != null;
    }

    /**
     * <p>startup.</p>
     *
     * @param forceStartup a boolean.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void startup(boolean forceStartup) throws GreenPepperServerException {
        log.info("Starting Plugin");
        if (!isPluginEnabled)
            return;

        final GreenPepperServerConfiguration configuration = getConfiguration();

        if ((configuration.isSetupComplete() && !isServerStarted) || forceStartup) {
            isServerStarted = false;

            try {
                closeSession();

                Properties properties = configuration.getProperties();

                injectAdditionalProperties(properties);

                HibernateSessionService sessionService = new HibernateSessionService(properties);

                log.info("Boostrapping datas");
                new BootstrapData(sessionService, properties).execute();
                new GreenPepperUserGroup().createIfNeeded();

                Authorizer authorizer = new OpenSourceAuthorizer(sessionService, properties);
                authorizer.initialize(GreenPepperServer.versionDate());

                ProjectDao projectDao = new HibernateProjectDao(sessionService);
                RepositoryDao repositoryDao = new HibernateRepositoryDao(sessionService);
                SystemUnderTestDao sutDao = new HibernateSystemUnderTestDao(sessionService);
                DocumentDao documentDao = new HibernateDocumentDao(sessionService);

                Object object = ContainerManager.getComponent("greenPepperServerService");
                GreenPepperServerServiceImpl service = (GreenPepperServerServiceImpl) object;

                service.setAuthorizer(authorizer);
                service.setDocumentDao(documentDao);
                service.setProjectDao(projectDao);
                service.setRepositoryDao(repositoryDao);
                service.setSessionService(sessionService);
                service.setSutDao(sutDao);

                object = ContainerManager.getComponent("greenPepperXmlRpcServerService");
                GreenPepperXmlRpcServer xmlRpcServer = (GreenPepperXmlRpcServer) object;
                xmlRpcServer.setService(service);

                hibernateSessionService = sessionService;

                configuration.setSetupComplete(true);
                storeConfiguration(configuration);

                isServerStarted = true;
            } catch (Exception ex) {
                log.error("Starting up GreenPepper plugin", ex);
                throw new GreenPepperServerException(GreenPepperServerErrorKey.GENERAL_ERROR, ex);
            }
        }
    }

    /**
     * <p>shutdown.</p>
     */
    public void shutdown() {
        log.info("Shutting down Plugin");
        closeSession();
    }

    private void closeSession() {
        if (hibernateSessionService != null) {
            hibernateSessionService.close();
        }

        hibernateSessionService = null;
        isServerStarted = false;
    }

    private void injectAdditionalProperties(Properties sProperties) {
        sProperties.setProperty("baseUrl", settingsManager.getGlobalSettings().getBaseUrl());
        sProperties.setProperty("confluence.home", getConfluenceHome());
        sProperties.setProperty(DefaultRunners.DEFAULT_RUNNER_BUILDER_INTERFACE, "com.greenpepper.server.runner.confluence5.ConfluenceDefaultRunnerBuilder");
    }

    /**
     * <p>Getter for the field <code>configuration</code>.</p>
     *
     * @return a {@link com.greenpepper.confluence.GreenPepperServerConfiguration} object.
     */
    public GreenPepperServerConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = getConfigurationFromBandana();
        }

        return configuration;
    }

    /**
     * <p>storeConfiguration.</p>
     *
     * @param configuration a {@link com.greenpepper.confluence.GreenPepperServerConfiguration} object.
     */
    public void storeConfiguration(GreenPepperServerConfiguration configuration) {
        // @todo : sanity check over the previous configuration

        storeConfigurationToBandana(configuration);
    }

    private GreenPepperServerConfiguration getConfigurationFromBandana() {
        Object value = getValue(GreenPepperServerConfiguration.class);
        if (value == null) {
            configuration = new GreenPepperServerConfiguration();
            storeConfigurationToBandana(configuration);
        } else {
                /* Configuration is not a String so it should be an instance of GreenPepperServerConfiguration.
                 * The problem is that we can have a conflict of Classloaders. The GreenPepperServerConfiguration
                 * class object exists in the current classloader but also from Bandana classLoader. We can't rely on a Cast.
                 * But we know that the class definition is the same. So the toString() implementation is the current one. 
                 */
                try {
                    configuration = GreenPepperServerConfiguration.fromString((String) value.toString());
                } catch (JsonParseException e) {
                    throw new RuntimeException("Problem retrieving greenpepper configuration ", e);
                } catch (IOException e) {
                    throw new RuntimeException("Problem retrieving greenpepper configuration ", e);
                }
        }
        return configuration;
    }

    private void storeConfigurationToBandana(GreenPepperServerConfiguration configuration) {
        bandanaManager.setValue(bandanaContext, GreenPepperServerConfiguration.class.getName(), configuration.toString());
    }

    private Object getValue(Class<?> classKey) {
        return bandanaManager.getValue(bandanaContext, classKey.getName());
    }

    /**
     * <p>getConfigJnriUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getConfigJnriUrl() {
        return (String) getConfiguration().getProperties().get("config$hibernate.connection.datasource");
    }

    /**
     * <p>getConfigDialect.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getConfigDialect() {
        return (String) getConfiguration().getProperties().get("config$hibernate.dialect");
    }

    /**
     * <p>initQuickInstallConfiguration.</p>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void initQuickInstallConfiguration() throws GreenPepperServerException {
        GreenPepperServerConfiguration configuration = getConfiguration();

        Properties properties = new DefaultServerProperties();

        properties.put("hibernate.c3p0.acquire_increment", "1");
        properties.put("hibernate.c3p0.idle_test_period", "100");
        properties.put("hibernate.c3p0.max_size", "15");
        properties.put("hibernate.c3p0.max_statements", "0");
        properties.put("hibernate.c3p0.min_size", "0");
        properties.put("hibernate.c3p0.timeout", "30");
        properties.remove("hibernate.connection.datasource"); // direct jdbc
        properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        properties.put("hibernate.connection.url", "jdbc:hsqldb:" + getConfluenceHome() + "/database/gpsdb");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password", "");
        properties.put("hibernate.dialect", HSQLDialect.class.getName());

        configuration.setProperties(properties);

        startup(true);
    }

    /**
     * <p>initCustomInstallConfiguration.</p>
     *
     * @param hibernateDialect a {@link java.lang.String} object.
     * @param jndiUrl a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void initCustomInstallConfiguration(String hibernateDialect, String jndiUrl) throws GreenPepperServerException {
        GreenPepperServerConfiguration configuration = getConfiguration();

        Properties properties = new DefaultServerProperties();

        properties.put("hibernate.connection.datasource", jndiUrl);
        properties.put("config$hibernate.connection.datasource", jndiUrl);
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("config$hibernate.dialect", hibernateDialect);
        // properties.put("hibernate.show_sql", "true");

        if (hibernateDialect.indexOf("Oracle") != -1) {
            // The Oracle JDBC driver doesn't like prepared statement caching very much.
            properties.put("hibernate.statement_cache.size", "0");
            // or baching with BLOBs very much.
            properties.put("hibernate.jdbc.batch_size", "0");
            // http://www.jroller.com/dashorst/entry/hibernate_3_1_something_performance1
            properties.put("hibernate.jdbc.wrap_result_sets", "true");
        }

        configuration.setProperties(properties);

        startup(true);
    }

    private String getConfluenceHome() {
        return bootstrapManager.getConfluenceHome();
    }

}
