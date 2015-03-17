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

import com.greenpepper.confluence.utils.ConfluenceVersion;
import com.greenpepper.util.ClassUtils;
import com.greenpepper.util.ExceptionImposter;

public class StyleSheetExtractorFactory
{
	private static StyleSheetExtractor instance;

	public static StyleSheetExtractor getInstance()
	{
		if (instance == null)
		{
			instance = createStyleSheetExtractor(ConfluenceVersion.getCurrentVersion().compareTo(ConfluenceVersion.V28X) < 0 ?
												 "OldStyleSheetExtractorImpl" : "DefaultStyleSheetExtractorImpl");
		}

		return instance;
	}

	private static StyleSheetExtractor createStyleSheetExtractor(String implementationClassName)
	{
		// We must create the instance dynamically coz the new implementation use classes not found
		// in previous version of confluence (and we just don't want to maintain a jar by version of confluence!)
		try
		{
			Class clazz = ClassUtils.loadClass(
					StyleSheetExtractorFactory.class.getPackage().getName() + "." + implementationClassName);

			return (StyleSheetExtractor)clazz.newInstance();
		}
		catch (Exception e)
		{
			throw ExceptionImposter.imposterize(e);
		}
	}
}
