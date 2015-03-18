package com.greenpepper.confluence.actions.server;

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

import static com.greenpepper.confluence.utils.HtmlUtils.stringSetToTextArea;

/**
 * Action for the <code>GreenPepper Server Properties</code>.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
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

	public String doGetRegistration()
    {
		if (!isPluginInstalledUnderWebInfLib())
		{
			addActionError(getText(ConfluenceGreenPepper.PLUGIN_NOT_INSTALLED_UNDER_WEBINFLIB,
								   new String[]{gpUtil.getWebInfLibDirectory()}));
			readonly = true;
			editMode = false;
			addMode = false;
			return SUCCESS;
		}

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

    public Set<String> getClasspaths() {
    	return selectedSut.getSutClasspaths();
    }

    public String getClasspathsAsTextAreaContent() {
    	return stringSetToTextArea(getClasspaths());
    }
    
    public String getClasspathTitle() {
    	return getText("greenpepper.suts.classpath");
    }
    
    public String getFixtureClasspathTitle() {
    	return getText("greenpepper.suts.fixture");
    }
    
    public Set<String> getFixtureClasspaths() {
    	return selectedSut.getFixtureClasspaths();
    }

    public String getFixtureClasspathsAsTextAreaContent() {
    	return stringSetToTextArea(getFixtureClasspaths());
    }

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

    @SuppressWarnings("unchecked")
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

    public SystemUnderTest getSelectedSut()
    {
        return selectedSut;
    }

    public String getRepositoryName()
    {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName)
    {
        this.repositoryName = repositoryName.trim();
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = StringUtil.toNullIfEmpty(pwd.trim());
	}

	public String getEscapedPassword() {
		return StringEscapeUtils.escapeHtml(getPwd());
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = StringUtil.toNullIfEmpty(username.trim());
	}

    public String getNewProjectName()
    {
        return newProjectName;
    }

    public void setNewProjectName(String newProjectName)
    {
        this.newProjectName = newProjectName.trim();
    }

    public String getNewSutName()
    {
        return newSutName;
    }

    public void setNewSutName(String newSutName)
    {
        this.newSutName = newSutName.trim();
    }

    public String getNewFixtureFactory()
    {
        return newFixtureFactory;
    }

    public void setNewFixtureFactory(String fixtureFactory)
    {
        this.newFixtureFactory = StringUtil.toNullIfEmpty(fixtureFactory.trim());
    }

    public String getNewFixtureFactoryArgs()
    {
        return newFixtureFactoryArgs;
    }

    public void setNewFixtureFactoryArgs(String fixtureFactoryArgs)
    {
        this.newFixtureFactoryArgs = StringUtil.toNullIfEmpty(fixtureFactoryArgs.trim());
    }

    public String getNewRunnerName()
    {
        return newRunnerName;
    }

    public void setNewRunnerName(String runnerName)
    {
        this.newRunnerName = runnerName.trim();
    }

    public String getSutClasspath()
    {
        return sutClasspath;
    }

    public void setSutClasspath(String sutClasspath)
    {
        this.sutClasspath = sutClasspath.trim();
    }

    public String getFixtureClasspath()
    {
        return fixtureClasspath;
    }

    public void setFixtureClasspath(String fixtureClasspath)
    {
        this.fixtureClasspath = fixtureClasspath.trim();
    }

	public String getNewProjectDependencyDescriptor()
	{
		return newProjectDependencyDescriptor;
	}

	public void setNewProjectDependencyDescriptor(String newProjectDependencyDescriptor)
	{
		this.newProjectDependencyDescriptor = StringUtil.toNullIfEmpty(newProjectDependencyDescriptor.trim());
	}

	public Boolean getDefault()
    {
        return selected;
    }

    public void setDefault(Boolean selected)
    {
        this.selected = selected;
    }

    public List<Runner> getRunners()
    {
        return runners;
    }

    public String getSelectedSutName()
    {
        return selectedSutName;
    }

    public void setSelectedSutName(String selectedSutName)
    {
        this.selectedSutName = selectedSutName;
    }

	public boolean isLicenseValid()
	{
		return licenseValid;
	}

	public boolean getReadonly()
    {
        return readonly;
    }

    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
    }

    public boolean isEditMode()
    {
        return editMode;
    }

    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }

    public boolean isAddMode()
    {
        return addMode;
    }

    public void setAddMode(boolean addMode)
    {
        this.addMode = addMode;
    }

    public boolean isEditClasspathsMode()
    {
        return editClasspathsMode;
    }

    public void setEditClasspathsMode(boolean editClasspathsMode)
    {
        this.editClasspathsMode = editClasspathsMode;
    }

    public boolean isEditFixturesMode()
    {
        return editFixturesMode;
    }

    public void setEditFixturesMode(boolean editFixturesMode)
    {
        this.editFixturesMode = editFixturesMode;
    }

    public boolean isEditPropertiesMode()
    {
        return editPropertiesMode;
    }

    public void setEditPropertiesMode(boolean editPropertiesMode)
    {
        this.editPropertiesMode = editPropertiesMode;
    }

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
