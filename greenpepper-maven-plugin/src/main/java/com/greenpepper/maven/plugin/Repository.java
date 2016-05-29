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

package com.greenpepper.maven.plugin;

import static com.greenpepper.util.CollectionUtil.toArray;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.TreeTraverser;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.server.domain.DocumentNode;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Repository class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Repository
{
    public final List<String> suites;
    public final List<String> tests;
    private String type;
    private String root;
    private String name;
    private boolean isDefault;
    private String projectName;
    private String systemUnderTest;

    private DocumentRepository documentRepository;

    /**
     * <p>Constructor for Repository.</p>
     */
    public Repository()
    {
        suites = new ArrayList<String>( );
        tests = new ArrayList<String>( );
    }

    /**
     * <p>addTest.</p>
     *
     * @param uri a {@link java.lang.String} object.
     */
    public void addTest(String uri)
    {
        tests.add( uri );
    }

    /**
     * <p>addSuite.</p>
     *
     * @param uri a {@link java.lang.String} object.
     */
    public void addSuite(String uri)
    {
        suites.add( uri );
    }

    /**
     * <p>Getter for the field <code>tests</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getTests()
    {
        return toArray(tests);
    }


    public DocumentNode retrieveDocumentHierarchy() throws Exception {
        DocumentRepository documentRepository = getDocumentRepository();
        Vector<Object> documentNodeParams = new Vector<Object>(documentRepository.getSpecificationsHierarchy(getProjectName(),getSystemUnderTest()));
        return DocumentNode.toDocumentNode(documentNodeParams);
    }

    /**
     * <p>Getter for the field <code>suites</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getSuites()
    {
        return toArray(suites);
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType()
    {
        return type;
    }

    /**
     * <p>Getter for the field <code>root</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRoot() 
    {
		return root;
	}

	/**
	 * <p>Setter for the field <code>root</code>.</p>
	 *
	 * @param root a {@link java.lang.String} object.
	 */
	public void setRoot(String root)
	{
		this.root = root;
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
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * <p>newInstance.</p>
     *
     * @return a {@link com.greenpepper.repository.DocumentRepository} object.
     * @throws java.lang.Exception if any.
     */
    public DocumentRepository newInstance() throws Exception {
        Class<?> klass = Class.forName( type );
        if (!DocumentRepository.class.isAssignableFrom(klass))
            throw new IllegalArgumentException("Not a " + DocumentRepository.class.getName() + ": " + type );

        Constructor<?> constructor = klass.getConstructor( String[].class );
        return (DocumentRepository) constructor.newInstance( new Object[]{ StringUtils.split(root,';') } );
    }

    /**
     * <p>Getter for the field <code>documentRepository</code>.</p>
     *
     * @return a {@link com.greenpepper.repository.DocumentRepository} object.
     * @throws java.lang.Exception if any.
     */
    public DocumentRepository getDocumentRepository() throws Exception {
        if (documentRepository == null) {
            documentRepository = newInstance();
        }
        return documentRepository;
    }

    /**
     * <p>isDefault.</p>
     *
     * @return a boolean.
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * <p>setDefault.</p>
     *
     * @param isDefault a boolean.
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSystemUnderTest() {
        return systemUnderTest;
    }

    public void setSystemUnderTest(String systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }
}
