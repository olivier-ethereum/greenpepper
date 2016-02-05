package com.greenpepper.call;

import com.greenpepper.Statistics;

/**
 * <p>CompileStatistics class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CompileStatistics implements Stub
{
    private final Statistics stats;

    /**
     * <p>Constructor for CompileStatistics.</p>
     *
     * @param stats a {@link com.greenpepper.Statistics} object.
     */
    public CompileStatistics( Statistics stats )
    {
        this.stats = stats;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        if (result.isRight()) stats.right();
        if (result.isWrong()) stats.wrong();
        if (result.isException()) stats.exception();
        if (result.isIgnored()) stats.ignored();
    }
}
