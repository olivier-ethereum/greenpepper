/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.util;

import java.text.Normalizer;

/**
 * <p>StringUtil class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class StringUtil
{
    private static final String EMPTY_STRING = "";

    private StringUtil()
    {
    }

    /**
     * <p>isEmpty.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isEmpty( String s )
    {
        return s == null || s.equals( EMPTY_STRING );
    }

    /**
     * <p>isBlank.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isBlank( String s )
    {
        return s == null || isEmpty( s.trim() );
    }

	/**
	 * <p>isEquals.</p>
	 *
	 * @param s1 a {@link java.lang.String} object.
	 * @param s2 a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isEquals( String s1, String s2)
	{
		return s1 == null ? s2 == null : s1.equals(s2);
	}

	/**
	 * <p>compare.</p>
	 *
	 * @param s1 a {@link java.lang.String} object.
	 * @param s2 a {@link java.lang.String} object.
	 * @return a int.
	 */
	public static int compare( String s1, String s2)
	{
		if (s1 == null && s2 == null)
		{
			return 0;
		}
		else if (s1 == null && s2 != null)
		{
			return -1;
		}
		else if (s1 != null && s2 == null)
		{
			return 1;
		}
		else
		{
			return s1.compareTo(s2);
		}
	}

	/**
	 * <p>toNullIfEmpty.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toNullIfEmpty( String s )
	{
		return isEmpty( s ) ? null : s;
	}

	/**
	 * <p>toEmptyIfNull.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toEmptyIfNull( String s )
	{
		return s == null ? "" : s;
	}

    /**
     * <p>removeDiacritics.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String removeDiacritics( String s )
    {
        return Normalizer.normalize( s, Normalizer.Form.NFD ).replaceAll( "\\p{InCombiningDiacriticalMarks}+", "" );
    }

    /**
     * <p>escapeSemiColon.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String escapeSemiColon(String s)
    {
        return s == null ? null : s.replaceAll( ";", "%3B" );
    }

    /**
     * <p>unescapeSemiColon.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String unescapeSemiColon(String s)
    {
        return s == null ? null : s.replaceAll( "%3B", ";" );
    }
}
