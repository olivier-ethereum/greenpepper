package com.greenpepper.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * DuckType. Implements Duck Typing for Java.  ("When it walks like a duck,
 * quacks like a duck, it...").  Essentially allows programs to treat
 * objects from separate hierarchies as if they were designed with common
 * interfaces as long as they adhere to common naming conventions.
 * <p/>
 * This version is the strict DuckType.  All methods present in
 * the type to implement must be present on the target object.
 *
 * @author Adapted from Dave Orme work, see http://www.coconut-palm-software.com/the_visual_editor/?p=25
 * @version $Id: $Id
 */
public class DuckType implements InvocationHandler
{
    /**
     * Causes object to implement the interfaceToImplement and returns
     * an instance of the object implementing interfaceToImplement even
     * if interfaceToImplement was not declared in object.getClass()'s
     * implements declaration.
     * <p/>
     * This works as long as all methods declared in interfaceToImplement
     * are present on object.
     *
     * @param type   The Java class of the interface to implement
     * @param object The object to force to implement interfaceToImplement
     * @param <T> a T object.
     * @return a T object.
     */
    public static <T> T implement(Class<T> type, Object object)
    {
        if (type.isInstance(object)) return type.cast(object);

        return type.cast(Proxy.newProxyInstance(type.getClassLoader(),
                new Class[]{type}, new DuckType(object)));
    }

    /**
     * Indicates if object is a (DuckType) instance of a type (interface).  That is,
     * is every method in type is present on object.
     *
     * @param type   The interface to implement
     * @param object The object to test
     * @return true if every method in type is present on object, false otherwise
     */
    public static boolean instanceOf(Class type, Object object)
    {
        if (type.isInstance(object)) return true;

        Class candidate = object.getClass();
        for (Method method : type.getMethods())
        {
            try
            {
                candidate.getMethod(method.getName(), (Class[])method.getParameterTypes());
            }
            catch (NoSuchMethodException e)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Constructor for DuckType.</p>
     *
     * @param object a {@link java.lang.Object} object.
     */
    protected DuckType(Object object)
    {
        this.object = object;
        this.objectClass = object.getClass();
    }

    protected Object object;
    protected Class objectClass;


    /**
     * <p>invoke.</p>
     *
     * @param proxy a {@link java.lang.Object} object.
     * @param method a {@link java.lang.reflect.Method} object.
     * @param args an array of {@link java.lang.Object} objects.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Method realMethod = objectClass.getMethod(method.getName(), (Class[])method.getParameterTypes());
        return realMethod.invoke(object, args);
    }
}
