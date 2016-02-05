package com.greenpepper.call;

import com.greenpepper.Example;
import com.greenpepper.annotation.Annotations;

/**
 * <p>AnnotateEntered class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateEntered implements Stub
{
    private final Example example;

    /**
     * <p>Constructor for AnnotateEntered.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     */
    public AnnotateEntered( Example example )
    {
        this.example = example;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        example.annotate( Annotations.entered() );
    }
}
