package com.greenpepper.call;

import com.greenpepper.Annotatable;

/**
 * <p>AnnotateExample class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateExample implements Stub
{
    private final Annotatable annotatable;
    private final boolean detailed;

    /**
     * <p>Constructor for AnnotateExample.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @param detailed a boolean.
     */
    public AnnotateExample( Annotatable annotatable, boolean detailed )
    {
        this.annotatable = annotatable;
        this.detailed = detailed;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        if (result.isRight()) Annotate.right( annotatable ).call( result );
        if (result.isWrong()) new AnnotateWrong( annotatable, detailed ).call( result );
        if (result.isException()) Annotate.exception( annotatable ).call( result );
        if (result.isIgnored()) Annotate.ignored( annotatable ).call( result );
    }
}
