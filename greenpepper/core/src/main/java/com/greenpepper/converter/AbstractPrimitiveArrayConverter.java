package com.greenpepper.converter;

import com.greenpepper.TypeConversion;

/**
 * <p>AbstractPrimitiveArrayConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AbstractPrimitiveArrayConverter extends ArrayConverter
{
    /** {@inheritDoc} */
    @Override
    public boolean canConvertTo( Class type )
    {
        return isArray( type ) && TypeConversion.supports( type.getComponentType() ) && type.getComponentType().isPrimitive();
    }
}
