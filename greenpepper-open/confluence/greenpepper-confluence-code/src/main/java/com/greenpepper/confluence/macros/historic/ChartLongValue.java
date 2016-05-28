
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
package com.greenpepper.confluence.macros.historic;

import java.io.Serializable;
public class ChartLongValue
		implements Serializable, Comparable
{

	private String value;
	private Long id;

	/**
	 * <p>Constructor for ChartLongValue.</p>
	 */
	public ChartLongValue()
	{
	}

	/**
	 * <p>Constructor for ChartLongValue.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @param id a {@link java.lang.Long} object.
	 */
	public ChartLongValue(String value, Long id)
	{
		this.value = value;
		this.id = id;
	}

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getId()
	{
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		return String.valueOf(value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ChartLongValue))
		{
			return false;
		}

		ChartLongValue o = (ChartLongValue)obj;
		return o.value.equals(value);
	}

	/** {@inheritDoc} */
	public int compareTo(Object o)
	{
		if (o instanceof ChartLongValue)
		{
			return ((ChartLongValue)o).value.compareTo(value);
		}
		else if (o instanceof String)
		{
			return ((String)o).compareTo(value);
		}

		return 1;
	}
}
