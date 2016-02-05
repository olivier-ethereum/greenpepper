package com.greenpepper.document;

import com.greenpepper.Interpreter;
import com.greenpepper.Example;

/**
 * <p>InterpreterSelector interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface InterpreterSelector
{
    /**
     * <p>selectInterpreter.</p>
     *
     * @param table a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Interpreter} object.
     */
    Interpreter selectInterpreter(Example table);
}
