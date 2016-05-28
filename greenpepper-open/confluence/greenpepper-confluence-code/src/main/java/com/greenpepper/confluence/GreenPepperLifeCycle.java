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
package com.greenpepper.confluence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.config.lifecycle.LifecycleContext;
import com.atlassian.config.lifecycle.LifecycleItem;
import com.atlassian.spring.container.ContainerManager;
import com.greenpepper.server.GreenPepperServer;

/**
 * <p>GreenPepperLifeCycle class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperLifeCycle
		implements LifecycleItem
{
	private static Logger log = LoggerFactory.getLogger(GreenPepperLifeCycle.class);

	private GreenPepperServerConfigurationActivator activator;

	/** {@inheritDoc} */
	public void startup(LifecycleContext lifecycleContext)
			throws Exception
	{
		log.debug(GreenPepperLifeCycle.class.getName() + "#startup(" + String.valueOf(lifecycleContext) + ")");
		log.info(String.format("*** Starting GreenPepper-Confluence-Plugin (v%s - %s ) ***",
							   GreenPepperServer.VERSION, GreenPepperServer.versionDate()));
		
		try
		{
			activator = (GreenPepperServerConfigurationActivator)ContainerManager.getComponent(
							"greenPepperServerConfigurationActivator");
			
			log.debug("Configuration:" + activator.getConfiguration());
			activator.startup(false);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	public void shutdown(LifecycleContext lifecycleContext)
			throws Exception
	{
		log.info(GreenPepperLifeCycle.class.getName() + "#shutdown(" + String.valueOf(lifecycleContext) + ")");
		if (activator != null)
		{
			activator.shutdown();
		}
	}
}
