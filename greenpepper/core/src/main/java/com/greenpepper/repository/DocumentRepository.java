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

package com.greenpepper.repository;

import com.greenpepper.document.Document;

import java.io.IOException;
import java.util.List;

/**
 * Retrieving of test specifications is done through the Repository interface.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface DocumentRepository
{
    /**
     * <p>loadDocument.</p>
     *
     * @param location a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.document.Document} object.
     * @throws java.lang.Exception if any.
     */
    Document loadDocument( String location ) throws Exception;

    /**
     * Consider renaming to listAllDocumentsUnder( String location )
     * and having a method listDocumentsAt( String location )
     * that would return the child specs exactly at the location
     * (i.e. no recursivity)
     *
     * @param location a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws java.lang.Exception if any.
     */
    List<String> listDocuments( String location ) throws Exception;

    /**
     * <p>listDocumentsInHierarchy.</p>
     *
     * @return a {@link java.util.List} object.
     * @throws java.lang.Exception if any.
     */
    List<Object> listDocumentsInHierarchy() throws Exception;

    /**
     *      TODO We should extract this method into its own interface
     *      and let Repository  implementations decide to implement the new
     *      interface or not.
     *      The eclipse plugin could check for the presence of that interface
     *      to decide whether to show the Set As Implemented context action
     *      on pages.
     *
     * @param location a {@link java.lang.String} object.
     * @throws java.lang.Exception if any.
     */
    void setDocumentAsImplemeted( String location ) throws Exception;

    /**
     * Retrieves only the specification related to a specified project and a systemUnderTest
     *
     * @param project
     * @param systemUnderTest
     * @return The specification hierarchy.
     */
    List<Object> getSpecificationsHierarchy( String project, String systemUnderTest) throws Exception;
}
