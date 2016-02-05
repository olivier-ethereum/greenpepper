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

/**
 * <p>Abstract Message class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class Message
{
    /**
     * <p>getArity.</p>
     *
     * @return a int.
     */
    public abstract int getArity();

    /**
     * <p>send.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    public abstract Object send( String... args ) throws Exception;

    /**
     * <p>assertArgumentsCount.</p>
     *
     * @param args a {@link java.lang.String} object.
     */
    protected void assertArgumentsCount( String... args )
    {
        if (args.length != getArity())
            throw new IllegalArgumentException(
                String.format( "Wrong number of arguments: expected %d but got %d", getArity(), args.length ) );
    }
}
