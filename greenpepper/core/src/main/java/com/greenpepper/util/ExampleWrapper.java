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

import com.greenpepper.AbstractExample;
import com.greenpepper.Example;
import com.greenpepper.annotation.Annotation;

import java.io.PrintWriter;

/**
 * <p>ExampleWrapper class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ExampleWrapper extends AbstractExample
{
    /**
     * <p>sandbox.</p>
     *
     * @param firstChild a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    public static Example sandbox( Example firstChild )
    {
        return new ExampleWrapper( firstChild, null );
    }

    /**
     * <p>empty.</p>
     *
     * @param nextSibling a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    public static Example empty( Example nextSibling )
    {
        return new ExampleWrapper( null, nextSibling );
    }

    private final Example nextSibling;

    private final Example firstChild;

    /**
     * <p>Constructor for ExampleWrapper.</p>
     *
     * @param firstChild a {@link com.greenpepper.Example} object.
     * @param nextSibling a {@link com.greenpepper.Example} object.
     */
    protected ExampleWrapper( Example firstChild, Example nextSibling )
    {
        super();
        
        this.nextSibling = nextSibling;
        this.firstChild = firstChild;
    }

    /**
     * <p>firstChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example firstChild()
    {
        return firstChild;
    }

    /**
     * <p>nextSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example nextSibling()
    {
        return nextSibling;
    }

    /**
     * <p>getContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getContent()
    {
        return null;
    }

    /** {@inheritDoc} */
    public void annotate( Annotation annotation )
    {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void print( PrintWriter out )
    {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void setContent( String content )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>addSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example addSibling()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>addChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example addChild()
    {
        throw new UnsupportedOperationException();
    }
}
