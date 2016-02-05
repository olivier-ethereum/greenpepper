package com.greenpepper.server.database.hibernate;

import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.runner.repository.AtlassianRepository;
import com.greenpepper.runner.repository.XWikiRepository;
import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.database.SessionService;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.SystemInfo;
import com.greenpepper.server.domain.dao.RepositoryDao;
import com.greenpepper.server.domain.dao.SystemInfoDao;
import com.greenpepper.server.domain.dao.SystemUnderTestDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateRepositoryDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemInfoDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemUnderTestDao;
import com.greenpepper.util.StringUtil;

/**
 * <p>InitialDatas class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class InitialDatas 
{
	/** Constant <code>DEFAULT_VERSION="1.0"</code> */
	public static final String DEFAULT_VERSION = "1.0";

    private final SystemInfoDao systDao;
    private final SystemUnderTestDao sutDao;
    private final RepositoryDao repoDao;
    
	/**
	 * <p>Constructor for InitialDatas.</p>
	 *
	 * @param sessionService a {@link com.greenpepper.server.database.SessionService} object.
	 */
	public InitialDatas(SessionService sessionService)
	{
        this(new HibernateSystemInfoDao(sessionService), new HibernateSystemUnderTestDao(sessionService),
			 new HibernateRepositoryDao(sessionService));
	}

	/**
	 * <p>Constructor for InitialDatas.</p>
	 *
	 * @param systemInfoDao a {@link com.greenpepper.server.domain.dao.SystemInfoDao} object.
	 * @param systemUnderTestDao a {@link com.greenpepper.server.domain.dao.SystemUnderTestDao} object.
	 * @param repositoryDao a {@link com.greenpepper.server.domain.dao.RepositoryDao} object.
	 */
	public InitialDatas(SystemInfoDao systemInfoDao, SystemUnderTestDao systemUnderTestDao,
						RepositoryDao repositoryDao) {
		this.systDao = systemInfoDao;
		this.sutDao = systemUnderTestDao;
		this.repoDao = repositoryDao;
	}
	
	/**
	 * <p>insert.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	public void insert() throws Exception
	{
		insertSystemInfo();
		insertEnvironmentTypes();
		insertRepositoryTypes();
	}

    private void insertSystemInfo()
    {
    	SystemInfo systemInfo = systDao.getSystemInfo();
        if(systemInfo == null)
        {
            systemInfo = new SystemInfo();
            systemInfo.setId(1l);
            systemInfo.setLicense("Invalid");
			systemInfo.setGpVersion(GreenPepperServer.VERSION);
            systDao.store(systemInfo);
        }
        
        if(StringUtil.isEmpty(systemInfo.getGpVersion()))
		{
            systemInfo.setGpVersion(DEFAULT_VERSION);
            systDao.store(systemInfo);
		}
    }
    
    private void insertEnvironmentTypes()
    {
        if(sutDao.getEnvironmentTypeByName("JAVA") == null)
            sutDao.create(EnvironmentType.newInstance("JAVA")); 
        
        if(sutDao.getEnvironmentTypeByName(".NET") == null)
            sutDao.create(EnvironmentType.newInstance(".NET"));
    }

    private void insertRepositoryTypes()
    {
    	EnvironmentType java = sutDao.getEnvironmentTypeByName("JAVA");
    	EnvironmentType dotnet = sutDao.getEnvironmentTypeByName(".NET");
        
        if(repoDao.getTypeByName("JIRA") == null)
        {
            RepositoryType type = RepositoryType.newInstance("JIRA");
            type.setDocumentUrlFormat("%s/browse/%s");
            type.setTestUrlFormat(null);
            type.registerClassForEnvironment(AtlassianRepository.class.getName(),java);
            type.registerClassForEnvironment("GreenPepper.Repositories.AtlassianRepository",dotnet);
            
            repoDao.create(type);
        }
        if(repoDao.getTypeByName("CONFLUENCE") == null)
        {
            RepositoryType type = RepositoryType.newInstance("CONFLUENCE");
            type.setDocumentUrlFormat("%s/%s");
            type.setTestUrlFormat(null);
            type.registerClassForEnvironment(AtlassianRepository.class.getName(),java);
            type.registerClassForEnvironment("GreenPepper.Repositories.AtlassianRepository",dotnet);
            
            repoDao.create(type);
        }
        if(repoDao.getTypeByName("FILE") == null)
        {
            RepositoryType type = RepositoryType.newInstance("FILE");
            type.setDocumentUrlFormat("%s%s");
            type.setTestUrlFormat("%s%s");
            type.registerClassForEnvironment(FileSystemRepository.class.getName(),java);
            type.registerClassForEnvironment("GreenPepper.Repositories.FileSystemRepository",dotnet);

            repoDao.create(type);
        }
		if(repoDao.getTypeByName("XWIKI") == null)
		{
			RepositoryType type = RepositoryType.newInstance("XWIKI");
			type.setDocumentUrlFormat("%s/%s");
			type.setTestUrlFormat(null);
			type.registerClassForEnvironment(XWikiRepository.class.getName(),java);
			type.registerClassForEnvironment("GreenPepper.Repositories.XWikiRepository",dotnet);

			repoDao.create(type);
		}
    }
}
