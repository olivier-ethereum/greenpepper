
/**
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.server.rpc.runner.report;

import java.io.IOException;
import java.io.Writer;

import com.greenpepper.server.domain.Execution;
public interface Report
{

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getName();

	/**
	 * <p>getType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getType();

	/**
	 * <p>generate.</p>
	 *
	 * @param execution a {@link com.greenpepper.server.domain.Execution} object.
	 * @throws java.lang.Exception if any.
	 */
	void generate(Execution execution)
			throws Exception;

	/**
	 * <p>renderException.</p>
	 *
	 * @param t a {@link java.lang.Throwable} object.
	 */
	void renderException(Throwable t);

	/**
	 * <p>printTo.</p>
	 *
	 * @param out a {@link java.io.Writer} object.
	 * @throws java.io.IOException if any.
	 */
	void printTo(Writer out)
			throws IOException;
}
