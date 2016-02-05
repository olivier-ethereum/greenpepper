package com.greenpepper.reflect;

import com.greenpepper.util.ClassUtils;
import com.greenpepper.TypeConversion;

import java.lang.reflect.Constructor;

/**
 * <p>Type class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Type<T>
{
    private final Class<? extends T> klass;

    /**
     * <p>Constructor for Type.</p>
     *
     * @param klass a {@link java.lang.Class} object.
     */
    public Type(Class<? extends T> klass)
    {
        if (klass == null) throw new NullPointerException("klass");
        this.klass = klass;
    }

    /**
     * <p>getUnderlyingClass.</p>
     *
     * @return a {@link java.lang.Class} object.
     */
    public Class<? extends T> getUnderlyingClass()
    {
        return klass;
    }

    /**
     * <p>newInstance.</p>
     *
     * @param args a {@link java.lang.Object} object.
     * @return a T object.
     * @throws java.lang.Throwable if any.
     */
    public T newInstance(Object... args) throws Throwable
    {
        Constructor<? extends T> constructor = ClassUtils.findBestTypedConstructor(klass, args);
        return ClassUtils.invoke(constructor, args);
    }

    /**
     * <p>newInstanceUsingCoercion.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a T object.
     * @throws java.lang.Throwable if any.
     */
    public T newInstanceUsingCoercion(String... args) throws Throwable
    {
        Constructor<? extends T> constructor = ClassUtils.findPossibleConstructor(klass, args);
        Class[] types = constructor.getParameterTypes();
        return newInstance( TypeConversion.convert( args, types ) );
    }
}
