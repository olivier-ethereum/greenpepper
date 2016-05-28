/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package com.greenpepper.confluence.utils.stylesheet;

/**
 * Today, this is not a real factory as it will only give you one instance.
 * The file is still here cause I don't want to take the time and refactor.
 *
 * @author wattazoum
 * @version $Id: $Id
 */
public class StyleSheetExtractorFactory
{
	private static StyleSheetExtractor instance = new DefaultStyleSheetExtractorImpl();

	/**
	 * <p>Getter for the field <code>instance</code>.</p>
	 *
	 * @return a {@link com.greenpepper.confluence.utils.stylesheet.StyleSheetExtractor} object.
	 */
	public static StyleSheetExtractor getInstance()
	{
		return instance;
	}

}
