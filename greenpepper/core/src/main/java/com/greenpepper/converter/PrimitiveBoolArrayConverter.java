package com.greenpepper.converter;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.TypeConversion;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.StringUtil;

/**
 * <p>PrimitiveBoolArrayConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PrimitiveBoolArrayConverter extends AbstractPrimitiveArrayConverter
{

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public Object parse( String value, Class type )
    {
        String text = removeSquareBrackets( value );

        List<Object> values = new ArrayList<Object>();
        if (StringUtil.isBlank( text )) return CollectionUtil.toArray( values, type.getComponentType() );

        String[] parts = text.split( separators );
        for (String part : parts)
        {
            values.add( TypeConversion.parse( part.trim(), type.getComponentType() ) );
        }
        
        return CollectionUtil.toPrimitiveBoolArray( values );
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString(Object value)
    {
        boolean[] array = (boolean[]) value;

        if (array.length == 0) return "";

        StringBuilder builder = new StringBuilder();

        builder.append( TypeConversion.toString(array[0]) );
        if (array.length == 1) return builder.toString();

        for (int i = 1; i < array.length; i++)
        {
            builder.append( ", " ).append( TypeConversion.toString( array[i] ) );
        }
        return builder.toString();
    }
}
