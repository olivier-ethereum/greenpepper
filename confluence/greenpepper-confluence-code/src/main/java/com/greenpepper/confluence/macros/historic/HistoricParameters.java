
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
package com.greenpepper.confluence.macros.historic;

import java.util.List;
import java.util.Map;

import com.atlassian.confluence.pages.Page;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.SystemUnderTest;
public class HistoricParameters
{

	/** Constant <code>PAGETITLE="pagetitle"</code> */
	public static final String PAGETITLE = "pagetitle";
	/** Constant <code>SPACEKEY="spacekey"</code> */
	public static final String SPACEKEY = "spacekey";
	/** Constant <code>WIDTH="width"</code> */
	public static final String WIDTH = "width";
	/** Constant <code>HEIGHT="height"</code> */
	public static final String HEIGHT = "height";
	/** Constant <code>BORDER="border"</code> */
	public static final String BORDER = "border";
	/** Constant <code>CHILDREN="children"</code> */
	public static final String CHILDREN = "children";
	/** Constant <code>MAXRESULT="maxresult"</code> */
	public static final String MAXRESULT = "maxresult";
	/** Constant <code>SUT="sut"</code> */
	public static final String SUT = "sut";
	/** Constant <code>SHOWIGNORED="showignored"</code> */
	public static final String SHOWIGNORED = "showignored";
	/** Constant <code>TITLE="title"</code> */
	public static final String TITLE = "title";
	/** Constant <code>SUBTITLE="subtitle"</code> */
	public static final String SUBTITLE = "subtitle";
	/** Constant <code>SUBTITLE2="subtitle2"</code> */
	public static final String SUBTITLE2 = "subtitle2";
	/** Constant <code>LABELS="labels"</code> */
	public static final String LABELS = "labels";
	/** Constant <code>POPUP_WIDTH="popupwidth"</code> */
	public static final String POPUP_WIDTH = "popupwidth";
	/** Constant <code>POPUP_HEIGHT="popupheight"</code> */
	public static final String POPUP_HEIGHT = "popupheight";

	/** Constant <code>DEFAULT_WIDTH=500</code> */
	public static final int DEFAULT_WIDTH = 500;
	/** Constant <code>DEFAULT_HEIGHT=500</code> */
	public static final int DEFAULT_HEIGHT = 500;
	/** Constant <code>DEFAULT_MAXRESULT=30</code> */
	public static final int DEFAULT_MAXRESULT = 30;
	/** Constant <code>DEFAULT_POPUP_WIDTH=800</code> */
	public static final int DEFAULT_POPUP_WIDTH = 800;
	/** Constant <code>DEFAULT_POPUP_HEIGHT=600</code> */
	public static final int DEFAULT_POPUP_HEIGHT = 600;

	public enum Children
	{
		False, First, All;

		public static Children toChildren(String id)
		{
			if (id.equalsIgnoreCase("first"))
			{
				return First;
			}
			else if (id.equalsIgnoreCase("all"))
			{
				return All;
			}
			else
			{
				return False;
			}
		}
	}

	private final ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();

	private final String spaceKey;
	private final Page page;
	private final String executionUID;
	private final int width;
	private final int height;
	private final boolean border;
	private final Children children;
	private final int maxResult;
	private final SystemUnderTest targetedSystemUnderTest;
	private final boolean showIgnored;
	private final String title;
	private final String subTitle;
	private final String subTitle2;
	private final String labels;
	private final int popupWidth;
	private final int popupHeight;

	/**
	 * <p>Constructor for HistoricParameters.</p>
	 *
	 * @param parameters a {@link java.util.Map} object.
	 * @param spaceKey a {@link java.lang.String} object.
	 * @param page a {@link com.atlassian.confluence.pages.Page} object.
	 * @param executionUID a {@link java.lang.String} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	public HistoricParameters(Map parameters, String spaceKey, Page page, String executionUID)
			throws GreenPepperServerException
	{
		this.spaceKey = spaceKey;
		this.page = page;
		this.executionUID = executionUID;
		
		width = getParameters(parameters, WIDTH, DEFAULT_WIDTH);
		height = getParameters(parameters, HEIGHT, DEFAULT_HEIGHT);
		border = getParameters(parameters, BORDER, false);

		children = getParameters(parameters, CHILDREN, Children.False);

		maxResult = getParameters(parameters, MAXRESULT, DEFAULT_MAXRESULT);
		targetedSystemUnderTest  = getSutParameter(parameters, SUT, page);
		showIgnored = getParameters(parameters, SHOWIGNORED, false);

		title = getParameters(parameters, TITLE, (String)null);
		subTitle = getParameters(parameters, SUBTITLE, (String)null);
		subTitle2 = getParameters(parameters, SUBTITLE2, (String)null);

		labels = getParameters(parameters, LABELS, (String)null);

		popupWidth = getParameters(parameters, POPUP_WIDTH, isNoChildren() ? DEFAULT_POPUP_WIDTH : width);
		popupHeight = getParameters(parameters, POPUP_HEIGHT, isNoChildren() ? DEFAULT_POPUP_HEIGHT : height);
	}

	/**
	 * <p>Getter for the field <code>page</code>.</p>
	 *
	 * @return a {@link com.atlassian.confluence.pages.Page} object.
	 */
	public Page getPage()
	{
		return page;
	}

	/**
	 * <p>Getter for the field <code>spaceKey</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSpaceKey()
	{
		return spaceKey;
	}

	/**
	 * <p>Getter for the field <code>executionUID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getExecutionUID()
	{
		return executionUID;
	}

	/**
	 * <p>Getter for the field <code>width</code>.</p>
	 *
	 * @return a int.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * <p>Getter for the field <code>height</code>.</p>
	 *
	 * @return a int.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * <p>isBorder.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isBorder()
	{
		return border;
	}

	/**
	 * <p>Getter for the field <code>children</code>.</p>
	 *
	 * @return a {@link com.greenpepper.confluence.macros.historic.HistoricParameters.Children} object.
	 */
	public Children getChildren()
	{
		return children;
	}

	/**
	 * <p>isNoChildren.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNoChildren()
	{
		return Children.False == children;
	}

	/**
	 * <p>isAllChildren.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAllChildren()
	{
		return Children.All == children;
	}

	/**
	 * <p>isFirstChildren.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFirstChildren()
	{
		return Children.First == children;
	}

	/**
	 * <p>Getter for the field <code>maxResult</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxResult()
	{
		return maxResult;
	}

	/**
	 * <p>Getter for the field <code>targetedSystemUnderTest</code>.</p>
	 *
	 * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 */
	public SystemUnderTest getTargetedSystemUnderTest()
	{
		return targetedSystemUnderTest;
	}

	/**
	 * <p>getSut.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSut()
	{
		return getTargetedSystemUnderTest().getName();
	}

	/**
	 * <p>isShowIgnored.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isShowIgnored()
	{
		return showIgnored;
	}

	/**
	 * <p>getIsShowIgnored.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getIsShowIgnored()
	{
		return isShowIgnored();
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle()
	{
		return title == null ? getDefaultTitle() : title;
	}

	/**
	 * <p>Getter for the field <code>subTitle</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSubTitle()
	{
		return subTitle == null ? getDefaultSubTitle() : subTitle;
	}

	/**
	 * <p>Getter for the field <code>subTitle2</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSubTitle2()
	{
		return subTitle2 == null ? getDefaultSubTitle2() : subTitle2;
	}

	/**
	 * <p>Getter for the field <code>labels</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLabels()
	{
		return labels;
	}

	/**
	 * <p>Getter for the field <code>popupHeight</code>.</p>
	 *
	 * @return a int.
	 */
	public int getPopupHeight()
	{
		return popupHeight;
	}

	/**
	 * <p>Getter for the field <code>popupWidth</code>.</p>
	 *
	 * @return a int.
	 */
	public int getPopupWidth()
	{
		return popupWidth;
	}

	private String getDefaultTitle()
	{
		return gpUtil.getText("greenpepper.historic.chart.title");
	}

	private String getDefaultSubTitle()
	{
		StringBuilder subTitle = new StringBuilder();

		if (getLabels() != null)
		{
			subTitle.append(gpUtil.getText("greenpepper.execution.forlabels"))
					.append(' ').append(gpUtil.getText("greenpepper.execution.openbraket")).append(' ')
					.append(getLabels())
					.append(' ').append(gpUtil.getText("greenpepper.execution.closebraket")).append(' ')
					.append(gpUtil.getText("greenpepper.execution.and")).append(' ');
		}

		subTitle.append(gpUtil.getText("greenpepper.execution.for"));

		if (!isNoChildren())
		{
			subTitle.append(isAllChildren() ?
							 gpUtil.getText("greenpepper.children.all")  :
							 gpUtil.getText("greenpepper.children.firstlvl"))
					.append(' ').append(gpUtil.getText("greenpepper.children.childrenof")).append(' ');
		}

		subTitle.append(gpUtil.getText("greenpepper.execution.openbraket"))
				.append(' ').append(getPage().getTitle().trim()).append(' ')
				.append(gpUtil.getText("greenpepper.execution.closebraket"));

		subTitle.append(' ').append(gpUtil.getText("greenpepper.execution.on"))
				.append(' ').append(getSpaceKey()).append(' ')
				.append(gpUtil.getText("greenpepper.execution.space"));

		return subTitle.toString();
	}

	private String getDefaultSubTitle2()
	{
		return gpUtil.getText("greenpepper.historic.chart.subtitle2", getSut(), getMaxResult());
	}

	private String getParameters(Map parameters, String parameterName, String defaultValue)
	{
		String value = (String)parameters.get(parameterName);
		return value == null ? defaultValue : value;
	}

	private int getParameters(Map parameters, String parameterName, int defaultValue)
	{
		String value = (String)parameters.get(parameterName);
		return value == null ? defaultValue : Integer.parseInt(value);
	}

	private boolean getParameters(Map parameters, String parameterName, boolean defaultValue)
	{
		String value = (String)parameters.get(parameterName);
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}

	private Children getParameters(Map parameters, String parameterName, Children defaultValue)
	{
		String value = (String)parameters.get(parameterName);
		return value == null ? defaultValue : Children.toChildren(value);
	}

	private SystemUnderTest getSutParameter(Map parameters, String parameterName, Page page)
			throws GreenPepperServerException
	{
		String sut = (String)parameters.get(parameterName);

		if (sut == null)
		{
			if (gpUtil.isExecutable(page))
			{
				String selectedSut = gpUtil.getSelectedSystemUnderTestInfo(getPage());
				sut = selectedSut.substring(selectedSut.indexOf('@') + 1);
			}
			else
			{
				Repository repository = gpUtil.getHomeRepository(getSpaceKey());

				List<SystemUnderTest> allSuts = gpUtil.getGPServerService().getSystemUnderTestsOfAssociatedProject(
						repository.getUid());
				
				for (SystemUnderTest s : allSuts)
				{
					if (s.isDefault())
					{
						return s;
					}
				}
			}
		}

		return findTargetSystemUnderTest(sut, getSpaceKey());
	}

	private SystemUnderTest findTargetSystemUnderTest(String sut, String spaceKey)
			throws GreenPepperServerException
	{
		SystemUnderTest targetedSystemUnderTest = null;
		List<SystemUnderTest> suts = gpUtil.getSystemsUnderTests(spaceKey);

		for (SystemUnderTest s : suts)
		{
			if (s.getName().equals(sut))
			{
				targetedSystemUnderTest = s;
				break;
			}
		}

		if (targetedSystemUnderTest == null)
		{
			throw new GreenPepperServerException("greenpepper.historic.sutnotinselection", sut);
		}

		return targetedSystemUnderTest;
	}
}
