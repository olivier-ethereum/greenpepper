
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
package com.greenpepper.server.domain;

import java.util.Comparator;
public class SystemUnderTestByNameComparator
		implements Comparator<SystemUnderTest>
{

	/**
	 * <p>compare.</p>
	 *
	 * @param o1 a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param o2 a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @return a int.
	 */
	public int compare(SystemUnderTest o1, SystemUnderTest o2)
	{
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}
