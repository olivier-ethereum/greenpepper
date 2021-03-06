package com.greenpepper.server.database.hibernate.upgrades;

import com.greenpepper.server.database.SessionService;
import com.greenpepper.server.domain.SystemInfo;
import com.greenpepper.server.domain.dao.SystemInfoDao;
import com.greenpepper.server.domain.dao.hibernate.HibernateSystemInfoDao;

public class UpgradeOf_VERSION_THAT_NEEDS_MORE_UPGRADES implements ServerVersionUpgrader
{

	public String upgradedTo()
	{
		return "VERSION.UPGRADED";
	}

	public void upgrade(SessionService service) throws Exception
	{
	    SystemInfoDao systemDao = new HibernateSystemInfoDao(service);
	    SystemInfo systemInfo = systemDao.getSystemInfo();
	    systemInfo.setLicense(systemInfo.getLicense() + "B");
	}
}