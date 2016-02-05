package com.greenpepper.interpreter;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

/**
 * <p>AnInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnInterpreter extends RuleForInterpreter
{
    /**
     * <p>Constructor for AnInterpreter.</p>
     *
     * @param systemUnderDevelopment a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public AnInterpreter( SystemUnderDevelopment systemUnderDevelopment )
    {
        this(new PlainOldFixture(new NullFixture()));
    }

    /**
     * <p>Constructor for AnInterpreter.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     */
    public AnInterpreter(Fixture fixture)
    {
        super(fixture);
    }

    public static class NullFixture
    {
        public String fixtureName()
        {
            return null;
        }

        public String fixtureParameters()
        {
            return null;
        }
    }
}
