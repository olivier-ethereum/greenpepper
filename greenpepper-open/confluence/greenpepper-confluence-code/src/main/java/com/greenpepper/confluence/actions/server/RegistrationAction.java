package com.greenpepper.confluence.actions.server;

import static com.greenpepper.confluence.utils.HtmlUtils.stringSetToTextArea;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.atlassian.user.User;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.ClasspathSet;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.server.license.GreenPepperLicenceException;
import com.greenpepper.util.StringUtil;

/**
 * Action for the <code>GreenPepper Server Properties</code>.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class RegistrationAction extends GreenPepperServerAction
{
    private Boolean readonly = Boolean.FALSE;

    private List<Runner> runners;
    private LinkedList<Project> projects;
    private List<SystemUnderTest> systemUnderTests;

    private SystemUnderTest selectedSut;
    private String selectedSutName;

    private String repositoryName;
    private String newProjectName;
    private String username;
    private String pwd;
    private String baseUrl;

    private String newSutName = "";
    private String newFixtureFactory;
    private String newFixtureFactoryArgs;
    private String newRunnerName = "";
	private Boolean selected = Boolean.FALSE;
	private String newProjectDependencyDescriptor;

	private String sutClasspath;
	
    private String fixtureClasspath;

	private boolean licenseValid = true;
	private boolean addMode;
    private boolean editMode;
    private boolean editPropertiesMode;
    private boolean editClasspathsMode;
    private boolean editFixturesMode;

	/**
	 * <p>doGetRegistration.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doGetRegistration()
    {


		if (!isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
			readonly = true;
			editMode = false;
			addMode = false;
			return SUCCESS;
		}

		try
        {
			/* BEGIN : COMMENT THIS FOR DEPLOYING TO GPS  */
			if (gpUtil.isCommercialLicense())
			{
                final User activeUser = getRemoteUser();

				if (activeUser == null)
				{
					addActionError(ConfluenceGreenPepper.ANONYMOUS_ACCESS_DENIED);
					readonly = true;
					editMode = false;
					addMode = false;
					return SUCCESS;
				}

				if (!gpUtil.getGreenPepperUserGroup().hasMembership(activeUser))
				{
					addActionError(ConfluenceGreenPepper.USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP);
					readonly = true;
					editMode = false;
					addMode = false;
					return SUCCESS;
				}
			}
			/* END : COMMENT THIS FOR DEPLOYING TO GPS  */

			licenseValid = true;

			if(StringUtil.isEmpty(projectName)) projectName	= getRegisteredRepository().getProject().getName();
            if(StringUtil.isEmpty(repositoryName)) repositoryName = getRegisteredRepository().getName();
            if(StringUtil.isEmpty(username)) username = getRegisteredRepository().getUsername();
            if(StringUtil.isEmpty(pwd)) pwd = getRegisteredRepository().getPassword();

			checkRepositoryBaseUrl();

			return doGetSystemUnderTests();
        }
        catch(GreenPepperLicenceException e)
        {
            addActionError(e.getId());
			licenseValid = false;
			readonly = true;
			editMode = false;
			addMode = false;
		}
        catch(GreenPepperServerException e)
        {
            if(editMode && StringUtil.isEmpty(projectName) && !isWithNewProject())
                projectName = getProjects().iterator().next().getName();

            readonly = true;
        }

        return editMode ? doGetSystemUnderTests(): SUCCESS;
    }

	/**
	 * <p>doRegister.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doRegister()
    {
        try
        {
			if (getUsername() != null)
			{
				gpUtil.verifyCredentials(getUsername(), getPwd());
			}

			String uid = gpUtil.getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + getSpaceKey();
            registeredRepository = Repository.newInstance(uid);
            registeredRepository.setProject(getProjectForRegistration());
            registeredRepository.setType(RepositoryType.newInstance("CONFLUENCE"));
            registeredRepository.setName(repositoryName);
            registeredRepository.setContentType(ContentType.TEST);
            registeredRepository.setBaseUrl(getBaseUrl());
            registeredRepository.setBaseRepositoryUrl(repositoryBaseUrl());
            registeredRepository.setBaseTestUrl(newTestUrl());
        	registeredRepository.setUsername(getUsername());
        	registeredRepository.setPassword(getPwd());
			registeredRepository.setMaxUsers(gpUtil.getNumberOfUserForGreenPepperUserGroup());

			getService().registerRepository(registeredRepository);
            projectName = isWithNewProject() ? newProjectName : projectName;
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
            editMode = true;
            readonly = true;
        }

        return doGetRegistration();
    }

    /**
     * <p>doUpdateRegistration.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doUpdateRegistration()
    {
        try
        {
			if (getUsername() != null)
			{
				gpUtil.verifyCredentials(getUsername(), getPwd());
			}

			String uid = gpUtil.getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + getSpaceKey();
            Repository newRepository = Repository.newInstance(uid);
        	newRepository.setProject(getProjectForRegistration());
        	newRepository.setType(RepositoryType.newInstance("CONFLUENCE"));
        	newRepository.setName(repositoryName);
        	newRepository.setContentType(ContentType.TEST);
        	newRepository.setBaseUrl(getBaseUrl());
        	newRepository.setBaseRepositoryUrl(repositoryBaseUrl());
        	newRepository.setBaseTestUrl(newTestUrl());
        	newRepository.setUsername(getUsername());
        	newRepository.setPassword(getPwd());
			newRepository.setMaxUsers(gpUtil.getNumberOfUserForGreenPepperUserGroup());

			getService().updateRepositoryRegistration(newRepository);
            projectName = isWithNewProject() ? newProjectName : projectName;
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
            editMode = true;
            readonly = true;
        }

        return doGetRegistration();
    }

    /**
     * <p>doGetSystemUnderTests.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doGetSystemUnderTests()
    {
        try
        {
            runners = getService().getAllRunners();
            if(runners.isEmpty())
                throw new GreenPepperServerException("greenpepper.suts.norunners", "No runners.");

            systemUnderTests = getService().getSystemUnderTestsOfProject(projectName);
            if(selectedSut == null)
            {
                for(SystemUnderTest sut : systemUnderTests)
                {
                    if(sut.getName().equals(selectedSutName))
                    {
                        selectedSut = sut;
                        return SUCCESS;
                    }
                    if(sut.isDefault())
                    {
                        selectedSut = sut;
                    }
                }

                if(selectedSut != null) selectedSutName = selectedSut.getName();
            }
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
            addMode = false;
        }

        return SUCCESS;
    }

    /**
     * <p>doAddSystemUnderTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doAddSystemUnderTest()
    {
        try
        {
            selectedSut = SystemUnderTest.newInstance(newSutName);
            selectedSut.setProject(Project.newInstance(projectName));

            selectedSut.setFixtureFactory(newFixtureFactory);
            selectedSut.setFixtureFactoryArgs(newFixtureFactoryArgs);
            selectedSut.setIsDefault(selected);
            selectedSut.setRunner(Runner.newInstance(newRunnerName));
			selectedSut.setProjectDependencyDescriptor(newProjectDependencyDescriptor);
			getService().createSystemUnderTest(selectedSut, getHomeRepository());
            successfullAction();
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetSystemUnderTests();
    }

    /**
     * <p>doUpdateSystemUnderTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doUpdateSystemUnderTest()
    {
        try
        {

            SystemUnderTest newSut = SystemUnderTest.newInstance(selectedSutName);
            newSut.setProject(Project.newInstance(projectName));
            newSut = getService().getSystemUnderTest(newSut, getHomeRepository());

            newSut.setName(newSutName);
            newSut.setFixtureFactory(newFixtureFactory);
            newSut.setFixtureFactoryArgs(newFixtureFactoryArgs);
            newSut.setRunner(Runner.newInstance(newRunnerName));
			newSut.setProjectDependencyDescriptor(newProjectDependencyDescriptor);

			getService().updateSystemUnderTest(selectedSutName, newSut, getHomeRepository());
            successfullAction();
            return doGetSystemUnderTests();
        }
        catch (GreenPepperServerException e)
        {
            try
            {
                runners = getService().getAllRunners();
                if(runners.isEmpty())
                    throw new GreenPepperServerException("greenpepper.suts.norunners", "No runners.");

                systemUnderTests = getService().getSystemUnderTestsOfProject(projectName);
                selectedSut = SystemUnderTest.newInstance(selectedSutName);
                selectedSut.setProject(Project.newInstance(projectName));
                selectedSut.setFixtureFactory(newFixtureFactory);
                selectedSut.setFixtureFactoryArgs(newFixtureFactoryArgs);
                selectedSut.setRunner(Runner.newInstance(newRunnerName));
				selectedSut.setProjectDependencyDescriptor(newProjectDependencyDescriptor);
			}
            catch (GreenPepperServerException e1)
            {
                addActionError(e.getId());
            }

            addActionError(e.getId());
        }

        return SUCCESS;
    }

    /**
     * <p>doRemoveSystemUnderTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doRemoveSystemUnderTest()
    {
        try
        {
            selectedSut = SystemUnderTest.newInstance(selectedSutName);
            selectedSut.setProject(Project.newInstance(projectName));
            getService().removeSystemUnderTest(selectedSut, getHomeRepository());
            selectedSutName = null;
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        selectedSut = null;
        return doGetSystemUnderTests();
    }

    /**
     * <p>doEditClasspath.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doEditClasspath()
    {
    	try
        {
            SystemUnderTest selectedSut = SystemUnderTest.newInstance(selectedSutName);
            selectedSut.setProject(Project.newInstance(projectName));
            selectedSut = getService().getSystemUnderTest(selectedSut, getHomeRepository());
            selectedSut.setSutClasspaths(ClasspathSet.parse(sutClasspath));
            getService().updateSystemUnderTest(selectedSutName, selectedSut, getHomeRepository());
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetSystemUnderTests();
    }

    /**
     * <p>getClasspaths.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getClasspaths() {
    	return selectedSut.getSutClasspaths();
    }

    /**
     * <p>getClasspathsAsTextAreaContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClasspathsAsTextAreaContent() {
    	return stringSetToTextArea(getClasspaths());
    }
    
    /**
     * <p>getClasspathTitle.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClasspathTitle() {
    	return getText("greenpepper.suts.classpath");
    }
    
    /**
     * <p>getFixtureClasspathTitle.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFixtureClasspathTitle() {
    	return getText("greenpepper.suts.fixture");
    }
    
    /**
     * <p>getFixtureClasspaths.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getFixtureClasspaths() {
    	return selectedSut.getFixtureClasspaths();
    }

    /**
     * <p>getFixtureClasspathsAsTextAreaContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFixtureClasspathsAsTextAreaContent() {
    	return stringSetToTextArea(getFixtureClasspaths());
    }

    /**
     * <p>doEditFixture.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doEditFixture()
    {
        try
        {
        	SystemUnderTest selectedSut = SystemUnderTest.newInstance(selectedSutName);
            selectedSut.setProject(Project.newInstance(projectName));
            selectedSut = getService().getSystemUnderTest(selectedSut, getHomeRepository());
            selectedSut.setFixtureClasspaths(ClasspathSet.parse(fixtureClasspath));
            getService().updateSystemUnderTest(selectedSutName, selectedSut, getHomeRepository());
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetSystemUnderTests();
    }

    /**
     * <p>doSetAsDefault.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doSetAsDefault()
    {
        try
        {
            SystemUnderTest sut = SystemUnderTest.newInstance(selectedSutName);
            sut.setProject(Project.newInstance(projectName));
            getService().setSystemUnderTestAsDefault(sut, getHomeRepository());
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetSystemUnderTests();
    }

    /** {@inheritDoc} */
    @Override
    public LinkedList<Project> getProjects()
    {
        if(projects != null) return projects;
        try
        {
            projects = new LinkedList<Project>(getService().getAllProjects());
            projects.addLast(Project.newInstance(projectCreateOption()));
            projectName = projectName == null ?  projects.iterator().next().getName() : projectName;

            return projects;
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return projects;
    }

    /**
     * <p>Getter for the field <code>selectedSut</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public SystemUnderTest getSelectedSut()
    {
        return selectedSut;
    }

    /**
     * <p>Getter for the field <code>repositoryName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRepositoryName()
    {
        return repositoryName;
    }

    /**
     * <p>Setter for the field <code>repositoryName</code>.</p>
     *
     * @param repositoryName a {@link java.lang.String} object.
     */
    public void setRepositoryName(String repositoryName)
    {
        this.repositoryName = repositoryName.trim();
    }

    /**
     * <p>getProjectName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getProjectName()
    {
        return projectName;
    }

    /** {@inheritDoc} */
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

	/**
	 * <p>Getter for the field <code>pwd</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPwd()
	{
		return pwd;
	}

	/**
	 * <p>Setter for the field <code>pwd</code>.</p>
	 *
	 * @param pwd a {@link java.lang.String} object.
	 */
	public void setPwd(String pwd)
	{
		this.pwd = StringUtil.toNullIfEmpty(pwd.trim());
	}

	/**
	 * <p>getEscapedPassword.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEscapedPassword() {
		return StringEscapeUtils.escapeHtml(getPwd());
	}

	/**
	 * <p>Getter for the field <code>username</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * <p>Setter for the field <code>username</code>.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 */
	public void setUsername(String username)
	{
		this.username = StringUtil.toNullIfEmpty(username.trim());
	}

    /**
     * <p>Getter for the field <code>newProjectName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewProjectName()
    {
        return newProjectName;
    }

    /**
     * <p>Setter for the field <code>newProjectName</code>.</p>
     *
     * @param newProjectName a {@link java.lang.String} object.
     */
    public void setNewProjectName(String newProjectName)
    {
        this.newProjectName = newProjectName.trim();
    }

    /**
     * <p>Getter for the field <code>newSutName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewSutName()
    {
        return newSutName;
    }

    /**
     * <p>Setter for the field <code>newSutName</code>.</p>
     *
     * @param newSutName a {@link java.lang.String} object.
     */
    public void setNewSutName(String newSutName)
    {
        this.newSutName = newSutName.trim();
    }

    /**
     * <p>Getter for the field <code>newFixtureFactory</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewFixtureFactory()
    {
        return newFixtureFactory;
    }

    /**
     * <p>Setter for the field <code>newFixtureFactory</code>.</p>
     *
     * @param fixtureFactory a {@link java.lang.String} object.
     */
    public void setNewFixtureFactory(String fixtureFactory)
    {
        this.newFixtureFactory = StringUtil.toNullIfEmpty(fixtureFactory.trim());
    }

    /**
     * <p>Getter for the field <code>newFixtureFactoryArgs</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewFixtureFactoryArgs()
    {
        return newFixtureFactoryArgs;
    }

    /**
     * <p>Setter for the field <code>newFixtureFactoryArgs</code>.</p>
     *
     * @param fixtureFactoryArgs a {@link java.lang.String} object.
     */
    public void setNewFixtureFactoryArgs(String fixtureFactoryArgs)
    {
        this.newFixtureFactoryArgs = StringUtil.toNullIfEmpty(fixtureFactoryArgs.trim());
    }

    /**
     * <p>Getter for the field <code>newRunnerName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewRunnerName()
    {
        return newRunnerName;
    }

    /**
     * <p>Setter for the field <code>newRunnerName</code>.</p>
     *
     * @param runnerName a {@link java.lang.String} object.
     */
    public void setNewRunnerName(String runnerName)
    {
        this.newRunnerName = runnerName.trim();
    }

    /**
     * <p>Getter for the field <code>sutClasspath</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSutClasspath()
    {
        return sutClasspath;
    }

    /**
     * <p>Setter for the field <code>sutClasspath</code>.</p>
     *
     * @param sutClasspath a {@link java.lang.String} object.
     */
    public void setSutClasspath(String sutClasspath)
    {
        this.sutClasspath = sutClasspath.trim();
    }

    /**
     * <p>Getter for the field <code>fixtureClasspath</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFixtureClasspath()
    {
        return fixtureClasspath;
    }

    /**
     * <p>Setter for the field <code>fixtureClasspath</code>.</p>
     *
     * @param fixtureClasspath a {@link java.lang.String} object.
     */
    public void setFixtureClasspath(String fixtureClasspath)
    {
        this.fixtureClasspath = fixtureClasspath.trim();
    }

	/**
	 * <p>Getter for the field <code>newProjectDependencyDescriptor</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNewProjectDependencyDescriptor()
	{
		return newProjectDependencyDescriptor;
	}

	/**
	 * <p>Setter for the field <code>newProjectDependencyDescriptor</code>.</p>
	 *
	 * @param newProjectDependencyDescriptor a {@link java.lang.String} object.
	 */
	public void setNewProjectDependencyDescriptor(String newProjectDependencyDescriptor)
	{
		this.newProjectDependencyDescriptor = StringUtil.toNullIfEmpty(newProjectDependencyDescriptor.trim());
	}

	/**
	 * <p>getDefault.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean getDefault()
    {
        return selected;
    }

    /**
     * <p>setDefault.</p>
     *
     * @param selected a {@link java.lang.Boolean} object.
     */
    public void setDefault(Boolean selected)
    {
        this.selected = selected;
    }

    /**
     * <p>Getter for the field <code>runners</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Runner> getRunners()
    {
        return runners;
    }

    /**
     * <p>Getter for the field <code>selectedSutName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSelectedSutName()
    {
        return selectedSutName;
    }

    /**
     * <p>Setter for the field <code>selectedSutName</code>.</p>
     *
     * @param selectedSutName a {@link java.lang.String} object.
     */
    public void setSelectedSutName(String selectedSutName)
    {
        this.selectedSutName = selectedSutName;
    }

	/**
	 * <p>isLicenseValid.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isLicenseValid()
	{
		return licenseValid;
	}

	/**
	 * <p>Getter for the field <code>readonly</code>.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getReadonly()
    {
        return readonly;
    }

    /**
     * <p>Setter for the field <code>readonly</code>.</p>
     *
     * @param readonly a boolean.
     */
    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
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
     * <p>isAddMode.</p>
     *
     * @return a boolean.
     */
    public boolean isAddMode()
    {
        return addMode;
    }

    /**
     * <p>Setter for the field <code>addMode</code>.</p>
     *
     * @param addMode a boolean.
     */
    public void setAddMode(boolean addMode)
    {
        this.addMode = addMode;
    }

    /**
     * <p>isEditClasspathsMode.</p>
     *
     * @return a boolean.
     */
    public boolean isEditClasspathsMode()
    {
        return editClasspathsMode;
    }

    /**
     * <p>Setter for the field <code>editClasspathsMode</code>.</p>
     *
     * @param editClasspathsMode a boolean.
     */
    public void setEditClasspathsMode(boolean editClasspathsMode)
    {
        this.editClasspathsMode = editClasspathsMode;
    }

    /**
     * <p>isEditFixturesMode.</p>
     *
     * @return a boolean.
     */
    public boolean isEditFixturesMode()
    {
        return editFixturesMode;
    }

    /**
     * <p>Setter for the field <code>editFixturesMode</code>.</p>
     *
     * @param editFixturesMode a boolean.
     */
    public void setEditFixturesMode(boolean editFixturesMode)
    {
        this.editFixturesMode = editFixturesMode;
    }

    /**
     * <p>isEditPropertiesMode.</p>
     *
     * @return a boolean.
     */
    public boolean isEditPropertiesMode()
    {
        return editPropertiesMode;
    }

    /**
     * <p>Setter for the field <code>editPropertiesMode</code>.</p>
     *
     * @param editPropertiesMode a boolean.
     */
    public void setEditPropertiesMode(boolean editPropertiesMode)
    {
        this.editPropertiesMode = editPropertiesMode;
    }

	/**
	 * <p>Getter for the field <code>systemUnderTests</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SystemUnderTest> getSystemUnderTests()
    {
        return systemUnderTests;
    }

    private Project getProjectForRegistration() throws GreenPepperServerException
    {
        if(isWithNewProject())
        {
            validateProjectName();
            return Project.newInstance(newProjectName);
        }

        return Project.newInstance(projectName);
    }

    private void validateProjectName() throws GreenPepperServerException
    {
        if(StringUtil.isBlank(newProjectName) || newProjectName.equals(getText("greenpepper.project.newname")))
        {
            throw new GreenPepperServerException("greenpepper.registration.invalidprojectname", "invalid name");
        }
    }

    private void successfullAction()
    {
        addMode = false;
        editPropertiesMode = false;
        editClasspathsMode = false;
        editFixturesMode = false;
		selectedSutName = newSutName;
        newSutName = "";
        newRunnerName = "";
        newFixtureFactory = null;
        newFixtureFactoryArgs = null;
		newProjectDependencyDescriptor = null;
	}

    private String getBaseUrl()
    {
        if(baseUrl != null) return baseUrl;
        baseUrl = gpUtil.getBaseUrl();
        return baseUrl;
    }

    private String repositoryBaseUrl()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUrl()).append("/display/").append(getSpaceKey());
        return sb.toString();
    }

    private String newTestUrl()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUrl()).append("/rpc/xmlrpc");
        sb.append("?handler=").append(getHandler());
        sb.append("#").append(getSpaceKey());
        return sb.toString();
    }

	private void checkRepositoryBaseUrl()
			throws GreenPepperServerException {

		if (!editMode && !addMode && !getRegisteredRepository().getBaseUrl().equals(getBaseUrl())) {
			addActionError(getText(ConfluenceGreenPepper.REPOSITORY_BASEURL_OUTOFSYNC,
								   new String[]{getRegisteredRepository().getBaseUrl(), getBaseUrl()}));
		}
	}
}
