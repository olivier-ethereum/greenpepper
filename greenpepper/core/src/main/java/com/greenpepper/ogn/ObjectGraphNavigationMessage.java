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

import com.greenpepper.reflect.Message;

import java.util.Collection;

/**
 * <p>ObjectGraphNavigationMessage class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ObjectGraphNavigationMessage extends Message
{
    private final Object target;

    private final Collection<ObjectGraphInvocable> invocableChain;
    
    /**
     * <p>Constructor for ObjectGraphNavigationMessage.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @param invocableChain a {@link java.util.Collection} object.
     */
    public ObjectGraphNavigationMessage(Object target, Collection<ObjectGraphInvocable> invocableChain)
    {
        super();
        
        this.target = target;
        this.invocableChain = invocableChain;
    }

    /** {@inheritDoc} */
    @Override
    public int getArity()
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public Object send(String... args) throws Exception
    {
        Object result = null;
        Object newTarget = target;

        for (ObjectGraphInvocable i : invocableChain)
        {
            result = i.invoke(newTarget, args);
            newTarget = result;
        }

        return result;
    }    
}
