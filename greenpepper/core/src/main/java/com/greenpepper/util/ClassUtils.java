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

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * <p>ClassUtils class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class ClassUtils
{
	/**
	 * <p>loadClass.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a {@link java.lang.Class} object.
	 * @throws java.lang.ClassNotFoundException if any.
	 */
	@SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(String className)
			throws ClassNotFoundException
	{
		return (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
	}

	/**
	 * <p>findBestTypedConstructor.</p>
	 *
	 * @param klass a {@link java.lang.Class} object.
	 * @param args a {@link java.lang.Object} object.
	 * @param <T> a T object.
	 * @return a {@link java.lang.reflect.Constructor} object.
	 * @throws java.lang.NoSuchMethodException if any.
	 */
	@SuppressWarnings("unchecked")
    public static <T> Constructor<T> findBestTypedConstructor( Class<T> klass, Object... args ) throws NoSuchMethodException
    {
        for (Constructor constructor : klass.getConstructors())
        {
            if (typesMatch( constructor, args )) return constructor;
        }

        throw noSuitableConstructorException(klass, args);
    }

    private static <T> NoSuchMethodException noSuitableConstructorException(Class<T> klass, Object... args)
    {
        return new NoSuchMethodException( klass.getName() + ".<init>(" + toString( args ) + ")" );
    }

    /**
     * <p>findPossibleConstructor.</p>
     *
     * @param klass a {@link java.lang.Class} object.
     * @param args a {@link java.lang.Object} object.
     * @param <T> a T object.
     * @return a {@link java.lang.reflect.Constructor} object.
     * @throws java.lang.NoSuchMethodException if any.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> Constructor<T> findPossibleConstructor( Class<T> klass, Object... args ) throws NoSuchMethodException
    {
        for (Constructor constructor : klass.getConstructors())
        {
            if (arityMatches(constructor, args)) return constructor;
        }

        throw noSuitableConstructorException(klass, args);
    }

    private static boolean arityMatches(Constructor constructor, Object... args)
    {
        return constructor.getParameterTypes().length == args.length;
    }

    private static boolean typesMatch(Constructor constructor, Object[] args)
    {
        if (!arityMatches(constructor, args)) return false;

        Class[] parameterTypes = constructor.getParameterTypes();
        parameterTypes = changePrimitivesTypesToNonOnes(parameterTypes);
        
        for (int i = 0; i < args.length; i++)
        {
            if (!parameterTypes[i].isInstance( args[i] )) return false;
        }

        return true;
    }

    private static Class[] changePrimitivesTypesToNonOnes(Class[] parameterTypes)
    {
        Class[] types = new Class[parameterTypes.length];
        int cursor = 0;
        
        for ( Class type : parameterTypes)
        {
            types[cursor] = getEquivalentNonPrimitiveType(type);
            cursor++;
        }
        
        return types;
    }

    private static Class getEquivalentNonPrimitiveType(Class type)
    {
        if (type == null) return null;
        
        if (type.isPrimitive())
        {
            Class resultType = null;
            if ( type == int.class) resultType = Integer.class;
            else if ( type == float.class) resultType = Float.class;
            else if ( type == boolean.class) resultType = Boolean.class;
            else if ( type == double.class) resultType = Double.class;
            else if ( type == long.class) resultType = Long.class;
            else if ( type == char.class) resultType = Character.class;
            else if ( type == byte.class) resultType = Byte.class;
            else if ( type == short.class) resultType = Short.class;
            return resultType;
        }
        return type;
    }

    /**
     * <p>invoke.</p>
     *
     * @param constructor a {@link java.lang.reflect.Constructor} object.
     * @param args a {@link java.lang.Object} object.
     * @param <T> a T object.
     * @return a T object.
     * @throws java.lang.Throwable if any.
     */
    public static <T> T invoke( Constructor<T> constructor, Object... args ) throws Throwable
    {
        try
        {
            return constructor.newInstance( args );
        }
        catch (InstantiationException e)
        {
            throw e.getCause();
        }
    }

    /**
     * <p>isStatic.</p>
     *
     * @param member a {@link java.lang.reflect.Member} object.
     * @return a boolean.
     */
    public static boolean isStatic( Member member )
    {
        return Modifier.isStatic( member.getModifiers() );
    }

    /**
     * <p>isPublic.</p>
     *
     * @param member a {@link java.lang.reflect.Member} object.
     * @return a boolean.
     */
    public static boolean isPublic( Member member )
    {
        return Modifier.isPublic( member.getModifiers() );
    }

    private static String toString( Object... args )
    {
        if (args.length == 0) return "";
        return "[" + CollectionUtil.joinAsString( args, ", " ) + "]";
    }

    private ClassUtils()
    {
    }
}
