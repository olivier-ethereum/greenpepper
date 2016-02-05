package com.greenpepper.interpreter;

import com.greenpepper.Statistics;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

/**
 * <p>CommentInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CommentInterpreter extends SkipInterpreter
{
    /**
     * <p>Constructor for CommentInterpreter.</p>
     *
     * @param sud a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public CommentInterpreter(SystemUnderDevelopment sud)
    {
        this();
    }
    
    /**
     * <p>Constructor for CommentInterpreter.</p>
     */
    public CommentInterpreter()
    {
    	super(new Statistics( 0, 0, 0, 1 ));
    }
}
