package com.greenpepper;

import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.reflect.JavaTypeLoader;
import com.greenpepper.reflect.PackageTypeLoader;
import com.greenpepper.reflect.SuffixTypeLoader;
import com.greenpepper.reflect.CamelCaseTypeLoader;
import com.greenpepper.reflect.Type;

/**
 * <p>TypeLoaderChain class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class TypeLoaderChain<T> implements TypeLoader<T>
{
    private final TypeLoader<T> typeLoader;

    /**
     * <p>Constructor for TypeLoaderChain.</p>
     *
     * @param type a {@link java.lang.Class} object.
     */
    public TypeLoaderChain(Class<? extends T> type)
    {
        this( type, Thread.currentThread().getContextClassLoader() );
    }

    /**
     * <p>Constructor for TypeLoaderChain.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param classLoader a {@link java.lang.ClassLoader} object.
     */
    public TypeLoaderChain(Class<? extends T> type, ClassLoader classLoader)
    {
        TypeLoader<T> typeLoaderAdapter = new JavaTypeLoader<T>(type, classLoader);
        PackageTypeLoader<T> packageClassLoader = new PackageTypeLoader<T>(typeLoaderAdapter);
        SuffixTypeLoader<T> suffixClassLoader = new SuffixTypeLoader<T>(packageClassLoader);
        typeLoader = new CamelCaseTypeLoader<T>(suffixClassLoader);
    }

    /** {@inheritDoc} */
    public void searchPackage(String prefix)
    {
        typeLoader.searchPackage(prefix);
    }

    /** {@inheritDoc} */
    public void addSuffix(String suffix)
    {
        typeLoader.addSuffix(suffix);
    }

    /** {@inheritDoc} */
    public Type<T> loadType(String name)
    {
        return typeLoader.loadType(name);
    }
}
