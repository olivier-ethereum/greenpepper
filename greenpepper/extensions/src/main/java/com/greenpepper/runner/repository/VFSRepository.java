package com.greenpepper.runner.repository;

import com.greenpepper.document.Document;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.repository.DocumentNotFoundException;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.UnsupportedDocumentException;
import com.greenpepper.util.ExceptionImposter;
import com.greenpepper.util.IOUtil;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.commons.vfs.provider.FileProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The goal of the file system repository is to give access to specification
 * documents kept in a file system directory hierarchical structure.
 *
 * This particular VFSRepository is implemented using commons.VFS
 * (Virtual File Systems). It allows us to implements our file system with
 * different kinds of protocol (file system, FTP, HTTP ...) It is also possible
 * to create our own resolver and implements, for example, a file system inside
 * a database table(s) or maybe, most interestigly for GreenPepper, inside a wiki.
 *
 * See documentation of VFS for more detail.
 *
 * @author clapointe
 * @version $Id: $Id
 */
public class VFSRepository implements DocumentRepository
{
    private final FileSystemManager fileSystemManager;
    /** Constant <code>NOT_HIDDEN_FILE_TYPE_SELECTOR</code> */
    protected static final NotHiddenFileTypeSelector NOT_HIDDEN_FILE_TYPE_SELECTOR = new NotHiddenFileTypeSelector(FileType.FILE);

    /**
     * <p>Constructor for VFSRepository.</p>
     */
    public VFSRepository()
    {
        try
        {
            fileSystemManager = VFS.getManager();
        }
        catch (FileSystemException ex)
        {
            throw ExceptionImposter.imposterize( ex );
        }
    }

    /** {@inheritDoc} */
    public void setDocumentAsImplemeted(String location) throws Exception {}

    @Override
    public List<Object> getSpecificationsHierarchy(String project, String systemUnderTest) {

        // TODO
        throw new UnsupportedOperationException("This functionnality is not yet implemented!");
    }

    /** {@inheritDoc} */
    public List<String> listDocuments(String location) throws Exception
    {
        FileObject root = getFileObject( location );
        if (!root.exists()) return Collections.emptyList();

        List<String> names = new ArrayList<String>();
        if (isDirectory(root))
        {
            for (FileObject child : root.findFiles(NOT_HIDDEN_FILE_TYPE_SELECTOR))
            {
                names.addAll( listDocuments(child.getName().getURI()));
            }
        }
        else
        {
            names.add(root.getName().getURI());
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
		throw new UnsupportedDocumentException("Hierechy not supported");
	}

    /** {@inheritDoc} */
    public Document loadDocument(String uri) throws Exception
    {
        FileObject file = getFileObject( uri );
        if (!file.exists()) throw new DocumentNotFoundException( file.getURL() );

        return createDocument(file);
    }

    /**
     * For testing purpose of new VFS providers (eg. Confluence, ...)
     *
     * @param urlScheme a {@link java.lang.String} object.
     * @param provider a {@link org.apache.commons.vfs.provider.FileProvider} object.
     * @throws org.apache.commons.vfs.FileSystemException if any.
     */
    public void addProvider(String urlScheme, FileProvider provider) throws FileSystemException
    {
        ((DefaultFileSystemManager)fileSystemManager).addProvider(urlScheme, provider);
    }

    private boolean isDirectory(FileObject fileObject) throws FileSystemException
    {
        return fileObject.getType().hasChildren();
    }

    private Document createDocument(FileObject file) throws Exception
    {
        String extension = file.getName().getExtension();

        if ("html".equalsIgnoreCase(extension))
        {
            return  createHtmlDocument(file);
        }

        throw new UnsupportedDocumentException(file.getURL());
    }

    private Document createHtmlDocument(FileObject file) throws IOException
    {
        Reader reader = new InputStreamReader(file.getContent().getInputStream());

        try
        {
            Document document = HtmlDocumentBuilder.tablesAndLists().build(reader);
            document.setUri(file.getURL().toString());
            return document;
        }
        finally
        {
            IOUtil.closeQuietly(reader);
        }
    }

    private FileObject getFileObject(String uri) throws IOException
    {
        return fileSystemManager.resolveFile(uri);
    }
}
