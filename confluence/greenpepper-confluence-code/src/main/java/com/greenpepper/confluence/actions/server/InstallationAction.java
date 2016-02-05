package com.greenpepper.confluence.actions.server;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.greenpepper.confluence.GreenPepperServerConfigurationActivator;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.database.SupportedDialects;
import com.greenpepper.util.I18nUtil;

/**
 * <p>InstallationAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class InstallationAction
		extends GreenPepperServerAction
{

	private static final Logger log = LoggerFactory.getLogger(InstallationAction.class);
	
	private static final String RESOURCE_BUNDLE = InstallationAction.class.getName();
	private final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
	private ResourceBundle resourceBundle;

	private String installType;

	private String jndiUrl;
	private String hibernateDialect;

	private boolean editMode;
	
	/** {@inheritDoc} */
	@Override
	public String getActionName(String fullClassName) {
		return getText("greenpepper.install.title");
	}

	/**
	 * <p>Constructor for InstallationAction.</p>
	 */
	public InstallationAction()
	{
	}

	/**
	 * <p>config.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String config() {
		return SUCCESS;
	}

	/**
	 * <p>getDialects.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SupportedDialects> getDialects()
	{
		return Arrays.asList(SupportedDialects.values());
	}

	//We want to force edit mode if DBMS not ready 
	/**
	 * <p>Getter for the field <code>editMode</code>.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean getEditMode() {
		return editMode | !(isServerReady());
	}

	/**
	 * <p>Setter for the field <code>editMode</code>.</p>
	 *
	 * @param editMode a {@link java.lang.Boolean} object.
	 */
	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * <p>getIsCustomSetup.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getIsCustomSetup()
	{
		return isCustomSetup();
	}

	/**
	 * <p>isCustomSetup.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCustomSetup()
	{
		return getInstallType().equals("customInstall");
	}

	/**
	 * <p>Getter for the field <code>installType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getInstallType()
	{
		return  installType == null ? (getJndiUrl() == null ? "quickInstall" : "customInstall") : installType;
	}

	/**
	 * <p>Setter for the field <code>installType</code>.</p>
	 *
	 * @param installType a {@link java.lang.String} object.
	 */
	public void setInstallType(String installType)
	{
		this.installType = installType;
	}

	/**
	 * <p>changeInstallationType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String changeInstallationType()
	{
		return SUCCESS;
	}

	/**
	 * <p>Getter for the field <code>jndiUrl</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJndiUrl()
	{
		return jndiUrl == null ? getConfigurationActivator().getConfigJnriUrl() : jndiUrl;
	}

	/**
	 * <p>Setter for the field <code>jndiUrl</code>.</p>
	 *
	 * @param jndiUrl a {@link java.lang.String} object.
	 */
	public void setJndiUrl(String jndiUrl)
	{
		this.jndiUrl = jndiUrl;
	}

	/**
	 * <p>Getter for the field <code>hibernateDialect</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHibernateDialect()
	{
		return hibernateDialect == null ? getConfigurationActivator().getConfigDialect() : hibernateDialect;
	}

	/**
	 * <p>Setter for the field <code>hibernateDialect</code>.</p>
	 *
	 * @param hibernateDialect a {@link java.lang.String} object.
	 */
	public void setHibernateDialect(String hibernateDialect)
	{
		this.hibernateDialect = hibernateDialect;
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
	 * <p>isSetupComplete.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSetupComplete()
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
	 * <p>editDbmsConfiguration.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String editDbmsConfiguration()
	{
		try
		{
			if (isCustomSetup())
			{
				if (hibernateDialect != null && jndiUrl != null)
				{
					if (canConnectToDbms())
					{
						getConfigurationActivator().initCustomInstallConfiguration(hibernateDialect, jndiUrl);
					}
					else
					{
						addActionError("greenpepper.install.dbms.test.failure");
					}
				}
			}
			else
			{
				getConfigurationActivator().initQuickInstallConfiguration();
			}
		}
		catch (GreenPepperServerException ex)
		{
			addActionError("greenpepper.install.dbms.init.failure");
		}

		return SUCCESS;
	}

	/**
	 * <p>testDbmsConnection.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String testDbmsConnection()
	{
		if (!canConnectToDbms())
		{
			addActionError("greenpepper.install.dbms.test.failure");
		}

		return SUCCESS;
	}

	private boolean canConnectToDbms()
	{
		try
		{
			InitialContext context = new InitialContext();

			DataSource ds = (DataSource)context.lookup(jndiUrl);

			Connection connection = ds.getConnection();

			connection.close();

			return true;
		}
		catch (Exception ex)
		{
			log.error("Testing Dbms Connection using jndi (" + jndiUrl + ")", ex);
			return false;
		}
	}

	private GreenPepperServerConfigurationActivator getConfigurationActivator() {
		return gpUtil.getGPServerConfigurationActivator();
	}
}
