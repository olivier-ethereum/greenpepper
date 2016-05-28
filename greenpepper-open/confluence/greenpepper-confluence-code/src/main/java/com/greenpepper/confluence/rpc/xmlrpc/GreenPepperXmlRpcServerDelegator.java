/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.confluence.rpc.xmlrpc;

import java.util.Vector;

import com.greenpepper.confluence.rpc.RpcClientService;
import com.greenpepper.server.rpc.GreenPepperRpcHelper;
import com.greenpepper.server.rpc.xmlrpc.GreenPepperXmlRpcServer;

/**
 * <p>GreenPepperXmlRpcServerDelegator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperXmlRpcServerDelegator
		implements RpcClientService
{

	private GreenPepperXmlRpcServer serviceDelegator;
	private GreenPepperRpcHelper confluenceServiceDelegator = new ConfluenceXmlRpcGreenPepperServiceImpl();

	/**
	 * <p>Constructor for GreenPepperXmlRpcServerDelegator.</p>
	 */
	public GreenPepperXmlRpcServerDelegator()
	{
	}

	/**
	 * <p>setGreenPepperXmlRpcServerService.</p>
	 *
	 * @param delegator a {@link com.greenpepper.server.rpc.xmlrpc.GreenPepperXmlRpcServer} object.
	 */
	public void setGreenPepperXmlRpcServerService(GreenPepperXmlRpcServer delegator)
	{
		this.serviceDelegator = delegator;
	}

	/**
	 * <p>license.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> license()
	{
		return serviceDelegator.license();
	}

	/** {@inheritDoc} */
	public String uploadNewLicense(String newLicense)
	{
		return serviceDelegator.uploadNewLicense(newLicense);
	}

	/**
	 * <p>testConnection.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String testConnection()
	{
		return serviceDelegator.testConnection();
	}

	/** {@inheritDoc} */
	public String ping(Vector<Object> repositoryParams)
	{
		return serviceDelegator.ping(repositoryParams);
	}

	/**
	 * <p>getAllEnvironmentTypes.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> getAllEnvironmentTypes()
	{
		return serviceDelegator.getAllEnvironmentTypes();
	}

	/** {@inheritDoc} */
	public Vector<Object> getRunner(String name)
	{
		return serviceDelegator.getRunner(name);
	}

	/**
	 * <p>getAllRunners.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> getAllRunners()
	{
		return serviceDelegator.getAllRunners();
	}

	/** {@inheritDoc} */
	public String createRunner(Vector<Object> runnerParams)
	{
		return serviceDelegator.createRunner(runnerParams);
	}

	/** {@inheritDoc} */
	public String updateRunner(String oldRunnerName, Vector<Object> runnerParams)
	{
		return serviceDelegator.updateRunner(oldRunnerName, runnerParams);
	}

	/** {@inheritDoc} */
	public String removeRunner(String name)
	{
		return serviceDelegator.removeRunner(name);
	}

	/** {@inheritDoc} */
	public Vector<Object> getRegisteredRepository(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getRegisteredRepository(repositoryParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> registerRepository(Vector<Object> repositoryParams)
	{
		return serviceDelegator.registerRepository(repositoryParams);
	}

	/** {@inheritDoc} */
	public String updateRepositoryRegistration(Vector<Object> repositoryParams)
	{
		return serviceDelegator.updateRepositoryRegistration(repositoryParams);
	}

	/** {@inheritDoc} */
	public String removeRepository(String repositoryUid)
	{
		return serviceDelegator.removeRepository(repositoryUid);
	}

	/**
	 * <p>getAllProjects.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> getAllProjects()
	{
		return serviceDelegator.getAllProjects();
	}

	/**
	 * <p>getAllSpecificationRepositories.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> getAllSpecificationRepositories()
	{
		return serviceDelegator.getAllSpecificationRepositories();
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecificationRepositoriesOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSpecificationRepositoriesOfAssociatedProject(repositoryParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getAllRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams)
	{
		return serviceDelegator.getAllRepositoriesForSystemUnderTest(systemUnderTestParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecificationRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams)
	{
		return serviceDelegator.getSpecificationRepositoriesForSystemUnderTest(systemUnderTestParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getRequirementRepositoriesOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getRequirementRepositoriesOfAssociatedProject(repositoryParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSystemUnderTestsOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSystemUnderTestsOfAssociatedProject(repositoryParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSystemUnderTestsOfProject(String projectName)
	{
		return serviceDelegator.getSystemUnderTestsOfProject(projectName);
	}

	/** {@inheritDoc} */
	public String addSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams,
												  Vector<Object> specificationParams)
	{
		return serviceDelegator.addSpecificationSystemUnderTest(systemUnderTestParams, specificationParams);
	}

	/** {@inheritDoc} */
	public String removeSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams,
													 Vector<Object> specificationParams)
	{
		return serviceDelegator.removeSpecificationSystemUnderTest(systemUnderTestParams, specificationParams);
	}

	/** {@inheritDoc} */
	public String doesSpecificationHasReferences(Vector<Object> specificationParams)
	{
		return serviceDelegator.doesSpecificationHasReferences(specificationParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecificationReferences(Vector<Object> specificationParams)
	{
		return serviceDelegator.getSpecificationReferences(specificationParams);
	}

	/** {@inheritDoc} */
	public String doesRequirementHasReferences(Vector<Object> requirementParams)
	{
		return serviceDelegator.doesRequirementHasReferences(requirementParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getRequirementReferences(Vector<Object> requirementParams)
	{
		return serviceDelegator.getRequirementReferences(requirementParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.getReference(referenceParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public String createSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.createSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public String updateSystemUnderTest(String oldSystemUnderTestName, Vector<Object> systemUnderTestParams,
										Vector<Object> repositoryParams)
	{
		return serviceDelegator.updateSystemUnderTest(oldSystemUnderTestName, systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public String removeSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.removeSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public String setSystemUnderTestAsDefault(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.setSystemUnderTestAsDefault(systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public String removeRequirement(Vector<Object> requirementParams)
	{
		return serviceDelegator.removeRequirement(requirementParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.getSpecification(specificationParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecifications(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSpecifications(systemUnderTestParams, repositoryParams);
	}

	/** {@inheritDoc} */
	public Vector<?> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName)
	{
		return serviceDelegator.getListOfSpecificationLocations(repositoryUID, systemUnderTestName);
	}

	/** {@inheritDoc} */
	public Vector<Object> createSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.createSpecification(specificationParams);
	}

	/** {@inheritDoc} */
	public String updateSpecification(Vector<Object> oldSpecificationParams, Vector<Object> newSpecificationParams)
	{
		return serviceDelegator.updateSpecification(oldSpecificationParams, newSpecificationParams);
	}

	/** {@inheritDoc} */
	public String removeSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.removeSpecification(specificationParams);
	}

	/** {@inheritDoc} */
	public String createReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.createReference(referenceParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> updateReference(Vector<Object> oldReferenceParams, Vector<Object> newReferenceParams)
	{
		return serviceDelegator.updateReference(oldReferenceParams, newReferenceParams);
	}

	/** {@inheritDoc} */
	public String removeReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.removeReference(referenceParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> runSpecification(Vector<Object> systemUnderTestParams, Vector<Object> specificationParams,
										   boolean implementedVersion, String locale)
	{
		return serviceDelegator.runSpecification(systemUnderTestParams, specificationParams, implementedVersion, locale);
	}

	/** {@inheritDoc} */
	public Vector<Object> runReference(Vector<Object> referenceParams, String locale)
	{
		return serviceDelegator.runReference(referenceParams, locale);
	}

	/** {@inheritDoc} */
	public Vector<Object> getRequirementSummary(Vector<Object> requirementParams)
	{
		return serviceDelegator.getRequirementSummary(requirementParams);
	}

	/** {@inheritDoc} */
	public Vector<Object> getSpecificationHierarchy(Vector<Object> repositoryParams, Vector<Object> sutParams)
	{
		return serviceDelegator.getSpecificationHierarchy(repositoryParams, sutParams);
	}

	/** {@inheritDoc} */
	public String getRenderedSpecification(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.getRenderedSpecification(username, password, args);
	}

	/** {@inheritDoc} */
	public Vector getSpecificationHierarchy(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.getSpecificationHierarchy(username, password, args);
	}

	/** {@inheritDoc} */
	public String setSpecificationAsImplemented(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.setSpecificationAsImplemented(username, password, args);
	}

	/** {@inheritDoc} */
	public String saveExecutionResult(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.saveExecutionResult(username, password, args);
	}
}
