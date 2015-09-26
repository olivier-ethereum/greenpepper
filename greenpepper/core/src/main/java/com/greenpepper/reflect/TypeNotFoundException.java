package com.greenpepper.reflect;

import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class TypeNotFoundException extends RuntimeException
{
    private final String name;
    private Class<? extends SystemUnderDevelopment> sudClass;

    public TypeNotFoundException(String name, Class<? extends SystemUnderDevelopment> sudClass)
    {
        super();
        this.sudClass = sudClass;
        this.name = name;
    }

    public String getMessage()
    {
        return name + " does not match any known type (using sud " + sudClass.getSimpleName() + ")";
    }
}
