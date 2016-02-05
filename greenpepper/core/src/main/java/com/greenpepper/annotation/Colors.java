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
package com.greenpepper.annotation;

import java.awt.Color;

/**
 * <p>Colors class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Colors
{
    /** Constant <code>YELLOW="#FFFFAA"</code> */
    public static final String YELLOW = "#FFFFAA"; // 255, 255, 170
    /** Constant <code>GREEN="#AAFFAA"</code> */
    public static final String GREEN = "#AAFFAA"; // 170, 255, 170
    /** Constant <code>GRAY="#CCCCCC"</code> */
    public static final String GRAY = "#CCCCCC"; // 204, 204, 204
    /** Constant <code>RED="#FFAAAA"</code> */
    public static final String RED = "#FFAAAA"; // 255, 170, 170 
	/** Constant <code>ORANGE="#FFC800"</code> */
	public static final String ORANGE = "#FFC800"; // 255, 200, 0

    /**
     * <p>toRGB.</p>
     *
     * @param color a {@link java.awt.Color} object.
     * @return a {@link java.lang.String} object.
     */
    public static String toRGB(Color color)
    {
        String rgb = "#";
        rgb += formatAsHex( color.getRed() );
        rgb += formatAsHex( color.getGreen() );
        rgb += formatAsHex( color.getBlue() );
        return rgb;
    }

    private static String formatAsHex(int value)
    {
        String hexaString = Integer.toString( value, 16 ).toUpperCase();
        if (hexaString.length() == 1)
            hexaString = "0" + hexaString;
        return hexaString;
    }

    private Colors()
    {
    }
}
