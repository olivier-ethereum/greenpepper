
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

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.imageio.ImageIO;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
import org.jfree.ui.HorizontalAlignment;
import org.apache.commons.lang.StringUtils;

import com.atlassian.confluence.servlet.download.ExportDownload;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.util.IOUtil;
public abstract class AbstractChartBuilder
{

	/** Constant <code>DEFAULT_FONT_NAME="Helvetica"</code> */
	protected static final String DEFAULT_FONT_NAME = "Helvetica";
	/** Constant <code>DEFAULT_TITLE_FONT</code> */
	protected static final Font DEFAULT_TITLE_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 12);
	/** Constant <code>DEFAULT_SUBTITLE_FONT</code> */
	protected static final Font DEFAULT_SUBTITLE_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 11);
	/** Constant <code>DEFAULT_SUBTITLE2_FONT</code> */
	protected static final Font DEFAULT_SUBTITLE2_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 11);
	/** Constant <code>DEFAULT_AXIS_FONT</code> */
	protected static final Font DEFAULT_AXIS_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 10);
	/** Constant <code>DEFAULT_LABEL_FONT</code> */
	protected static final Font DEFAULT_LABEL_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 10);
	/** Constant <code>TRANSPARENT_COLOR</code> */
	protected static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
	/** Constant <code>GREEN_COLOR</code> */
	protected static final Color GREEN_COLOR = new Color(Integer.parseInt("33cc00", 16));

	private ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
	
	protected ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();
	protected HistoricParameters settings;

	/**
	 * <p>Constructor for AbstractChartBuilder.</p>
	 *
	 * @param settings a {@link com.greenpepper.confluence.macros.historic.HistoricParameters} object.
	 */
	protected AbstractChartBuilder(HistoricParameters settings)
	{
		this.settings = settings;
	}

	/**
	 * <p>generateChart.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	public abstract String generateChart()
			throws GreenPepperServerException;

	/**
	 * <p>getChartMap.</p>
	 *
	 * @param chartMapId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.io.IOException if any.
	 */
	public String getChartMap(String chartMapId)
			throws IOException
	{
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);

		try
		{
			ChartUtilities.writeImageMap(pw, chartMapId, chartRenderingInfo,
										 new StandardToolTipTagFragmentGenerator(),
										 new StandardURLTagFragmentGenerator());
		}
		finally
		{
			IOUtil.closeQuietly(pw);
		}

		return writer.toString();
	}

	/**
	 * <p>getDownloadPath.</p>
	 *
	 * @param chartImage a {@link java.awt.image.BufferedImage} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.io.IOException if any.
	 */
	@SuppressWarnings("deprecated")
	protected String getDownloadPath(BufferedImage chartImage)
			throws IOException
	{
		File imageOutputFile = ExportDownload.createTempFile("chart", ".png");

		ImageIO.write(chartImage, "png", imageOutputFile);

		return ExportDownload.getUrl(imageOutputFile, "image/png");
	}

	/**
	 * <p>createChartImage.</p>
	 *
	 * @param chart a {@link org.jfree.chart.JFreeChart} object.
	 * @return a {@link java.awt.image.BufferedImage} object.
	 */
	protected BufferedImage createChartImage(JFreeChart chart)
	{
		return chart.createBufferedImage(settings.getWidth(), settings.getHeight(), chartRenderingInfo);
	}

	/**
	 * <p>customizeTitle.</p>
	 *
	 * @param title a {@link org.jfree.chart.title.TextTitle} object.
	 * @param font a {@link java.awt.Font} object.
	 */
	protected void customizeTitle(TextTitle title, Font font)
	{
		title.setFont(font);
		title.setTextAlignment(HorizontalAlignment.LEFT);
		title.setPaint(Color.BLACK);
		title.setBackgroundPaint(TRANSPARENT_COLOR);
	}

	/**
	 * <p>customizeAxis.</p>
	 *
	 * @param axis a {@link org.jfree.chart.axis.Axis} object.
	 */
	protected void customizeAxis(Axis axis)
	{
		axis.setLabelFont(DEFAULT_LABEL_FONT);
		axis.setTickLabelFont(DEFAULT_AXIS_FONT);
	}

	/**
	 * <p>addSubTitle.</p>
	 *
	 * @param chart a {@link org.jfree.chart.JFreeChart} object.
	 * @param subTitle a {@link java.lang.String} object.
	 * @param font a {@link java.awt.Font} object.
	 */
	protected void addSubTitle(JFreeChart chart, String subTitle, Font font)
	{
		if (StringUtils.isNotEmpty(subTitle))
		{
			TextTitle chartSubTitle = new TextTitle(subTitle);
			customizeTitle(chartSubTitle, font);
			chart.addSubtitle(chartSubTitle);
		}
	}
}
