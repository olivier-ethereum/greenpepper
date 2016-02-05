package com.greenpepper.util.cli;

import java.io.File;

/**
 * <p>FileConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class FileConverter implements Converter
{
    /** {@inheritDoc} */
    public Object convert( String value ) throws Exception
    {
        return new File( value );
    }
}
