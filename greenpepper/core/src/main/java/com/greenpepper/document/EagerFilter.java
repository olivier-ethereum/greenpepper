package com.greenpepper.document;

import com.greenpepper.Example;

/**
 * <p>EagerFilter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class EagerFilter implements ExampleFilter
{
    private final ExampleFilter filter;

    /**
     * <p>Constructor for EagerFilter.</p>
     *
     * @param filter a {@link com.greenpepper.document.ExampleFilter} object.
     */
    public EagerFilter( ExampleFilter filter )
    {
        this.filter = filter;
    }

    /** {@inheritDoc} */
    public boolean canFilter( Example example )
    {
        return filter.canFilter( example );
    }

    /** {@inheritDoc} */
    public Example filter( Example example )
    {
        Example next = example;
        while (next != null && canFilter( next ))
        {
            next = filter.filter( next );
        }
        return next;
    }
}
