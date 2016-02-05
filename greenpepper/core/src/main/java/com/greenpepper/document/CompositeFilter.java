package com.greenpepper.document;

import com.greenpepper.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>CompositeFilter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CompositeFilter implements ExampleFilter
{
    private final List<ExampleFilter> filters;

    /**
     * <p>Constructor for CompositeFilter.</p>
     */
    public CompositeFilter()
    {
        this.filters = new ArrayList<ExampleFilter>();
    }

    /**
     * <p>add.</p>
     *
     * @param filter a {@link com.greenpepper.document.ExampleFilter} object.
     */
    public void add( ExampleFilter filter )
    {
        filters.add( filter );
    }

    /** {@inheritDoc} */
    public boolean canFilter( Example example )
    {
        for (int i = filters.size() - 1; i >= 0; i--)
        {
            if (filters.get( i ).canFilter( example )) return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    public Example filter( Example example )
    {
        Example next = example;
        for (int i = filters.size() - 1; next != null && i >= 0; i--)
        {
            next = filters.get( i ).filter( next );
        }
        return next;
    }
}
