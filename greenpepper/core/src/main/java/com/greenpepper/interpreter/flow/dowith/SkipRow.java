package com.greenpepper.interpreter.flow.dowith;

import com.greenpepper.Specification;
import com.greenpepper.interpreter.flow.Row;

/**
 * <p>SkipRow class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SkipRow implements Row
{

    /** {@inheritDoc} */
    public void interpret(Specification row)
    {
        row.nextExample();
    }
}
