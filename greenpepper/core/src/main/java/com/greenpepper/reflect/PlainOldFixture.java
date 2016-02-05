package com.greenpepper.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>PlainOldFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PlainOldFixture extends AbstractFixture
{
    /**
     * <p>Constructor for PlainOldFixture.</p>
     *
     * @param target a {@link java.lang.Object} object.
     */
    public PlainOldFixture(Object target)
    {
        super(target);
    }

    /** {@inheritDoc} */
    public Fixture fixtureFor(Object target)
    {
        return new PlainOldFixture(target);
    }

    /** {@inheritDoc} */
    protected Message getCheckMessage(String name)
    {
        final Class type = target.getClass();

        InvocationMessage invocationMessage = getInvocations( name );
        
        Method getterMethod = getGetter(type, name);
        
        if (getterMethod != null)
            invocationMessage.addMessage( new StaticInvocation(target, getterMethod) );
        
        Field field = getField(type, name);
        if (field != null) invocationMessage.addMessage( new FieldReader(target, field) );

        if (!invocationMessage.isEmpty()) return invocationMessage;
        
        if (getSystemUnderTest() == null) return null;

        PlainOldFixture fixture = new PlainOldFixture(getSystemUnderTest());
        return fixture.getCheckMessage(name);
    }

    /** {@inheritDoc} */
    protected Message getSendMessage(String name)
    {
        final Class type = target.getClass();

        InvocationMessage invocationMessage = getInvocations( name );

        Method setterMethod = getSetter(type, name);
        if (setterMethod != null)
            invocationMessage.addMessage( new StaticInvocation(target, setterMethod) );

        Field field = getField(type, name);
        if (field != null) invocationMessage.addMessage( new FieldWriter(target, field) );

        if (!invocationMessage.isEmpty()) return invocationMessage;

        if (getSystemUnderTest() == null) return null;

        PlainOldFixture fixture = new PlainOldFixture(getSystemUnderTest());
        return fixture.getSendMessage(name);
    }

    private InvocationMessage getInvocations(String name)
    {
        InvocationMessage invocationMessage = new InvocationMessage();

        for(Method method : getMethods(target.getClass(), name))
        {
            invocationMessage.addMessage( new StaticInvocation(target, method) );
        }
        return invocationMessage;
    }
}
