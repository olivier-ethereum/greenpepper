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

package com.greenpepper.report;

import com.greenpepper.TextExample;
import com.greenpepper.document.Document;
import com.greenpepper.util.ExceptionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * <p>PlainReport class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PlainReport implements Report
{
    private final String name;
    private Document document;

    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.report.PlainReport} object.
     */
    public static PlainReport newInstance( String name )
    {
        return new PlainReport( name );
    }

    /**
     * <p>Constructor for PlainReport.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public PlainReport( String name )
    {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>getType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType()
    {
        return document != null ? document.getType() : null;
    }

    /** {@inheritDoc} */
    public void printTo( Writer writer ) throws IOException
    {
        if (document == null) return;
        document.print( new PrintWriter( writer ) );
    }

    /** {@inheritDoc} */
    public void renderException( Throwable t )
    {
        document = Document.text( new TextExample( ExceptionUtils.stackTrace( t, "\n" ) ) );
    }

    /** {@inheritDoc} */
    public void generate( Document doc )
    {
        this.document = doc;
    }

    /** {@inheritDoc} */
    @Override
    public String getDocumentUri() {
        if (document == null)  {
            return null;
        }
        return document.getUri();
    }
}
