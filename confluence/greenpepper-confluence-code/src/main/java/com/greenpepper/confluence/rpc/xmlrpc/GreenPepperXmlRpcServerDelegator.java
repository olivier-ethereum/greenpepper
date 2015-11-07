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

public class GreenPepperXmlRpcServerDelegator
		implements RpcClientService
{

	private GreenPepperXmlRpcServer serviceDelegator;
	private GreenPepperRpcHelper confluenceServiceDelegator = new ConfluenceXmlRpcGreenPepperServiceImpl();

	public GreenPepperXmlRpcServerDelegator()
	{
	}

	public void setGreenPepperXmlRpcServerService(GreenPepperXmlRpcServer delegator)
	{
		this.serviceDelegator = delegator;
	}

	public Vector<Object> license()
	{
		return serviceDelegator.license();
	}

	public String uploadNewLicense(String newLicense)
	{
		return serviceDelegator.uploadNewLicense(newLicense);
	}

	public String testConnection()
	{
		return serviceDelegator.testConnection();
	}

	public String ping(Vector<Object> repositoryParams)
	{
		return serviceDelegator.ping(repositoryParams);
	}

	public Vector<Object> getAllEnvironmentTypes()
	{
		return serviceDelegator.getAllEnvironmentTypes();
	}

	public Vector<Object> getRunner(String name)
	{
		return serviceDelegator.getRunner(name);
	}

	public Vector<Object> getAllRunners()
	{
		return serviceDelegator.getAllRunners();
	}

	public String createRunner(Vector<Object> runnerParams)
	{
		return serviceDelegator.createRunner(runnerParams);
	}

	public String updateRunner(String oldRunnerName, Vector<Object> runnerParams)
	{
		return serviceDelegator.updateRunner(oldRunnerName, runnerParams);
	}

	public String removeRunner(String name)
	{
		return serviceDelegator.removeRunner(name);
	}

	public Vector<Object> getRegisteredRepository(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getRegisteredRepository(repositoryParams);
	}

	public Vector<Object> registerRepository(Vector<Object> repositoryParams)
	{
		return serviceDelegator.registerRepository(repositoryParams);
	}

	public String updateRepositoryRegistration(Vector<Object> repositoryParams)
	{
		return serviceDelegator.updateRepositoryRegistration(repositoryParams);
	}

	public String removeRepository(String repositoryUid)
	{
		return serviceDelegator.removeRepository(repositoryUid);
	}

	public Vector<Object> getAllProjects()
	{
		return serviceDelegator.getAllProjects();
	}

	public Vector<Object> getAllSpecificationRepositories()
	{
		return serviceDelegator.getAllSpecificationRepositories();
	}

	public Vector<Object> getSpecificationRepositoriesOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSpecificationRepositoriesOfAssociatedProject(repositoryParams);
	}

	public Vector<Object> getAllRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams)
	{
		return serviceDelegator.getAllRepositoriesForSystemUnderTest(systemUnderTestParams);
	}

	public Vector<Object> getSpecificationRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams)
	{
		return serviceDelegator.getSpecificationRepositoriesForSystemUnderTest(systemUnderTestParams);
	}

	public Vector<Object> getRequirementRepositoriesOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getRequirementRepositoriesOfAssociatedProject(repositoryParams);
	}

	public Vector<Object> getSystemUnderTestsOfAssociatedProject(Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSystemUnderTestsOfAssociatedProject(repositoryParams);
	}

	public Vector<Object> getSystemUnderTestsOfProject(String projectName)
	{
		return serviceDelegator.getSystemUnderTestsOfProject(projectName);
	}

	public String addSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams,
												  Vector<Object> specificationParams)
	{
		return serviceDelegator.addSpecificationSystemUnderTest(systemUnderTestParams, specificationParams);
	}

	public String removeSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams,
													 Vector<Object> specificationParams)
	{
		return serviceDelegator.removeSpecificationSystemUnderTest(systemUnderTestParams, specificationParams);
	}

	public String doesSpecificationHasReferences(Vector<Object> specificationParams)
	{
		return serviceDelegator.doesSpecificationHasReferences(specificationParams);
	}

	public Vector<Object> getSpecificationReferences(Vector<Object> specificationParams)
	{
		return serviceDelegator.getSpecificationReferences(specificationParams);
	}

	public String doesRequirementHasReferences(Vector<Object> requirementParams)
	{
		return serviceDelegator.doesRequirementHasReferences(requirementParams);
	}

	public Vector<Object> getRequirementReferences(Vector<Object> requirementParams)
	{
		return serviceDelegator.getRequirementReferences(requirementParams);
	}

	public Vector<Object> getReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.getReference(referenceParams);
	}

	public Vector<Object> getSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	public String createSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.createSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	public String updateSystemUnderTest(String oldSystemUnderTestName, Vector<Object> systemUnderTestParams,
										Vector<Object> repositoryParams)
	{
		return serviceDelegator.updateSystemUnderTest(oldSystemUnderTestName, systemUnderTestParams, repositoryParams);
	}

	public String removeSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.removeSystemUnderTest(systemUnderTestParams, repositoryParams);
	}

	public String setSystemUnderTestAsDefault(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.setSystemUnderTestAsDefault(systemUnderTestParams, repositoryParams);
	}

	public String removeRequirement(Vector<Object> requirementParams)
	{
		return serviceDelegator.removeRequirement(requirementParams);
	}

	public Vector<Object> getSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.getSpecification(specificationParams);
	}

	public Vector<Object> getSpecifications(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams)
	{
		return serviceDelegator.getSpecifications(systemUnderTestParams, repositoryParams);
	}

	public Vector<?> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName)
	{
		return serviceDelegator.getListOfSpecificationLocations(repositoryUID, systemUnderTestName);
	}

	public Vector<Object> createSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.createSpecification(specificationParams);
	}

	public String updateSpecification(Vector<Object> oldSpecificationParams, Vector<Object> newSpecificationParams)
	{
		return serviceDelegator.updateSpecification(oldSpecificationParams, newSpecificationParams);
	}

	public String removeSpecification(Vector<Object> specificationParams)
	{
		return serviceDelegator.removeSpecification(specificationParams);
	}

	public String createReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.createReference(referenceParams);
	}

	public Vector<Object> updateReference(Vector<Object> oldReferenceParams, Vector<Object> newReferenceParams)
	{
		return serviceDelegator.updateReference(oldReferenceParams, newReferenceParams);
	}

	public String removeReference(Vector<Object> referenceParams)
	{
		return serviceDelegator.removeReference(referenceParams);
	}

	public Vector<Object> runSpecification(Vector<Object> systemUnderTestParams, Vector<Object> specificationParams,
										   boolean implementedVersion, String locale)
	{
		return serviceDelegator.runSpecification(systemUnderTestParams, specificationParams, implementedVersion, locale);
	}

	public Vector<Object> runReference(Vector<Object> referenceParams, String locale)
	{
		return serviceDelegator.runReference(referenceParams, locale);
	}

	public Vector<Object> getRequirementSummary(Vector<Object> requirementParams)
	{
		return serviceDelegator.getRequirementSummary(requirementParams);
	}

	public Vector<Object> getSpecificationHierarchy(Vector<Object> repositoryParams, Vector<Object> sutParams)
	{
		return serviceDelegator.getSpecificationHierarchy(repositoryParams, sutParams);
	}

	public String getRenderedSpecification(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.getRenderedSpecification(username, password, args);
	}

	public Vector getSpecificationHierarchy(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.getSpecificationHierarchy(username, password, args);
	}

	public String setSpecificationAsImplemented(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.setSpecificationAsImplemented(username, password, args);
	}

	public String saveExecutionResult(String username, String password, Vector<?> args)
	{
		return confluenceServiceDelegator.saveExecutionResult(username, password, args);
	}
}
