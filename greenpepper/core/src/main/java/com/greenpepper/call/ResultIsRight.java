package com.greenpepper.call;

/**
 * <p>ResultIsRight class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ResultIsRight implements ResultMatcher
{
    /** {@inheritDoc} */
    public boolean matches(Result result)
    {
        return result.isRight();
    }
}
