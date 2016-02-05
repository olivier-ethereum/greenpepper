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
package com.greenpepper.expectation;

import java.util.Arrays;

/**
 * <p>EqualExpectation class.</p>
 *
 * @version $Revision: $ $Date: $
 * @author oaouattara
 */
public class EqualExpectation implements Expectation
{
    private final Object matchee;

    /**
     * <p>Constructor for EqualExpectation.</p>
     *
     * @param matchee a {@link java.lang.Object} object.
     */
    public EqualExpectation( Object matchee )
    {
        this.matchee = matchee;
    }

    /** {@inheritDoc} */
    public StringBuilder describeTo( StringBuilder sb )
    {
        return sb.append( toString() );
    }

    /** {@inheritDoc} */
    public boolean meets( Object result )
    {
        if (isArray( matchee ))
        {
            return isArray( result ) && Arrays.deepEquals( (Object[]) matchee, (Object[]) result );
        }

        return matchee.equals( result );
    }

    private boolean isArray( Object o )
    {
        return o.getClass().getComponentType() != null;
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString()
    {
        return isArray( matchee ) ? Arrays.deepToString( (Object[]) matchee ) : matchee.toString();
    }
}
