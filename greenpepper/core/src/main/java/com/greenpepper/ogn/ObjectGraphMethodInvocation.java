/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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
package com.greenpepper.ogn;

import com.greenpepper.TypeConversion;

import java.lang.reflect.Method;

/**
 * <p>ObjectGraphMethodInvocation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ObjectGraphMethodInvocation implements ObjectGraphInvocable
{
    private final Method method;

    private final boolean getter;

    /**
     * <p>Constructor for ObjectGraphMethodInvocation.</p>
     *
     * @param method a {@link java.lang.reflect.Method} object.
     * @param isGetter a boolean.
     */
    public ObjectGraphMethodInvocation(Method method, boolean isGetter)
    {
        this.method = method;
        this.getter = isGetter;
    }

    /**
     * <p>invoke.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    public Object invoke(Object target, String... args) throws Exception
    {
        if (getter || method.getParameterTypes().length == 0)
        {
            return method.invoke(target);
        }
        else
        {
            Object[] values = TypeConversion.convert(args, method.getParameterTypes());

            return method.invoke(target, values);
        }
    }
}
