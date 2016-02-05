package com.greenpepper.server.database.hibernate.upgrades;

import com.greenpepper.server.database.SessionService;

/**
 * <p>NoServerUpgrades class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class NoServerUpgrades implements ServerVersionUpgrader
{
	private String currentVersion;
	
	/**
	 * <p>Constructor for NoServerUpgrades.</p>
	 *
	 * @param currentVersion a {@link java.lang.String} object.
	 */
	public NoServerUpgrades(String currentVersion)
	{
		this.currentVersion = currentVersion;
	}

	/**
	 * <p>upgradedTo.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String upgradedTo() 
	{
		return currentVersion;
	}

	/** {@inheritDoc} */
	public void upgrade(SessionService service) 
	{
		// NO UPGRADES TO PERFORM
	}
}
