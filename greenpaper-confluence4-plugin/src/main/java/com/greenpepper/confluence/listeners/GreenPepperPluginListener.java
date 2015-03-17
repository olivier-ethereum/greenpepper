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
package com.greenpepper.confluence.listeners;

import org.apache.log4j.Logger;

import com.atlassian.confluence.event.events.plugin.PluginEnableEvent;
import com.atlassian.confluence.event.events.plugin.PluginEvent;
import com.atlassian.confluence.event.events.plugin.PluginInstallEvent;
import com.atlassian.event.Event;
import com.atlassian.event.EventListener;
import com.atlassian.spring.container.ContainerManager;
import com.greenpepper.confluence.GreenPepperServerConfigurationActivator;
import com.greenpepper.server.GreenPepperServerException;

public class GreenPepperPluginListener implements EventListener
{

	private static final Logger log = Logger.getLogger(GreenPepperPluginListener.class);

	private GreenPepperServerConfigurationActivator configurationActivator;

	public void handleEvent(Event event)
	{
		if (event instanceof PluginInstallEvent
			|| event instanceof PluginEnableEvent)
		{
			try
			{
				getServerConfigurationActivator().startup(false);
			}
			catch (GreenPepperServerException ex)
			{
				log.error("Post-install : startup failed", ex);
				throw new RuntimeException("Post-install : startup failed", ex);
			}

		}
	}

	public Class[] getHandledEventClasses()
	{
		return new Class[] {PluginEvent.class};
	}

	private GreenPepperServerConfigurationActivator getServerConfigurationActivator()
	{
		if (configurationActivator == null)
		{
			configurationActivator = (GreenPepperServerConfigurationActivator)ContainerManager.getComponent(
							"greenPepperServerConfigurationActivator");
		}

		return configurationActivator;
	}
}
