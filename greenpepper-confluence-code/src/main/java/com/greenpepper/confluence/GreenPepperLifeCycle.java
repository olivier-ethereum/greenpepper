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

import org.apache.log4j.Logger;

import com.atlassian.config.lifecycle.LifecycleContext;
import com.atlassian.config.lifecycle.LifecycleItem;
import com.atlassian.spring.container.ContainerManager;
import com.greenpepper.server.GreenPepperServer;

public class GreenPepperLifeCycle
		implements LifecycleItem
{
	private static Logger log = Logger.getLogger(GreenPepperLifeCycle.class);

	private GreenPepperServerConfigurationActivator activator;

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
			
			activator.setServletContext(lifecycleContext.getServletContext()); // sync

			log.debug("Configuration:" + activator.getConfiguration());
			activator.startup(false);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

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