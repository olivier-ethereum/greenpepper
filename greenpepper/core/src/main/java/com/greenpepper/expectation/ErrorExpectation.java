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

import com.greenpepper.util.FactoryMethod;

/**
 * <p>ErrorExpectation class.</p>
 *
 * @version $Revision: $ $Date: $
 * @author oaouattara
 */
public class ErrorExpectation implements Expectation
{
    static final String ERROR = "error";

    /**
     * <p>create.</p>
     *
     * @param expected a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.expectation.ErrorExpectation} object.
     */
    @FactoryMethod
    public static ErrorExpectation create( String expected )
    {
        return "error".equalsIgnoreCase( expected ) ? new ErrorExpectation() : null;
    }

    /** {@inheritDoc} */
    public StringBuilder describeTo( StringBuilder sb )
    {
        return sb.append( ERROR );
    }

    /** {@inheritDoc} */
    public boolean meets( Object result )
    {
        return Throwable.class.isInstance( result );
    }
}
