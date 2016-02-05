package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.ExampleUtil.contentOf;

/**
 * <p>Abstract AbstractTableFilter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractTableFilter implements ExampleFilter
{
    private final String[] keywords;

    /**
     * <p>Constructor for AbstractTableFilter.</p>
     *
     * @param keywords a {@link java.lang.String} object.
     */
    protected AbstractTableFilter( String... keywords )
    {
        this.keywords = keywords;
    }

    /** {@inheritDoc} */
    public boolean canFilter( Example example )
    {
        for (String keyword : keywords)
        {
            if (keyword.equalsIgnoreCase( contentOf( example.at( 0, 0, 0 ) ) )) return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    public Example filter( Example example )
    {
        if (!canFilter( example )) return example;
        return doFilter( example );
    }

    /**
     * <p>doFilter.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    protected abstract Example doFilter( Example example );
}
