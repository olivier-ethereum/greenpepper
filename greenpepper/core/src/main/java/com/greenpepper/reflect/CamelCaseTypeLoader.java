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

import com.greenpepper.util.NameUtils;

/**
 * <p>CamelCaseTypeLoader class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CamelCaseTypeLoader<T> implements TypeLoader<T>
{
    private final TypeLoader<T> parent;

    /**
     * <p>Constructor for CamelCaseTypeLoader.</p>
     *
     * @param parent a {@link com.greenpepper.reflect.TypeLoader} object.
     */
    public CamelCaseTypeLoader(TypeLoader<T> parent)
    {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    public void searchPackage(String prefix)
    {
        parent.searchPackage( prefix );
    }

    /** {@inheritDoc} */
    public void addSuffix(String suffix) 
    {
        parent.addSuffix( suffix );
    }

    /** {@inheritDoc} */
    public Type<T> loadType(String name)
    {
        Type<T> type = parent.loadType(name);
        if (type == null)
            type = parent.loadType(NameUtils.toClassName(name));
        return type;
    }
}
