package com.greenpepper.extensions.ognl;

import java.lang.reflect.Method;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.SystemUnderTest;

/**
 * <p>OgnlFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class OgnlFixture implements Fixture
{
    private final Fixture decorated;

    /**
     * <p>Constructor for OgnlFixture.</p>
     *
     * @param decorated a {@link com.greenpepper.reflect.Fixture} object.
     */
    public OgnlFixture( Fixture decorated )
    {
        this.decorated = decorated;
    }

    /**
     * <p>getTarget.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getTarget()
    {
        return decorated.getTarget();
    }

    /** {@inheritDoc} */
    public Fixture fixtureFor( Object target )
    {
        return new OgnlFixture( decorated.fixtureFor( target ) );
    }

    /** {@inheritDoc} */
    public boolean canSend( String message )
    {
        return decorated.canSend( message ) || getSendMessage( message ) != null;
    }

    /** {@inheritDoc} */
    public boolean canCheck( String message )
    {
        return decorated.canCheck( message ) || getCheckMessage( message ) != null;
    }

    /** {@inheritDoc} */
    public Message check( String message ) throws NoSuchMessageException
    {
        if (decorated.canCheck( message )) return decorated.check( message );

        Message check = getCheckMessage( message );
        if (check == null) throw new NoSuchMessageException( message );

        return check;
    }

    /** {@inheritDoc} */
    public Message send( String message ) throws NoSuchMessageException
    {
        if (decorated.canSend( message )) return decorated.send( message );

        Message send = getSendMessage( message );
        if (send == null) throw new NoSuchMessageException( message );

        return send;
    }

    private Object[] getTargets()
    {
        return getSystemUnderTest() != null ? new Object[]{getTarget(), getSystemUnderTest()} : new Object[]{getTarget()};
    }

    private Message getCheckMessage( String message )
    {
        if (!OgnlExpression.isGetter( message )) return null;

        OgnlExpression ognlExpression = OgnlExpression.onUnresolvedExpression( message, getTargets() );
        return new OgnlGetter( ognlExpression );
    }

    private Message getSendMessage( String key )
    {
        if (!OgnlExpression.isSetter( key )) return null;
        OgnlExpression ognlExpression = OgnlExpression.onUnresolvedExpression( key, getTargets() );

        return new OgnlSetter( ognlExpression );
    }

    private Object getSystemUnderTest()
    {
        for (Method method : getTarget().getClass().getMethods())
        {
            if (method.isAnnotationPresent( SystemUnderTest.class ))
            {
                return invoke( method );
            }
        }
        return null;
    }

    private Object invoke( Method method )
    {
        try
        {
            return method.invoke( getTarget() );
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
