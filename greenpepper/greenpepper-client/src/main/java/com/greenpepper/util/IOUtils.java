package com.greenpepper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>IOUtils class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class IOUtils
{
    /**
     * <p>copyFile.</p>
     *
     * @param srcFile a {@link java.io.File} object.
     * @param destFile a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public static void copyFile(File srcFile, File destFile) throws IOException
    {
        InputStream reader = new FileInputStream(srcFile);
        OutputStream out = new FileOutputStream(destFile);
        try
        {
            byte[] buffer = new byte[2048];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) 
            {
                out.write(buffer, 0, n);
            } 
        }
        finally
        {
            org.apache.commons.io.IOUtils.closeQuietly(out);
            org.apache.commons.io.IOUtils.closeQuietly(reader);
        }
    }
    
    /**
     * <p>uniquePath.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param ext a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public static String uniquePath(String name, String ext) throws IOException
    {
        File file = File.createTempFile(name, ext);
        String path = file.getAbsolutePath();
        file.delete();

        return path;
    }
}
