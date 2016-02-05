package com.greenpepper.document;

import java.util.Map;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.Variables;
import com.greenpepper.util.ExampleWrapper;

/**
 * <p>Abstract AbstractSpecification class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractSpecification implements Specification
{
    protected final Variables variables = new Variables();
    protected Example cursor;

    /**
     * <p>setStart.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     */
    protected void setStart( Example example )
    {
        this.cursor = before( example );
    }

    private Example before( Example example )
    {
        return ExampleWrapper.empty( example );
    }

    /**
     * <p>nextExample.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    public Example nextExample()
    {
        cursor = peek();
        return cursor;
    }
    
    /**
     * <p>getAllVariables.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getAllVariables()
    {
    	return variables.getAllVariables();
    }
    
    /**
     * <p>peek.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    protected abstract Example peek();

    /**
     * <p>hasMoreExamples.</p>
     *
     * @return a boolean.
     */
    public boolean hasMoreExamples()
    {
        return peek() != null;
    }

    /** {@inheritDoc} */
    public abstract void exampleDone( Statistics statistics );

    /** {@inheritDoc} */
    public Object getVariable(String symbol)
    {
        return variables.getVariable( symbol );
    }

    /** {@inheritDoc} */
    public void setVariable(String symbol, Object value)
    {
        variables.setVariable( symbol, value );
    }
}
