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

/**
 * <p>BootstrapData class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class BootstrapData
{
    private static Logger log = LoggerFactory.getLogger(BootstrapData.class);

	private final SessionService sessionService;
	private final Properties properties;
	private SystemInfoDao systemInfoDao;
	private SystemUnderTestDao systemUnderTestDao;
	private RepositoryDao repositoryDao;

	/**
	 * <p>Constructor for BootstrapData.</p>
	 *
	 * @param sessionService a {@link com.greenpepper.server.database.SessionService} object.
	 * @param properties a {@link java.util.Properties} object.
	 */
	public BootstrapData(SessionService sessionService, Properties properties)
    {
		this(sessionService, properties, new HibernateSystemInfoDao(sessionService),
			 new HibernateSystemUnderTestDao(sessionService), new HibernateRepositoryDao(sessionService));
	}

	/**
	 * <p>Constructor for BootstrapData.</p>
	 *
	 * @param sessionService a {@link com.greenpepper.server.database.SessionService} object.
	 * @param properties a {@link java.util.Properties} object.
	 * @param systemInfoDao a {@link com.greenpepper.server.domain.dao.SystemInfoDao} object.
	 * @param systemUnderTestDao a {@link com.greenpepper.server.domain.dao.SystemUnderTestDao} object.
	 * @param repositoryDao a {@link com.greenpepper.server.domain.dao.RepositoryDao} object.
	 */
	public BootstrapData(SessionService sessionService, Properties properties, SystemInfoDao systemInfoDao,
						 SystemUnderTestDao systemUnderTestDao, RepositoryDao repositoryDao) {
		this.sessionService = sessionService;
		this.properties = properties;
		this.systemInfoDao = systemInfoDao;
		this.systemUnderTestDao = systemUnderTestDao;
		this.repositoryDao = repositoryDao;
	}
    
    /**
     * <p>execute.</p>
     *
     * @throws java.lang.Exception if any.
     */
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
