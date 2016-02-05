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
package com.greenpepper.util;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.Annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>Group class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Group implements Annotatable
{
    private final List<Annotatable> annotatables;

    /**
     * <p>Constructor for Group.</p>
     *
     * @param annotatables a {@link java.util.Collection} object.
     */
    public Group( Collection<Annotatable> annotatables )
    {
        this.annotatables = new ArrayList<Annotatable>( annotatables );
    }

    /**
     * <p>empty.</p>
     *
     * @return a {@link com.greenpepper.util.Group} object.
     */
    public static Group empty()
    {
        return new Group( Collections.<Annotatable>emptyList() );
    }

    /**
     * <p>composedOf.</p>
     *
     * @param annotatables a {@link java.util.Collection} object.
     * @return a {@link com.greenpepper.util.Group} object.
     */
    @SuppressWarnings("unchecked")
    public static Group composedOf( Collection annotatables )
    {
        return new Group( annotatables );
    }

    /**
     * <p>composedOf.</p>
     *
     * @param annotatables a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.util.Group} object.
     */
    public static Group composedOf( Annotatable... annotatables )
    {
        return composedOf( Arrays.asList( annotatables ) );
    }

    /**
     * <p>and.</p>
     *
     * @param listener a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.util.Group} object.
     */
    public Group and( Annotatable listener )
    {
        annotatables.add( listener );
        return this;
    }

    /** {@inheritDoc} */
    public void annotate( Annotation annotation )
    {
        for (Annotatable listener : annotatables)
        {
            listener.annotate( annotation );
        }
    }
}
