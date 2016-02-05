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

package com.greenpepper.converter;

import com.greenpepper.TypeConversion;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ArrayConverter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ArrayConverter implements TypeConverter
{
    protected final String separators;

    /**
     * <p>Constructor for ArrayConverter.</p>
     */
    public ArrayConverter()
    {
        this( "," );
    }

    /**
     * <p>Constructor for ArrayConverter.</p>
     *
     * @param separators a {@link java.lang.String} object.
     */
    public ArrayConverter( String separators )
    {
        this.separators = separators;
    }

    /** {@inheritDoc} */
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
        
        return CollectionUtil.toArray( values, type.getComponentType() );
    }

    /**
     * <p>removeSquareBrackets.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    protected String removeSquareBrackets( String value )
    {
        if (value.startsWith( "[" ) && value.endsWith( "]" ))
            return value.substring( 1, value.length() - 1 );
        else
            return value;
    }


	/** {@inheritDoc} */
	public String toString(Object value) {
		Object[] array = (Object[]) value;

        if (array.length == 0) return "";

        StringBuilder builder = new StringBuilder();

        builder.append( TypeConversion.toString(array[0]) );
        if (array.length == 1) return builder.toString();

        for (int i = 1; i < array.length; i++)
        {
            builder.append( ", " ).append( TypeConversion.toString( array[i]) );
        }
        return builder.toString();

	}

    /** {@inheritDoc} */
    public boolean canConvertTo( Class type )
    {
        return isArray( type ) && TypeConversion.supports( type.getComponentType() ) && ! type.getComponentType().isPrimitive();
    }


    /**
     * <p>isArray.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @return a boolean.
     */
    protected boolean isArray( Class type )
    {
        return type.getComponentType() != null;
    }
}
