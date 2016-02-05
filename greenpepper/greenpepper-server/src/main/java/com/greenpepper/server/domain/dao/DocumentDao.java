package com.greenpepper.server.domain.dao;

import java.util.List;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.Requirement;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;

/**
 * <p>DocumentDao interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface DocumentDao
{
    /**
     * Retrieves the Requirement for the specified repository UID.
     * If none found an GreenPepperServerException is thrown.
     * </p>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param requirementName a {@link java.lang.String} object.
     * @return the Requirement for the specified repository UID.
     */
    public Requirement getRequirementByName(String repositoryUid, String requirementName);
    
    /**
     * Saves the Requirement for the specified repository UID.
     * </p>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param requirementName a {@link java.lang.String} object.
     * @return the new Requirement
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Requirement createRequirement(String repositoryUid, String requirementName) throws GreenPepperServerException;

    /**
     * Retrieves the Requirement for the specified repository UID.
     * If none found then a new is saved and returned.
     * </p>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param requirementName a {@link java.lang.String} object.
     * @return the retrieved/created Requirement
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Requirement getOrCreateRequirement(String repositoryUid, String requirementName) throws GreenPepperServerException;

    /**
     * Removes the Requirement.
     * </p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRequirement(Requirement requirement) throws GreenPepperServerException;
    
    /**
     * Retrieves the Specification for the specified repository UID.
     * </p>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param specificationName a {@link java.lang.String} object.
     * @return the Specification for the specified repository UID.
     */
    public Specification getSpecificationByName(String repositoryUid, String specificationName);

    /**
     * Retrieves the Specifications for the specified repository UID and Names. This method will not check if some of the
     * names given are not available.
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @param specificationNames the list of specification names.
     * @return The list of Specifications. Will never return null.
     */
    List<Specification> getSpecificationsByName(String repositoryUid, List<String> specificationNames);
    
	/**
	 * Retrieves the Specification for the specified id.
	 * </p>
	 *
	 * @param id Specification id to retrieve
	 * @return the Specification for the given id
	 */
	public Specification getSpecificationById(Long id);
    
    /**
     * Saves the Specification for the specified repository UID.
     * </p>
     *
     * @param systemUnderTestName a {@link java.lang.String} object.
     * @param repositoryUid a {@link java.lang.String} object.
     * @param specificationName a {@link java.lang.String} object.
     * @return the new Specification
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification createSpecification(String systemUnderTestName, String repositoryUid, String specificationName) throws GreenPepperServerException;

    /**
     * Retrieves the Specification for the specified repository UID.
     * If none found then a new is saved and returned.
     * </p>
     *
     * @param systemUnderTestName a {@link java.lang.String} object.
     * @param repositoryUid a {@link java.lang.String} object.
     * @param specificationName a {@link java.lang.String} object.
     * @return the retrieved/created Specification
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification getOrCreateSpecification(String systemUnderTestName, String repositoryUid, String specificationName) throws GreenPepperServerException;

    /**
     * UPdates the Specification.
     * </p>
     *
     * @param newSpecification a {@link com.greenpepper.server.domain.Specification} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param oldSpecification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public void updateSpecification(Specification oldSpecification, Specification newSpecification) throws GreenPepperServerException;

    /**
     * Removes the Specification.
     * </p>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public void removeSpecification(Specification specification) throws GreenPepperServerException;

    /**
     * Retrieves the Reference from dataBase.
     * </p>
     *
     * @return the Reference from dataBase.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     */
    public Reference get(Reference reference);

    /**
     * Retrieves the list of References linked to the Specification
     * </p>
     *
     * @return the list of References linked to the Specification
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public List<Reference> getAllReferences(Specification specification);

    /**
     * Retrieves the list of References linked to the Requirement
     * </p>
     *
     * @return the list of References linked to the Requirement
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     */
    public List<Reference> getAllReferences(Requirement requirement);

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the Specification
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification) throws GreenPepperServerException;
    
    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the Specification
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification) throws GreenPepperServerException;
    
    /**
     * Creates the Reference.
     * The Project, the repositories and the System under test have to exist
     * else an exception will be thrown.
     * </p>
     *
     * @return the new Created Reference
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     */
    public Reference createReference(Reference reference) throws GreenPepperServerException;
    
    /**
     * Deletes the Reference
     * </p>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     */
    public void removeReference(Reference reference) throws GreenPepperServerException;
    
    /**
     * Updates the old Reference with the new one.
     * Basically removes the old one and creates a new one.
     * </p>
     *
     * @param oldReference a {@link com.greenpepper.server.domain.Reference} object.
     * @param newReference a {@link com.greenpepper.server.domain.Reference} object.
     * @return the updated Reference
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Reference updateReference(Reference oldReference, Reference newReference) throws GreenPepperServerException;

	/**
	 * Creates the Execution.
	 *
	 * @param execution a {@link com.greenpepper.server.domain.Execution} object.
	 * @return the new created Execution
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	public Execution createExecution(Execution execution) throws GreenPepperServerException;
	
    /**
     * Run the Specification on the SystemUnderTest.
     * </p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param implemeted a boolean.
     * @param locale a {@link java.lang.String} object.
     * @return the execution of the Specification on the SystemUnderTest.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implemeted, String locale) throws GreenPepperServerException;
    
    /**
     * Run the Specification of the reference.
     * </p>
     *
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     * @param locale a {@link java.lang.String} object.
     * @return the executed Reference
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Reference runReference(Reference reference, String locale) throws GreenPepperServerException;

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     * <p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    public List<Specification> getSpecifications(SystemUnderTest sut, Repository repository);

	/**
	 * Retrieve specification Executions for a given Specification where the specification has been
	 * executed before the given start date.
	 *
	 * @param specification a {@link com.greenpepper.server.domain.Specification} object.
	 * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 * @param maxResults a int.
	 * @return Specification executions containing at most the max-result items
	 */
	public List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults);

	/**
	 * Retrieve an Execution for the given id.
	 *
	 * @param id a {@link java.lang.Long} object.
	 * @return execution for the given id
	 * @throws GreenPepperServerException if any.
	 */
	public Execution getSpecificationExecution(Long id);

}
