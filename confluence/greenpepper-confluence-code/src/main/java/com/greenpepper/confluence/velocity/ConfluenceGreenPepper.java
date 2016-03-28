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

import com.greenpepper.confluence.Plugin;
import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;

import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.atlassian.config.util.BootstrapUtils;
import com.atlassian.confluence.content.render.xhtml.Renderer;
import com.atlassian.confluence.content.render.xhtml.compatibility.BodyTypeAwareRenderer;
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
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserPreferences;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
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

/**
 * <p>ConfluenceGreenPepper class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ConfluenceGreenPepper {

    /** Constant <code>EXECUTION_KEY="greenpeeper.executionKey"</code> */
    public static final String EXECUTION_KEY = "greenpeeper.executionKey";
    /** Constant <code>EXECUTE_CHILDREN="greenpeeper.executeChildren"</code> */
    public static final String EXECUTE_CHILDREN = "greenpeeper.executeChildren";
    /** Constant <code>IMPLEMENTED_VERSION="greenpeeper.implementedversion"</code> */
    public static final String IMPLEMENTED_VERSION = "greenpeeper.implementedversion";
    /** Constant <code>PREVIOUS_IMPLEMENTED_VERSION="greenpeeper.previous.implementedversion"</code> */
    public static final String PREVIOUS_IMPLEMENTED_VERSION = "greenpeeper.previous.implementedversion";
    /** Constant <code>NEVER_IMPLEMENTED="greenpepper.page.neverimplemented"</code> */
    public static final String NEVER_IMPLEMENTED = "greenpepper.page.neverimplemented";
    /** Constant <code>SERVER_NOCONFIGURATION="greenpepper.server.noconfiguration"</code> */
    public static final String SERVER_NOCONFIGURATION = "greenpepper.server.noconfiguration";
    /** Constant <code>ANONYMOUS_ACCESS_DENIED="greenpepper.anonymous.accessdenied"</code> */
    public static final String ANONYMOUS_ACCESS_DENIED = "greenpepper.anonymous.accessdenied";
    /** Constant <code>USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP="greenpepper.notmemberof.greenpepperuser"{trunked}</code> */
    public static final String USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP = "greenpepper.notmemberof.greenpepperusers.group";
    /** Constant <code>PLUGIN_NOT_INSTALLED_UNDER_WEBINFLIB="greenpepper.server.plugin.notinstalledu"{trunked}</code> */
    public static final String PLUGIN_NOT_INSTALLED_UNDER_WEBINFLIB = "greenpepper.server.plugin.notinstalledunderwebinflib";
    /** Constant <code>REPOSITORY_BASEURL_OUTOFSYNC="greenpepper.server.repourloutofsync"</code> */
    public static final String REPOSITORY_BASEURL_OUTOFSYNC = "greenpepper.server.repourloutofsync";

    private static Logger log = Logger.getLogger(ConfluenceGreenPepper.class);
    private static final String RESOURCE_BUNDLE = SpecificationAction.class.getName();
    private static final int CRITICAL_PERIOD = 29;

    private GreenPepperServerService service;
    private GreenPepperServerConfigurationActivator configurationActivator;
    private TokenAuthenticationManager tokenManager;
    private PlatformTransactionManager transactionManager;
    private SettingsManager settingsManager;
    private AtlassianBootstrapManager bootstrapManager;
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
    private Renderer viewRenderer;
    private BodyTypeAwareRenderer bodyTypeAwareRenderer;

    private final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
    private ResourceBundle resourceBundle;

    /**
     * <p>getVersion.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVersion() {
        return Plugin.VERSION;
    }

    /**
     * <p>getVersionDate.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVersionDate() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return df.format(GreenPepperServer.versionDate());
    }

    /**
     * <p>getConfluenceVersion.</p>
     *
     * @return a {@link com.greenpepper.confluence.utils.ConfluenceVersion} object.
     */
    public ConfluenceVersion getConfluenceVersion() {
        return ConfluenceVersion.getCurrentVersion();
    }

    /**
     * <p>isConfluenceVersion28.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceVersion28() {
        return getConfluenceVersion().equals(ConfluenceVersion.V28X);
    }

    /**
     * <p>isConfluenceVersion29.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceVersion29() {
        return getConfluenceVersion().equals(ConfluenceVersion.V29X);
    }

    /**
     * <p>isConfluenceVersion3.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceVersion3() {
        ConfluenceVersion confluenceVersion = getConfluenceVersion();
        return confluenceVersion.compareTo(ConfluenceVersion.V30X) >= 0 && confluenceVersion.compareTo(ConfluenceVersion.V40X) < 0;
    }

    /**
     * <p>isConfluenceVersion4.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceVersion4() {
        ConfluenceVersion confluenceVersion = getConfluenceVersion();
        return confluenceVersion.compareTo(ConfluenceVersion.V40X) >= 0 && confluenceVersion.compareTo(ConfluenceVersion.V50X) < 0;
    }

    /**
     * <p>isConfluenceMajorVersionLessThan_2_6.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceMajorVersionLessThan_2_6() {
        return getConfluenceVersion().compareTo(ConfluenceVersion.V26X) < 0;
    }

    /**
     * <p>isConfluenceMajorVersionLessThan_2_8.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceMajorVersionLessThan_2_8() {
        return getConfluenceVersion().compareTo(ConfluenceVersion.V28X) < 0;
    }

    /**
     * <p>isConfluenceMajorVersionGreaterOrEqualThan_2_8.</p>
     *
     * @return a boolean.
     */
    public boolean isConfluenceMajorVersionGreaterOrEqualThan_2_8() {
        return getConfluenceVersion().compareTo(ConfluenceVersion.V28X) >= 0;
    }

    /**
     * Custom I18n. Based on WebWork i18n.
     *
     * @param key a {@link java.lang.String} object.
     * @return the i18nzed message. If none found key is returned.
     */
    @HtmlSafe
    public String getText(String key) {
        return I18nUtil.getText(key, getResourceBundle());
    }

    /**
     * <p>getText.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param arguments a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     */
    @HtmlSafe
    public String getText(String key, Object... arguments) {
        return I18nUtil.getText(key, getResourceBundle(), arguments);
    }

    private ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            Locale locale = threadLocale.get();
            if (locale == null) {
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
     *
     * @param spaceKey a {@link java.lang.String} object.
     * @return the home repository of the confluence space.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Repository getHomeRepository(String spaceKey) throws GreenPepperServerException {
        String uid = getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + spaceKey;
        Repository repository = Repository.newInstance(uid);
        repository.setMaxUsers(getNumberOfUserForGreenPepperUserGroup());
        return repository;
    }

    /**
     * Returns the number of user associated to the 'greenpepper-users' group.
     *
     * @return number of user for the group is the license type is 'Commercial', 1 otherwise
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public int getNumberOfUserForGreenPepperUserGroup() throws GreenPepperServerException {
        if (isCommercialLicense()) {
            return getGreenPepperUserGroup().getNumberOfUserForGroup();
        } else {
            return 1;
        }
    }

    /**
     * Returns a message if an exception occures.
     * <p/>
     *
     * @param spaceKey
     *            Space Key
     * @return a message if an exception occures.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public String enable(String spaceKey) throws GreenPepperServerException {
        try {
            if (!isServerReady()) {
                return getText(SERVER_NOCONFIGURATION);
            }

            /* BEGIN : COMMENT THIS FOR DEPLOYING TO GPS */
            if (isCommercialLicense()) {
                final User activeUser = getRemoteUser();

                if (activeUser == null) {
                    return getText(ANONYMOUS_ACCESS_DENIED);
                }

                if (!getGreenPepperUserGroup().hasMembership(activeUser)) {
                    return getText(USER_NOTMEMBEROF_GREENPEPPERUSERS_GROUP);
                }
            }
            /* END : COMMENT THIS FOR DEPLOYING TO GPS */

            Repository repository = getHomeRepository(spaceKey);
            getGPServerService().getRegisteredRepository(repository);
            return null;
        } catch (GreenPepperServerException e) {
            log.info(e.getMessage());
            return getText(e.getId());
        }
    }

    /**
     * Indicates if the current license type is 'Commercial'.
     *
     * @return true if current license type is 'Commercial', false otherwise
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public boolean isCommercialLicense() throws GreenPepperServerException {
        return getGPServerService().isCommercialLicense();
    }

    /**
     * Retrieves the specification
     * <p/>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the specification.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification getSpecification(Page page) throws GreenPepperServerException {
        return getSpecification(page.getSpaceKey(), page.getTitle());
    }

    /**
     * Retrieves the specification
     * <p/>
     *
     * @param spaceKey
     *            Space Key
     * @param pageTitle
     *            String pageTitle
     * @return the specification.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Specification getSpecification(String spaceKey, String pageTitle) throws GreenPepperServerException {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        return getGPServerService().getSpecification(specification);
    }

    /**
     * True if the specification exists
     * <p/>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return if the specification exists
     */
    public boolean isExecutable(Page page) {
        try {
            return getSpecification(page) != null;
        } catch (GreenPepperServerException e) {
            return false;
        }
    }

    /**
     * Get the repositories from the GreenPepper Server wich are requirements dedicated
     * <p>
     *
     * @return List of requirement repositories
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param spaceKey a {@link java.lang.String} object.
     */
    public List<Repository> getRepositories(String spaceKey) throws GreenPepperServerException {
        Repository repository = getHomeRepository(spaceKey);
        List<Repository> repositories = getGPServerService().getRequirementRepositoriesOfAssociatedProject(repository.getUid());
        return repositories;
    }

    /**
     * Get the systems under test associated with the space specified
     * <p>
     *
     * @return List of systems under test
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param spaceKey a {@link java.lang.String} object.
     */
    public List<SystemUnderTest> getSystemsUnderTests(String spaceKey) throws GreenPepperServerException {
        Repository repository = getHomeRepository(spaceKey);
        return getGPServerService().getSystemUnderTestsOfAssociatedProject(repository.getUid());
    }

    /**
     * Get the systems under test list associated with the page specified
     * <p>
     *
     * @return the systems under test list associated with the page specified
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     */
    public Set<SystemUnderTest> getPageSystemsUnderTests(Page page) throws GreenPepperServerException {
        return getPageSystemsUnderTests(page.getSpaceKey(), page.getTitle());
    }

    /**
     * Get the systems under test list associated with the page specified
     * <p>
     *
     * @param spaceKey a {@link java.lang.String} object.
     * @param pageTitle a {@link java.lang.String} object.
     * @return the systems under test list associated with the page specified
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public Set<SystemUnderTest> getPageSystemsUnderTests(String spaceKey, String pageTitle) throws GreenPepperServerException {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        Specification specificationFromGPService = getGPServerService().getSpecification(specification);
        return specificationFromGPService.getTargetedSystemUnderTests();
    }

    /**
     * Get the References with the specified page.
     * <p>
     *
     * @return List of References
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     */
    public List<Reference> getReferences(Page page) throws GreenPepperServerException {
        return getReferences(page.getSpaceKey(), page.getTitle());
    }

    /**
     * Get the References with the specified page and space.
     * <p>
     *
     * @return List of References
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     * @param spaceKey a {@link java.lang.String} object.
     * @param pageTitle a {@link java.lang.String} object.
     */
    public List<Reference> getReferences(String spaceKey, String pageTitle) throws GreenPepperServerException {
        Specification specification = Specification.newInstance(pageTitle);
        specification.setRepository(getHomeRepository(spaceKey));
        List<Reference> references = getGPServerService().getSpecificationReferences(specification);
        return getUniqueReferences(references);
    }

    /**
     * <p>getPageContent.</p>
     *
     * @param currentPage a {@link com.atlassian.confluence.pages.Page} object.
     * @param implementedVersion a {@link java.lang.Boolean} object.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public String getPageContent(Page currentPage, Boolean implementedVersion) throws GreenPepperServerException {
        AbstractPage page = currentPage;
        if (implementedVersion) {
            page = getImplementedPage(currentPage);
        }
        return getBodyTypeAwareRenderer().render(page);
    }

    /**
     * Retrieves the body of a page in HTML rendering.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the body of a page in HTML rendering.
     */
    public String getPageContent(Page page) {
        try {
            return getPageContent(page, false);
        } catch (GreenPepperServerException e) {
            return e.getMessage();
        }
    }

    /**
     * Retrieves from the page propeties the selectedSystemUnderTestInfo.
     * If none registered the default seeds execution will be saved and returned.
     *
     * @param spaceKey a {@link java.lang.String} object.
     * @param pageTitle a {@link java.lang.String} object.
     * @return the selectedSystemUnderTestInfo.
     */
    public String getSelectedSystemUnderTestInfo(String spaceKey, String pageTitle) {
        return getSelectedSystemUnderTestInfo(pageManager.getPage(spaceKey, pageTitle));
    }

    /**
     * Retrieves from the page propeties the selectedSystemUnderTestInfo.
     * If none registered the default sut will be saved and returned.
     * The key must correspond to an excisting SystemUnderTest else the default one will be
     * saved and returned.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the selectedSystemUnderTestInfo.
     */
    public String getSelectedSystemUnderTestInfo(Page page) {
        SystemUnderTest selectedSut = getSavedSelectedSystemUnderTest(page);
        SystemUnderTest defaultSut = null;

        try {
            Set<SystemUnderTest> suts = getPageSystemsUnderTests(page.getSpaceKey(), page.getTitle());
            for (SystemUnderTest sut : suts) {
                if (selectedSut != null && selectedSut.equalsTo(sut)) {
                    // enougth said return the key now !
                    return buildSelectedSystemUnderTestInfo(selectedSut);
                }
                if (sut.isDefault()) {
                    defaultSut = sut;
                }
            }

            // else if no default pick first.
            if (defaultSut == null && !suts.isEmpty()) {
                defaultSut = suts.iterator().next();
            }
        } catch (GreenPepperServerException e) {
        }

        String key = buildSelectedSystemUnderTestInfo(defaultSut);
        saveSelectedSystemUnderTestInfo(page, key);
        return key;
    }

    /**
     * <p>getSelectedSystemUnderTest.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public SystemUnderTest getSelectedSystemUnderTest(Page page) {
        return buildSelectedSystemUnderTest(getSelectedSystemUnderTestInfo(page));
    }

    /**
     * Saves the execution key into the page properties.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @param value a {@link java.lang.String} object.
     */
    public void saveSelectedSystemUnderTestInfo(Page page, String value) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, EXECUTION_KEY, value);
    }

    /**
     * Sets the implemented version to the previous implemented version.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     */
    public void revertImplementation(Page page) {
        Integer previousImplementedVersion = getPreviousImplementedVersion(page);
        if (previousImplementedVersion != null) {
            saveImplementedVersion(page, previousImplementedVersion);
            savePreviousImplementedVersion(page, null);
        }
    }

    /**
     * Retrieves the previous implemented version of the specification.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the previous implemented version of the specification.
     */
    public Integer getPreviousImplementedVersion(Page page) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, PREVIOUS_IMPLEMENTED_VERSION);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * Saves the sprecified version as the Previous implemented version
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @param version a {@link java.lang.Integer} object.
     */
    public void savePreviousImplementedVersion(Page page, Integer version) {
        String value = version != null ? String.valueOf(version) : null;
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, PREVIOUS_IMPLEMENTED_VERSION, value);
    }

    /**
     * Verifies if the specification can be Implemented.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return true if the specification can be Implemented.
     */
    public boolean canBeImplemented(Page page) {
        Integer implementedVersion = getImplementedVersion(page);
        return implementedVersion == null || page.getVersion() != implementedVersion;
    }

    /**
     * Retrieves the implemented version of the specification.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the implemented version of the specification.
     */
    public Integer getImplementedVersion(Page page) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, IMPLEMENTED_VERSION);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * Saves the sprecified version as the Iimplemented version
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @param version a {@link java.lang.Integer} object.
     */
    public void saveImplementedVersion(Page page, Integer version) {
        Integer previousImplementedVersion = getImplementedVersion(page);
        if (previousImplementedVersion != null && version != null && previousImplementedVersion == version)
            return;

        if (previousImplementedVersion != null)
            savePreviousImplementedVersion(page, previousImplementedVersion);

        String value = version != null ? String.valueOf(version) : null;
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, IMPLEMENTED_VERSION, value);
    }

    /**
     * Retrieves the content of the specification at the implemented version.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the content of the specification at the implemented version.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public AbstractPage getImplementedPage(Page page) throws GreenPepperServerException {
        Integer version = getImplementedVersion(page);
        if (version == null)
            throw new GreenPepperServerException(NEVER_IMPLEMENTED, "Never Implemented");

        return getPageManager().getPageByVersion(page, version);
    }

    /**
     * Verifies if the Specification has stayed to long in the WORKING state.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return true if the Specification has stayed to long in the WORKING state.
     */
    public boolean isImplementationDue(Page page) {
        int version = page.getVersion();

        Integer implementedVersion = getImplementedVersion(page);
        if (implementedVersion != null)
            version = page.getVersion() == implementedVersion ? implementedVersion : implementedVersion + 1;

        Date date = getPageManager().getPageByVersion(page, version).getLastModificationDate();
        Period period = Period.fromTo(date, new Date(System.currentTimeMillis()));
        return period.daysCount() > CRITICAL_PERIOD;
    }

    /**
     * Retrieves from the page propeties the Execute childs boolean.
     * If none registered false is returned.
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return the Execute childs boolean.
     */
    public boolean getExecuteChildren(Page page) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String value = getContentPropertyManager().getStringProperty(entityObject, EXECUTE_CHILDREN);
        return value == null ? false : Boolean.valueOf(value);
    }

    /**
     * <p>saveExecuteChildren.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @param doExecuteChildren a {@link java.lang.Boolean} object.
     */
    public void saveExecuteChildren(Page page, Boolean doExecuteChildren) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        getContentPropertyManager().setStringProperty(entityObject, ConfluenceGreenPepper.EXECUTE_CHILDREN, doExecuteChildren != null ? String.valueOf(doExecuteChildren) : null);
    }

    /**
     * Verifies if the the selectedSystemUnderTestInfo matches the specified key
     * </p>
     *
     * @param selectedSystemUnderTestInfo a {@link java.lang.String} object.
     * @param key a {@link java.lang.String} object.
     * @return true if the the selectedSystemUnderTestInfo matches the specified key.
     */
    public boolean isSelected(String selectedSystemUnderTestInfo, String key) {
        return selectedSystemUnderTestInfo != null ? selectedSystemUnderTestInfo.equals(key) : false;
    }

    /**
     * <p>getBaseUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBaseUrl() {
        // DEPRECATION WARNING we should use "Settings.getBaseUrl()" instead
        // Have to wait until we do not support version under confluence 2.3
        // return getBootstrapManager().getBaseUrl();
        return getSettingsManager().getGlobalSettings().getBaseUrl();
    }

    /**
     * <p>getEncoding.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getEncoding() {
        return getSettingsManager().getGlobalSettings().getDefaultEncoding();
    }

    /**
     * <p>getPageUrl.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return a {@link java.lang.String} object.
     */
    public String getPageUrl(Page page) {
        return getBaseUrl() + page.getUrlPath();
    }

    /**
     * <p>canEdit.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return a boolean.
     */
    public boolean canEdit(Page page) {
        List<String> permTypes = new ArrayList<String>();
        permTypes.add(SpacePermission.CREATEEDIT_PAGE_PERMISSION);
        return getSpacePermissionManager().hasPermissionForSpace(getRemoteUser(), permTypes, page.getSpace());
    }

    /**
     * <p>canView.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return a boolean.
     */
    public boolean canView(Page page) {
        return canView(page.getSpace());
    }

    /**
     * <p>canView.</p>
     *
     * @param space a {@link com.atlassian.confluence.spaces.Space} object.
     * @return a boolean.
     */
    public boolean canView(Space space) {
        List<String> permTypes = new ArrayList<String>();
        permTypes.add(SpacePermission.VIEWSPACE_PERMISSION);
        return getSpacePermissionManager().hasPermissionForSpace(getRemoteUser(), permTypes, space);
    }

    /**
     * <p>getHeader.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHeader() {
        return "/templates/greenpepper/confluence/themes/greenpepper-header.vm";
    }

    /**
     * <p>getBody.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBody() {
        return "templates/greenpepper/confluence/themes/greenpepper-body.vm";
    }

    /**
     * <p>isInSutList.</p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param sutList a {@link java.util.Collection} object.
     * @return a boolean.
     */
    public boolean isInSutList(SystemUnderTest sut, Collection<SystemUnderTest> sutList) {
        for (SystemUnderTest aSut : sutList) {
            if (aSut.equalsTo(sut)) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>clean.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String clean(String text) {
        if (text == null)
            return "";

        text = text.trim();
        text = text.replace("\"", "\\\"");
        text = text.replace("\'", "\\\'");
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        return text;
    }

    /**
     * <p>getRemoteUser.</p>
     *
     * @return a {@link com.atlassian.user.User} object.
     */
    public User getRemoteUser() {
        HttpServletRequest request = ServletActionContext.getRequest();

        if (request != null) {
            String remoteUserName = request.getRemoteUser();

            if (remoteUserName != null) {
                return getUserAccessor().getUserIfAvailable(remoteUserName);
            }
        }

        return AuthenticatedUserThreadLocal.getUser();
    }

    /**
     * <p>getPageProperty.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getPageProperty(String key, String identifier) {
        Space space = getSpaceManager().getSpace(identifier);
        if (space == null)
            return null;

        ContentEntityObject entityObject = getContentEntityManager().getById(space.getHomePage().getId());
        return getContentPropertyManager().getStringProperty(entityObject, ServerPropertiesManager.SEQUENCE + key);
    }

    /**
     * <p>setPageProperty.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     */
    public void setPageProperty(String key, String value, String identifier) {
        Space space = getSpaceManager().getSpace(identifier);
        ContentEntityObject entityObject = getContentEntityManager().getById(space.getHomePage().getId());
        getContentPropertyManager().setStringProperty(entityObject, ServerPropertiesManager.SEQUENCE + key, value);
    }

    /**
     * <p>getGPServerConfiguration.</p>
     *
     * @return a {@link com.greenpepper.confluence.GreenPepperServerConfiguration} object.
     */
    public GreenPepperServerConfiguration getGPServerConfiguration() {
        return getGPServerConfigurationActivator().getConfiguration();
    }

    /**
     * <p>storeGPServerConfiguration.</p>
     *
     * @param configuration a {@link com.greenpepper.confluence.GreenPepperServerConfiguration} object.
     */
    public void storeGPServerConfiguration(GreenPepperServerConfiguration configuration) {
        getGPServerConfigurationActivator().storeConfiguration(configuration);
    }

    /**
     * <p>isServerSetupComplete.</p>
     *
     * @return a boolean.
     */
    public boolean isServerSetupComplete() {
        return getGPServerConfiguration().isSetupComplete();
    }

    /**
     * <p>isServerReady.</p>
     *
     * @return a boolean.
     */
    public boolean isServerReady() {
        return getGPServerConfigurationActivator().isReady();
    }

    /**
     * <p>verifyCredentials.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void verifyCredentials(String username, String password) throws GreenPepperServerException {
        if (username != null && !isCredentialsValid(username, password)) {
            throw new GreenPepperServerException("greenpepper.confluence.badcredentials", "The username and password are incorrect.");
        }
    }

    /**
     * <p>isCredentialsValid.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isCredentialsValid(String username, String password) {
        try {
            String token = getTokenAuthenticationManager().login(StringUtil.toEmptyIfNull(username), StringUtil.toEmptyIfNull(password));
            getTokenAuthenticationManager().logout(token);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * <p>getUserPreferencesDateFormatter.</p>
     *
     * @return a {@link com.atlassian.confluence.core.DateFormatter} object.
     */
    public DateFormatter getUserPreferencesDateFormatter() {
        ConfluenceUserPreferences preferences = getUserAccessor().getConfluenceUserPreferences(getRemoteUser());

        return preferences.getDateFormatter(getFormatSettingsManager());
    }

    /*************************************************************************************************/
    /************************** Access to container services *************************************/
    /**
     * <p>getGPServerService.</p>
     *
     * @return a {@link com.greenpepper.server.GreenPepperServerService} object.
     */
    public GreenPepperServerService getGPServerService() {
        if (service != null) {
            return service;
        }
        service = (GreenPepperServerService) ContainerManager.getComponent("greenPepperServerService");
        return service;
    }

    /**
     * <p>Getter for the field <code>settingsManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.setup.settings.SettingsManager} object.
     */
    public SettingsManager getSettingsManager() {
        if (settingsManager != null) {
            return settingsManager;
        }
        settingsManager = (SettingsManager) ContainerManager.getComponent("settingsManager");
        return settingsManager;
    }

    /**
     * <p>Getter for the field <code>bootstrapManager</code>.</p>
     *
     * @return a {@link com.atlassian.config.bootstrap.AtlassianBootstrapManager} object.
     */
    public AtlassianBootstrapManager getBootstrapManager() {
        if (bootstrapManager != null) {
            return bootstrapManager;
        }
        bootstrapManager = BootstrapUtils.getBootstrapManager();
        return bootstrapManager;
    }

    /**
     * <p>Getter for the field <code>contentPermissionManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.core.ContentPermissionManager} object.
     */
    public ContentPermissionManager getContentPermissionManager() {
        if (contentPermissionManager != null) {
            return contentPermissionManager;
        }
        contentPermissionManager = (ContentPermissionManager) ContainerManager.getComponent("contentPermissionManager");
        return contentPermissionManager;
    }

    /**
     * <p>Getter for the field <code>contentPropertyManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.core.ContentPropertyManager} object.
     */
    public ContentPropertyManager getContentPropertyManager() {
        if (contentPropertyManager != null) {
            return contentPropertyManager;
        }
        contentPropertyManager = (ContentPropertyManager) ContainerManager.getComponent("contentPropertyManager");
        return contentPropertyManager;
    }

    /**
     * <p>Getter for the field <code>contentEntityManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.core.ContentEntityManager} object.
     */
    public ContentEntityManager getContentEntityManager() {
        if (contentEntityManager != null) {
            return contentEntityManager;
        }
        contentEntityManager = (ContentEntityManager) ContainerManager.getComponent("contentEntityManager");
        return contentEntityManager;
    }

    /**
     * <p>Getter for the field <code>wikiStyleRenderer</code>.</p>
     *
     * @return a {@link com.atlassian.renderer.WikiStyleRenderer} object.
     */
    public WikiStyleRenderer getWikiStyleRenderer() {
        if (wikiStyleRenderer != null) {
            return wikiStyleRenderer;
        }
        wikiStyleRenderer = (WikiStyleRenderer) ContainerManager.getComponent("wikiStyleRenderer");
        return wikiStyleRenderer;
    }

    /**
     * <p>Getter for the field <code>viewRenderer</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.content.render.xhtml.Renderer} object.
     */
    public Renderer getViewRenderer() {
        if (viewRenderer != null) {
            return viewRenderer;
        }
        viewRenderer = (Renderer) ContainerManager.getComponent("viewRenderer");
        return viewRenderer;
    }

    /**
     * <p>Getter for the field <code>bodyTypeAwareRenderer</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.content.render.xhtml.compatibility.BodyTypeAwareRenderer} object.
     */
    public BodyTypeAwareRenderer getBodyTypeAwareRenderer() {
        if (bodyTypeAwareRenderer != null) {
            return bodyTypeAwareRenderer;            
        }
        bodyTypeAwareRenderer = new BodyTypeAwareRenderer(getViewRenderer(), getWikiStyleRenderer());
        return bodyTypeAwareRenderer;
    }

    /**
     * <p>Getter for the field <code>pageManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.pages.PageManager} object.
     */
    public PageManager getPageManager() {
        if (pageManager != null) {
            return pageManager;
        }
        pageManager = (PageManager) ContainerManager.getComponent("pageManager");
        return pageManager;
    }

    /**
     * <p>Getter for the field <code>spaceManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.spaces.SpaceManager} object.
     */
    public SpaceManager getSpaceManager() {
        if (spaceManager != null) {
            return spaceManager;
        }
        spaceManager = (SpaceManager) ContainerManager.getComponent("spaceManager");
        return spaceManager;
    }

    /**
     * <p>Getter for the field <code>spacePermissionManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.security.SpacePermissionManager} object.
     */
    public SpacePermissionManager getSpacePermissionManager() {
        if (spacePermissionManager != null) {
            return spacePermissionManager;
        }
        spacePermissionManager = (SpacePermissionManager) ContainerManager.getComponent("spacePermissionManager");
        return spacePermissionManager;
    }

    /**
     * <p>Getter for the field <code>labelManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.labels.LabelManager} object.
     */
    public LabelManager getLabelManager() {
        if (labelManager != null) {
            return labelManager;
        }
        labelManager = (LabelManager) ContainerManager.getComponent("labelManager");
        return labelManager;
    }

    /**
     * <p>getTokenAuthenticationManager.</p>
     *
     * @return a {@link com.atlassian.confluence.rpc.auth.TokenAuthenticationManager} object.
     */
    public TokenAuthenticationManager getTokenAuthenticationManager() {
        if (tokenManager != null) {
            return tokenManager;
        }
        tokenManager = (TokenAuthenticationManager) ContainerManager.getComponent("tokenAuthenticationManager");
        return tokenManager;
    }

    /**
     * <p>getPlatformTransactionManager.</p>
     *
     * @return a {@link org.springframework.transaction.PlatformTransactionManager} object.
     */
    public PlatformTransactionManager getPlatformTransactionManager() {
        if (transactionManager != null) {
            return transactionManager;
        }
        transactionManager = (PlatformTransactionManager) ContainerManager.getComponent("transactionManager");
        return transactionManager;
    }

    /**
     * <p>Getter for the field <code>userAccessor</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.user.UserAccessor} object.
     */
    public UserAccessor getUserAccessor() {
        if (userAccessor != null) {
            return userAccessor;
        }
        userAccessor = (UserAccessor) ContainerManager.getComponent("userAccessor");
        return userAccessor;
    }

    /**
     * <p>getGPServerConfigurationActivator.</p>
     *
     * @return a {@link com.greenpepper.confluence.GreenPepperServerConfigurationActivator} object.
     */
    public GreenPepperServerConfigurationActivator getGPServerConfigurationActivator() {
        if (configurationActivator != null) {
            return configurationActivator;
        }
        configurationActivator = (GreenPepperServerConfigurationActivator) ContainerManager.getComponent("greenPepperServerConfigurationActivator");
        return configurationActivator;
    }

    /**
     * <p>getGreenPepperUserGroup.</p>
     *
     * @return a {@link com.greenpepper.confluence.GreenPepperUserGroup} object.
     */
    public GreenPepperUserGroup getGreenPepperUserGroup() {
        if (gpUserGroup != null) {
            return gpUserGroup;
        }
        gpUserGroup = new GreenPepperUserGroup();
        return gpUserGroup;
    }

    /**
     * <p>Getter for the field <code>formatSettingsManager</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.core.FormatSettingsManager} object.
     */
    public FormatSettingsManager getFormatSettingsManager() {
        if (formatSettingsManager != null) {
            return formatSettingsManager;
        }
        formatSettingsManager = (FormatSettingsManager) ContainerManager.getComponent("formatSettingsManager");
        return formatSettingsManager;
    }

    /*************************************************************************************************/

    private String buildSelectedSystemUnderTestInfo(SystemUnderTest sut) {
        return sut.getProject().getName() + "@" + sut.getName();
    }

    private SystemUnderTest getSavedSelectedSystemUnderTest(Page page) {
        ContentEntityObject entityObject = getContentEntityManager().getById(page.getId());
        String key = getContentPropertyManager().getStringProperty(entityObject, EXECUTION_KEY);
        return buildSelectedSystemUnderTest(key);
    }

    private SystemUnderTest buildSelectedSystemUnderTest(String selectedSystemUnderTestInfo) {
        if (StringUtil.isBlank(selectedSystemUnderTestInfo)) {
            return null;
        }
        StringTokenizer stk = new StringTokenizer(selectedSystemUnderTestInfo, "@");
        Project project = Project.newInstance(stk.nextToken());
        SystemUnderTest sut = SystemUnderTest.newInstance(stk.nextToken());
        sut.setProject(project);
        return sut;
    }

    private List<Reference> getUniqueReferences(List<Reference> references) {
        Map<String, Reference> uniqueReferences = new HashMap<String, Reference>();

        for (Reference reference : references) {
            Reference ref = uniqueReferences.get(reference.getRequirement().getUUID());

            if (ref == null) {
                uniqueReferences.put(reference.getRequirement().getUUID(), reference);
            }
        }

        return new ArrayList<Reference>(uniqueReferences.values());
    }

    /**
     * <p>saveExecutionResult.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @param sut a {@link java.lang.String} object.
     * @param xmlReport a {@link com.greenpepper.report.XmlReport} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void saveExecutionResult(Page page, String sut, XmlReport xmlReport) throws GreenPepperServerException {
        Specification specification = getSpecification(page);

        List<SystemUnderTest> systemUnderTests = getSystemsUnderTests(page.getSpaceKey());

        SystemUnderTest systemUnderTest = null;

        for (SystemUnderTest s : systemUnderTests) {
            if (s.getName().equals(sut)) {
                systemUnderTest = s;
                break;
            }
        }

        if (systemUnderTest == null) {
            throw new GreenPepperServerException(GreenPepperServerErrorKey.SUT_NOT_FOUND, sut);
        }

        getGPServerService().createExecution(systemUnderTest, specification, xmlReport);
    }

    /**
     * <p>isPluginInstalledUnderWebInfLib.</p>
     *
     * @return a boolean.
     */
    public boolean isPluginInstalledUnderWebInfLib() {
        URL root = getClass().getResource("/templates/greenpepper/confluence/blank.vm");
        // TODO CLAP added 4. should we cut at confluence?
        return root.toExternalForm().indexOf("WEB-INF/lib/greenpepper-confluence5-plugin") != -1;
    }

    /**
     * <p>getWebInfLibDirectory.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWebInfLibDirectory() {
        String realPath = ServletActionContext.getServletContext().getRealPath("WEB-INF/lib");
        try {
            return new File(realPath).getCanonicalPath();
        } catch (IOException e) {
            return new File(realPath).getAbsolutePath();
        }
    }
}
