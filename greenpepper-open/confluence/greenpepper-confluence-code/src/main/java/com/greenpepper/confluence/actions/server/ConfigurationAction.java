package com.greenpepper.confluence.actions.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceType;
import com.atlassian.confluence.spaces.SpacesQuery;
import com.atlassian.confluence.spaces.SpacesQuery.Builder;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.ClasspathSet;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.license.LicenseBean;
import com.greenpepper.util.StringUtil;

/**
 * Action for the <code>GreenPepper Server Properties</code>.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ConfigurationAction extends GreenPepperServerAction
{  
    private static final String NONE_SELECTED = null;
    
    private List<Runner> runners;
    private List<EnvironmentType> envTypes;
    private Runner selectedRunner;
    private String selectedRunnerName;
    private String classpath;
    
    private String newRunnerName = "";
    private String newCmdLineTemplate = "";
    private String newMainClass;
    private String newServerName;
    private String newServerPort;
    private String newEnvType = "";

    private boolean secured;
    private boolean addMode;
    private boolean editPropertiesMode;
    private boolean editClasspathsMode;
    
    private String newLicense;
    private LicenseBean license;

	/**
	 * <p>getSpaces.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Space> getSpaces()
	{
        Builder newQueryBuilder = SpacesQuery.newQuery();
        newQueryBuilder.withSpaceType(SpaceType.GLOBAL);
        SpacesQuery globalSpaceQuery = newQueryBuilder.build();
        return gpUtil.getSpaceManager().getAllSpaces(globalSpaceQuery);
	}
    
    /**
     * <p>getConfiguration.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getConfiguration()
    {
		if (isServerReady()) doGetRunners();
        return SUCCESS;
    }
    
    /**
     * <p>uploadLicense.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String uploadLicense()
    {
        try
        {
            gpUtil.getGPServerService().uploadNewLicense(newLicense.replaceAll(" ", "+"));
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
        return SUCCESS;
    }
    
    /**
     * <p>testConnection.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String testConnection()
    {
		return SUCCESS;
    }
    
    /**
     * <p>updateConfiguration.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String updateConfiguration()
    {
		if(isServerReady()) doGetRunners();
        return SUCCESS;
    }
    
    /**
     * <p>Getter for the field <code>license</code>.</p>
     *
     * @return a {@link com.greenpepper.server.license.LicenseBean} object.
     */
    public LicenseBean getLicense()
    {
		try
        {
            if(license != null) return license;
            license = gpUtil.getGPServerService().license();
        }
        catch (GreenPepperServerException e)
        {
            license = new LicenseBean();
        }
        
        return license;
    }

	/**
	 * <p>verifyServerReady.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String verifyServerReady()
	{
		if (!isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
		}

		return SUCCESS;
	}

	/**
	 * <p>doGetRunners.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doGetRunners()
    {
		if (!isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
			return SUCCESS;
		}
		
		try
        {
        	envTypes = getService().getAllEnvironmentTypes();
            runners = getService().getAllRunners();
            if(!StringUtil.isEmpty(selectedRunnerName))
            {
                for(Runner runner : runners)
                {
                    if(runner.getName().equals(selectedRunnerName))
                    {
                        selectedRunner = runner;
                        return SUCCESS;
                    }
                }
            }

            editPropertiesMode = false;
            editClasspathsMode = false;
            selectedRunnerName = NONE_SELECTED;
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
        return SUCCESS;
    }
    
    /**
     * <p>doAddRunner.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doAddRunner()
    {
        try
        {     
            selectedRunner = Runner.newInstance(newRunnerName);
            selectedRunner.setCmdLineTemplate(newCmdLineTemplate);
            selectedRunner.setMainClass(newMainClass);
            selectedRunner.setServerName(newServerName);
            selectedRunner.setServerPort(newServerPort);
            selectedRunner.setEnvironmentType(EnvironmentType.newInstance(newEnvType));
            selectedRunner.setSecured(secured);
            
            getService().createRunner(selectedRunner);
            successfullAction();
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }
        
        return doGetRunners();
    }
    
    /**
     * <p>doUpdateRunner.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doUpdateRunner()
    {
        try
        {     
            selectedRunner = getService().getRunner(selectedRunnerName);
            selectedRunner.setName(newRunnerName);
            selectedRunner.setCmdLineTemplate(newCmdLineTemplate);
            selectedRunner.setMainClass(newMainClass);
            selectedRunner.setServerName(newServerName);
            selectedRunner.setServerPort(newServerPort);
            selectedRunner.setEnvironmentType(EnvironmentType.newInstance(newEnvType));
            selectedRunner.setSecured(secured);
            
            getService().updateRunner(selectedRunnerName, selectedRunner);
            successfullAction();
            
            return doGetRunners();
        }
        catch (GreenPepperServerException e)
        {
            try
            {
                runners = getService().getAllRunners();
                selectedRunner.setName(selectedRunnerName);
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
     * <p>doRemoveRunner.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String doRemoveRunner()
    {
        try
        {     
            getService().removeRunner(selectedRunnerName);
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetRunners();
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
    		selectedRunner = getService().getRunner(selectedRunnerName);
            selectedRunner.setClasspaths(ClasspathSet.parse(classpath));
            getService().updateRunner(selectedRunnerName, selectedRunner);
        }
        catch (GreenPepperServerException e)
        {
            addActionError(e.getId());
        }

        return doGetRunners();
    }
    
    /**
     * <p>getClasspaths.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getClasspaths() {
    	Set<String> classpaths = selectedRunner.getClasspaths();
       	return classpaths == null ? new HashSet<String>() : classpaths;
    }

    /**
     * <p>getClasspathsAsTextAreaContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClasspathsAsTextAreaContent() {
    	StringBuilder sb = new StringBuilder();
    	for (String classpath : getClasspaths()) {
    		sb.append(classpath);
    		sb.append("\r");
    	}
    	return sb.toString();
    }
    
    /**
     * <p>getClasspathTitle.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClasspathTitle() {
    	return getText("greenpepper.runners.classpath");
    }
    
    /**
     * <p>Getter for the field <code>newLicense</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewLicense()
    {
        return newLicense;
    }

    /**
     * <p>Setter for the field <code>newLicense</code>.</p>
     *
     * @param newLicense a {@link java.lang.String} object.
     */
    public void setNewLicense(String newLicense)
    {
        this.newLicense = newLicense;
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
     * <p>Getter for the field <code>selectedRunnerName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSelectedRunnerName()
    {
        return selectedRunnerName;
    }

    /**
     * <p>Setter for the field <code>selectedRunnerName</code>.</p>
     *
     * @param selectedRunnerName a {@link java.lang.String} object.
     */
    public void setSelectedRunnerName(String selectedRunnerName)
    {
        this.selectedRunnerName = selectedRunnerName.equals("none") ? null : selectedRunnerName;
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
     * <p>Getter for the field <code>selectedRunner</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Runner} object.
     */
    public Runner getSelectedRunner()
    {
        return selectedRunner;
    }

    /**
     * <p>Getter for the field <code>classpath</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getClasspath()
    {
        return classpath;
    }

    /**
     * <p>Setter for the field <code>classpath</code>.</p>
     *
     * @param classpath a {@link java.lang.String} object.
     */
    public void setClasspath(String classpath)
    {
        this.classpath = classpath.trim();
    }

    /**
     * <p>Getter for the field <code>newMainClass</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewMainClass()
    {
        return newMainClass;
    }

    /**
     * <p>Setter for the field <code>newMainClass</code>.</p>
     *
     * @param newMainClass a {@link java.lang.String} object.
     */
    public void setNewMainClass(String newMainClass)
    {
        this.newMainClass = newMainClass.trim();
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
     * @param newRunnerName a {@link java.lang.String} object.
     */
    public void setNewRunnerName(String newRunnerName)
    {
        this.newRunnerName = newRunnerName.trim();
    }

    /**
     * <p>Getter for the field <code>newServerName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewServerName()
    {
        return newServerName;
    }

    /**
     * <p>Setter for the field <code>newServerName</code>.</p>
     *
     * @param newServerName a {@link java.lang.String} object.
     */
    public void setNewServerName(String newServerName)
    {
        this.newServerName = newServerName.trim();
    }

    /**
     * <p>Getter for the field <code>newServerPort</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewServerPort()
    {
        return newServerPort;
    }

    /**
     * <p>Setter for the field <code>newServerPort</code>.</p>
     *
     * @param newServerPort a {@link java.lang.String} object.
     */
    public void setNewServerPort(String newServerPort)
    {
        this.newServerPort = newServerPort.trim();
    }

	/**
	 * <p>Getter for the field <code>newEnvType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNewEnvType() 
	{
		return newEnvType;
	}

	/**
	 * <p>Setter for the field <code>newEnvType</code>.</p>
	 *
	 * @param newEnvType a {@link java.lang.String} object.
	 */
	public void setNewEnvType(String newEnvType)
	{
		this.newEnvType = newEnvType;
	}

    /**
     * <p>Setter for the field <code>runners</code>.</p>
     *
     * @param runners a {@link java.util.List} object.
     */
    public void setRunners(List<Runner> runners)
    {
        this.runners = runners;
    }

	/**
	 * <p>Getter for the field <code>envTypes</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<EnvironmentType> getEnvTypes()
	{
		return envTypes;
	}

	/**
	 * <p>Setter for the field <code>envTypes</code>.</p>
	 *
	 * @param envTypes a {@link java.util.List} object.
	 */
	public void setEnvTypes(List<EnvironmentType> envTypes)
	{
		this.envTypes = envTypes;
	}

    /**
     * <p>Getter for the field <code>newCmdLineTemplate</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNewCmdLineTemplate()
    {
        return newCmdLineTemplate;
    }

    /**
     * <p>Setter for the field <code>newCmdLineTemplate</code>.</p>
     *
     * @param newCmdLineTemplate a {@link java.lang.String} object.
     */
    public void setNewCmdLineTemplate(String newCmdLineTemplate)
    {
        this.newCmdLineTemplate = newCmdLineTemplate.trim();
    }
    
    private void successfullAction()
    {
        addMode = false;
        editPropertiesMode = false;
        editClasspathsMode = false;
        secured = false;
        selectedRunnerName = newRunnerName;
        newRunnerName = "";
        newServerName = "";
        newServerPort = "";
        newEnvType = "";
        newCmdLineTemplate = "";
    }

	/**
	 * <p>isSecured.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSecured()
	{
		return secured;
	}
	
	/**
	 * <p>Setter for the field <code>secured</code>.</p>
	 *
	 * @param secured a boolean.
	 */
	public void setSecured(boolean secured)
	{
		this.secured = secured;
	}
}
