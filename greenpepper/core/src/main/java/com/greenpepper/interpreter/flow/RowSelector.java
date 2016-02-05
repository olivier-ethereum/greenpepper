package com.greenpepper.interpreter.flow;

import com.greenpepper.Example;

/**
 * <p>RowSelector interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface RowSelector
{
    /**
     * <p>select.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.interpreter.flow.Row} object.
     */
    Row select(Example example);
}
