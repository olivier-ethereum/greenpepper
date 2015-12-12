package com.greenpepper.interpreter.flow;

import com.greenpepper.Specification;
import com.greenpepper.reflect.Fixture;

/**
 * You need to have a constructor taking a {@link Fixture}.
 * @author wattazoum
 *
 */
public interface Row
{
    void interpret(Specification row);
}
