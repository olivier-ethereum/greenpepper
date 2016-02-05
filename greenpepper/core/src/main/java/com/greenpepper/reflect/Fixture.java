package com.greenpepper.reflect;

/**
 * <p>Fixture interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Fixture
{
    /**
     * <p>canSend.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @return a boolean.
     */
    boolean canSend( String message );

    /**
     * <p>canCheck.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @return a boolean.
     */
    boolean canCheck( String message );

    /**
     * <p>check.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Message} object.
     * @throws com.greenpepper.reflect.NoSuchMessageException if any.
     */
    Message check( String message ) throws NoSuchMessageException;

    /**
     * <p>send.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Message} object.
     * @throws com.greenpepper.reflect.NoSuchMessageException if any.
     */
    Message send( String message ) throws NoSuchMessageException;
    
    /**
     * <p>getTarget.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    Object getTarget();

    /**
     * <p>fixtureFor.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.reflect.Fixture} object.
     */
    Fixture fixtureFor( Object target );
}
