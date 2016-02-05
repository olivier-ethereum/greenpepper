package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.Annotations;

/**
 * <p>AnnotateIgnored class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateIgnored implements Stub
{
    private final Annotatable annotatable;

    /**
     * <p>Constructor for AnnotateIgnored.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     */
    public AnnotateIgnored( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        annotatable.annotate( Annotations.ignored( result.getActual() ) );
    }
}
