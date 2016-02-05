package com.greenpepper.call;

/**
 * <p>ResultIs class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class ResultIs
{
    /**
     * <p>wrong.</p>
     *
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public static ResultMatcher wrong()
    {
        return new ResultIsWrong();
    }

    /**
     * <p>not.</p>
     *
     * @param condition a {@link com.greenpepper.call.ResultMatcher} object.
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public static ResultMatcher not( ResultMatcher condition )
    {
        return new ResultIsNot( condition );
    }

    /**
     * <p>exception.</p>
     *
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public static ResultMatcher exception()
    {
        return new ResultIsException();
    }

    /**
     * <p>equalTo.</p>
     *
     * @param value a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public static ResultMatcher equalTo( Object value )
    {
        return new ActualEquals( value );
    }

    /**
     * <p>right.</p>
     *
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public static ResultMatcher right()
    {
        return new ResultIsRight();
    }

    private ResultIs()
    {
    }
}
