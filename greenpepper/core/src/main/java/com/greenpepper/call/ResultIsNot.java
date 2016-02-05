package com.greenpepper.call;

/**
 * <p>ResultIsNot class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ResultIsNot implements ResultMatcher
{
    private final ResultMatcher condition;

    /**
     * <p>Constructor for ResultIsNot.</p>
     *
     * @param condition a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public ResultIsNot(ResultMatcher condition)
    {
        this.condition = condition;
    }

    /** {@inheritDoc} */
    public boolean matches(Result result)
    {
        return !condition.matches( result );
    }
}
