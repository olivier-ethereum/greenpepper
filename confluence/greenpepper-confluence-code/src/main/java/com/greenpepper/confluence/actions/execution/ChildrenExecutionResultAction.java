
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

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.greenpepper.confluence.actions.AbstractGreenPepperAction;
import com.greenpepper.confluence.macros.historic.HistoricParameters;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Specification;
public class ChildrenExecutionResultAction
		extends AbstractGreenPepperAction
{

	private Long id;
	private Specification specification;
	private String spaceKey;
	private int width;
	private int height;
	private String sut;
	private boolean showIgnored;
	private int maxResult;

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

	/** {@inheritDoc} */
	public void setSpaceKey(String spaceKey)
	{
		this.spaceKey = spaceKey;
	}

	/**
	 * <p>Setter for the field <code>width</code>.</p>
	 *
	 * @param width a int.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * <p>Setter for the field <code>height</code>.</p>
	 *
	 * @param height a int.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * <p>Setter for the field <code>sut</code>.</p>
	 *
	 * @param sut a {@link java.lang.String} object.
	 */
	public void setSut(String sut)
	{
		this.sut = sut;
	}

	/**
	 * <p>Setter for the field <code>showIgnored</code>.</p>
	 *
	 * @param showIgnored a boolean.
	 */
	public void setShowIgnored(boolean showIgnored)
	{
		this.showIgnored = showIgnored;
	}

	/**
	 * <p>Setter for the field <code>maxResult</code>.</p>
	 *
	 * @param maxResult a int.
	 */
	public void setMaxResult(int maxResult)
	{
		this.maxResult = maxResult;
	}

	/**
	 * <p>getPageTitle.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPageTitle()
	{
		if (specification == null) return "";
		return specification.getName();
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
			specification = gpUtil.getGPServerService().getSpecificationById(getId());
		}
		catch (GreenPepperServerException e)
		{
			addActionError(e.getId());
		}

		return SUCCESS;
	}

	/**
	 * <p>getRenderedContent.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@HtmlSafe
	public String getRenderedContent()
	{
		Page page = gpUtil.getPageManager().getPage(spaceKey, getPageTitle());
		if (page == null) return gpUtil.getText("greenpepper.children.pagenotfound"); 

		StringBuilder content = new StringBuilder("{greenpepper-historic:");

		content.append(HistoricParameters.SPACEKEY).append("=").append(spaceKey)
				.append("|").append(HistoricParameters.PAGETITLE).append("=").append(page.getTitle().trim())
				.append("|").append(HistoricParameters.WIDTH).append("=").append(width)
				.append("|").append(HistoricParameters.HEIGHT).append("=").append(height)
				.append("|").append(HistoricParameters.SUT).append("=").append(sut)
				.append("|").append(HistoricParameters.SHOWIGNORED).append("=").append(showIgnored)
				.append("|").append(HistoricParameters.MAXRESULT).append("=").append(maxResult);
		
		content.append("}");

//		return gpUtil.getWikiStyleRenderer().convertWikiToXHtml(page.toPageContext(), content.toString());
		return gpUtil.getViewRenderer().render(page);
	}
}
