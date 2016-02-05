package com.greenpepper.call;

/**
 * <p>ResultMatcher interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface ResultMatcher
{
    /**
     * <p>matches.</p>
     *
     * @param result a {@link com.greenpepper.call.Result} object.
     * @return a boolean.
     */
    boolean matches(Result result);
}
