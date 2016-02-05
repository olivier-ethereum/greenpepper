package com.greenpepper.call;

import com.greenpepper.expectation.Expectation;

/**
 * <p>Result class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Result
{
    private Expectation expectation;
    private Object actual;
    private Throwable exception;

    /**
     * <p>Constructor for Result.</p>
     */
    public Result()
    {
    }

    /**
     * <p>Constructor for Result.</p>
     *
     * @param expectation a {@link com.greenpepper.expectation.Expectation} object.
     */
    public Result( Expectation expectation )
    {
        this.expectation = expectation;
    }

    /**
     * <p>isIgnored.</p>
     *
     * @return a boolean.
     */
    public boolean isIgnored()
    {
        return !isException() && expectation == null;
    }

    /**
     * <p>isRight.</p>
     *
     * @return a boolean.
     */
    public boolean isRight()
    {
        return !isException() && !isIgnored() && expectation.meets( actual );
    }

    /**
     * <p>isWrong.</p>
     *
     * @return a boolean.
     */
    public boolean isWrong()
    {
        return !isException() && !isIgnored() && !isRight();
    }

    /**
     * <p>isException.</p>
     *
     * @return a boolean.
     */
    public boolean isException()
    {
        return exception != null;
    }

    /**
     * <p>Setter for the field <code>actual</code>.</p>
     *
     * @param actual a {@link java.lang.Object} object.
     */
    public void setActual( Object actual )
    {
        this.actual = actual;
    }

    /**
     * <p>getExpected.</p>
     *
     * @return a {@link com.greenpepper.expectation.Expectation} object.
     */
    public Expectation getExpected()
    {
        return expectation;
    }

    /**
     * <p>Getter for the field <code>actual</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getActual()
    {
        return actual;
    }

    /**
     * <p>exceptionOccured.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     */
    public void exceptionOccured( Throwable t )
    {
        if (!isIgnored() && expectation.meets( t ))
        {
            actual = t;
        }
        else
        {
            exception = t;
        }
    }

    /**
     * <p>Getter for the field <code>exception</code>.</p>
     *
     * @return a {@link java.lang.Throwable} object.
     */
    public Throwable getException()
    {
        return exception;
    }
}
