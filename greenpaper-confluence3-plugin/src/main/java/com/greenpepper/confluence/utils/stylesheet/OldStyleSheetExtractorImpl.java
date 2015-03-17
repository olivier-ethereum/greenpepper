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

import java.lang.reflect.Method;

import com.atlassian.confluence.spaces.Space;
import com.greenpepper.util.ExceptionImposter;

/**
 * Stylesheet extractor for old version (for Confluence version <= 2.7)
 */
public class OldStyleSheetExtractorImpl
		implements StyleSheetExtractor
{
	private static Method renderSpaceStylesheetMethod;

	public String renderStyleSheet(Space space)
	{
		// Note : this will not compile if using confluence version 2.9+ in the pom.xml - this is ok
		//        since we want to be compatible with binaries compiled with previous version

		// @todo : for next version of Confluence that break this, use reflection since this class
		//         will only be use if confuence version <= 2.7
		//return StylesheetAction.renderSpaceStylesheet(space);

		try
		{
			throw new Exception("Unsuported confluence version. Need 2.9+");
//			if (renderSpaceStylesheetMethod == null)
//			{
//				renderSpaceStylesheetMethod = StylesheetAction.class.getMethod("renderSpaceStylesheet",
//																			   new Class[] { Space.class});
//			}
//
//			return (String)renderSpaceStylesheetMethod.invoke(null, space);
		}
		catch (Exception e)
		{
			throw ExceptionImposter.imposterize(e);
		}
	}
}
