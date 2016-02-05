package com.greenpepper.interpreter;

import com.greenpepper.interpreter.flow.AbstractFlowInterpreter;
import com.greenpepper.interpreter.flow.action.ActionRowSelector;
import com.greenpepper.reflect.Fixture;

/**
 * <p>ActionInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ActionInterpreter extends AbstractFlowInterpreter
{
    /**
     * <p>Constructor for ActionInterpreter.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     */
    public ActionInterpreter(Fixture fixture)
    {
        setRowSelector( new ActionRowSelector( fixture ) );
    }
}
