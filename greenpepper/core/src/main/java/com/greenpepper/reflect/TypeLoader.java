package com.greenpepper.reflect;

/**
 * <p>TypeLoader interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface TypeLoader<T>
{
    /**
     * <p>searchPackage.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    void searchPackage(String name);

    /**
     * <p>addSuffix.</p>
     *
     * @param suffix a {@link java.lang.String} object.
     */
    void addSuffix(String suffix);

    /**
     * <p>loadType.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Type} object.
     */
    Type<T> loadType(String name);
}
