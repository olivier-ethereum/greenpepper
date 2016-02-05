package com.greenpepper.server.domain.dao;

import java.util.List;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;

/**
 * <p>SystemUnderTestDao interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface SystemUnderTestDao
{
	/**
	 * Retrieves the EnvironmentType for the specified name.
	 * </p>
	 *
	 * @param name of the EnvironmentType
	 * @return the EnvironmentType for the specified name.
	 */
	public EnvironmentType getEnvironmentTypeByName(String name);
	
	/**
	 * Retrieves all the Environment Types available.
	 * </p>
	 *
	 * @return all the Environment Types available.
	 */
	public List<EnvironmentType> getAllEnvironmentTypes();
	
	/**
	 * Creates the EnvironmentType
	 * </p>
	 *
	 * @param environmentType a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 * @return the new environmentType.
	 */
	public EnvironmentType create(EnvironmentType environmentType);
	
    /**
     * Retrieves the Runner for the specified name.
     * </p>
     *
     * @param name of the runner
     * @return the Runner for the specified name.
     */
    public Runner getRunnerByName(String name);
    
    /**
     * Retrieves All available runners.
     * </p>
     *
     * @return All available runners.
     */
    public List<Runner> getAllRunners();
    
    /**
     * Creates the Runner
     * </p>
     *
     * @param runner a {@link com.greenpepper.server.domain.Runner} object.
     * @return the new runner.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Runner create(Runner runner) throws GreenPepperServerException;
    
    /**
     * Updates the runner.
     * </p>
     *
     * @param oldRunnerName a {@link java.lang.String} object.
     * @param runner a {@link com.greenpepper.server.domain.Runner} object.
     * @return the updated runner.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Runner update(String oldRunnerName, Runner runner) throws GreenPepperServerException;
    
    /**
     * Removes the runner.
     * </p>
     *
     * @param runnerName a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRunner(String runnerName) throws GreenPepperServerException;
    
    /**
     * Retrieves the SystemUnderTest for the specified name.
     * </p>
     *
     * @return the SystemUnderTest for the specified name.
     * @param projectName a {@link java.lang.String} object.
     * @param sutName a {@link java.lang.String} object.
     */
    public SystemUnderTest getByName(String projectName, String sutName);
    
    /**
     * Retrieves all the SystemUnderTest for the registered Project.
     * </p>
     *
     * @param projectName a {@link java.lang.String} object.
     * @return all the SystemUnderTest for the registered Project.
     */
    public List<SystemUnderTest> getAllForProject(String projectName);
    
    /**
     * Retrieves all the SystemUnderTest for the registered Runner.
     * </p>
     *
     * @param runnerName a {@link java.lang.String} object.
     * @return all the SystemUnderTest for the registered Runner.
     */
    public List<SystemUnderTest> getAllForRunner(String runnerName);
    
    /**
     * Saves the specified SystemUnderTest.
     * </p>
     *
     * @param newSystemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return the new SystemUnderTest.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public SystemUnderTest create(SystemUnderTest newSystemUnderTest) throws GreenPepperServerException;
    
    /**
     * Updates the specified SystemUnderTest.
     * </p>
     *
     * @param oldSutName a {@link java.lang.String} object.
     * @param updatedSystemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return the updated SystemUnderTest.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public SystemUnderTest update(String oldSutName, SystemUnderTest updatedSystemUnderTest) throws GreenPepperServerException;
    
    /**
     * Deletes the specified SystemUnderTest.
     * </p>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param projectName a {@link java.lang.String} object.
     * @param sutName a {@link java.lang.String} object.
     */
    public void remove(String projectName, String sutName) throws GreenPepperServerException;
    
    /**
     * Set the specified SystemUnderTest as the new project default.
     * </p>
     *
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void setAsDefault(SystemUnderTest systemUnderTest) throws GreenPepperServerException;
    
    /**
     * Retrieves all references that depends on the SystemUnderTest
     * </p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return all references that depends on the SystemUnderTest
     */
    public List<Reference> getAllReferences(SystemUnderTest sut);
    
    /**
     * Retrieves all specifications that depends on the SystemUnderTest
     * </p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return all specifications that depends on the SystemUnderTest
     */
    public List<Specification> getAllSpecifications(SystemUnderTest sut);
}
