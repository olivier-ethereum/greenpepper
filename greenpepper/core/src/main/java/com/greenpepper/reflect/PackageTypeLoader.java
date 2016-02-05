/*
 * Copyright (c) 2006 Pyxis Technologies inc.
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

package com.greenpepper.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>PackageTypeLoader class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PackageTypeLoader<T> implements TypeLoader<T>
{
    private final TypeLoader<T> parent;
    private final List<String> packages = new ArrayList<String>();

    /**
     * <p>Constructor for PackageTypeLoader.</p>
     *
     * @param parent a {@link com.greenpepper.reflect.TypeLoader} object.
     */
    public PackageTypeLoader(TypeLoader<T> parent)
    {
        this.parent = parent;
        packages.add("");
    }

    /** {@inheritDoc} */
    public void searchPackage(String name)
    {
		addPackage(name + ".");
    }

    /** {@inheritDoc} */
    public void addSuffix(String suffix)
    {
        parent.addSuffix(suffix);
    }

    /** {@inheritDoc} */
    public Type<T> loadType(String className)
    {
        for (String packageName : packages)
        {
            Type<T> type = parent.loadType( packageName + className );
            if (type != null) return type;
        }

        return null;
    }

	private void addPackage(String name)
	{
		if (packages.contains( name ))
		{
			packages.remove( name );
		}

		packages.add( 0, name );
	}
}
