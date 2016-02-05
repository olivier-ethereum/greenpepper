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

package com.greenpepper.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * <p>CollectionUtil class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class CollectionUtil
{
    private CollectionUtil()
    {
    }

    /**
     * <p>isEmpty.</p>
     *
     * @param objects a T object.
     * @param <T> a T object.
     * @return a boolean.
     */
    public static <T> boolean isEmpty( T... objects )
    {
        return objects.length == 0;
    }

    /**
     * <p>toArray.</p>
     *
     * @param objects a T object.
     * @param <T> a T object.
     * @return an array of T objects.
     */
    public static <T> T[] toArray( T... objects )
    {
        return objects;
    }

    /**
     * <p>toVector.</p>
     *
     * @param objects a T object.
     * @param <T> a T object.
     * @return a {@link java.util.Vector} object.
     */
    public static <T> Vector<T> toVector( T... objects )
    {
        return new Vector<T>( Arrays.asList( objects ) );
    }

    /**
     * <p>toList.</p>
     *
     * @param objects a T object.
     * @param <T> a T object.
     * @return a {@link java.util.List} object.
     */
    public static <T> List<T> toList( T... objects )
    {
        List<T> list = new ArrayList<T>( objects.length );
        for (T t : objects)
        {
            list.add( t );
        }
        return list;
    }

    /**
     * <p>toArray.</p>
     *
     * @param list a {@link java.util.List} object.
     * @param type a {@link java.lang.Class} object.
     * @param <T> a T object.
     * @return an array of T objects.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray( List<?> list, Class<T> type )
    {
        T[] array = (T[])Array.newInstance( type, list.size() );
        return list.toArray( array );
    }

    /**
     * <p>toArray.</p>
     *
     * @param list a {@link java.util.List} object.
     * @return an array of {@link java.lang.String} objects.
     */
    public static String[] toArray( List<String> list )
    {
        return toArray( list, String.class );
    }

    /**
     * <p>even.</p>
     *
     * @param objects a {@link java.lang.Iterable} object.
     * @param <T> a T object.
     * @return a {@link java.util.List} object.
     */
    public static <T> List<T> even( Iterable<T> objects )
    {
        return split( objects, true );
    }

    /**
     * <p>odd.</p>
     *
     * @param objects a {@link java.lang.Iterable} object.
     * @param <T> a T object.
     * @return a {@link java.util.List} object.
     */
    public static <T> List<T> odd( Iterable<T> objects )
    {
        return split( objects, false );
    }

    /**
     * <p>joinAsString.</p>
     *
     * @param objects an array of {@link java.lang.Object} objects.
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String joinAsString( Object[] objects, String separator )
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < objects.length; i++)
        {
            Object element = objects[i];
            result.append( String.valueOf( element ) );
            if (i < objects.length - 1) result.append( separator );
        }
        return result.toString();
    }

    /**
     * <p>filter.</p>
     *
     * @param collection a {@link java.lang.Iterable} object.
     * @param predicate a {@link com.greenpepper.util.CollectionUtil.Predicate} object.
     * @param <T> a T object.
     * @return a {@link java.util.List} object.
     */
    public static <T> List<T> filter( Iterable<T> collection, Predicate<T> predicate)
    {
        List<T> filtered = new ArrayList<T>();
        for (T element : collection)
        {
        	if (predicate.isVerifiedBy(element))
        	{
        		filtered.add(element);
        	}
        }
        return filtered;
    }

    public interface Predicate<T>
    {
        boolean isVerifiedBy(T element);
    }

    /**
     * <p>split.</p>
     *
     * @param objects a {@link java.lang.Iterable} object.
     * @param even a boolean.
     * @param <T> a T object.
     * @return a {@link java.util.List} object.
     */
    public static <T> List<T> split( Iterable<T> objects, boolean even )
    {
        List<T> parts = new ArrayList<T>();
        for (T element : objects)
        {
            if (even) parts.add( element );
            even = !even;
        }
        return parts;
    }

    /**
     * <p>shift.</p>
     *
     * @param list a {@link java.util.List} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T shift( List<T> list )
    {
        return remove( list, 0 );
    }

    /**
     * <p>removeLast.</p>
     *
     * @param list a {@link java.util.List} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T removeLast( List<T> list )
    {
        return remove( list, list.size() - 1 );
    }

    private static <T> T remove( List<T> list, int position )
    {
        if (list.isEmpty()) return null;
        return list.remove( position );
    }

    /**
     * <p>first.</p>
     *
     * @param list a {@link java.util.List} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T first( List<T> list )
    {
        return get( list, 0 );
    }

    /**
     * <p>last.</p>
     *
     * @param list a {@link java.util.List} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T last( List<T> list )
    {
        return get( list, list.size() - 1 );
    }

    private static <T> T get( List<T> list, int index )
    {
        if (list.size() <= index) return null;
        return list.get( index );
    }

    /**
     * <p>toPrimitiveIntArray.</p>
     *
     * @param values a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object toPrimitiveIntArray(List<?> values)
    {
        int[] array = new int[values.size()];
        int cursor = 0;
        
        for(Object o : values)
        {
            array[cursor] = (Integer) o;
            cursor ++;
        }
        return array;
    }
    
    /**
     * <p>toPrimitiveFloatArray.</p>
     *
     * @param values a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object toPrimitiveFloatArray(List<?> values)
    {
        float[] array = new float[values.size()];
        int cursor = 0;
        
        for(Object o : values)
        {
            array[cursor] = (Float) o;
            cursor ++;
        }
        return array;
    }
    
    /**
     * <p>toPrimitiveLongArray.</p>
     *
     * @param values a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object toPrimitiveLongArray(List<?> values)
    {
        long[] array = new long[values.size()];
        int cursor = 0;
        
        for(Object o : values)
        {
            array[cursor] = (Long) o;
            cursor ++;
        }
        return array;
    }
    
    /**
     * <p>toPrimitiveDoubleArray.</p>
     *
     * @param values a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object toPrimitiveDoubleArray(List<?> values)
    {
        double[] array = new double[values.size()];
        int cursor = 0;
        
        for(Object o : values)
        {
            array[cursor] = (Double) o;
            cursor ++;
        }
        return array;
    }
    
    /**
     * <p>toPrimitiveBoolArray.</p>
     *
     * @param values a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object toPrimitiveBoolArray(List<?> values)
    {
        boolean[] array = new boolean[values.size()];
        int cursor = 0;
        
        for(Object o : values)
        {
            array[cursor] = (Boolean) o;
            cursor ++;
        }
        return array;
    }
  
}
