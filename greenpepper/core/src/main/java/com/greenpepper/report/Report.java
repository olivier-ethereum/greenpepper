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

import com.greenpepper.document.Document;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for formating test results.
 */
public interface Report
{
    String getName();

    String getType();

    /**
     * Adds a test result as a string to this <code>OutputFormat</code>.
     */
    void printTo( Writer out ) throws IOException;

    void renderException( Throwable t );

    //
    void generate( Document doc );

    String getDocumentUri();
}