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
package com.greenpepper.runner.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * <p>SectionElement class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SectionElement
{
	private List<TextData> includes = new ArrayList<TextData>();

	/**
	 * <p>Constructor for SectionElement.</p>
	 */
	public SectionElement()
	{
	}

	/**
	 * <p>createIncludes.</p>
	 *
	 * @return a {@link com.greenpepper.runner.ant.TextData} object.
	 */
	public TextData createIncludes()
	{
		TextData include = new TextData();
		includes.add(include);
		return include;
	}

	/**
	 * <p>toArgument.</p>
	 *
	 * @param task a {@link org.apache.tools.ant.Task} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toArgument(Task task)
	{
		StringBuilder argumentBuilder = new StringBuilder();

		for(TextData include : includes) {

			if (argumentBuilder.length() > 0)
            {
				argumentBuilder.append(";");
			}

			argumentBuilder.append(include.getText());
			
			task.log(String.format("\tSection \"%s\"", include), Project.MSG_VERBOSE);
        }

		return argumentBuilder.toString();
	}
}
