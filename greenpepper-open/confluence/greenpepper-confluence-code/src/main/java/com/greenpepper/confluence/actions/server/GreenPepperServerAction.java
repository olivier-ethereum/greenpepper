package com.greenpepper.confluence.actions.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.atlassian.confluence.spaces.actions.AbstractSpaceAction;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.GreenPepperServerService;
import com.greenpepper.server.ServerPropertiesManager;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.rpc.RpcServerService;
import com.greenpepper.util.I18nUtil;

/**
 * <p>GreenPepperServerAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class GreenPepperServerAction extends AbstractSpaceAction
{    
    private static final String RESOURCE_BUNDLE = ConfigurationAction.class.getName();
    private final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
    private ResourceBundle resourceBundle;
    
    protected ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();
    private List<SystemUnderTest> systemUnderTests;
    
    protected String projectName;
    protected Repository registeredRepository;
    protected Repository homeRepository;
    private String spaceKey;
    
    private String url;
    private String handler = RpcServerService.SERVICE_HANDLER;
    private Boolean isRegistered;
	protected LinkedList<Project> projects;

	/**
	 * <p>getService.</p>
	 *
	 * @return a {@link com.greenpepper.server.GreenPepperServerService} object.
	 */
	protected GreenPepperServerService getService()
	{
		return gpUtil.getGPServerService();
	}

	/**
	 * <p>isPluginInstalledUnderWebInfLib.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPluginInstalledUnderWebInfLib() {
		return gpUtil.isPluginInstalledUnderWebInfLib();
	}

	/**
	 * <p>isServerSetupComplete.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isServerSetupComplete()
	{
		return gpUtil.isServerSetupComplete();
	}

	/**
	 * <p>isServerReady.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isServerReady()
	{
		return gpUtil.isServerReady();
	}

	/**
	 * <p>getIsServerReady.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getIsServerReady()
	{
		return isServerReady();
	}

	/**
	 * <p>Getter for the field <code>projectName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProjectName()
    {
        return projectName;
    }

    /**
     * <p>Setter for the field <code>projectName</code>.</p>
     *
     * @param projectName a {@link java.lang.String} object.
     */
    public void setProjectName(String projectName)
    {
        this.projectName = projectName.trim();
    }

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl()
    {
        if(url != null) return url;
        url = getNotNullProperty(ServerPropertiesManager.URL);
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(String url)
    {
        this.url = url.trim();
    }

    /**
     * <p>Getter for the field <code>handler</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHandler()
    {
        return handler;
    }

    /**
     * <p>Setter for the field <code>handler</code>.</p>
     *
     * @param handler a {@link java.lang.String} object.
     */
    public void setHandler(String handler)
    {
        this.handler = handler.trim();
    }
    
    /**
     * <p>getIdentifier.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIdentifier()
    {
        return key;
    }

    /**
     * <p>Getter for the field <code>registeredRepository</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Repository} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository getRegisteredRepository() throws GreenPepperServerException
    {
        if(registeredRepository != null)return registeredRepository;
        registeredRepository = getService().getRegisteredRepository(getHomeRepository());
        return registeredRepository;
    }

    /**
     * <p>Setter for the field <code>registeredRepository</code>.</p>
     *
     * @param registeredRepository a {@link com.greenpepper.server.domain.Repository} object.
     */
    public void setRegisteredRepository(Repository registeredRepository)
    {
        this.registeredRepository = registeredRepository;
    }
    
    /**
     * <p>Getter for the field <code>homeRepository</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Repository} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository getHomeRepository()
			throws GreenPepperServerException
	{
    	if(homeRepository != null) return homeRepository;
	    if(key == null)
	    {
	    	homeRepository = Repository.newInstance("UNKNOWN_UID");
	    }
	    else
	    {
	    	homeRepository = gpUtil.getHomeRepository(key);
	    }
	    
	    return homeRepository;
    }
    
    /**
     * <p>Getter for the field <code>systemUnderTests</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<SystemUnderTest> getSystemUnderTests()
    {
        try 
        {
            if(projectName == null)
            if(systemUnderTests != null) return systemUnderTests;
            systemUnderTests = gpUtil.getGPServerService().getSystemUnderTestsOfProject(projectName);
        }
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
        }
        
        return systemUnderTests;
    }
    
    /**
     * <p>isRegistered.</p>
     *
     * @return a boolean.
     */
    public boolean isRegistered()
    {
		if(isRegistered != null) return isRegistered;
        if(!isServerReady()) return false;
        
    	try 
    	{
			getRegisteredRepository();
			isRegistered =  true;
		} 
    	catch (GreenPepperServerException e) 
    	{
    		isRegistered =  false;
		}

		return isRegistered;
    }

	/**
	 * <p>getCanConnect.</p>
	 *
	 * @deprecated use {@link #isServerReady}
	 * @return a boolean.
	 */
	public boolean getCanConnect()
    {
		return isServerReady();
	}

    /**
     * <p>isWithNewProject.</p>
     *
     * @return a boolean.
     */
    public boolean isWithNewProject()
    {
        return projectName != null && projectName.equals(projectCreateOption());
    }
    
    /**
     * <p>getUID.</p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return a {@link java.lang.String} object.
     */
    public String getUID(SystemUnderTest sut)
    {
        return sut.getName().replaceAll(" ", "_");
    }
    
    /**
     * <p>projectCreateOption.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    protected String projectCreateOption()
    {
        return getText("greenpepper.registration.newproject");
    }
    
    private String getNotNullProperty(String key)
    {
		String value = gpUtil.getPageProperty(key, getIdentifier());
		return value == null ? "" : value;
	}

    /**
     * <p>Getter for the field <code>spaceKey</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSpaceKey()
    {
        if(spaceKey == null) spaceKey = key;
        return spaceKey;
    }

    /**
     * <p>Setter for the field <code>spaceKey</code>.</p>
     *
     * @param spaceKey a {@link java.lang.String} object.
     */
    public void setSpaceKey(String spaceKey)
    {
        key = spaceKey;
        this.spaceKey = spaceKey;
    }
    
	/**
	 * {@inheritDoc}
	 *
	 * Custom I18n. Based on WebWork i18n.
	 */
	@HtmlSafe
    public String getText(String key)
    {
		String text = super.getText(key);

		if (text.equals(key))
		{
			text = I18nUtil.getText(key, getResourceBundle());
		}

		return text;
    }

	/**
	 * <p>getText.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param args an array of {@link java.lang.Object} objects.
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getText(String key, Object[] args)
	{
		String text = super.getText(key, args);

		if (text.equals(key)) {
			text = I18nUtil.getText(key, getResourceBundle(), args);
		}

		return text;
	}

	private ResourceBundle getResourceBundle() {

		if (resourceBundle == null)
		{
			Locale locale = threadLocale.get();
			if (locale == null)
			{
				locale = getLocale();
				threadLocale.set(locale == null ? Locale.ENGLISH : locale);
			}

			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
		}

		return resourceBundle;
	}

	/**
	 * <p>Getter for the field <code>projects</code>.</p>
	 *
	 * @return a {@link java.util.LinkedList} object.
	 */
	public LinkedList<Project> getProjects() {
	    if(projects != null) return projects;
	    try
	    {
	        projects = new LinkedList<Project>(getService().getAllProjects());
	        projectName = projectName == null ?  projects.iterator().next().getName() : projectName;
	        
	        return projects;
	    }
	    catch (GreenPepperServerException e)
	    {
	        addActionError(e.getId());
	    }
	    
	    return projects;
	}
}
