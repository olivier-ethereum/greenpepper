package com.greenpepper.reflect;

import com.greenpepper.ogn.ObjectGraphNavigationFixture;
import com.greenpepper.util.log.GreenPepperLogger;
import org.slf4j.Logger;

/**
 * <p>DefaultFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DefaultFixture implements Fixture
{
    public static final String GREENPEPPER_FIXTURES_LOGGERNAME = "greenpepper.fixtures";

    private static final Logger LOGGER = GreenPepperLogger.getLogger(GREENPEPPER_FIXTURES_LOGGERNAME);
    private final Fixture delegate;

    /**
     * <p>Constructor for DefaultFixture.</p>
     *
     * @param target a {@link java.lang.Object} object.
     */
    public DefaultFixture( Object target )
    {
        delegate = new ObjectGraphNavigationFixture(target);
    }

    /** {@inheritDoc} */
    public Fixture fixtureFor( Object target )
    {
        return new DefaultFixture( target );
    }

    /**
     * <p>getTarget.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getTarget()
    {
        return delegate.getTarget();
    }

    /** {@inheritDoc} */
    public boolean canSend( String message )
    {
        return delegate.canSend( message );
    }

    /** {@inheritDoc} */
    public boolean canCheck( String message )
    {
        return delegate.canCheck( message );
    }

    /** {@inheritDoc} */
    public Message check( String message ) throws NoSuchMessageException
    {
        LOGGER.info(message);
        return delegate.check( message );
    }

    /** {@inheritDoc} */
    public Message send( String message ) throws NoSuchMessageException
    {
        return delegate.send( message );
    }
}
