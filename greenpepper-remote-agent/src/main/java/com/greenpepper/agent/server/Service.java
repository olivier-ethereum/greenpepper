package com.greenpepper.agent.server;

import java.util.Vector;

public interface Service 
{
	/**
	 * Executes the Specification under the given context:
	 * Runner / SystemUnderTest.
	 * @param runnerParams
	 * @param sutParams
	 * @param specificationParams
	 * @param implemented
	 * @param sections
	 * @param locale
	 * @return the Execution of the specification under the given context
	 */
	public Vector<Object> execute(Vector<Object> runnerParams, 
						  Vector<Object> sutParams, 
						  Vector<Object> specificationParams, 
						  boolean implemented, 
						  String sections, 
						  String locale);
}
