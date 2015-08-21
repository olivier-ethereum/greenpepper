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
package com.greenpepper.confluence.macros.historic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.atlassian.confluence.core.DateFormatter;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.util.StringUtil;

public class LinearExecutionChartBuilder
		extends AbstractChartBuilder
{

	private final List<Execution> executions;

	private LinearExecutionChartBuilder(HistoricParameters settings, List<Execution> executions)
	{
		super(settings);

		this.executions = executions;
	}

	public static AbstractChartBuilder newInstance(HistoricParameters settings, List<Execution> executions)
	{
		return new LinearExecutionChartBuilder(settings, executions);
	}

	public String generateChart()
			throws GreenPepperServerException
	{
		try
		{
			CategoryDataset dataset = generateDataset();

			JFreeChart chart = createChart(dataset);
			
			BufferedImage chartImage = createChartImage(chart);

			return getDownloadPath(chartImage);
		}
		catch (IOException e)
		{
			throw new GreenPepperServerException("greenpepper.server.generalexeerror", e.getMessage(), e);
		}
	}

	private CategoryDataset generateDataset()
	{
		final DateFormatter dateFormatter = gpUtil.getUserPreferencesDateFormatter();

		final String successLabel = gpUtil.getText("greenpepper.historic.success");
		final String failuresLabel = gpUtil.getText("greenpepper.historic.failures");
		final String ignoredLabel = gpUtil.getText("greenpepper.historic.ignored");

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int count = executions.size();

		for (Execution exec : executions)
		{
			// Need to prefix with the count since we can have execution at the same time (formatDateTime is removing seconds!)
			String category = String.format("%2d. %s", count, dateFormatter.formatDateTime(exec.getExecutionDate()));

			if (!StringUtil.isEmpty(exec.getSections()))
			{
				category += "*";
			}

			ChartLongValue value = new ChartLongValue(category, exec.getId());

			double failureCount = exec.getErrors() + exec.getFailures()  + (exec.hasException() ? 1 : 0);

			dataset.addValue(new Double(exec.getSuccess()), successLabel, value);

			if (settings.isShowIgnored())
			{
				dataset.addValue(new Double(exec.getIgnored()), ignoredLabel, value);
			}

			dataset.addValue(new Double(failureCount), failuresLabel, value);

			count--;
		}

		return dataset;
	}

	private JFreeChart createChart(CategoryDataset dataset)
			throws IOException
	{
		JFreeChart chart = ChartFactory.createAreaChart(
				settings.getTitle(), null, gpUtil.getText("greenpepper.historic.chart.x.title"), dataset,
				PlotOrientation.VERTICAL, true, false, false);

		customizeChart(chart);

		return chart;
	}

	private void customizeChart(JFreeChart chart)
			throws IOException
	{
		chart.setBackgroundPaint(Color.white);
		chart.setBorderVisible(settings.isBorder());

		TextTitle chartTitle = chart.getTitle();
		customizeTitle(chartTitle, DEFAULT_TITLE_FONT);

		addSubTitle(chart, settings.getSubTitle(), DEFAULT_SUBTITLE_FONT);
		addSubTitle(chart, settings.getSubTitle2(), DEFAULT_SUBTITLE2_FONT);

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		plot.setNoDataMessage(gpUtil.getText("greenpepper.historic.nodata"));

		CategoryItemRenderer renderer = plot.getRenderer();

		int index = 0;
		renderer.setSeriesPaint(index++, GREEN_COLOR);
		if (settings.isShowIgnored()) renderer.setSeriesPaint(index++, Color.yellow);
		renderer.setSeriesPaint(index, Color.red);

		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		renderer.setItemURLGenerator(new CategoryURLGenerator() {

			public String generateURL(CategoryDataset data, int series, int category)
			{
				Comparable valueKey = data.getColumnKey(category);
				ChartLongValue value = (ChartLongValue)valueKey;
				return "javascript:" + settings.getExecutionUID() + "_showExecutionResult('" + value.getId() + "');";
			}
		});

		CategoryAxis domainAxis = plot.getDomainAxis();
		customizeAxis(domainAxis);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);

		ValueAxis rangeAxis = plot.getRangeAxis();
		customizeAxis(rangeAxis);
		rangeAxis.setLowerBound(0);

		if (rangeAxis instanceof NumberAxis)
		{
			((NumberAxis) rangeAxis).setTickUnit(new NumberTickUnit(1));
		}

		plot.setForegroundAlpha(0.8f);
	}
}