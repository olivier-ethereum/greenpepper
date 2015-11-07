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
package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.actions.execution.LabelExecutionAction;
import com.greenpepper.confluence.macros.historic.AbstractChartBuilder;
import com.greenpepper.confluence.macros.historic.AggregationExecutionChartBuilder;
import com.greenpepper.confluence.macros.historic.LinearExecutionChartBuilder;
import com.greenpepper.confluence.macros.historic.HistoricParameters;
import com.greenpepper.confluence.utils.MacroCounter;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;

public class GreenPepperHistoric
		extends AbstractGreenPepperMacro
{

	@SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException
	{
        try
        {
			Map contextMap = MacroUtils.defaultVelocityContext();
			String spaceKey = getSpaceKey(parameters, renderContext, false);
			Page page = getPage(parameters, renderContext, spaceKey);

			String executionUID = "GP_HISTORIC_" + MacroCounter.instance().getNextCount();

			HistoricParameters settings = new HistoricParameters(parameters, spaceKey, page, executionUID);

			AbstractChartBuilder chartBuilder;

			if (settings.getLabels() == null && settings.isNoChildren())
			{
				Specification specification = gpUtil.getSpecification(page.getSpaceKey(), page.getTitle().trim());

				List<Execution> executions = gpUtil.getGPServerService().getSpecificationExecutions(
						specification, settings.getTargetedSystemUnderTest(), settings.getMaxResult());

				chartBuilder = LinearExecutionChartBuilder.newInstance(settings, executions);
			}
			else
			{
				List<Specification> specifications = new ArrayList<Specification>();

				if (!settings.isNoChildren())
				{
					List<Specification> specificationsChildren = getTargetSpecificationsChildren(
							page, settings.getSut(), settings.isAllChildren());
					specifications.addAll(specificationsChildren);
				}

				if (settings.getLabels() != null)
				{
					List<Specification> specificationsWithLabel = getTargetSpecificationsLabels(
							settings.getSpaceKey(), settings.getSut(), settings.getLabels());

					if (settings.isNoChildren())
					{
						specifications.addAll(specificationsWithLabel);
					}
					else
					{
						specifications.retainAll(specificationsWithLabel);
					}
				}

				List<Execution> executions = aggregateExecutions(specifications, settings.getTargetedSystemUnderTest(),
																 settings.getMaxResult());

				chartBuilder = AggregationExecutionChartBuilder.newInstance(settings, executions);
			}

			String chartMapId = executionUID + "_map";

			contextMap.put("executionUID", executionUID);
			contextMap.put("chartImage", chartBuilder.generateChart());
			contextMap.put("settings", settings);
			contextMap.put("chartMapHtml", chartBuilder.getChartMap(chartMapId));
			contextMap.put("chartMapId", "#" + chartMapId);

			return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperHistoric.vm", contextMap);
        }
        catch (GreenPepperServerException gpe)
        {
            return getErrorView("greenpepper.historic.macroid", gpe.getId());
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.historic.macroid", e.getMessage());
        }
	}

	private List<Specification> getTargetSpecificationsChildren(final Page page, String sut, boolean allChildren)
			throws GreenPepperServerException
	{
		List<Page> pages = new ArrayList<Page>() {{
			add(page);
		}};

		fillChildrenPages(page, allChildren, pages);

		return getSpecificationsFromPages(sut, pages);
	}

	private boolean isSpecificationAssociatedToSut(Specification specification, String systemUnderTest)
	{
		Set<SystemUnderTest> suts = specification.getTargetedSystemUnderTests();

		for (SystemUnderTest sut : suts)
		{
			if (sut.getName().equals(systemUnderTest))
			{
				return true;
			}
		}

		return false;
	}

	private void fillChildrenPages(Page page, boolean allChildren, List<Page> childrens)
	{
		@SuppressWarnings("unchecked")
		List<Page> pageChildren = gpUtil.getContentPermissionManager().getPermittedChildren(page, gpUtil.getRemoteUser());

		for (Page child : pageChildren)
		{
			if (gpUtil.isExecutable(child))
			{
				childrens.add(child);
			}

			if (allChildren)
			{
				fillChildrenPages(child, allChildren, childrens);
			}
		}
	}

	private List<Specification> getTargetSpecificationsLabels(String spaceKey, String sut, String labels)
			throws GreenPepperServerException
	{
		LabelExecutionAction action = new LabelExecutionAction();

		action.setSpaceKey(spaceKey);
		action.setLabels(labels);
		action.setForcedSuts(sut);

		List<Page> pages = action.getExecutableList();

		return getSpecificationsFromPages(sut, pages);
	}

	private void sort(List<Specification> specifications)
	{
		Collections.sort(specifications, new Comparator<Specification>() {
			public int compare(Specification o1, Specification o2)
			{
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
	}

	protected List<Specification> getSpecificationsFromPages(String sut, List<Page> pages)
			throws GreenPepperServerException
	{
		List<Specification> specifications = new ArrayList<Specification>();

		for (Page childPage : pages)
		{
			Specification specification = gpUtil.getSpecification(childPage);

			if (specification != null && isSpecificationAssociatedToSut(specification, sut))
			{
				specifications.add(specification);
			}
		}

		sort(specifications);

		return specifications;
	}

	private List<Execution> aggregateExecutions(List<Specification> specifications,
												SystemUnderTest targetedSystemUnderTest, int maxResult)
			throws GreenPepperServerException
	{
		List<Execution> executions = new ArrayList<Execution>();

		for (Specification specification : specifications)
		{
			List<Execution> execs = gpUtil.getGPServerService().getSpecificationExecutions(
					specification, targetedSystemUnderTest, maxResult);

			int failureCount = 0;
			int errorCount = 0;
			int ignoredCount = 0;
			int successCount = 0;

			for (Execution exec : execs)
			{
				failureCount += exec.getFailures();
				errorCount += exec.getErrors() + (exec.hasException() ? 1 : 0);
				ignoredCount += exec.getIgnored();
				successCount += exec.getSuccess();
			}

			Execution execution = new Execution();
			execution.setErrors(errorCount);
			execution.setFailures(failureCount);
			execution.setIgnored(ignoredCount);
			execution.setSuccess(successCount);
			execution.setSpecification(specification);
			execution.setId(specification.getId());
			executions.add(execution);
		}

		return executions;
	}
}