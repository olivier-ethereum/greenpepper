package com.greenpepper.confluence.actions.server;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

/**
 * <p>FileSystemRegistration class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class FileSystemRegistration extends GreenPepperServerAction
{

	private static RepositoryType FILE = RepositoryType.newInstance("FILE");
	private List<Repository> fileRepositories;
	private Repository newRepository;
	private String repositoryUid;
	private String newName;
	private String newBaseTestUrl;
	private String newProjectName;
	private boolean editMode;
	private LinkedList<Project> projects;

	/**
	 * <p>doGetFileSystemRegistration.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doGetFileSystemRegistration()
	{
		if (!isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
			return SUCCESS;
		}
        
		try
        {
			setFileRepositories(getService().getAllSpecificationRepositories());
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
		return SUCCESS;
	}

	/**
	 * <p>doAddFileSystem.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doAddFileSystem()
	{
        try
        {
			setFileRepositories(getService().getAllSpecificationRepositories());

			if(!pathAlreadyExists())
        	{
	            getNewRepository().setProject(Project.newInstance(newProjectName));
	            newRepository.setType(FILE);
	            newRepository.setName(newName);
	            newRepository.setContentType(ContentType.TEST);
	            newRepository.setBaseUrl(newBaseTestUrl);
	            newRepository.setBaseRepositoryUrl(newBaseTestUrl);
	            newRepository.setBaseTestUrl(newBaseTestUrl);
	            
	            getService().registerRepository(newRepository);
	            newRepository = null;
        	}
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
        return doGetFileSystemRegistration();
	}

	/**
	 * <p>doRemoveFileSystem.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doRemoveFileSystem()
	{
        try
        {
        	getService().removeRepository(repositoryUid);
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
		return doGetFileSystemRegistration();
	}
	
	/**
	 * <p>Getter for the field <code>fileRepositories</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Repository> getFileRepositories()
	{
		if(fileRepositories != null)
		{	
			try {
				setFileRepositories(getService().getAllSpecificationRepositories());
			} catch (GreenPepperServerException e) {
				addActionError(e.getId());
			}
		}
		return fileRepositories;
	}
	
	/**
	 * <p>Setter for the field <code>fileRepositories</code>.</p>
	 *
	 * @param repositories a {@link java.util.List} object.
	 */
	public void setFileRepositories(List<Repository> repositories)
	{
		fileRepositories = new ArrayList<Repository>();
		for(Repository repository : repositories)
			if(repository.getType().equals(FILE))
				fileRepositories.add(repository);
	}

    /**
     * <p>Getter for the field <code>repositoryUid</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRepositoryUid() 
    {
		return repositoryUid;
	}

	/**
	 * <p>Setter for the field <code>repositoryUid</code>.</p>
	 *
	 * @param repositoryUid a {@link java.lang.String} object.
	 */
	public void setRepositoryUid(String repositoryUid) 
	{
		this.repositoryUid = repositoryUid;
	}

	/**
	 * <p>isEditMode.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isEditMode()
	{
		return editMode;
	}

	/**
	 * <p>Setter for the field <code>editMode</code>.</p>
	 *
	 * @param editMode a boolean.
	 */
	public void setEditMode(boolean editMode) 
	{
		this.editMode = editMode;
	}

	/**
	 * <p>Getter for the field <code>newBaseTestUrl</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNewBaseTestUrl()
	{
		return newBaseTestUrl;
	}

	/**
	 * <p>Setter for the field <code>newBaseTestUrl</code>.</p>
	 *
	 * @param newBaseTestUrl a {@link java.lang.String} object.
	 */
	public void setNewBaseTestUrl(String newBaseTestUrl) 
	{
		newBaseTestUrl = newBaseTestUrl.trim();
		if(!newBaseTestUrl.endsWith("/"))
			newBaseTestUrl += "/";
		
		this.newBaseTestUrl = newBaseTestUrl;
	}

	/**
	 * <p>Getter for the field <code>newName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNewName()
	{
		return newName;
	}

	/**
	 * <p>Setter for the field <code>newName</code>.</p>
	 *
	 * @param newName a {@link java.lang.String} object.
	 */
	public void setNewName(String newName) 
	{
		this.newName = newName.trim();
	}

	/**
	 * <p>getProjectName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProjectName()
	{
		return newProjectName;
	}

	/** {@inheritDoc} */
	public void setProjectName(String projectName)
	{
		this.newProjectName = projectName;
	}

	/**
	 * <p>Getter for the field <code>newRepository</code>.</p>
	 *
	 * @return a {@link com.greenpepper.server.domain.Repository} object.
	 */
	public Repository getNewRepository()
	{
		if(newRepository != null) return newRepository;
    	String uid = gpUtil.getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + getProjectName() + "-F"+fileRepositories.size();
		newRepository = Repository.newInstance(uid);
		return newRepository;
	}
	
	private boolean pathAlreadyExists() throws GreenPepperServerException
	{
		if(fileRepositories == null)
		{
			setFileRepositories(getService().getAllSpecificationRepositories());
		}
		
		for(Repository repo : fileRepositories)
		{
			if(repo.getBaseTestUrl().equals(newBaseTestUrl))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * <p>Getter for the field <code>projects</code>.</p>
	 *
	 * @return a {@link java.util.LinkedList} object.
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Project> getProjects()
	{
		if(projects != null) return projects;

		try
		{
			projects = new LinkedList<Project>(getService().getAllProjects());
		}
		catch (GreenPepperServerException e)
		{
			addActionError(e.getId());
		}

		return projects;
	}

}
