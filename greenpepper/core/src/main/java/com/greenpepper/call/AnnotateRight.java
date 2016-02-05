package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.RightAnnotation;

/**
 * <p>AnnotateRight class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateRight implements Stub
{
    private Annotatable annotatable;

    /**
     * <p>Constructor for AnnotateRight.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     */
    public AnnotateRight( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        annotatable.annotate( new RightAnnotation() );
    }
}
