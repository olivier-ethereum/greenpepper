package com.greenpepper.call;

import com.greenpepper.Example;
import com.greenpepper.annotation.Annotations;

/**
 * <p>AnnotateNotEntered class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateNotEntered implements Stub
{
    private final Example example;

    /**
     * <p>Constructor for AnnotateNotEntered.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     */
    public AnnotateNotEntered( Example example )
    {
        this.example = example;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
        example.annotate( Annotations.notEntered() );
    }
}
