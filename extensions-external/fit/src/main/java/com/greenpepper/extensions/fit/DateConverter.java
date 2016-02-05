package com.greenpepper.extensions.fit;

import java.util.Date;

import com.greenpepper.converter.AbstractTypeConverter;

/**
 * <p>DateConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DateConverter extends AbstractTypeConverter
{
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
	protected Object doConvert(String value)
    {
    	return new Date(Date.parse(value));
    }

    /** {@inheritDoc} */
    public boolean canConvertTo(Class type)
    {
        return Date.class.isAssignableFrom(type);
    }
}
