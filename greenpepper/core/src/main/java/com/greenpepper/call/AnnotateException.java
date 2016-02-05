package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.Annotations;

/**
 * <p>AnnotateException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateException implements Stub
{
    private final Annotatable annotatable;

    /**
     * <p>Constructor for AnnotateException.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     */
    public AnnotateException( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        annotatable.annotate( Annotations.exception( result.getException() ) );
    }
}
