package com.greenpepper.server.rpc;

import java.util.Vector;

/**
 * <p>GreenPepperRpcHelper interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface GreenPepperRpcHelper
{
    /**
     * <p>getRenderedSpecification.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @param args a {@link java.util.Vector} object.
     * @return a {@link java.lang.String} object.
     */
    String getRenderedSpecification(String username, String password, Vector<?> args);
    /**
     * <p>getSpecificationHierarchy.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @param args a {@link java.util.Vector} object.
     * @return a {@link java.util.Vector} object.
     */
    Vector getSpecificationHierarchy(String username, String password, Vector<?> args);
    /**
     * <p>setSpecificationAsImplemented.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @param args a {@link java.util.Vector} object.
     * @return a {@link java.lang.String} object.
     */
    String setSpecificationAsImplemented(String username, String password, Vector<?> args);
	/**
	 * <p>saveExecutionResult.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 * @param password a {@link java.lang.String} object.
	 * @param args a {@link java.util.Vector} object.
	 * @return a {@link java.lang.String} object.
	 */
	String saveExecutionResult(String username, String password, Vector<?> args);
}
