package com.greenpepper.reflect;

import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

/**
 * <p>TypeNotFoundException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class TypeNotFoundException extends RuntimeException
{
    private final String name;
    private Class<? extends SystemUnderDevelopment> sudClass;

    /**
     * <p>Constructor for TypeNotFoundException.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param sudClass a {@link java.lang.Class} object.
     */
    public TypeNotFoundException(String name, Class<? extends SystemUnderDevelopment> sudClass)
    {
        super();
        this.sudClass = sudClass;
        this.name = name;
    }

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage()
    {
        return name + " does not match any known type (using sud " + sudClass.getSimpleName() + ")";
    }
}
