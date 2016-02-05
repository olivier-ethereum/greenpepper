package com.greenpepper.server.domain.dao;

import java.util.List;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.component.ContentType;

/**
 * <p>RepositoryDao interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface RepositoryDao
{
    /**
     * Retrieves the Repository.
     * If none found an GreenPepperServerException is thrown.
     * </p>
     *
     * @return the Repository.
     * @param repositoryUID a {@link java.lang.String} object.
     */
    public Repository getByUID(String repositoryUID);

    /**
     * Retrieves the Repository.
     * If none found an GreenPepperServerException is thrown.
     * </p>
     *
     * @param projectName a {@link java.lang.String} object.
     * @param repositoryName a {@link java.lang.String} object.
     * @return the Repository.
     */
    public Repository getByName(String projectName, String repositoryName);

    /**
     * Retrieves all the registered Repositories.
     * </p>
     *
     * @return the repositories
     */
    public List<Repository> getAll();

    /**
     * Retrieves all the registered Repositories for a project.
     * </p>
     *
     * @param projectName a {@link java.lang.String} object.
     * @return all the registered Repositories for a project.
     */
    public List<Repository> getAll(String projectName);

    /**
     * Retrieves all the registered Test Repositories for  project.
     * </p>
     *
     * @param projectName a {@link java.lang.String} object.
     * @return the Tests repositories
     */
    public List<Repository> getAllTestRepositories(String projectName);

    /**
     * Retrieves all the registered Requirement Repositories for a project.
     * </p>
     *
     * @param projectName a {@link java.lang.String} object.
     * @return the Requirements repositories
     */
    public List<Repository> getAllRequirementRepositories(String projectName);


    /**
     * Retrieve all the repository of a certain type.
     *
     * @param contentType a {@link com.greenpepper.server.domain.component.ContentType} object.
     * @return a {@link java.util.List} object.
     */
    public List<Repository> getAllRepositories(ContentType contentType);

    /**
     * Retrieves the repository type by name.
     * </p>
     *
     * @param repositoryTypeName a {@link java.lang.String} object.
     * @return the repository type.
     */
    public RepositoryType getTypeByName(String repositoryTypeName);

    /**
     * Creates a new Repository.
     * </p>
     *
     * @param newRepository a {@link com.greenpepper.server.domain.Repository} object.
     * @return the created repository
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository create(Repository newRepository) throws GreenPepperServerException;

    /**
     * Updates the Repository.
     * </p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void update(Repository repository) throws GreenPepperServerException;
    
    /**
     * Removes the repository if this one doesnt hold any specifications
     *
     * @param repositoryUid a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void remove(String repositoryUid) throws GreenPepperServerException;

    /**
     * Retrieves all available RepositoryTypes.
     * <p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<RepositoryType> getAllTypes();
    
    /**
     * Create a new Repository Type
     * </p>
     *
     * @return the Requirement type created
     * @param repositoryType a {@link com.greenpepper.server.domain.RepositoryType} object.
     */
    public RepositoryType create(RepositoryType repositoryType);

}
