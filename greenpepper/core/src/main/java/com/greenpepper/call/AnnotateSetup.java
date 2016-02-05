package com.greenpepper.call;

import com.greenpepper.Example;

/**
 * <p>AnnotateSetup class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AnnotateSetup implements Stub
{
    private final Example row;

    /**
     * <p>Constructor for AnnotateSetup.</p>
     *
     * @param row a {@link com.greenpepper.Example} object.
     */
    public AnnotateSetup( Example row )
    {
        this.row = row;
    }

    /** {@inheritDoc} */
    public void call( Result result )
    {
    	Example newLastCell = row.addChild();
    	
        if (result.isRight()) Annotate.entered( newLastCell ).call( result );
        if (result.isWrong()) Annotate.notEntered( newLastCell ).call( result );
        if (result.isException()) Annotate.exception( newLastCell ).call( result );
        if (result.isIgnored()) Annotate.entered( newLastCell ).call( result );
    }
}
