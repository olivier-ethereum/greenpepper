package com.greenpepper.call;

import com.greenpepper.Statistics;

/**
 * <p>Compile class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Compile
{
    /**
     * <p>statistics.</p>
     *
     * @param stats a {@link com.greenpepper.Statistics} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub statistics(Statistics stats)
    {
        return new CompileStatistics( stats );
    }

    private Compile()
    {
    }
}
