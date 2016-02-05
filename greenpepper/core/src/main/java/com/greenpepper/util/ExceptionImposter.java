package com.greenpepper.util;

/**
 * <p>ExceptionImposter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ExceptionImposter extends RuntimeException
{
    private final Throwable imposterized;

    /**
     * <p>imposterize.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     * @return a {@link java.lang.RuntimeException} object.
     */
    public static RuntimeException imposterize( Throwable t )
    {
        if (t instanceof RuntimeException) return (RuntimeException) t;

        return new ExceptionImposter( t  );
    }

    /**
     * <p>Constructor for ExceptionImposter.</p>
     *
     * @param e a {@link java.lang.Throwable} object.
     */
    public ExceptionImposter(Throwable e)
    {
        super( e.getMessage(), e.getCause() );
        imposterized = e;
        setStackTrace( e.getStackTrace() );
    }

    /**
     * <p>getRealException.</p>
     *
     * @return a {@link java.lang.Throwable} object.
     */
    public Throwable getRealException()
    {
        return imposterized;
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString()
    {
        return imposterized.toString();
    }

}
