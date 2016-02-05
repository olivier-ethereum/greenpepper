
/**
 * <p>NullColumn class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Example;
import com.greenpepper.Statistics;
public class NullColumn extends Column
{
    /** {@inheritDoc} */
    public Statistics doCell( Example cell )
    {
        return new Statistics();
    }
}
