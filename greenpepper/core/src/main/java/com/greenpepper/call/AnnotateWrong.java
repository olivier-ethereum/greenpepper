package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.WrongAnnotation;

/**
 * <p>AnnotateWrong class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateWrong implements Stub
{
    private Annotatable annotatable;
    private final boolean detailed;

    /**
     * <p>Constructor for AnnotateWrong.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @param withDetails a boolean.
     */
    public AnnotateWrong( Annotatable annotatable, boolean withDetails )
    {
        this.annotatable = annotatable;
        this.detailed = withDetails;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        WrongAnnotation wrong = new WrongAnnotation();
        if (detailed) wrong.giveDetails( result.getExpected(), result.getActual() );
        annotatable.annotate( wrong );
    }
}
