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

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.EntityException;
import com.atlassian.user.Group;
import com.atlassian.user.GroupManager;
import com.atlassian.user.User;
import com.atlassian.user.search.page.Pager;
import com.greenpepper.server.GreenPepperServerException;

public class GreenPepperUserGroup
{
	private static final Logger log = Logger.getLogger(GreenPepperUserGroup.class);

	private static final String GREENPEPPER_USERS = "greenpepper-users";

	private GroupManager groupManager;

	public void createIfNeeded()
	{
		Group group = null;

		try
		{
			group = getGreenPepperUserGroup();
		}
		catch (Exception ex)
		{
			log.warn("No 'greenpepper-users' group defined.  Will be created");
		}

		if (group == null)
		{
			try
			{
				getGroupManager().createGroup(GREENPEPPER_USERS);
			}
			catch (Exception ex)
			{
				log.warn("Creating 'greenpepper-users' group fail", ex);
			}
		}
	}

	public int getNumberOfUserForGroup()
			throws GreenPepperServerException
	{
		try
		{
			final long start = System.currentTimeMillis();
			Iterator itr = getMembers().iterator();
			int count = 0;

			while (itr.hasNext())
			{
				itr.next();
				count++;
			}

			log.debug("Number of user (member of 'greenpepper-users') = " + count +
					  " (" + (System.currentTimeMillis() - start) + " ms.)");
			return count;
		}
		catch (Exception ex)
		{
			log.error("Getting user-count for group", ex);
			throw new RuntimeException("Getting user-count for group", ex);
		}
	}

	public boolean hasMembership(User user)
	{
		try
		{
			Group group = getGreenPepperUserGroup();
			
			return getGroupManager().hasMembership(group, user);
		}
		catch (Exception ex)
		{
			log.error("Verifying membership of  user '" + user.getName() + "'", ex);
			return false;
		}
	}

	public boolean addMembership(User user)
	{
		try
		{
			Group group = getGreenPepperUserGroup();

			getGroupManager().addMembership(group, user);

			return true;
		}
		catch (Exception ex)
		{
			log.error("Adding membership of  user '" + user.getName() + "'", ex);
			return false;
		}
	}

	private Pager getMembers()
			throws EntityException
	{
		Group group = getGreenPepperUserGroup();

		return getGroupManager().getMemberNames(group);
	}

	private Group getGreenPepperUserGroup()
			throws EntityException
	{
		return getGroupManager().getGroup(GREENPEPPER_USERS);
	}

	private GroupManager getGroupManager()
	{
		if (groupManager != null){return groupManager;}
		groupManager = (GroupManager) ContainerManager.getComponent("groupManager");
		return groupManager;
	}
}