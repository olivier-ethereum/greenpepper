package com.greenpepper.agent.server;

import java.util.Vector;

import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;

public class ServiceImpl
		implements Service
{

	public Vector<Object> execute(Vector<Object> runnerParams,
								  Vector<Object> sutParams,
								  Vector<Object> specificationParams,
								  boolean implemented,
								  String sections,
								  String locale)
	{
		Runner runner = XmlRpcDataMarshaller.toRunner(runnerParams);

		// To prevent call forwarding
		runner.setServerName(null);
		runner.setServerPort(null);

		SystemUnderTest systemUnderTest = XmlRpcDataMarshaller.toSystemUnderTest(sutParams);
		Specification specification = XmlRpcDataMarshaller.toSpecification(specificationParams);

		Execution exe = runner.execute(specification, systemUnderTest, implemented, sections, locale);
		return exe.marshallize();
	}
}