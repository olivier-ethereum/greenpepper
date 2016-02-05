package com.greenpepper.interpreter;

import com.greenpepper.Specification;
import com.greenpepper.Statistics;

/**
 * <p>SkipInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SkipInterpreter extends AbstractInterpreter
{
    private final Statistics stats;

    /**
     * <p>Constructor for SkipInterpreter.</p>
     */
    public SkipInterpreter()
    {
        this( new Statistics() );
    }

    /**
     * <p>Constructor for SkipInterpreter.</p>
     *
     * @param stats a {@link com.greenpepper.Statistics} object.
     */
    public SkipInterpreter( Statistics stats )
    {
        this.stats = stats;
    }

    /** {@inheritDoc} */
    public void interpret( Specification specification )
    {
        specification.nextExample();
        specification.exampleDone( stats );
    }
}
