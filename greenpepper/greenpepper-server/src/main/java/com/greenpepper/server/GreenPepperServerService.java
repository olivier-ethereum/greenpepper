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
package com.greenpepper.server;

import java.util.List;
import java.util.Vector;

import com.greenpepper.report.XmlReport;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.Requirement;
import com.greenpepper.server.domain.RequirementSummary;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.license.GreenPepperLicenceException;
import com.greenpepper.server.license.LicenseBean;
import com.greenpepper.server.license.Permission;

/**
 * <p>GreenPepperServerService interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface GreenPepperServerService
{

	/**
	 * Retrieves the GreenpPepper Server license.
	 *
	 * @return the GreenpPepper Server license bean.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	LicenseBean license()
			throws GreenPepperServerException;

	/**
	 * Uploads the new GreenpPepper Server license.
	 *
	 * @param newLicense New license information to upload
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void uploadNewLicense(String newLicense)
			throws GreenPepperServerException;

	/**
	 * Indicates if the current license type is 'Commercial'.
	 *
	 * @return true if license type is 'Commercial', false otherwise
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	boolean isCommercialLicense()
			throws GreenPepperServerException;

	/**
	 * Verifies that the license supports the repository has the rgiht permission.
	 *
	 * @param repository The repository to verify
	 * @param permission Permission access to verify on the given repository
	 * @throws com.greenpepper.server.license.GreenPepperLicenceException if any.
	 */
	void verifyRepositoryPermission(Repository repository, Permission permission)
			throws GreenPepperLicenceException;

	/**
	 * Retrieves the EnvironmentTypes available.
	 *
	 * @return all the EnvironmentTypes available.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<EnvironmentType> getAllEnvironmentTypes()
			throws GreenPepperServerException;

	/**
	 * Retrieves the runner for a given the name.
	 *
	 * @param name The name of the runner to retrieve
	 * @return the runner for a given the name.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Runner getRunner(String name)
			throws GreenPepperServerException;

	/**
	 * Retrieves all available Runners.
	 *
	 * @return all available Runners
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Runner> getAllRunners()
			throws GreenPepperServerException;

	/**
	 * Creates a new Runner.
	 *
	 * @param runner The runner to create
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void createRunner(Runner runner)
			throws GreenPepperServerException;

	/**
	 * Updates the Runner.
	 *
	 * @param oldRunnerName The name of the old runner to be updated
	 * @param runner		The runner to update
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void updateRunner(String oldRunnerName, Runner runner)
			throws GreenPepperServerException;

	/**
	 * Creates a new Runner.
	 *
	 * @param name The name of the runner to remove
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeRunner(String name)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Repository for the uid.
	 *
	 * @param uid The repository identifier
	 * @param maxUsers The maximum user the repository should allow, null for no check
	 * @return the Repository for the uid.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Repository getRepository(String uid, Integer maxUsers)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Repository for the uid.
	 *
	 * @param repository The repository
	 * @return the Repository for the uid.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Repository getRegisteredRepository(Repository repository)
			throws GreenPepperServerException;

	/**
	 * Registers the repository in GreenPepper-server. If project not found it will be created.
	 *
	 * @param repository The repository to be registered
	 * @return the registered repository.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Repository registerRepository(Repository repository)
			throws GreenPepperServerException;

	/**
	 * Updates the Repository Registration. If project not found it will be created.
	 *
	 * @param repository The repository to update
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void updateRepositoryRegistration(Repository repository)
			throws GreenPepperServerException;

	/**
	 * Removes the Repository if this one doesnt hold any specifications.
	 *
	 * @param repositoryUid The repository identifier to be removed
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeRepository(String repositoryUid)
			throws GreenPepperServerException;

	/**
	 * Gets all repository associated to the given project.
	 *
	 * @param projectName Name of the project
	 * @return list of repository
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getRepositoriesOfAssociatedProject(String projectName)
			throws GreenPepperServerException;

	/**
	 * Retrieves the project for a given the name.
	 *
	 * @param name The name of the project to retrieve
	 * @return the project for a given the name.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Project getProject(String name)
			throws GreenPepperServerException;

	/**
	 * Creates a new Project.
	 *
	 * @param project The project to create
	 * @return the newly created project instance
	 * @throws com.greenpepper.server.GreenPepperServerException Exception
	 */
	Project createProject(Project project)
			throws GreenPepperServerException;

	/**
	 * Updates the Project.
	 *
	 * @param oldProjectName The name of the old project to be updated
	 * @param project		The project to update
	 * @return the newly updated project instance
	 * @throws com.greenpepper.server.GreenPepperServerException Exception
	 */
	Project updateProject(String oldProjectName, Project project)
			throws GreenPepperServerException;

	/**
	 * Retrieves the complete project list.
	 *
	 * @return the complete project list.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Project> getAllProjects()
			throws GreenPepperServerException;

	/**
	 * Retrieves all the Specification repository grouped by project or an error id in a Hastable if an error occured.
	 *
	 * @return the Specification repository list grouped by types for the project or an error id in a Hastable if an error
	 *         occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getAllSpecificationRepositories()
			throws GreenPepperServerException;

	/**
	 * Retrieves the Specification repository list grouped by types for the project associated with the specified
	 * repository or an error id in a Hastable if an error occured.
	 * <p/>
	 *
	 * @param repositoryUid The repository identifier
	 * @return the Specification repository list grouped by types for the project associated with the specified repository
	 *         or an error id in a Hastable if an error occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getSpecificationRepositoriesOfAssociatedProject(String repositoryUid)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Repository list for the project associated with the specified system under test or an error id in a
	 * Hastable if an error occured.
	 *
	 * @param sut The system under test to retrieve the list of repository
	 * @return the repository list for the project associated with the specified systemUnderTest
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest sut)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Specification repository list grouped by types for the project associated with the specified
	 * SystemUnderTest or an error id in a Hastable if an error occured.
	 *
	 * @param sut The system under test to retrieve the list of repository
	 * @return the Specification repository list grouped by types for the project associated with the specified
	 *         SystemUnderTest or an error id in a Hastable if an error occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getSpecificationRepositoriesForSystemUnderTest(SystemUnderTest sut)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Requirement repository list for the project associated with the specified repository or an error id in
	 * a Hastable if an error occured.
	 *
	 * @param repositoryUid The repository identifer to retrieve the list of requirement
	 * @return the Requirement repository list for the project associated with the specified repository or an error id in a
	 *         Hastable if an error occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Repository> getRequirementRepositoriesOfAssociatedProject(String repositoryUid)
			throws GreenPepperServerException;

	/**
	 * Retrieves the SystemUnderTest list for the project associated with the specified repository or an error id in a
	 * Hastable if an error occured.
	 *
	 * @param repositoryUid The repository identifier to retrieve the list of sut
	 * @return the SystemUnderTest list for the project associated with the specified repository or an error id in a
	 *         Hastable if an error occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(String repositoryUid)
			throws GreenPepperServerException;

	/**
	 * Retrieves the SystemUnderTest list for the project associated or an error id in a Hastable if an error occured.
	 *
	 * @param projectName The name of the project to retrieve the list of sut
	 * @return the SystemUnderTest list for the project associated or an error id in a Hastable if an error occured.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<SystemUnderTest> getSystemUnderTestsOfProject(String projectName)
			throws GreenPepperServerException;

	/**
	 * Adds the SystemUnderTest to the SystemUnderTest list of the Specification.
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void addSpecificationSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
			throws GreenPepperServerException;

	/**
	 * Removes the SystemUnderTest to the SystemUnderTest list of the Specification.
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeSpecificationSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
			throws GreenPepperServerException;

	/**
	 * Checks if the Specification is in atleast one reference.
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @return true if the Specification is in atleast one reference.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	boolean doesSpecificationHasReferences(Specification specification)
			throws GreenPepperServerException;

	/**
	 * Retrieves the references list of the specified Specification
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @return the references list of the specified Specification
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Reference> getSpecificationReferences(Specification specification)
			throws GreenPepperServerException;

	/**
	 * Retrieve executions list of the specified Specification
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param maxResults a int.
	 * @return the executions list of the specified Specification containing at most max-results items
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults)
			throws GreenPepperServerException;

	/**
	 * Retrieve execution for the given id.
	 *
	 * @param id a {@link java.lang.Long} object.
	 * @return execution
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Execution getSpecificationExecution(Long id)
			throws GreenPepperServerException;

	/**
	 * Checks if the Requirement is in atleast one Reference.
	 *
	 * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
	 * @return true if the Requirement is in atleast one Reference.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	boolean doesRequirementHasReferences(Requirement requirement)
			throws GreenPepperServerException;

	/**
	 * Retrieves the References list of the specified requirement
	 *
	 * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
	 * @return the References list of the specified requirement
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Reference> getRequirementReferences(Requirement requirement)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Requirement summary.
	 *
	 * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
	 * @return the Requirement summary.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	RequirementSummary getRequirementSummary(Requirement requirement)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Reference.
	 *
	 * @param reference a {@link com.greenpepper.server.domain.Reference} object.
	 * @return the Reference.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Reference getReference(Reference reference)
			throws GreenPepperServerException;

	/**
	 * Retrieves the systemUnderTest
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @return the System under test
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository)
			throws GreenPepperServerException;

	/**
	 * Creates the systemUnderTest
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository)
			throws GreenPepperServerException;

	/**
	 * Updates the systemUnderTest
	 *
	 * @param oldSystemUnderTestName a {@link java.lang.String} object.
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest systemUnderTest,
									  Repository repository)
			throws GreenPepperServerException;

	/**
	 * Removes the systemUnderTest
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository)
			throws GreenPepperServerException;

	/**
	 * Sets the systemUnderTest as the project default SystemUnderTest
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository)
			throws GreenPepperServerException;

	/**
	 * Removes the Requirement.
	 *
	 * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeRequirement(Requirement requirement)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Specification
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @return the Specification
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Specification getSpecification(Specification specification)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Specification using the given id.
	 *
	 * @param id Specification id to retrieve
	 * @return the specification
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Specification getSpecificationById(Long id)
			throws GreenPepperServerException;

	/**
	 * Retrieves all Specifications for a given SystemUnderTest and Repository
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @return all Specifications for a given SystemUnderTest and Repository
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	List<Specification> getSpecifications(SystemUnderTest systemUnderTest, Repository repository)
			throws GreenPepperServerException;

	/**
	 * Retrieves the Specification location list for a given SystemUnderTest and Repository
	 *
	 * @param repositoryUID a {@link java.lang.String} object.
	 * @param systemUnderTestName a {@link java.lang.String} object.
	 * @return the Specification location list for a given SystemUnderTest and Repository
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Vector<Object> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName)
			throws GreenPepperServerException;

	/**
	 * Retrieve the specifications hierarchy for a Repository.
	 *
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @return the TestCase executed
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest)
			throws GreenPepperServerException;

	/**
	 * Creates the Specification
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @return the new Specification
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Specification createSpecification(Specification specification)
			throws GreenPepperServerException;

	/**
	 * Updates the Specification.
	 *
	 * @param oldSpecification a {@link com.greenpepper.server.domain.Specification} object.
	 * @param newSpecification a {@link com.greenpepper.server.domain.Specification} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void updateSpecification(Specification oldSpecification, Specification newSpecification)
			throws GreenPepperServerException;

	/**
	 * Removes the Specification.
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeSpecification(Specification specification)
			throws GreenPepperServerException;

	/**
	 * Creates a Reference
	 *
	 * @param reference a {@link com.greenpepper.server.domain.Reference} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void createReference(Reference reference)
			throws GreenPepperServerException;

	/**
	 * Update the Reference. The Old one will be deleted based on the oldReferenceParams and a new One will be created
	 * based on the newReferenceParams.
	 *
	 * @param oldReference a {@link com.greenpepper.server.domain.Reference} object.
	 * @param newReference a {@link com.greenpepper.server.domain.Reference} object.
	 * @return the updated Reference
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Reference updateReference(Reference oldReference, Reference newReference)
			throws GreenPepperServerException;

	/**
	 * Deletes the specified Reference.
	 *
	 * @param reference a {@link com.greenpepper.server.domain.Reference} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeReference(Reference reference)
			throws GreenPepperServerException;

	/**
	 * Creates an Execution.
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @param xmlReport a {@link com.greenpepper.report.XmlReport} object.
	 * @return the new created Execution
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Execution createExecution(SystemUnderTest systemUnderTest, Specification specification, XmlReport xmlReport)
			throws GreenPepperServerException;

	/**
	 * Executes the Specification over the selected SystemUnderTest.
	 *
	 * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @param implementedVersion a boolean.
	 * @param locale a {@link java.lang.String} object.
	 * @return the Execution of the Specification over the selected SystemUnderTest.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification,
									  boolean implementedVersion, String locale)
			throws GreenPepperServerException;

	/**
	 * Executes the Reference.
	 *
	 * @param reference a {@link com.greenpepper.server.domain.Reference} object.
	 * @param locale a {@link java.lang.String} object.
	 * @return the Reference executed
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	Reference runReference(Reference reference, String locale)
			throws GreenPepperServerException;

	/**
	 * Removes an existing Project.
	 *
	 * @param project a {@link com.greenpepper.server.domain.Project} object.
	 * @param cascade Indicates to remove the project in cascading mode (remove any associations)
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	void removeProject(Project project, boolean cascade)
			throws GreenPepperServerException;
}
