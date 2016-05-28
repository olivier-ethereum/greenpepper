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

package com.greenpepper.extensions.ognl;

import com.greenpepper.reflect.Message;

/**
 * This <code>Action</code> implementation extract the value of an OGNL expression.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class OgnlGetter extends Message
{
    private final OgnlExpression ognlExpression;

    /**
     * <p>Constructor for OgnlGetter.</p>
     *
     * @param ognlExpression a {@link com.greenpepper.extensions.ognl.OgnlExpression} object.
     */
    public OgnlGetter( OgnlExpression ognlExpression )
    {
        this.ognlExpression = ognlExpression;
    }

    /**
     * <p>getArity.</p>
     *
     * @return a int.
     */
    public int getArity()
    {
        return 0;
    }

    /**
     * <p>send.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    public Object send( String... args ) throws Exception
    {
        assertArgumentsCount( args );
        return ognlExpression.extractValue();
    }
}
