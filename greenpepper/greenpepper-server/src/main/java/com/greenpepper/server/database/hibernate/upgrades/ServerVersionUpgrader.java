package com.greenpepper.server.database.hibernate.upgrades;

import com.greenpepper.server.database.SessionService;

/**
 * <p>ServerVersionUpgrader interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface ServerVersionUpgrader 
{
	/**
	 * <p>upgrade.</p>
	 *
	 * @param service a {@link com.greenpepper.server.database.SessionService} object.
	 * @throws java.lang.Exception if any.
	 */
	public void upgrade(SessionService service) throws Exception;

	/**
	 * <p>upgradedTo.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String upgradedTo();
}
