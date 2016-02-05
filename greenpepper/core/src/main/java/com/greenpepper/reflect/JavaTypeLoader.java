package com.greenpepper.reflect;

/**
 * <p>JavaTypeLoader class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class JavaTypeLoader<T> implements TypeLoader<T>
{
    private final Class<? extends T> type;
    private final java.lang.ClassLoader classLoader;

    /**
     * <p>Constructor for JavaTypeLoader.</p>
     *
     * @param type a {@link java.lang.Class} object.
     */
    public JavaTypeLoader( Class<? extends T> type )
    {
        this( type, Thread.currentThread().getContextClassLoader() );
    }

    /**
     * <p>Constructor for JavaTypeLoader.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param classLoader a {@link java.lang.ClassLoader} object.
     */
    public JavaTypeLoader(Class<? extends T> type, java.lang.ClassLoader classLoader)
    {
        this.type = type;
        this.classLoader = classLoader;
    }

    /** {@inheritDoc} */
    public void searchPackage(String prefix)
    {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void addSuffix(String suffix)
    {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Type<T> loadType(String name)
    {
        Class clazz = loadClass( name );
        return classLoaded(clazz) && typeMatches(clazz) ? new Type<T>(clazz) : null;
    }

    private boolean classLoaded(Class clazz)
    {
        return clazz != null;
    }

    private boolean typeMatches(Class clazz)
    {
        return type.isAssignableFrom(clazz);
    }

    /**
     * <p>loadClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.Class} object.
     */
    public Class loadClass(String name)
    {
        try
        {
            return classLoader.loadClass( name );
        }
        catch (ClassNotFoundException notFound)
        {
            return null;
        }
        catch (NoClassDefFoundError notFound)
        {
            return null;
        }
    }
}
