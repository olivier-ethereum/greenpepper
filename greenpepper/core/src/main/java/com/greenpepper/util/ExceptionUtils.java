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

import java.io.StringWriter;

import com.greenpepper.GreenPepper;

/**
 * Utility methods for manipulating <code>Throwable</code> objects.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class ExceptionUtils
{
    /** Constant <code>FULL</code> */
    public final static Integer FULL = Integer.MAX_VALUE;
    
    private ExceptionUtils()
    {
    }

    /**
     * <p>stackTrace.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String stackTrace( Throwable t, String separator )
    {
        return stackTrace( t, separator, FULL);
    }

    /**
     * <p>stackTrace.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     * @param separator a {@link java.lang.String} object.
     * @param depth a int.
     * @return a {@link java.lang.String} object.
     */
    public static String stackTrace( Throwable t, String separator, int depth )
    {
        if (GreenPepper.isDebugEnabled()) depth = FULL;
        
        StringWriter sw = new StringWriter();
        sw.append( t.toString() );
        for (int i = 0; i < t.getStackTrace().length && i <= depth; i++)
        {
            StackTraceElement element = t.getStackTrace()[i];
            sw.append( separator ).append( element.toString() );
        }
        return sw.toString();
    }
}
