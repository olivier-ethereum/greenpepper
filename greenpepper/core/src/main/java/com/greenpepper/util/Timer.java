
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
package com.greenpepper.util;
public class Timer
{

	private long start;
	private long finish;
	private boolean running;

	/**
	 * <p>Constructor for Timer.</p>
	 */
	public Timer()
	{
	}

	/**
	 * <p>start.</p>
	 *
	 * @return a {@link com.greenpepper.util.Timer} object.
	 */
	public Timer start()
	{
		start = System.currentTimeMillis();
		running = true;
		return this;
	}

	/**
	 * <p>stop.</p>
	 *
	 * @return a {@link com.greenpepper.util.Timer} object.
	 */
	public Timer stop()
	{
		finish = System.currentTimeMillis();
		running = false;
		return this;
	}

	/**
	 * <p>elapse.</p>
	 *
	 * @return a long.
	 */
	public long elapse()
	{
		if (running)
		{
			return finish - start;
		}
		else if (start == 0)
		{
			return 0;
		}
		else
		{
			return System.currentTimeMillis() - start;
		}
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString()
	{
		return elapse() + " ms.";
	}
}
