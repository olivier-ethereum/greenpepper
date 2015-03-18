package com.greenpepper.confluence.velocity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;

import com.atlassian.confluence.content.render.xhtml.DefaultConversionContext;
import com.atlassian.confluence.content.render.xhtml.DefaultRenderer;
import com.atlassian.confluence.content.render.xhtml.Renderer;
import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.core.ContentEntityManager;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.core.ContentPermissionManager;
import com.atlassian.confluence.core.ContentPropertyManager;
import com.atlassian.confluence.core.DateFormatter;
import com.atlassian.confluence.core.FormatSettingsManager;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.rpc.auth.TokenAuthenticationManager;
import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.confluence.setup.BootstrapManager;
import com.atlassian.confluence.setup.BootstrapUtils;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserPreferences;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.renderer.WikiStyleRenderer;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.User;
import com.greenpepper.confluence.GreenPepperServerConfiguration;
import com.greenpepper.confluence.GreenPepperServerConfigurationActivator;
import com.greenpepper.confluence.GreenPepperUserGroup;
import com.greenpepper.confluence.actions.SpecificationAction;
import com.greenpepper.confluence.utils.ConfluenceVersion;
import com.greenpepper.report.XmlReport;
import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.GreenPepperServerService;
import com.greenpepper.server.ServerPropertiesManager;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.util.I18nUtil;
import com.greenpepper.util.Period;
import com.greenpepper.util.StringUtil;
import com.opensymphony.webwork.ServletActionContext;

public class ConfluenceGreenPepper
{
    public static final String EXECUTION_KEY = "greenpeeper.executionKey";
    public static final String EXECUTE_CHILDREN = "greenpeeper.executeChildren";
    public static final String IMPLEMENTED_VERSION = "greenpeeper.implementedversion";
    public static final String PREVIOUS_IMPLEMENTED_VERSION = "greenpeeper.previous.implementedversion";
    public static final String NEVER_IMPLEMENTED = "greenpepper.page.neverimplemented";
	public static final String SERVER_NOCONFIGURATION = "greenpepper.server.noconfiguration";
	public static final String ANONYMOUS_ACCESS_DENIED = "greenpepper.anonymous.accessdenied";
	public static final String USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP = "greenpepper.notmemberof.greenpepperusers.group";
	public static final String PLUGIN_NOT_INSTALLED_UNDER_WEBINFLIB = "greenpepper.server.plugin.notinstalledunderwebinflib";
	public static final String REPOSITORY_BASEURL_OUTOFSYNC = "greenpepper.server.repourloutofsync";

	private static Logger log = Logger.getLogger(ConfluenceGreenPepper.class);
    private static final String RESOURCE_BUNDLE = SpecificationAction.class.getName();
	private static final int CRITICAL_PERIOD = 29;

	private GreenPepperServerService service;
	private GreenPepperServerConfigurationActivator configurationActivator;
	private TokenAuthenticationManager tokenManager;
    private PlatformTransactionManager transactionManager;
    private SettingsManager settingsManager;
    private BootstrapManager bootstrapManager;
    private ContentPropertyManager contentPropertyManager;
    private ContentPermissionManager contentPermissionManager;
    private ContentEntityManager contentEntityManager;
    private WikiStyleRenderer wikiStyleRenderer;
    private PageManager pageManager;
    private SpaceManager spaceManager;
    private SpacePermissionManager spacePermissionManager;
    private LabelManager labelManager;
    private UserAccessor userAccessor;
	private FormatSettingsManager formatSettingsManager;
	private GreenPepperUserGroup gpUserGroup;
	private XhtmlContent xhtmlUtils;
	private Renderer viewRenderer;

	private final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
    private ResourceBundle resourceBundle;

	public String getVersion()
	{
		return GreenPepperServer.VERSION;
	}

	public String getVersionDate()
	{
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		return df.format(GreenPepperServer.versionDate());
	}

	public ConfluenceVersion getConfluenceVersion()
	{
		return ConfluenceVersion.getCurrentVersion();
	}

	public boolean isConfluenceVersion28()
	{
		return getConfluenceVersion().equals(ConfluenceVersion.V28X);
	}

	public boolean isConfluenceVersion29()
	{
		return getConfluenceVersion().equals(ConfluenceVersion.V29X);
	}

	public boolean isConfluenceMajorVersionLessThan_2_6()
	{
		return getConfluenceVersion().compareTo(ConfluenceVersion.V26X) < 0;
	}

	public boolean isConfluenceMajorVersionLessThan_2_8()
	{
		return getConfluenceVersion().compareTo(ConfluenceVersion.V28X) < 0;
	}

	public boolean isConfluenceMajorVersionGreaterOrEqualThan_2_8()
	{
		return getConfluenceVersion().compareTo(ConfluenceVersion.V28X) >= 0;
	}

	/**
     * Custom I18n. Based on WebWork i18n.
     * @param key
     * @return the i18nzed message. If none found key is returned.
     */
	@HtmlSafe
    public String getText(String key)
    {
		return I18nUtil.getText(key, getResourceBundle());
    }

	@HtmlSafe
	public String getText(String key, Object... arguments)
	{
		return I18nUtil.getText(key, getResourceBundle(), arguments);
	}

	private ResourceBundle getResourceBundle()
	{
		if (resourceBundle == null)
		{
			Locale locale = threadLocale.get();
			if (locale == null)
			{
				locale = new ConfluenceActionSupport().getLocale();
				threadLocale.set(locale == null ? Locale.ENGLISH : locale);
			}

			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
		}

		return resourceBundle;
	}

    /**
     * Retrieves the home repository of the confluence space.
     * </p>
     * @param spaceKey
     * @return the home repository of the confluence space.
	 * @throws GreenPepperServerException
     */
    public Repository getHomeRepository(String spaceKey)
			throws GreenPepperServerException
	{
        String uid = getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + spaceKey;
        Repository repository = Repository.newInstance(uid);
		repository.setMaxUsers(getNumberOfUserForGreenPepperUserGroup());
		return repository;
    }

	/**
	 * Returns the number of user associated to the 'greenpepper-users' group.
	 *
	 * @return number of user for the group is the license type is 'Commercial', 1 otherwise
	 * @throws GreenPepperServerException
	 */
	public int getNumberOfUserForGreenPepperUserGroup()
			throws GreenPepperServerException
	{
		if (isCommercialLicense())
		{
			return getGreenPepperUserGroup().getNumberOfUserForGroup();
		}
		else
		{
			return 1;
		}
	}

	/**
     * Returns a message if an exception occures.
     * <p/>
     * @param spaceKey Space Key
     * @return a message if an exception occures.
     * @throws GreenPepperServerException
     */
    public String enable(String spaceKey) throws GreenPepperServerException
    {
        try
		{
			if (!isServerReady())
			{
				return getText(SERVER_NOCONFIGURATION);
			}

			/* BEGIN : COMMENT THIS FOR DEPLOYING TO GPS  */
			if (isCommercialLicense())
			{
				final User activeUser = getRemoteUser();

				if (activeUser == null)
				{
					return getText(ANONYMOUS_ACCESS_DENIED);
				}

				if (!getGreenPepperUserGroup().hasMembership(activeUser))
				{
					return getText(USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP);
				}
			}
			/* END : COMMENT THIS FOR DEPLOYING TO GPS  */

			Repository repository = getHomeRepository(spaceKey);
			getGPServerService().getRegisteredRepository(repository);
			return null;
		}
        catch (GreenPepperServerException e)
        {
            log.info(e.getMessage());
            return getText(e.getId());
        }
    }

	/**
	 * Indicates if the current license type is 'Commercial'.
	 *
	 * @return true if current license type is 'Commercial', false otherwise
	 * @throws GreenPepperServerException
	 */
	public boolean isCommercialLicense()
			throws GreenPepperServerException
	{
		return getGPServerService().isCommercialLicense();
	}

	/**
     * Retrieves the specification
     * <p/>
     * @param page
     * @return the specification.
     * @throws GreenPepperServerException
     */
    public Specification getSpecification(Page page) throws GreenPepperServerException
    {
        return getSpecification(page.getSpaceKey(), page.getTitle());
    }

    /**
     * Retrieves the specification
     * <p/>
     * @param spaceKey Space Key
     * @param pageTitle String pageTitle
     * @return the specification.
     * @throws GreenPepperServerException
     */
    public Specification getSpecification(String spaceKey, String pageTitle) throws GreenPepperServerException
    {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        return getGPServerService().getSpecification(specification);
    }

    /**
     * True if the specification exists
     * <p/>
     * @param page
     * @return  if the specification exists
     */
    public boolean isExecutable(Page page)
    {
		try
		{
			return getSpecification(page) != null;
		}
		catch (GreenPepperServerException e)
		{
			return false;
		}
	}

    /**
     * Get the repositories from the GreenPepper Server wich are requirements dedicated
     * <p>
     * @param Space Key
     * @return List of requirement repositories
     * @throws GreenPepperServerException
     */
    public List<Repository> getRepositories(String spaceKey) throws GreenPepperServerException
    {
        Repository repository = getHomeRepository(spaceKey);
        List<Repository> repositories = getGPServerService().getRequirementRepositoriesOfAssociatedProject(repository.getUid());
        return repositories;
    }

    /**
     * Get the systems under test associated with the space specified
     * <p>
     * @param Space Key
     * @return List of systems under test
     * @throws GreenPepperServerException
     */
    public List<SystemUnderTest> getSystemsUnderTests(String spaceKey) throws GreenPepperServerException
    {
        Repository repository = getHomeRepository(spaceKey);
        return getGPServerService().getSystemUnderTestsOfAssociatedProject(repository.getUid());
    }

    /**
     * Get the systems under test list associated with the page specified
     * <p>
     * @param Page
     * @return the systems under test list associated with the page specified
     * @throws GreenPepperServerException
     */
    public Set<SystemUnderTest> getPageSystemsUnderTests(Page page) throws GreenPepperServerException
    {
        return getPageSystemsUnderTests(page.getSpaceKey(), page.getTitle());
    }

    /**
     * Get the systems under test list associated with the page specified
     * <p>
     * @param spaceKey
     * @param pageTitle
     * @return the systems under test list associated with the page specified
     * @throws GreenPepperServerException
     */
    public Set<SystemUnderTest> getPageSystemsUnderTests(String spaceKey, String pageTitle) throws GreenPepperServerException
    {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        return getGPServerService().getSpecification(specification).getTargetedSystemUnderTests();
    }

    /**
     * Get the References with the specified page.
     * <p>
     * @param Page page
     * @return List of References
     * @throws GreenPepperServerException
     */
    public List<Reference> getReferences(Page page) throws GreenPepperServerException
    {
        return getReferences(page.getSpaceKey() , page.getTitle());
    }

    /**
     * Get the References with the specified page and space.
     * <p>
     * @param Space Key
     * @param Page Title
     * @return List of References
     * @throws GreenPepperServerException
     */
    public List<Reference> getReferences(String spaceKey, String pageTitle) throws GreenPepperServerException
    {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        List<Reference> references = getGPServerService().getSpecificationReferences(specification);
		return getUniqueReferences(references);
	}

    public String getPageContent(Page currentPage, Boolean implementedVersion) throws GreenPepperServerException {
    	AbstractPage page = currentPage;
    	if (implementedVersion) {
    		page =  getImplementedPage(currentPage);
    	}
    	
    	return page.getBodyAsString();
    }
	/**
     * Retrieves the body of a page in HTML rendering.
     * @param page
     * @return the body of a page in HTML rendering.
     */
    public String getPageContent(Page page)
    {
    	try {
			return getPageContent(page, false);
		} catch (GreenPepperServerException e) {
			return e.getMessage();
		}
    }

    /**
     * Retrieves from the page propeties the selectedSystemUnderTestInfo.
     * If none registered the default seeds execution will be saved and returned.
     * @param spaceKey
     * @param pageTitle
     * @return the selectedSystemUnderTestInfo.
     */
    public String getSelectedSystemUnderTestInfo(String spaceKey, String pageTitle)
    {
        return getSelectedSystemUnderTestInfo(pageManager.getPage(spaceKey, pageTitle));
    }

    /**
     * Retrieves from the page propeties the selectedSystemUnderTestInfo.
     * If none registered the default sut will be saved and returned.
     * The key must correspond to an excisting SystemUnderTest else the default one will be
     * saved and returned.
     * @param page
     * @return the selectedSystemUnderTestInfo.
     */
    public String getSelectedSystemUnderTestInfo(Page page)
    {
        SystemUnderTest selectedSut = getSavedSelectedSystemUnderTest(page);
        SystemUnderTest defaultSut = null;

        try
        {
            Set<SystemUnderTest> suts = getPageSystemsUnderTests(page.getSpaceKey(), page.getTitle());
            for(SystemUnderTest sut : suts)
            {
                if(selectedSut != null && selectedSut.equalsTo(sut))
                {
                    // enougth said return the key now !
                    return buildSelectedSystemUnderTestInfo(selectedSut);
                }
                if(sut.isDefault())
                {
                    defaultSut = sut;
                }
            }

            // else if no default pick first.
            if(defaultSut == null && !suts.isEmpty())
            {
                defaultSut = suts.iterator().next();
            }
        }
        catch (GreenPepperServerException e) {}

        String key = buildSelectedSystemUnderTestInfo(defaultSut);
        saveSelectedSystemUnderTestInfo(page, key);
        return key;
    }

    public SystemUnderTest getSelectedSystemUnderTest(Page page)
    {
        return buildSelectedSystemUnderTest(getSelectedSystemUnderTestInfo(page));
    }

    /**
     * Saves the execution key into the page properties.
     * @param page
     * @param value
     */
    public void saveSelectedSystemUnderTestInfo(Page page, String value)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, EXECUTION_KEY, value);
    }
    
    /**
     * Sets the implemented version to the previous implemented version.
     * @param page
     */
    public void revertImplementation(Page page)
    {
    	Integer previousImplementedVersion = getPreviousImplementedVersion(page);
    	if(previousImplementedVersion != null)
    	{
	        saveImplementedVersion(page, previousImplementedVersion);
	        savePreviousImplementedVersion(page, null);
    	}
    }

    /**
     * Retrieves the previous implemented version of the specification.
     * @param page
     * @return the previous implemented version of the specification.
     */
    public Integer getPreviousImplementedVersion(Page page)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, PREVIOUS_IMPLEMENTED_VERSION);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * Saves the sprecified version as the Previous implemented version
     * @param page
     * @param version
     */
    public void savePreviousImplementedVersion(Page page, Integer version)
    {
    	String value = version != null ? String.valueOf(version) : null;
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, PREVIOUS_IMPLEMENTED_VERSION, value);
    }
    
    /**
     * Verifies if the specification can be Implemented.
     * @param page
     * @return true if the specification can be Implemented.
     */
    public boolean canBeImplemented(Page page)
    {
    	Integer implementedVersion = getImplementedVersion(page);
    	return  implementedVersion == null || page.getVersion() != implementedVersion;
    }

    /**
     * Retrieves the implemented version of the specification.
     * @param page
     * @return the implemented version of the specification.
     */
    public Integer getImplementedVersion(Page page)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, IMPLEMENTED_VERSION);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * Saves the sprecified version as the Iimplemented version
     * @param page
     * @param version
     */
    public void saveImplementedVersion(Page page, Integer version)
    {
    	Integer previousImplementedVersion = getImplementedVersion(page);
    	if(previousImplementedVersion != null && version != null && previousImplementedVersion == version)return;
    	
    	if(previousImplementedVersion != null)
    		savePreviousImplementedVersion(page, previousImplementedVersion);

    	String value = version != null ? String.valueOf(version) : null;
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, IMPLEMENTED_VERSION, value);
    }
    
    /**
     * Retrieves the content of the specification at the implemented version.
     * @param page
     * @return the content of the specification at the implemented version.
     * @throws GreenPepperServerException
     */
    public AbstractPage getImplementedPage(Page page) throws GreenPepperServerException
    {
    	Integer version = getImplementedVersion(page);
    	if(version == null)
    		throw new GreenPepperServerException(NEVER_IMPLEMENTED, "Never Implemented");
    	
    	return getPageManager().getPageByVersion(page, version);
    }
    
    /**
     * Verifies if the Specification has stayed to long in the WORKING state.
     * @param page
     * @return true if the Specification has stayed to long in the WORKING state.
     */
    public boolean isImplementationDue(Page page)
    {
    	int version = page.getVersion();
    	
    	Integer implementedVersion = getImplementedVersion(page);
    	if(implementedVersion != null)
    		version = page.getVersion() == implementedVersion ? implementedVersion : implementedVersion + 1;

		Date date = getPageManager().getPageByVersion(page, version).getLastModificationDate();
		Period period = Period.fromTo(date, new Date(System.currentTimeMillis()));
		return period.daysCount() > CRITICAL_PERIOD;
    }

    /**
     * Retrieves from the page propeties the Execute childs boolean.
     * If none registered false is returned.
     * @param page
     * @return the Execute childs boolean.
     */
    public boolean getExecuteChildren(Page page)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, EXECUTE_CHILDREN);
        return value == null ? false : Boolean.valueOf(value);
    }

    public void saveExecuteChildren(Page page, Boolean doExecuteChildren)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, ConfluenceGreenPepper.EXECUTE_CHILDREN,
													  doExecuteChildren != null ? String.valueOf(doExecuteChildren) : null);
    }

    /**
     * Verifies if the the selectedSystemUnderTestInfo matches the specified key
     * </p>
     * @param selectedSystemUnderTestInfo
     * @param key
     * @return true if the the selectedSystemUnderTestInfo matches the specified key.
     */
    public boolean isSelected(String selectedSystemUnderTestInfo, String key)
    {
        return selectedSystemUnderTestInfo != null ? selectedSystemUnderTestInfo.equals(key) : false;
    }

    public String getBaseUrl()
    {
    	// DEPRECATION WARNING we should use "Settings.getBaseUrl()" instead
        // Have to wait until we do not support version under confluence 2.3
    	//return getBootstrapManager().getBaseUrl();
		return getSettingsManager().getGlobalSettings().getBaseUrl();
	}

	public String getEncoding() {
		return getSettingsManager().getGlobalSettings().getDefaultEncoding();
	}

    public String getPageUrl(Page page)
    {
        return getBaseUrl() + page.getUrlPath();
    }

    public boolean canEdit(Page page)
    {
        List<String> permTypes = new ArrayList<String>();
        permTypes.add(SpacePermission.CREATEEDIT_PAGE_PERMISSION);
        return getSpacePermissionManager().hasPermissionForSpace(getRemoteUser(), permTypes, page.getSpace());
    }

    public boolean canView(Page page)
    {
        return canView(page.getSpace());
    }

    public boolean canView(Space space)
    {
        List<String> permTypes = new ArrayList<String>();
        permTypes.add(SpacePermission.VIEWSPACE_PERMISSION);
        return getSpacePermissionManager().hasPermissionForSpace(getRemoteUser(), permTypes, space);
    }

    public String getHeader()
    {
        return "/templates/greenpepper/confluence/themes/greenpepper-header.vm";
    }

    public String getBody()
    {
        return "templates/greenpepper/confluence/themes/greenpepper-body.vm";
    }

    public boolean isInSutList(SystemUnderTest sut, Collection<SystemUnderTest> sutList)
    {
        for(SystemUnderTest aSut : sutList)
        {
            if(aSut.equalsTo(sut))
            {
                return true;
            }
        }

        return false;
    }

    public static String clean(String text)
    {
        if(text == null) return "";

        text = text.trim();
        text = text.replace("\"", "\\\"");
        text = text.replace("\'", "\\\'");
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        return text;
    }

    public User getRemoteUser()
    {
        HttpServletRequest request = ServletActionContext.getRequest();
		
		if (request != null)
        {
            String remoteUserName = request.getRemoteUser();
			
			if (remoteUserName != null)
			{
				return getUserAccessor().getUser(remoteUserName);
			}
		}
		
		return AuthenticatedUserThreadLocal.getUser();
    }

	public String getPageProperty(String key, String identifier)
	{
		Space space = getSpaceManager().getSpace(identifier);
		if(space == null) return null;

		ContentEntityObject entityObject = getContentEntityManager().getById(space.getHomePage().getId());
		return getContentPropertyManager().getStringProperty(entityObject, ServerPropertiesManager.SEQUENCE+key);
	}

	public void setPageProperty(String key, String value, String identifier)
	{
		Space space = getSpaceManager().getSpace(identifier);
		ContentEntityObject entityObject = getContentEntityManager().getById(space.getHomePage().getId());
		getContentPropertyManager().setStringProperty(entityObject, ServerPropertiesManager.SEQUENCE+key, value);
	}

	public GreenPepperServerConfiguration getGPServerConfiguration()
	{
		return getGPServerConfigurationActivator().getConfiguration();
	}

	public void storeGPServerConfiguration(GreenPepperServerConfiguration configuration)
	{
		getGPServerConfigurationActivator().storeConfiguration(configuration);
	}

	public boolean isServerSetupComplete()
	{
		return getGPServerConfiguration().isSetupComplete();
	}

	public boolean isServerReady()
	{
		return getGPServerConfigurationActivator().isReady();
	}

	public void verifyCredentials(String username, String password)
			throws GreenPepperServerException
	{
		if (username != null && !isCredentialsValid(username, password))
		{
			throw new GreenPepperServerException("greenpepper.confluence.badcredentials",
												 "The username and password are incorrect.");
		}
	}

	public boolean isCredentialsValid(String username, String password)
	{
		try
		{
			String token = getTokenAuthenticationManager().login(StringUtil.toEmptyIfNull(username),
																 StringUtil.toEmptyIfNull(password));
			getTokenAuthenticationManager().logout(token);

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public DateFormatter getUserPreferencesDateFormatter()
	{
		ConfluenceUserPreferences preferences = getUserAccessor().getConfluenceUserPreferences(getRemoteUser());

		return preferences.getDateFormatter(getFormatSettingsManager());
	}


	/*************************************************************************************************/
    /**************************   Access to container services   *************************************/
    /*************************************************************************************************/

    public GreenPepperServerService getGPServerService()
    {
        if (service != null){return service;}
        service = (GreenPepperServerService) ContainerManager.getComponent("greenPepperServerService");
        return service;
    }

    public SettingsManager getSettingsManager()
    {
        if (settingsManager != null){return settingsManager;}
        settingsManager = (SettingsManager) ContainerManager.getComponent("settingsManager");
        return settingsManager;
    }

    public BootstrapManager getBootstrapManager()
    {
        if (bootstrapManager != null){return bootstrapManager;}
        bootstrapManager = BootstrapUtils.getBootstrapManager();
        return bootstrapManager;
    }

    public ContentPermissionManager getContentPermissionManager()
    {
        if (contentPermissionManager != null){return contentPermissionManager;}
        contentPermissionManager = (ContentPermissionManager) ContainerManager.getComponent("contentPermissionManager");
        return contentPermissionManager;
    }

    public ContentPropertyManager getContentPropertyManager()
    {
        if (contentPropertyManager != null){return contentPropertyManager;}
        contentPropertyManager = (ContentPropertyManager) ContainerManager.getComponent("contentPropertyManager");
        return contentPropertyManager;
    }

    public ContentEntityManager getContentEntityManager()
    {
        if (contentEntityManager != null){return contentEntityManager;}
        contentEntityManager = (ContentEntityManager) ContainerManager.getComponent("contentEntityManager");
        return contentEntityManager;
    }

    public WikiStyleRenderer getWikiStyleRenderer()
    {
        if (wikiStyleRenderer != null){return wikiStyleRenderer;}
        wikiStyleRenderer = (WikiStyleRenderer) ContainerManager.getComponent("wikiStyleRenderer");
        return wikiStyleRenderer;
    }

    public Renderer getViewRenderer()
    {
        if (viewRenderer != null){return viewRenderer;}
        viewRenderer = (DefaultRenderer) ContainerManager.getComponent("viewRenderer");
        return viewRenderer;
    }

    public PageManager getPageManager()
    {
        if (pageManager != null){return pageManager;}
        pageManager = (PageManager) ContainerManager.getComponent("pageManager");
        return pageManager;
    }

    public SpaceManager getSpaceManager()
    {
        if (spaceManager != null){return spaceManager;}
        spaceManager = (SpaceManager) ContainerManager.getComponent("spaceManager");
		return spaceManager;
    }

    public SpacePermissionManager getSpacePermissionManager()
    {
        if (spacePermissionManager != null){return spacePermissionManager;}
        spacePermissionManager = (SpacePermissionManager) ContainerManager.getComponent("spacePermissionManager");
		return spacePermissionManager;
    }

	public LabelManager getLabelManager()
    {
        if (labelManager != null){return labelManager;}
        labelManager = (LabelManager) ContainerManager.getComponent("labelManager");
        return labelManager;
    }

    public TokenAuthenticationManager getTokenAuthenticationManager()
    {
        if (tokenManager != null){return tokenManager;}
        tokenManager = (TokenAuthenticationManager) ContainerManager.getComponent("tokenAuthenticationManager");
        return tokenManager;
    }

    public PlatformTransactionManager getPlatformTransactionManager()
    {
        if (transactionManager != null){return transactionManager;}
        transactionManager = (PlatformTransactionManager) ContainerManager.getComponent("transactionManager");
        return transactionManager;
    }

    public UserAccessor getUserAccessor()
    {
        if (userAccessor != null){return userAccessor;}
        userAccessor = (UserAccessor) ContainerManager.getComponent("userAccessor");
        return userAccessor;
    }

	public GreenPepperServerConfigurationActivator getGPServerConfigurationActivator()
	{
		if (configurationActivator != null){return configurationActivator;}
		configurationActivator = (GreenPepperServerConfigurationActivator)ContainerManager.getComponent(
						"greenPepperServerConfigurationActivator");
		return configurationActivator;
	}

	public GreenPepperUserGroup getGreenPepperUserGroup()
	{
		if (gpUserGroup != null) { return gpUserGroup; }
		gpUserGroup = new GreenPepperUserGroup();
		return gpUserGroup;
	}

	public FormatSettingsManager getFormatSettingsManager()
	{
		if (formatSettingsManager != null) { return formatSettingsManager; }
		formatSettingsManager = (FormatSettingsManager)ContainerManager.getComponent("formatSettingsManager");
		return formatSettingsManager;
	}

	/*************************************************************************************************/


    private String buildSelectedSystemUnderTestInfo(SystemUnderTest sut)
    {
        return sut.getProject().getName() + "@" + sut.getName();
    }

    private SystemUnderTest getSavedSelectedSystemUnderTest(Page page)
    {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String key = getContentPropertyManager().getStringProperty(entityObject, EXECUTION_KEY);
        return buildSelectedSystemUnderTest(key);
    }

    private SystemUnderTest buildSelectedSystemUnderTest(String selectedSystemUnderTestInfo)
    {
        if(StringUtil.isBlank(selectedSystemUnderTestInfo)){return null;}
        StringTokenizer stk = new StringTokenizer(selectedSystemUnderTestInfo, "@");
        Project project = Project.newInstance(stk.nextToken());
        SystemUnderTest sut = SystemUnderTest.newInstance(stk.nextToken());
        sut.setProject(project);
        return sut;
    }

	private List<Reference> getUniqueReferences(List<Reference> references)
	{
		Map<String, Reference> uniqueReferences = new HashMap<String, Reference>();

		for (Reference reference : references)
		{
			Reference ref = uniqueReferences.get(reference.getRequirement().getUUID());

			if (ref == null)
			{
				uniqueReferences.put(reference.getRequirement().getUUID(), reference);
			}
		}

		return new ArrayList<Reference>(uniqueReferences.values());
	}

	public void saveExecutionResult(Page page, String sut, XmlReport xmlReport) throws GreenPepperServerException
	{
		Specification specification = getSpecification(page);

		List<SystemUnderTest> systemUnderTests = getSystemsUnderTests(page.getSpaceKey());

		SystemUnderTest systemUnderTest = null;

		for (SystemUnderTest s : systemUnderTests)
		{
			if (s.getName().equals(sut))
			{
				systemUnderTest = s;
				break;
			}
		}

		if (systemUnderTest == null)
		{
			throw new GreenPepperServerException(GreenPepperServerErrorKey.SUT_NOT_FOUND, sut);
		}
		
		getGPServerService().createExecution(systemUnderTest, specification, xmlReport);
	}

	public boolean isPluginInstalledUnderWebInfLib() {
		URL root = getClass().getResource("/templates/greenpepper/confluence/blank.vm");
		//TODO CLAP added 4. should we cut at confluence?
		return root.toExternalForm().indexOf("WEB-INF/lib/greenpepper-confluence4-plugin") != -1;
	}

	public String getWebInfLibDirectory() {
		String realPath = ServletActionContext.getServletContext().getRealPath("WEB-INF/lib");
		try {
			return new File(realPath).getCanonicalPath();
		}
		catch (IOException e) {
			return new File(realPath).getAbsolutePath();
		}
	}
}