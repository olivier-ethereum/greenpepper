package com.greenpepper.call;

/**
 * <p>ResultHandler class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ResultHandler implements StubSyntax
{
    private final Stub stub;
    private ResultMatcher matcher;

    /**
     * <p>Constructor for ResultHandler.</p>
     *
     * @param stub a {@link com.greenpepper.call.Stub} object.
     */
    public ResultHandler( Stub stub )
    {
        this.stub = stub;
        this.matcher = new AnyResult();
    }

    /** {@inheritDoc} */
    public void when( ResultMatcher matcher )
    {
        this.matcher = matcher;
    }

    /**
     * <p>handle.</p>
     *
     * @param result a {@link com.greenpepper.call.Result} object.
     */
    public void handle( Result result )
    {
        if (matcher.matches( result )) stub.call( result );
    }
}
