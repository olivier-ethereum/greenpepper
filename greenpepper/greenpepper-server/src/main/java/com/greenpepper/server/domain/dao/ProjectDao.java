package com.greenpepper.server.domain.dao;

import java.util.List;

import com.greenpepper.server.domain.Project;
import com.greenpepper.server.GreenPepperServerException;

/**
 * <p>ProjectDao interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface ProjectDao
{
    /**
     * Retrieves the Project for the specified name.
     * If none found an GreenPepperServerException is thrown.
     * </p>
     *
     * @param name of the project
     * @return the Project for the specified name.
     */
    Project getByName(String name);

    /**
     * Retrieves all the registered Projects.
     * </p>
     *
     * @return all the registered Projects.
     */
    List<Project> getAll();

    /**
     * Saves the specified Project.
     * </p>
     *
     * @param name of the project
     * @return the new Project.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    Project create(String name)
			throws GreenPepperServerException;

	/**
	 * Removes the specified Project.
	 * </p>
	 *
	 * @param name of the project
	 * @throws com.greenpepper.server.GreenPepperServerException Exception
	 */
	void remove(String name)
			throws GreenPepperServerException;

	/**
	 * Updates the project.
	 *
	 * @param oldProjectName Name of the project to be updated
	 * @param project Project information to update
	 * @return newly updated project instance
	 * @throws com.greenpepper.server.GreenPepperServerException Exception
	 */
	Project update(String oldProjectName, Project project)
			throws GreenPepperServerException;
}
