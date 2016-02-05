package com.greenpepper.call;

/**
 * <p>ActualEquals class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ActualEquals implements ResultMatcher
{
    private final Object value;

    /**
     * <p>Constructor for ActualEquals.</p>
     *
     * @param value a {@link java.lang.Object} object.
     */
    public ActualEquals( Object value )
    {
        this.value = value;
    }

    /** {@inheritDoc} */
    public boolean matches( Result result )
    {
        return value.equals( result.getActual() );
    }
}
