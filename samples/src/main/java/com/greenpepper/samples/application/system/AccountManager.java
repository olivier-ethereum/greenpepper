
/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.samples.application.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AccountManager
{
	private static final String ADMINISTRATOR_GROUP_ID = "administrator";
	private static final String USER_GROUP_ID = "user";
	private static final String ADMIN_USER_ID = "admin";

	private static List<String> allGroups = new ArrayList<String>();
	private static List<String> allUsers = new ArrayList<String>();
	private static Map<String, List<String>> allAssociations = new HashMap<String, List<String>>();

	static
	{
		// Pre-existing data (mandatory)
		allGroups.add(ADMINISTRATOR_GROUP_ID);
		allGroups.add(USER_GROUP_ID);
		allUsers.add(ADMIN_USER_ID);
		allAssociations.put(ADMIN_USER_ID, Arrays.asList(ADMINISTRATOR_GROUP_ID));

		/**
		 groups.add("cartoon");
		 users.add("bart.simpson");
		 users.add("homer.simpson");
		 associations.put("bart.simpson", Arrays.asList("cartoon"));
		 associations.put("homer.simpson", Arrays.asList("cartoon"));
		 /**/
	}

	/**
	 * <p>insertGroup.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean insertGroup(String name)
	{
		if (isGroupExist(name))
		{
			throw new SystemException(String.format("Group '%s' already exist.", name));
		}

		return allGroups.add(name);
	}

	/**
	 * <p>deleteGroup.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean deleteGroup(String name)
	{
		if (ADMINISTRATOR_GROUP_ID.equals(name))
		{
			throw new SystemException(String.format("Cannot delete '%s' group.", ADMINISTRATOR_GROUP_ID));
		}

		if (USER_GROUP_ID.equals(name))
		{
			throw new SystemException(String.format("Cannot delete '%s' group.", USER_GROUP_ID));
		}

		if (!isGroupExist(name))
		{
			throw new SystemException(String.format("Group '%s' does not exist.", name));
		}

		if (getGroupUserCount(name) > 0)
		{
			throw new SystemException("Cannot delete a group containing users.");
		}

		return allGroups.remove(name);
	}

	/**
	 * <p>Getter for the field <code>allGroups</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getAllGroups()
	{
		String[] groups = new String[allGroups.size()];
		allGroups.toArray(groups);
		return groups;
	}

	/**
	 * <p>insertUser.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean insertUser(String name)
	{
		if (isUserExist(name))
		{
			throw new SystemException(String.format("User '%s' already exist.", name));
		}

		return allUsers.add(name);
	}

	/**
	 * <p>deleteUser.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean deleteUser(String name)
	{
		if (ADMIN_USER_ID.equals(name))
		{
			throw new SystemException(String.format("Cannot delete '%s' user.", ADMIN_USER_ID));
		}

		if (!isUserExist(name))
		{
			throw new SystemException(String.format("User '%s' does not exist.", name));
		}

		allAssociations.remove(name);

		return allUsers.remove(name);
	}

	/**
	 * <p>Getter for the field <code>allUsers</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getAllUsers()
	{
		String[] users = new String[allUsers.size()];
		allUsers.toArray(users);
		return users;
	}

	/**
	 * <p>associateUserWithGroup.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 * @param group a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean associateUserWithGroup(String user, String group)
	{
		if (!isUserExist(user))
		{
			throw new SystemException(String.format("User '%s' does not exist.", user));
		}

		if (!isGroupExist(group))
		{
			throw new SystemException(String.format("Group '%s' does not exist.", group));
		}

		List<String> userGroups = allAssociations.get(user);

		if (userGroups == null)
		{
			userGroups = new ArrayList<String>();
		}
		else if (userGroups.contains(group))
		{
			return false;
		}

		userGroups.add(group);

		allAssociations.put(user, userGroups);

		return true;
	}

	/**
	 * <p>isUserAssociatedToGroup.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 * @param group a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isUserAssociatedToGroup(String user, String group)
	{
		if (!isUserExist(user))
		{
			throw new SystemException(String.format("User '%s' does not exist.", user));
		}

		if (!isGroupExist(group))
		{
			throw new SystemException(String.format("Group '%s' does not exist.", group));
		}

		List<String> userGroups = allAssociations.get(user);

		return userGroups != null && userGroups.contains(group);
	}

	/**
	 * <p>isGroupExist.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isGroupExist(String name)
	{
		return allGroups.contains(name);
	}

	/**
	 * <p>isUserExist.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isUserExist(String name)
	{
		return allUsers.contains(name);
	}

	private int getGroupUserCount(String name)
	{
		int count = 0;

		for (String user : allAssociations.keySet())
		{
			List<String> groups = allAssociations.get(user);

			if (groups != null && groups.contains(name))
			{
				count++;
			}
		}

		return count;
	}
}
