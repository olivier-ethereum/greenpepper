package com.greenpepper.call;

/**
 * <p>ResultIsWrong class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ResultIsWrong implements ResultMatcher
{
    /** {@inheritDoc} */
    public boolean matches(Result result)
    {
        return result.isWrong();
    }
}
