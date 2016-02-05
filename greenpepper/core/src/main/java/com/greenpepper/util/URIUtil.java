package com.greenpepper.util;

import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>URIUtil class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class URIUtil
{
	private URIUtil() {}

	/**
	 * A basic quoting implementation. It escapes path separators and spaces.
	 * If a more robust solution is required look at {@link java.net.URI} or
	 * org.apache.commons.httpclient.URI.
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String raw(String uri)
	{
		String quoted = uri.replaceAll("\\%", "%25"); 
		quoted = quoted.replaceAll("\\s", "%20");
		quoted = quoted.replaceAll("\\\"", "%22");
		quoted = quoted.replaceAll("\\'", "%27");
		quoted = quoted.replaceAll("\\" + File.separator, "/");
		return quoted;
	}

	/**
	 * <p>decoded.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String decoded(String uri)
	{
		String decoded = uri.replaceAll("%20", " ");
		decoded = decoded.replaceAll("%22", "\"");
		decoded = decoded.replaceAll("%27", "'");
		decoded = decoded.replaceAll("\\s", " ");
		decoded = decoded.replaceAll("%25", "%");
		decoded = decoded.replaceAll("\\" + File.separator, "/");
		return decoded;
	}

	/**
	 * <p>flatten.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String flatten(String uri)
	{
		URI normalized = URI.create( raw( uri ) ).normalize();
		String path = normalized.getPath();
		path = stripLeadingSlash( path );
		path = path.replaceAll( "/", "-" );
		return path;
	}

	/**
	 * <p>relativize.</p>
	 *
	 * @param base a {@link java.lang.String} object.
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String relativize(String base, String uri)
	{
		URI child = URI.create( raw( uri ) );
		URI parent = URI.create( raw( base ) );
		return parent.relativize( child ).getPath();
	}

	/**
	 * <p>resolve.</p>
	 *
	 * @param base a {@link java.lang.String} object.
	 * @param child a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String resolve( String base, String child )
	{
		String childPath = child.startsWith( "/" ) ? child.substring( 1 ) : child;
		String basePath = base.endsWith( "/" ) ? base : base + "/";
		return basePath + childPath;
	}

	/**
	 * <p>getAttribute.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @param attributeName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getAttribute(URI uri, String attributeName)
	{
		String query = uri.getQuery();
		if(StringUtil.isEmpty(query)) return null;

		Pattern pattern = Pattern.compile( attributeName + "\\=([^&]*)" );
		Matcher matcher = pattern.matcher( query );

		if (matcher.find())
			return matcher.group( 1 );
		else
			return null;
	}

	private static String stripLeadingSlash(String path)
	{
		return path.startsWith( "/" ) ? path.substring( 1 ) : path;
	}

	/**
	 * <p>escapeFileSystemForbiddenCharacters.</p>
	 *
	 * @param input a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String escapeFileSystemForbiddenCharacters(String input) {
		return input
					.replace("?", "%3F")
					.replace(">", "%3E")
					.replace("<", "%3C")
					.replace("\"", "%22")
					.replace("|", "%7C");
	}
}
