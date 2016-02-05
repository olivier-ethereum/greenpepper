package com.greenpepper.call;

/**
 * <p>ResultIsException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ResultIsException implements ResultMatcher
{
    /** {@inheritDoc} */
    public boolean matches(Result result)
    {
        return result.isException();
    }
}
