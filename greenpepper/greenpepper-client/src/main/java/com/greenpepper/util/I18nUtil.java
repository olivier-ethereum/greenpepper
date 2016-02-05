package com.greenpepper.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Util class for internalisation.
 * JIRA dependend.
 * <p/>
 * Copyright (c) 2005 Pyxis technologies inc. All Rights Reserved.
 *
 * @author jchuet
 * @version $Id: $Id
 */
public final class I18nUtil
{
    private I18nUtil() {}

    /**
     * Custom I18n. Based on WebWork i18n.
     *
     * @param key a {@link java.lang.String} object.
     * @return the i18nze message. If none found key is returned.
     * @param bundle a {@link java.util.ResourceBundle} object.
     */
    public static String getText(String key, ResourceBundle bundle)
    {
        try
        {
            return  bundle.getString(key);
        }
        catch (MissingResourceException ex)
        {
            return key;
        }
    }

	/**
	 * <p>getText.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param bundle a {@link java.util.ResourceBundle} object.
	 * @param arguments a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getText(String key, ResourceBundle bundle, Object... arguments)
	{
		try
		{
			String value = bundle.getString(key);

			return MessageFormat.format(value, arguments);
		}
		catch (MissingResourceException ex)
		{
			return key;
		}
	}

    /**
     * <p>getResourceBundle.</p>
     *
     * @param bundleName a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.util.ResourceBundle} object.
     */
    public static ResourceBundle getResourceBundle(String bundleName, Locale locale)
    {
        return ResourceBundle.getBundle(bundleName, locale);
    }
}
