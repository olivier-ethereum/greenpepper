package com.greenpepper.server.database.hibernate;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.database.SessionService;
import com.greenpepper.server.domain.dao.RepositoryDao;
import com.greenpepper.server.domain.dao.SystemInfoDao;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateRepositoryDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemInfoDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemUnderTestDao;

public class BootstrapData
{
    private static Logger log = LoggerFactory.getLogger(BootstrapData.class);

	private final SessionService sessionService;
	private final Properties properties;
	private SystemInfoDao systemInfoDao;
	private SystemUnderTestDao systemUnderTestDao;
	private RepositoryDao repositoryDao;

	public BootstrapData(SessionService sessionService, Properties properties)
    {
		this(sessionService, properties, new HibernateSystemInfoDao(sessionService),
			 new HibernateSystemUnderTestDao(sessionService), new HibernateRepositoryDao(sessionService));
	}

	public BootstrapData(SessionService sessionService, Properties properties, SystemInfoDao systemInfoDao,
						 SystemUnderTestDao systemUnderTestDao, RepositoryDao repositoryDao) {
		this.sessionService = sessionService;
		this.properties = properties;
		this.systemInfoDao = systemInfoDao;
		this.systemUnderTestDao = systemUnderTestDao;
		this.repositoryDao = repositoryDao;
	}
    
    public void execute() throws Exception
    {
        try 
        {
			sessionService.beginTransaction();
            log.info("Inserting InitialDatas");
			new InitialDatas(systemInfoDao, systemUnderTestDao, repositoryDao).insert();

            log.info("Inserting DefaultRunners");
			new DefaultRunners(systemUnderTestDao, properties).insert();

            log.info("Upgrading to version {} ", GreenPepperServer.VERSION);
			new ServerUpgrader(sessionService, systemInfoDao).upgradeTo(GreenPepperServer.VERSION);
			
			sessionService.commitTransaction();
		} 
        catch (Exception e) 
        {
        	sessionService.rollbackTransaction();
        	log.error("Bootstrap Failure: ", e);
        	throw new GreenPepperServerException("", "Boostrap Failure", e);
		}
    }
}
