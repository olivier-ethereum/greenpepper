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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.confluence.importexport.resource.ResourceAccessor;
import com.atlassian.confluence.plugin.webresource.ConfluenceWebResourceManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.themes.Theme;
import com.atlassian.confluence.themes.ThemeManager;
import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.spring.container.ContainerManager;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

/**
 * Stylesheet extractor for version from 2.8+
 */
public class DefaultStyleSheetExtractorImpl
		implements StyleSheetExtractor
{

	private Logger log = Logger.getLogger(StyleSheetExtractor.class);

	private ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();
	private String tableCssContent;
	
	public String renderStyleSheet(Space space)
	{
		StringBuilder css = new StringBuilder();

		css.append('\n');

		// We always put the tables.css content since the @import url contains the server URL that can be out of reach
		// and we want to make sure tables are rendered with borders (too confusing if not!)
		css.append(getTablesCssContent());

		css.append('\n');
		css.append(getCombinedCss(space));

		return css.toString();
	}

	private String getTablesCssContent()
	{
		if (tableCssContent == null)
		{
			InputStream tableCssStream = null;

			try
			{
				ResourceAccessor resourceAccessor = (ResourceAccessor)ContainerManager.getComponent("resourceAccessor");

				tableCssStream = resourceAccessor.getResource("/includes/css/tables.css");

				tableCssContent = IOUtils.toString(tableCssStream);
			}
			catch (Exception ex)
			{
				log.error("Failed to get tables stylesheet. Omitting tables styles.", ex);
				tableCssContent = "/* Failed to get tables stylesheet. Omitting tables styles. */";
			}
			finally
			{
				IOUtils.closeQuietly(tableCssStream);
			}
		}

		return tableCssContent;
	}

	private String getCombinedCss(final Space space)
	{
		final ThemeManager themeManager = (ThemeManager) ContainerManager.getComponent("themeManager");

		final String spaceKey = space == null ? "" : space.getKey();

		Theme activeTheme = themeManager.getGlobalTheme();

		if (StringUtils.isNotEmpty(spaceKey))
		{
			activeTheme = themeManager.getSpaceTheme(spaceKey);
		}

		final ConfluenceWebResourceManager webResourceManager = (ConfluenceWebResourceManager)ContainerManager.getComponent("webResourceManager");

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("spaceKey", spaceKey);
		contextMap.put("globalPrefix", gpUtil.getBaseUrl() + webResourceManager.getGlobalCssResourcePrefix());
		contextMap.put("prefix", gpUtil.getBaseUrl() + webResourceManager.getSpaceCssPrefix(spaceKey));
		contextMap.put("theme", activeTheme);
		contextMap.put("forWysiwyg", Boolean.TRUE);
		contextMap.put("generalUtil", new GeneralUtil());

		return VelocityUtils.getRenderedTemplate("styles/combined-css.vm", contextMap);
	}
}