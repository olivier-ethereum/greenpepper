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
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.URIUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * <p>FileSystemRepository class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class FileSystemRepository implements DocumentRepository
{
    private static final FileFilter NOT_HIDDEN = new NotHiddenFilter();
    private final File root;

    /**
     * <p>Constructor for FileSystemRepository.</p>
     *
     * @param args a {@link java.lang.String} object.
     */
    public FileSystemRepository( String... args )
    {
    	if (args.length != 1) throw new IllegalArgumentException("root");
        this.root = new File(URIUtil.decoded(args[0]));
    }

    /**
     * <p>Constructor for FileSystemRepository.</p>
     *
     * @param root a {@link java.io.File} object.
     */
    public FileSystemRepository( File root )
    {
        this.root = root;
    }

	/** {@inheritDoc} */
	public void setDocumentAsImplemeted(String location) throws Exception{	}

    @Override
    public List<Object> getSpecificationsHierarchy(String project, String systemUnderTest) throws IOException {

        if (root == null) {
            return new ArrayList<Object>();
        }

        return  generateDocumentNode(root, null);
    }

    private void navigateInto(File directory, Hashtable<String, Vector<?>> pageBranch) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                generateDocumentNode(file, pageBranch);
            }
        }
    }

    private Vector<Object> generateDocumentNode(File file, Hashtable<String, Vector<?>> pageBranch) throws IOException {
        Vector<Object> page = new Vector<Object>();
        page.add(file.equals(root) ? root.getName() : relativePath(file));
        page.add(file.isFile());
        page.add(Boolean.FALSE);
        Hashtable<String, Vector<?>> subPageBranch = new Hashtable<String, Vector<?>>();
        page.add(subPageBranch);
        if (pageBranch != null) {
            pageBranch.put(relativePath(file), page);
        }
        if (file.isDirectory()) {
            navigateInto(file, subPageBranch);
        }
        return page;
    }

    /** {@inheritDoc} */
    public List<String> listDocuments( String location ) throws IOException
    {
        File parent = fileAt( location );
        if (!parent.exists()) return Collections.emptyList();

        List<String> names = new ArrayList<String>();
        if (parent.isDirectory())
        {
            for (File child : parent.listFiles( NOT_HIDDEN ))
            {
                if (child.isDirectory())
                {
                    names.addAll( listDocuments( relativePath( child ) ) );
                }
                else if (isSupported( child ))
                {
                    names.add( relativePath( child ) );
                }
            }
        }
        return names;
    }

	/**
	 * <p>listDocumentsInHierarchy.</p>
	 *
	 * @return a {@link java.util.List} object.
	 * @throws java.lang.Exception if any.
	 */
	public List<Object> listDocumentsInHierarchy() throws Exception 
	{
		Vector<Object> hierarchy = toHierarchyNodeVector(root);
		hierarchy.setElementAt(root.getName(), 0);
		hierarchy.setElementAt(false, 1);
		hierarchy.setElementAt(false, 2);
		return hierarchy;
	}

    /** {@inheritDoc} */
    public Document loadDocument( String location ) throws Exception
    {
        File file = fileAt( location );
        if (!file.exists()) throw new DocumentNotFoundException( file.getAbsolutePath() );

        if (isSupported( file ))
        {
            return loadHtmlDocument( file );
        }

        throw new UnsupportedDocumentException( location );
    }

    private File fileAt( String location )
    {
        return new File( root, location );
    }

    private String relativePath( File file ) throws IOException
    {
        return normalizedPath( file ).substring( normalizedPath( root ).length() );
    }

    private String normalizedPath( File file ) throws IOException
    {
        return file.getCanonicalPath().replaceAll( "\\" + File.separator, "/" );
    }

    private Document loadHtmlDocument( File file ) throws IOException
    {
        Reader reader = new FileReader( file );
        try
        {
            Document document = HtmlDocumentBuilder.tablesAndLists().build(reader);
            document.setUri(file.toURI().toString());
            return document;
        }
        finally
        {
            IOUtil.closeQuietly( reader );
        }
    }

    private static class NotHiddenFilter implements FileFilter
    {
        public boolean accept( File pathname )
        {
            return !pathname.isHidden();
        }
    }

    private boolean isSupported( File pathname )
    {
        return pathname.getAbsolutePath().toLowerCase().endsWith( ".html" );
    }
	
	private Vector<Object> toHierarchyNodeVector(File file)
	{
        Vector<Object> vector = new Vector<Object>();
        vector.add(0, URIUtil.relativize(root.getAbsolutePath(), file.getAbsolutePath()));
        vector.add(1, !file.isDirectory());
        vector.add(2, false);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        if(file.isDirectory() && file.listFiles() != null)
        {
	        for (File node : file.listFiles( NOT_HIDDEN ))
	        {
	        	try
	        	{
	        		hashtable.put(node.getName(), toHierarchyNodeVector(node));
	        	}
	        	catch(Exception e)
	        	{
	        		// URI not standard skip it !
	        	}
	        }
        }

        vector.add(3, hashtable);
        return vector;
	}
}
