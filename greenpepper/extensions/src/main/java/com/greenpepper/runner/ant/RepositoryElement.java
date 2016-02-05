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

import com.greenpepper.repository.FileSystemRepository;
import static com.greenpepper.util.StringUtil.escapeSemiColon;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>RepositoryElement class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class RepositoryElement
{
	private String className = FileSystemRepository.class.getName();

	private List<TextData> arguments = new ArrayList<TextData>();

	/**
	 * <p>Constructor for RepositoryElement.</p>
	 */
	public RepositoryElement()
	{

	}

	/**
	 * <p>createArguments.</p>
	 *
	 * @return a {@link com.greenpepper.runner.ant.TextData} object.
	 */
	public TextData createArguments()
	{
		TextData argument = new TextData();
		arguments.add(argument);
		return argument;
	}

	/**
	 * <p>setClass.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 */
	public void setClass(String className)
	{
		this.className = className;
	}

	/**
	 * <p>toArgument.</p>
	 *
	 * @param task a {@link org.apache.tools.ant.Task} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toArgument(Task task)
	{
		task.log(String.format("Repository Class \"%s\"", className), Project.MSG_VERBOSE);

		StringBuilder argumentBuilder = new StringBuilder(className);

		for (TextData argument : arguments)
		{
			task.log(String.format("\tArgument \"%s\"", argument), Project.MSG_VERBOSE);

			argumentBuilder.append(";").append(escapeSemiColon(argument.getText()));
		}

		return argumentBuilder.toString();
	}

}
