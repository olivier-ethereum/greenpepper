package com.greenpepper.reflect;

public class SyntaxException extends RuntimeException
{
    private final String name;
    private String message;

    public SyntaxException(String name, String message)
    {
        super();
        this.message = message;
        this.name = name;
    }

    public String getMessage()
    {
        String.format("Wrong Syntax for [%s]: %s", name,message);
        return  String.format("Wrong Syntax for [%s]: %s", name,message);
    }
}
