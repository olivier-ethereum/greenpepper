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

package com.greenpepper.repository;

import java.net.URI;
import java.net.URL;

/**
 * <p>DocumentNotFoundException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class DocumentNotFoundException extends RepositoryException
{
    private final String location;

    /**
     * <p>Constructor for DocumentNotFoundException.</p>
     *
     * @param location a {@link java.net.URI} object.
     */
    public DocumentNotFoundException( URI location )
    {
        this( location.toASCIIString() );
    }

    /**
     * <p>Constructor for DocumentNotFoundException.</p>
     *
     * @param location a {@link java.net.URL} object.
     */
    public DocumentNotFoundException( URL location )
    {
        this( location.toExternalForm() );
    }

    /**
     * <p>Constructor for DocumentNotFoundException.</p>
     *
     * @param location a {@link java.lang.String} object.
     */
    public DocumentNotFoundException( String location )
    {
        super();

        this.location = location;
    }

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage()
    {
        return "Document not found " + location;
    }
}
