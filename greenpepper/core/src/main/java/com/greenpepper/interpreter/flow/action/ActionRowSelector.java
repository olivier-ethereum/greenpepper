package com.greenpepper.interpreter.flow.action;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.reflect.Fixture;

/**
 * <p>ActionRowSelector class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ActionRowSelector implements RowSelector
{
    private Fixture fixture;

    /**
     * <p>Constructor for ActionRowSelector.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     */
    public ActionRowSelector(Fixture fixture)
    {
        super();
        this.fixture = fixture;
    }

    /** {@inheritDoc} */
    public Row select(Example example)
    {
        return new ActionRow( fixture );
    }
}
