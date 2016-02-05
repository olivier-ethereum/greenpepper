package com.greenpepper.reflect;

/**
 * <p>SyntaxException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SyntaxException extends RuntimeException
{
    private final String name;
    private String message;

    /**
     * <p>Constructor for SyntaxException.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     */
    public SyntaxException(String name, String message)
    {
        super();
        this.message = message;
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage()
    {
        String.format("Wrong Syntax for [%s]: %s", name,message);
        return  String.format("Wrong Syntax for [%s]: %s", name,message);
    }
}
