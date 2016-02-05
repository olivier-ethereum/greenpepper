package com.greenpepper.runner.repository;

import org.apache.commons.vfs.FileSelectInfo;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.FileTypeSelector;

/**
 * VFS File selector that exclude hidden files, and hidden directory and it's children.
 *
 * @author frochambeau
 * @version $Id: $Id
 */
public class NotHiddenFileTypeSelector extends FileTypeSelector
{

    /**
     * <p>Constructor for NotHiddenFileTypeSelector.</p>
     *
     * @param fileType a {@link org.apache.commons.vfs.FileType} object.
     */
    public NotHiddenFileTypeSelector(FileType fileType)
    {
        super(fileType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean includeFile(FileSelectInfo fileSelectInfo) throws FileSystemException
    {
        if (fileSelectInfo.getFile().isHidden())
        {
            return false;
        }
        return super.includeFile(fileSelectInfo);
    }

    /** {@inheritDoc} */
    @Override
    public boolean traverseDescendents(FileSelectInfo fileSelectInfo)
    {
        try
        {
            if (fileSelectInfo.getFile().isHidden())
            {
                return false;
            }
        }
        catch (FileSystemException e) {}
        
        return super.traverseDescendents(fileSelectInfo);
    }
    

}
