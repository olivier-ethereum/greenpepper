package com.greenpepper.document;

import com.greenpepper.Example;

/**
 * <p>ExampleFilter interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface ExampleFilter
{
    /**
     * <p>canFilter.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a boolean.
     */
    boolean canFilter( Example example );

    /**
     * <p>filter.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    Example filter( Example example );
}
