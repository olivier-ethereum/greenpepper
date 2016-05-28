package com.greenpepper.confluence.utils;

import java.util.Set;

/**
 * <p>HtmlUtils class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HtmlUtils {

	private final static String TEXTAREA_ELEMENTS_SEPARATOR = "\n";
	
	/**
	 * <p>stringSetToTextArea.</p>
	 *
	 * @param elements a {@link java.util.Set} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringSetToTextArea(Set<String> elements) {
		StringBuilder sb = new StringBuilder();
		if (elements != null) {
			for (String element : elements) {
				sb.append(element);
				sb.append(TEXTAREA_ELEMENTS_SEPARATOR);
			}
		}
    	int length = sb.length();
		return sb.substring(0, length > 0 ? length - 1 : 0);
	}
}
