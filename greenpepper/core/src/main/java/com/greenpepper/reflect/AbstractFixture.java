package com.greenpepper.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.greenpepper.util.Introspector;

import static com.greenpepper.util.NameUtils.toJavaIdentifierForm;

/**
 * <p>Abstract AbstractFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractFixture implements Fixture
{
    protected final Object target;

    /**
     * <p>Constructor for AbstractFixture.</p>
     *
     * @param target a {@link java.lang.Object} object.
     */
    public AbstractFixture(Object target)
    {
        this.target = target;
    }

    /**
     * <p>Getter for the field <code>target</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getTarget()
    {
        return target;
    }

    /** {@inheritDoc} */
    public boolean canSend(String message)
    {
        return getSendMessage(message) != null;
    }

    /** {@inheritDoc} */
    public boolean canCheck(String message)
    {
        return getCheckMessage(message) != null;
    }

    /** {@inheritDoc} */
    public Message check(String message) throws NoSuchMessageException
    {
        Message check = getCheckMessage(message);
        if (check == null) throw new NoSuchMessageException(message);

        return check;
    }

    /** {@inheritDoc} */
    public Message send(String message) throws NoSuchMessageException
    {
        Message send = getSendMessage(message);
        if (send == null) throw new NoSuchMessageException(message);

        return send;
    }

    /**
     * <p>getCheckMessage.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Message} object.
     */
    protected abstract Message getCheckMessage(String name);

    /**
     * <p>getSendMessage.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Message} object.
     */
    protected abstract Message getSendMessage(String name);

    /**
     * <p>getSetter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.reflect.Method} object.
     */
    protected Method getSetter(Class type, String name)
    {
        return introspector(type).getSetter( toJavaIdentifierForm(name));
    }

    /**
     * <p>getField.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.reflect.Field} object.
     */
    protected Field getField(Class type, String name)
    {
        return introspector(type).getField( toJavaIdentifierForm(name));
    }

    /**
     * <p>getMethods.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    protected List<Method> getMethods(Class type, String name)
    {
        return introspector(type).getMethods( toJavaIdentifierForm(name));
    }

    /**
     * <p>getGetter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.reflect.Method} object.
     */
    protected Method getGetter(Class type, String name)
    {
        return introspector(type).getGetter( toJavaIdentifierForm(name));
    }

    /**
     * <p>introspector.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @return a {@link com.greenpepper.util.Introspector} object.
     */
    protected Introspector introspector(Class type)
    {
        return Introspector.ignoringCase(type);
    }

    /**
     * <p>getSystemUnderTest.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    protected Object getSystemUnderTest()
    {
        for (Method method : target.getClass().getMethods())
        {
            if (method.isAnnotationPresent(SystemUnderTest.class))
            {
                return invoke(method);
            }
        }

        Method m = getGetter( target.getClass(), "systemUnderTest" );
        return m != null ? invoke( m ) : null;
    }

    /**
     * <p>invoke.</p>
     *
     * @param method a {@link java.lang.reflect.Method} object.
     * @return a {@link java.lang.Object} object.
     */
    protected Object invoke(Method method)
    {
        try
        {
            return method.invoke(target);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
