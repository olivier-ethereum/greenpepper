package com.greenpepper.runner;

import com.greenpepper.util.StringUtil;
import com.greenpepper.util.cli.Converter;

/**
 * <p>StringArrayConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class StringArrayConverter implements Converter<String[]>
{
    private String separators;

    /**
     * <p>Constructor for StringArrayConverter.</p>
     *
     * @param separators a {@link java.lang.String} object.
     */
    public StringArrayConverter( String separators )
    {
        this.separators = separators;
    }

    /** {@inheritDoc} */
    public String[] convert( String value ) throws Exception
    {
        return StringUtil.isBlank( value ) ? new String[0] : value.split( separators );
    }
}
