package com.greenpepper.call;

/**
 * <p>AnyResult class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnyResult implements ResultMatcher
{
    /** {@inheritDoc} */
    public boolean matches(Result result)
    {
        return true;
    }
}
