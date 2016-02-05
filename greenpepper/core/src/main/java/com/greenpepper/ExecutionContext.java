package com.greenpepper;

import java.util.Map;

/**
 * <p>ExecutionContext interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface ExecutionContext
{
    /**
     * <p>setVariable.</p>
     *
     * @param symbol a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    void setVariable(String symbol, Object value);

    /**
     * <p>getVariable.</p>
     *
     * @param symbol a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    Object getVariable(String symbol);

    /**
     * <p>getAllVariables.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    Map<String, Object> getAllVariables();
}
