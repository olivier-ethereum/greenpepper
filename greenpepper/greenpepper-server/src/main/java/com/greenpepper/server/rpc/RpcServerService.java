package com.greenpepper.server.rpc;

import java.util.Vector;

/**
 * The XmlRpcService provides an XML-RPC interface into GreenPepper Server.
 * All available methods are documented here.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */
public interface RpcServerService
{
	/** Constant <code>SERVICE_HANDLER="greenpepper1"</code> */
	String SERVICE_HANDLER = "greenpepper1";
	
    /**
     * Retrieves the GreenpPepper Server license.
     * <p/>
     *
     * @return the GreenpPepper Server license message.
     */
    public Vector<Object> license();

    /**
     * Uploads the new GreenpPepper Server license.
     * <p/>
     *
     * @param newLicense a {@link java.lang.String} object.
     * @return success or error.
     */
    public String uploadNewLicense(String newLicense);

    /**
     * Test the connection to the server.
     * <p/>
     *
     * @return success.
     */
    public String testConnection();

    /**
     * Pings the server.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return success.
     */
    public String ping(Vector<Object> repositoryParams);
    
    /**
     * Retrieves the EnvironmentTypes available.
     * <p/>
     *
     * @return all the EnvironmentTypes available.
     */
    public Vector<Object> getAllEnvironmentTypes();

    /**
     * Retrieves the runner for a given the name.
     * <p/>
     *
     * @param name a {@link java.lang.String} object.
     * @return the runner for a given the name.
     */
    public Vector<Object> getRunner(String name);

    /**
     * Retrieves all available Runners.
     * <p/>
     *
     * @return  all available Runners
     */
    public Vector<Object> getAllRunners();

    /**
     * Creates a new Runner.
     * <p/>
     *
     * @param runnerParams Runner['name','cmd',['envtypename'],'servername','serverport','mainclass',['cp1','cp2'],'secured']
     * @return error id if an error occured
     */
    public String createRunner(Vector<Object> runnerParams);

    /**
     * Updates the Runner.
     * <p/>
     *
     * @param oldRunnerName a {@link java.lang.String} object.
     * @param runnerParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String updateRunner(String oldRunnerName, Vector<Object> runnerParams);

    /**
     * Creates a new Runner.
     * <p/>
     *
     * @param name a {@link java.lang.String} object.
     * @return error id if an error occured
     */
    public String removeRunner(String name);
    
    /**
     * Retrieves the Repository for the uid.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return the Repository for the uid.
     */
    public Vector<Object> getRegisteredRepository(Vector<Object> repositoryParams);

    /**
     * Registers the repository in GreenPepper-server.
     * If project not found it will be created.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return the registered repository.
     */
    public Vector<Object> registerRepository(Vector<Object> repositoryParams);

    /**
     * Updates the Repository Registration.
     * If project not found it will be created.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return a {@link java.lang.String} object.
     */
    public String updateRepositoryRegistration(Vector<Object> repositoryParams);

    /**
     * Removes the Repository if this one doesnt hold any specifications.
     * <p/>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String removeRepository(String repositoryUid);

    /**
     * Retrieves the complete project list.
     * <p/>
     *
     * @return the complete project list.
     */
    public Vector<Object> getAllProjects();

    /**
     * Retrieves all the Specification repository grouped by project
     * or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @return the Specification repository list grouped by types for the project
     * or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getAllSpecificationRepositories();

    /**
     * Retrieves the Specification repository list grouped by types for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return the Specification repository list grouped by types for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getSpecificationRepositoriesOfAssociatedProject(Vector<Object> repositoryParams);

    /**
     * Retrieves the Repository list for the project associated with
     * the specified system under test or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @return the repository list for the project associated with
     * the specified systemUnderTest or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getAllRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams);

    /**
     * Retrieves the Specification repository list grouped by types for the project associated with
     * the specified SystemUnderTest or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @return the Specification repository list grouped by types for the project associated with
     * the specified SystemUnderTest or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getSpecificationRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams);

    /**
     * Retrieves the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getRequirementRepositoriesOfAssociatedProject(Vector<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return the SystemUnderTest list for the project associated with
     * the specified repository or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getSystemUnderTestsOfAssociatedProject(Vector<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated
     * or an error id in a Hastable if an error occured.
     * <p/>
     *
     * @param projectName a {@link java.lang.String} object.
     * @return the SystemUnderTest list for the project associated
     * or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getSystemUnderTestsOfProject(String projectName);

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the Specification.
     * <p/>
     *
     * @param systemUnderTestParams SUT[name,Project[name]]
     * @param specificationParams Spec[name, Repo[name,uid]]
     * @return error id if an error occured
     */
    public String addSpecificationSystemUnderTest(Vector<Object>  systemUnderTestParams, Vector<Object>  specificationParams);

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the Specification.
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param specificationParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String removeSpecificationSystemUnderTest(Vector<Object>  systemUnderTestParams, Vector<Object>  specificationParams);

    /**
     * Checks if the Specification is in atleast one reference.
     * <p/>
     *
     * @param specificationParams a {@link java.util.Vector} object.
     * @return true if the Specification is in atleast one reference.
     */
    public String doesSpecificationHasReferences(Vector<Object> specificationParams);

    /**
     * Retrieves the references list of the specified Specification
     * <p/>
     *
     * @param specificationParams a {@link java.util.Vector} object.
     * @return the references list of the specified Specification
     */
    public Vector<Object> getSpecificationReferences(Vector<Object> specificationParams);

    /**
     * Checks if the Requirement is in atleast one Reference.
     * <p/>
     *
     * @param requirementParams a {@link java.util.Vector} object.
     * @return true if the Requirement is in atleast one Reference.
     */
    public String doesRequirementHasReferences(Vector<Object> requirementParams);

    /**
     * Retrieves the References list of the specified requirement
     * <p/>
     *
     * @param requirementParams a {@link java.util.Vector} object.
     * @return the References list of the specified requirement
     */
    public Vector<Object> getRequirementReferences(Vector<Object> requirementParams);

    /**
     * Retrieves the Reference.
     * </p>
     *
     * @param referenceParams a {@link java.util.Vector} object.
     * @return the Reference.
     */
    public Vector<Object> getReference(Vector<Object> referenceParams);

    /**
     * Retrieves the systemUnderTest
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public Vector<Object> getSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Creates the systemUnderTest
     * <p/>
     *
     * @param systemUnderTestParams Vector[name, Vector[project parameters], Vector[seeds classPaths], Vector[fixture classPaths], fixturefactory,
     *                              fixturefactoryargs, isdefault,
     *                              Runner['name','cmd',['envtypename'],'servername','serverport','mainclass',['cp1','cp2'],'secured'],
     *                              projectdependencydescriptor]
     * @param repositoryParams REPO[name, uid]
     * @return error id if an error occured
     */
    public String createSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Updates the systemUnderTest
     * <p/>
     *
     * @param oldSystemUnderTestName a {@link java.lang.String} object.
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String updateSystemUnderTest(String oldSystemUnderTestName, Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Removes the systemUnderTest
     * <p/>
     *
     * @param systemUnderTestParams SUT[name, project[name]]
     * @param repositoryParams REPO[name,uid]
     * @return error id if an error occured
     */
    public String removeSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String setSystemUnderTestAsDefault(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Removes the Requirement.
     * <p/>
     *
     * @param requirementParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String removeRequirement(Vector<Object> requirementParams);

    /**
     * Retrieves the Specification
     * <p/>
     *
     * @param specificationParams [name,repository[name,uid]]
     * @return the Specification
     */
    public Vector<Object> getSpecification(Vector<Object> specificationParams);

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     * <p>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param repositoryParams a {@link java.util.Vector} object.
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    public Vector<Object> getSpecifications(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Retrieves the Specification location list for a given SystemUnderTest and Repository
     * <p/>
     *
     * @param repositoryUID a {@link java.lang.String} object.
     * @param systemUnderTestName a {@link java.lang.String} object.
     * @return the Specification location list for a given SystemUnderTest and Repository
     */
    public Vector<?> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName);

    /**
     * Creates the Specification
     * <p/>
     *
     * @param specificationParams a {@link java.util.Vector} object.
     * @return the new Specification
     */
    public Vector<Object> createSpecification(Vector<Object> specificationParams);

    /**
     * Updates the Specification.
     * <p/>
     *
     * @param oldSpecificationParams a {@link java.util.Vector} object.
     * @param newSpecificationParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String updateSpecification(Vector<Object> oldSpecificationParams, Vector<Object> newSpecificationParams);

    /**
     * Removes the Specification.
     * <p/>
     *
     * @param specificationParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String removeSpecification(Vector<Object> specificationParams);

    /**
     * Creates a Reference
     * <p/>
     *
     * @param referenceParams a {@link java.util.Vector} object.
     * @return error id if an error occured
     */
    public String createReference(Vector<Object> referenceParams);

    /**
     * Update the Reference.
     * The Old one will be deleted based on the oldReferenceParams and a new One
     * will be created based on the newReferenceParams.
     * <p/>
     *
     * @param oldReferenceParams a {@link java.util.Vector} object.
     * @param newReferenceParams a {@link java.util.Vector} object.
     * @return the updated Reference
     */
    public Vector<Object> updateReference(Vector<Object> oldReferenceParams, Vector<Object> newReferenceParams);

    /**
     * Deletes the specified Reference.
     * <p/>
     *
     * @param referenceParams a {@link java.util.Vector} object.
     * @return error id if an eror occured
     */
    public String removeReference(Vector<Object> referenceParams);

    /**
     * Executes the Specification over the selected SystemUnderTest.
     * <p/>
     *
     * @param systemUnderTestParams a {@link java.util.Vector} object.
     * @param specificationParams a {@link java.util.Vector} object.
     * @param implementedVersion a boolean.
     * @param locale a {@link java.lang.String} object.
     * @return the Execution of the Specification over the selected SystemUnderTest.
     */
    public Vector<Object> runSpecification(Vector<Object> systemUnderTestParams, Vector<Object> specificationParams, boolean implementedVersion, String locale);

    /**
     * Executes the Reference.
     * <p/>
     *
     * @param referenceParams a {@link java.util.Vector} object.
     * @param locale a {@link java.lang.String} object.
     * @return the Reference executed
     */
    public Vector<Object> runReference(Vector<Object> referenceParams, String locale);

    /**
     * Retrieves the Requirement summary.
     * <p/>
     *
     * @param requirementParams a {@link java.util.Vector} object.
     * @return the Requirement summary.
     */
    public Vector<Object> getRequirementSummary(Vector<Object> requirementParams);

    /**
     * Retrieve the specifications hierarchy for a Repository.
     * <p/>
     *
     * @param repositoryParams a {@link java.util.Vector} object.
     * @param sutParams a {@link java.util.Vector} object.
     * @return the TestCase executed
     */
    public Vector<Object> getSpecificationHierarchy(Vector<Object> repositoryParams, Vector<Object> sutParams);

}
