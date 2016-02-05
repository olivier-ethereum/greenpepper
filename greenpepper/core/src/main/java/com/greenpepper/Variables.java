package com.greenpepper;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>Variables class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Variables implements ExecutionContext
{
    private final Map<String, Object> variables = new HashMap<String, Object>();

    /** {@inheritDoc} */
    public void setVariable(String symbol, Object value)
    {
        variables.put( symbol, value );
    }

    /** {@inheritDoc} */
    public Object getVariable(String symbol)
    {
        return variables.get( symbol );
    }

    /**
     * <p>getAllVariables.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getAllVariables()
    {
        return Collections.unmodifiableMap( variables );
    }
}
