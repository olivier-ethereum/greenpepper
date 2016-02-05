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

package com.greenpepper;

import com.greenpepper.annotation.Annotation;

import java.io.PrintWriter;

/**
 * <p>TextExample class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class TextExample extends AbstractExample
{
    private final String text;

    /**
     * <p>Constructor for TextExample.</p>
     *
     * @param text a {@link java.lang.String} object.
     */
    public TextExample( String text )
    {
        super();
        
        this.text = text;
    }

    /**
     * <p>firstChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example firstChild()
    {
        return null;
    }

    /**
     * <p>nextSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example nextSibling()
    {
        return null;
    }

    /** {@inheritDoc} */
    public void print( PrintWriter out )
    {
        out.write( text );
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
