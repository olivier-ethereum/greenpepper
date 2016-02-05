/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.server.configuration;

import java.util.Properties;

/**
 * <p>DefaultServerProperties class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class DefaultServerProperties extends Properties
{

	/**
	 * <p>Constructor for DefaultServerProperties.</p>
	 */
	public DefaultServerProperties()
	{
		put("licence.keystore", "publicCerts.store");
		put("licence.key.alias", "publiccert");
		put("licence.keystore.pwd", "gr33np3pp3r");
		put("licence.cipher.key", "gr33np3pp3r");
		put("licence.subject", "GreenPepper");
		put("jta.UserTransaction", "java:comp/env/UserTransaction");
		put("hibernate.connection.datasource", "java:comp/env/jdbc/GreenPepperDS");
		put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		put("hibernate.hbm2ddl.auto", "update");
		put("hibernate.show_sql", "false");
		put("hibernate.setup", "true");
	}
}
