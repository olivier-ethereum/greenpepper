package com.greenpepper.server.rpc;

import java.util.Set;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.ServerPropertiesManager;
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
import com.greenpepper.server.license.LicenseBean;

/**
 * The GreenPepper Client interface.
 * All available methods are documented here.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */
public interface RpcClientService
{
    /**
     * Retrieves the GreenpPepper Server license.
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the GreenpPepper Server license.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public LicenseBean license(String identifier) throws GreenPepperServerException;

    /**
     * Uploads the new GreenpPepper Server license.
     * <p/>
     *
     * @param newLicence a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void uploadLicense(String newLicence, String identifier) throws GreenPepperServerException;

    /**
     * Tests the connection at the url and handler.
     * </p>
     *
     * @param url a {@link java.lang.String} object.
     * @param handler a {@link java.lang.String} object.
     * @return true if server successfully pinged.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public boolean testConnection(String url, String handler) throws GreenPepperServerException;

    /**
     * Pings the server.
     * </p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return true if server successfully pinged.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public boolean ping(Repository repository, String identifier) throws GreenPepperServerException;
    
    /**
     * Retrieves the EnvironmentTypes available.
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return all the EnvironmentTypes available.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<EnvironmentType> getAllEnvironmentTypes(String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Runner for a given name.
     * <p/>
     *
     * @param name a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Runner for a given name.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Runner getRunner(String name, String identifier) throws GreenPepperServerException;
    
    /**
     * Retrieves the Runners available.
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the all Runners available.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Runner> getAllRunners(String identifier) throws GreenPepperServerException;
    
    /**
     * Creates a new Runner
     * <p/>
     *
     * @param runner a {@link com.greenpepper.server.domain.Runner} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void createRunner(Runner runner, String identifier) throws GreenPepperServerException;
    
    /**
     * Updates the Runner
     * <p/>
     *
     * @param oldRunnerName a {@link java.lang.String} object.
     * @param runner a {@link com.greenpepper.server.domain.Runner} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void updateRunner(String oldRunnerName, Runner runner, String identifier) throws GreenPepperServerException;
    
    /**
     * Removes the Runner of the given name
     * <p/>
     *
     * @param name a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRunner(String name, String identifier) throws GreenPepperServerException;
    
    /**
     * Checks if registered and Retrieves the Repository.
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the registered Repository.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository getRegisteredRepository(Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Registers the repository in GreenPepper-server.
     * If project not found it will be created.
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the registered repository.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository registerRepository(Repository repository, String identifier) throws GreenPepperServerException;


    /**
     * Updates the Repository Registration.
     * If project not found it will be created.
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void updateRepositoryRegistration(Repository repository, String identifier) throws GreenPepperServerException;
    
    /**
     * Removes the Repository if this one doesnt hold any specifications.
     * <p/>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRepository(String repositoryUid, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the complete project list.
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the complete project list.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Project> getAllProjects(String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Specification repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Specification repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject(Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Specification repository list for the project associated with
     * the specified system under test or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Specification repository list for the project associated with
     * the specified systemUnderTest or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject(SystemUnderTest systemUnderTest, String identifier) throws GreenPepperServerException;
    
    /**
     * Retrieves all the Specification repositorys list by project
     * or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the Specification repository list for the project
     * or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Repository> getAllSpecificationRepositories(String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Repository list for the project associated with
     * the specified system under test or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the repository list for the project associated with
     * the specified systemUnderTest or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest systemUnderTest, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Repository> getRequirementRepositoriesOfAssociatedProject(Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the SystemUnderTest list for for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated.
     * <p/>
     *
     * @param projectName a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the SystemUnderTest list for for the project.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<SystemUnderTest> getSystemUnderTestsOfProject(String projectName, String identifier) throws GreenPepperServerException;

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the Specification.
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the Specification.
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Checks if the Specification is in atleast one Reference.
     * <p/>
     *
     * @return true if the specification is in atleast one Reference.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     */
    public boolean hasReferences(Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the References list of the specified Specification
     * <p/>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the References list of the specified Specification
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<Reference> getReferences(Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Checks if the Requirement is in atleast one Reference.
     * <p/>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @return true if the Requirement is in atleast one Reference.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param identifier a {@link java.lang.String} object.
     */
    public boolean hasReferences(Requirement requirement, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the References list of the specified requirement
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the References list of the specified requirement
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     */
    public Set<Reference> getReferences(Requirement requirement, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the Reference.
     * </p>
     *
     * @param identifier a {@link java.lang.String} object.
     * @return the Reference.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     */
    public Reference getReference(Reference reference, String identifier) throws GreenPepperServerException;

    /**
     * Creates a new SystemUnderTest.
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the SystemUnderTest.
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @return SystemUnderTest
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Updates the SystemUnderTest.
     * </p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param oldsyStemUnderTestName a {@link java.lang.String} object.
     * @param newSystemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void updateSystemUnderTest(String oldsyStemUnderTestName, SystemUnderTest newSystemUnderTest, Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Removes the SystemUnderTest.
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository, String identifier) throws GreenPepperServerException;

    /**
     * Removes the Requirement
     * <p/>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRequirement(Requirement requirement, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the specification
     * <p/>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the specification
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification getSpecification(Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Creates the Specification
     * <p/>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the new Specification
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification createSpecification(Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Updates the Specification
     * <p/>
     *
     * @param oldSpecification a {@link com.greenpepper.server.domain.Specification} object.
     * @param newSpecification a {@link com.greenpepper.server.domain.Specification} object.
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void updateSpecification(Specification oldSpecification, Specification newSpecification, String identifier) throws GreenPepperServerException;

    /**
     * Removes the Specification
     * <p/>
     *
     * @param identifier a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public void removeSpecification(Specification specification, String identifier) throws GreenPepperServerException;

    /**
     * Creates a Reference
     * <p/>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     * @param identifier a {@link java.lang.String} object.
     */
    public void createReference(Reference reference, String identifier) throws GreenPepperServerException;

    /**
     * Update the Reference.
     * The Old one will be deleted based on the oldReferenceParams and a new One
     * will be created based on the newReferenceParams.
     * <p/>
     *
     * @param oldReference a {@link com.greenpepper.server.domain.Reference} object.
     * @param newReference a {@link com.greenpepper.server.domain.Reference} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the updated Reference.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Reference updateReference(Reference oldReference, Reference newReference, String identifier) throws GreenPepperServerException;

    /**
     * Deletes the specified Reference.
     * <p/>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     * @param identifier a {@link java.lang.String} object.
     */
    public void removeReference(Reference reference, String identifier) throws GreenPepperServerException;

    /**
     * Executes the Specification over the selected SystemUnderTest.
     * <p/>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param implementedVersion a boolean.
     * @param locale a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Execution of the Specification over the selected SystemUnderTest.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implementedVersion, String locale, String identifier) throws GreenPepperServerException;

    /**
     * Executes the Reference.
     * <p/>
     *
     * @param locale a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the Reference with its last execution.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     */
    public Reference runReference(Reference reference, String locale, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the list of specification
     * <p/>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param identifier a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.DocumentNode} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest, String identifier) throws GreenPepperServerException;

    /**
     * Retrieves the requirement summary.
     * <p/>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the requirement summary.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public RequirementSummary getSummary(Requirement requirement, String identifier) throws GreenPepperServerException;
    
    /**
     * Retrieves the server properties manager.
     * <p/>
     *
     * @return the server properties manager.
     */
    public ServerPropertiesManager getServerPropertiesManager();
}
