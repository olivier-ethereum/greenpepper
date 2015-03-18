package com.greenpepper.confluence.actions.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceType;
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
 * @author JCHUET
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

	@SuppressWarnings("unchecked")
	public List<Space> getSpaces()
	{
		return gpUtil.getSpaceManager().getSpacesByType(SpaceType.GLOBAL);
	}
    
    public String getConfiguration()
    {
		if (isServerReady()) doGetRunners();
        return SUCCESS;
    }
    
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
    
    public String testConnection()
    {
		return SUCCESS;
    }
    
    public String updateConfiguration()
    {
		if(isServerReady()) doGetRunners();
        return SUCCESS;
    }
    
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

	public String verifyServerReady()
	{
		if (!isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
		}

		return SUCCESS;
	}

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
    
    public Set<String> getClasspaths() {
    	Set<String> classpaths = selectedRunner.getClasspaths();
       	return classpaths == null ? new HashSet<String>() : classpaths;
    }

    public String getClasspathsAsTextAreaContent() {
    	StringBuilder sb = new StringBuilder();
    	for (String classpath : getClasspaths()) {
    		sb.append(classpath);
    		sb.append("\r");
    	}
    	return sb.toString();
    }
    
    public String getClasspathTitle() {
    	return getText("greenpepper.runners.classpath");
    }
    
    public String getNewLicense()
    {
        return newLicense;
    }

    public void setNewLicense(String newLicense)
    {
        this.newLicense = newLicense;
    }

    public List<Runner> getRunners()
    {
        return runners;
    }
    
    public String getSelectedRunnerName()
    {
        return selectedRunnerName;
    }

    public void setSelectedRunnerName(String selectedRunnerName)
    {
        this.selectedRunnerName = selectedRunnerName.equals("none") ? null : selectedRunnerName;
    }

    public boolean isAddMode()
    {
        return addMode;
    }

    public void setAddMode(boolean addMode)
    {
        this.addMode = addMode;
    }

    public boolean isEditPropertiesMode()
    {
        return editPropertiesMode;
    }

    public void setEditPropertiesMode(boolean editPropertiesMode)
    {
        this.editPropertiesMode = editPropertiesMode;
    }

    public boolean isEditClasspathsMode()
    {
        return editClasspathsMode;
    }

    public void setEditClasspathsMode(boolean editClasspathsMode)
    {
        this.editClasspathsMode = editClasspathsMode;
    }

    public Runner getSelectedRunner()
    {
        return selectedRunner;
    }

    public String getClasspath()
    {
        return classpath;
    }

    public void setClasspath(String classpath)
    {
        this.classpath = classpath.trim();
    }

    public String getNewMainClass()
    {
        return newMainClass;
    }

    public void setNewMainClass(String newMainClass)
    {
        this.newMainClass = newMainClass.trim();
    }

    public String getNewRunnerName()
    {
        return newRunnerName;
    }

    public void setNewRunnerName(String newRunnerName)
    {
        this.newRunnerName = newRunnerName.trim();
    }

    public String getNewServerName()
    {
        return newServerName;
    }

    public void setNewServerName(String newServerName)
    {
        this.newServerName = newServerName.trim();
    }

    public String getNewServerPort()
    {
        return newServerPort;
    }

    public void setNewServerPort(String newServerPort)
    {
        this.newServerPort = newServerPort.trim();
    }

	public String getNewEnvType() 
	{
		return newEnvType;
	}

	public void setNewEnvType(String newEnvType)
	{
		this.newEnvType = newEnvType;
	}

    public void setRunners(List<Runner> runners)
    {
        this.runners = runners;
    }

	public List<EnvironmentType> getEnvTypes()
	{
		return envTypes;
	}

	public void setEnvTypes(List<EnvironmentType> envTypes)
	{
		this.envTypes = envTypes;
	}

    public String getNewCmdLineTemplate()
    {
        return newCmdLineTemplate;
    }

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

	public boolean isSecured()
	{
		return secured;
	}
	
	public void setSecured(boolean secured)
	{
		this.secured = secured;
	}
}
