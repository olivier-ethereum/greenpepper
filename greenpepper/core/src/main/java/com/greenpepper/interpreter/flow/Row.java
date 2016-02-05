package com.greenpepper.interpreter.flow;

import com.greenpepper.Specification;
import com.greenpepper.reflect.Fixture;

/**
 * You need to have a constructor taking a {@link com.greenpepper.reflect.Fixture}.
 *
 * @author wattazoum
 * @version $Id: $Id
 */
public interface Row
{
    /**
     * <p>interpret.</p>
     *
     * @param row a {@link com.greenpepper.Specification} object.
     */
    void interpret(Specification row);
}
