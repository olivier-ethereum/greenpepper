
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

import java.util.Collection;
import java.util.TreeSet;

import com.greenpepper.util.StringUtil;
public class ClasspathSet extends TreeSet<String>
{

    /**
     * <p>Constructor for ClasspathSet.</p>
     */
    public ClasspathSet()
    {
        super( new ClasspathComparator() );
    }

    /**
     * <p>Constructor for ClasspathSet.</p>
     *
     * @param c a {@link java.util.Collection} object.
     */
    public ClasspathSet(Collection<? extends String> c)
    {
        super( c );
    }

    /**
     * <p>parse.</p>
     *
     * @param classpaths a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.ClasspathSet} object.
     */
    public static ClasspathSet parse(String classpaths)
    {

        ClasspathSet newClasspaths = new ClasspathSet();

        if (!StringUtil.isEmpty( classpaths ))
        {
            String[] entries = classpaths.split( "[\r\n|\n|\r]" );

            for (String entry : entries)
            {
                if (!StringUtil.isEmpty( entry ))
                {
                    newClasspaths.add( entry.trim() );
                }
            }
        }

        return newClasspaths;
    }
}
