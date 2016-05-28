package com.greenpepper.confluence.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.atlassian.renderer.v2.components.HtmlEscaper;

/**
 * <p>MacroParametersUtils class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class MacroParametersUtils {

	/**
	 * <p>extractParameter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param parameters a {@link java.util.Map} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String extractParameter(String name, Map parameters) {
		Object value = parameters.get(name);
		return (value != null) ? xssEscape(value.toString()) : "";
	}

	
	private static String xssEscape(String value) {
		return HtmlEscaper.escapeAll(value, true);
	}


	/**
	 * <p>extractParameterMultiple.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param parameters a {@link java.util.Map} object.
	 * @return an array of {@link java.lang.String} objects.
	 */
	public static String[] extractParameterMultiple(String name, Map parameters) {
		String paramValues = extractParameter(name, parameters);
		return StringUtils.stripAll(StringUtils.split(paramValues, ", "));
	}
}
