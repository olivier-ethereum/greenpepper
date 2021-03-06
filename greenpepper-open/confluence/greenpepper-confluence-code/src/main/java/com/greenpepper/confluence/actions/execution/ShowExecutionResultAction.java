
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.confluence.actions.execution;

import org.apache.commons.lang.StringUtils;

import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.greenpepper.confluence.actions.AbstractGreenPepperAction;
import com.greenpepper.confluence.utils.stylesheet.StyleSheetExtractorFactory;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Execution;
public class ShowExecutionResultAction
		extends AbstractGreenPepperAction
{
	private Long id;
	private Execution execution;

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.Long} object.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * <p>Getter for the field <code>execution</code>.</p>
	 *
	 * @return a {@link com.greenpepper.server.domain.Execution} object.
	 */
	public Execution getExecution()
	{
		return execution;
	}

	/**
	 * <p>Setter for the field <code>execution</code>.</p>
	 *
	 * @param execution a {@link com.greenpepper.server.domain.Execution} object.
	 */
	public void setExecution(Execution execution)
	{
		this.execution = execution;
	}

	/**
	 * <p>getTitleHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getTitleHtml()
	{
		return getTitle(false);
	}

	/**
	 * <p>getTitleWithAnchorHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getTitleWithAnchorHtml()
	{
		return getTitle(true);
	}
	
	private String getTitle(boolean useAnchor)
	{
		if (execution == null) return "";

		StringBuilder title = new StringBuilder();

		title.append(useAnchor ? getTitleAnchor() : execution.getSpecification().getName()).append(' ')
				.append(gpUtil.getText("greenpepper.execution.for"))
				.append(' ').append(gpUtil.getText("greenpepper.execution.openbraket"))
				.append(execution.getSystemUnderTest().getName()).append(' ')
				.append(gpUtil.getText("greenpepper.execution.closebraket")).append(" - ")
				.append(getDateFormatter().formatDateTime(execution.getExecutionDate()));
		
		return title.toString();
	}

	private String getTitleAnchor()
	{
		String resolvedName = null;

		try
		{
			resolvedName = execution.getSpecification().getResolvedName();
		}
		catch (GreenPepperServerException e)
		{
			//leave to null
		}

		return resolvedName == null ? execution.getSpecification().getName()
									: String.format("<a href=\"%s\" alt=\"\">%s</a>", resolvedName,
													execution.getSpecification().getName());
	}

	/**
	 * <p>getStylesheetHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getStylesheetHtml()
	{
		Space space = gpUtil.getSpaceManager().getSpace(getSpaceKey());
		return String.format("<style>\n%s\n</style>\n<base href=\"%s\"/>\n",
							 StyleSheetExtractorFactory.getInstance().renderStyleSheet(space), gpUtil.getBaseUrl());
	}

	/**
	 * <p>getHasException.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getHasException()
	{
		return execution != null && execution.hasException();
	}

	/**
	 * <p>getExceptionHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getExceptionHtml()
	{
		return execution.getExecutionErrorId();
	}

	/**
	 * <p>getHasBody.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getHasBody()
	{
		return execution != null && StringUtils.isNotEmpty(execution.getResults());
	}

	/**
	 * <p>getBodyHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getBodyHtml()
	{
		String body = execution.getResults().trim();

		return body.replaceAll("<html>", "").replaceAll("</html>", "");
	}

	/**
	 * <p>getHasSections.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getHasSections()
	{
		return execution != null && StringUtils.isNotEmpty(execution.getSections());
	}

	/**
	 * <p>getSectionsHtml.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getSectionsHtml()
	{
		return String.format("%s : %s", gpUtil.getText("greenpepper.page.sections"), execution.getSections());
	}
	
	/**
	 * <p>show.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String show()
	{
		try
		{
			execution = gpUtil.getGPServerService().getSpecificationExecution(getId());
		}
		catch (GreenPepperServerException e)
		{
			addActionError(e.getId());
		}

		return SUCCESS;
	}
}
