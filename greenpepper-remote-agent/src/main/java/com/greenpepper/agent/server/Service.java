package com.greenpepper.agent.server;

import java.util.Vector;

/**
 * <p>Service interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Service 
{
	/**
	 * Executes the Specification under the given context:
	 * Runner / SystemUnderTest.
	 *
	 * @param runnerParams a {@link java.util.Vector} object.
	 * @param sutParams a {@link java.util.Vector} object.
	 * @param specificationParams a {@link java.util.Vector} object.
	 * @param implemented a boolean.
	 * @param sections a {@link java.lang.String} object.
	 * @param locale a {@link java.lang.String} object.
	 * @return the Execution of the specification under the given context
	 */
	public Vector<Object> execute(Vector<Object> runnerParams, 
						  Vector<Object> sutParams, 
						  Vector<Object> specificationParams, 
						  boolean implemented, 
						  String sections, 
						  String locale);
}
