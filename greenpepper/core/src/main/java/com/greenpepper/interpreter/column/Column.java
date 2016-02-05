
/**
 * <p>Abstract Column class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Example;
import com.greenpepper.ExecutionContext;
import com.greenpepper.Statistics;
import com.greenpepper.Variables;
public abstract class Column
{
    protected ExecutionContext context = new Variables();

    /**
     * <p>bindTo.</p>
     *
     * @param context a {@link com.greenpepper.ExecutionContext} object.
     */
    public void bindTo(ExecutionContext context)
    {
        this.context = context;
    }

    /**
     * <p>doCell.</p>
     *
     * @param cell a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Statistics} object.
     * @throws java.lang.Exception if any.
     */
    public abstract Statistics doCell(Example cell) throws Exception;
}
