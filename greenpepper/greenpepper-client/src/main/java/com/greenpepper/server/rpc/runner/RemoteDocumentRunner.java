
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
package com.greenpepper.server.rpc.runner;

import java.util.Locale;

import com.greenpepper.runner.SpecificationRunner;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.rpc.runner.report.Report;
import com.greenpepper.server.rpc.runner.report.ReportGenerator;
public class RemoteDocumentRunner
		implements SpecificationRunner
{

	private XmlRpcRemoteRunner xmlRpcRemoteRunner;
	private SpecificationRunnerMonitor monitor;
	private String project;
	private String systemUnderTest;
	private String repositoryId;
	private ReportGenerator reportGenerator;
	private Locale locale;

	/**
	 * <p>Setter for the field <code>xmlRpcRemoteRunner</code>.</p>
	 *
	 * @param xmlRpcRemoteRunner a {@link com.greenpepper.server.rpc.runner.XmlRpcRemoteRunner} object.
	 */
	public void setXmlRpcRemoteRunner(XmlRpcRemoteRunner xmlRpcRemoteRunner)
	{
		this.xmlRpcRemoteRunner = xmlRpcRemoteRunner;
	}

	/**
	 * <p>Setter for the field <code>monitor</code>.</p>
	 *
	 * @param monitor a {@link com.greenpepper.runner.SpecificationRunnerMonitor} object.
	 */
	public void setMonitor(SpecificationRunnerMonitor monitor)
	{
		this.monitor = monitor;
	}

	/**
	 * <p>Setter for the field <code>project</code>.</p>
	 *
	 * @param project a {@link java.lang.String} object.
	 */
	public void setProject(String project)
	{
		this.project = project;
	}

	/**
	 * <p>Setter for the field <code>systemUnderTest</code>.</p>
	 *
	 * @param systemUnderTest a {@link java.lang.String} object.
	 */
	public void setSystemUnderTest(String systemUnderTest)
	{
		this.systemUnderTest = systemUnderTest;
	}

	/**
	 * <p>Setter for the field <code>repositoryId</code>.</p>
	 *
	 * @param repositoryId a {@link java.lang.String} object.
	 */
	public void setRepositoryId(String repositoryId)
	{
		this.repositoryId = repositoryId;
	}

	/**
	 * <p>Setter for the field <code>reportGenerator</code>.</p>
	 *
	 * @param reportGenerator a {@link com.greenpepper.server.rpc.runner.report.ReportGenerator} object.
	 */
	public void setReportGenerator(ReportGenerator reportGenerator)
	{
		this.reportGenerator = reportGenerator;
	}

	/**
	 * <p>Setter for the field <code>locale</code>.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/** {@inheritDoc} */
	public void run(String source, String output)
	{
		Report report = null;

		try
		{
			monitor.testRunning(getLocation(source, '/'));

			Execution execution = xmlRpcRemoteRunner.runSpecification(
					project, systemUnderTest, repositoryId, source, false, locale.getLanguage());

			report = reportGenerator.openReport(getLocation(source, '-'));
			
			report.generate(execution);

			monitor.testDone(execution.getSuccess(), execution.getFailures(), execution.getErrors(),
							 execution.getIgnored());
		}
		catch (Throwable t)
		{
			if (report != null)
			{
				report.renderException(t);
			}
			monitor.exceptionOccured(t);
		}
		finally
		{
			closeReport(report);
		}
	}

	private String getLocation(String specificationName, char separator)
	{
		return repositoryId + separator + specificationName;
	}

	private void closeReport(Report report)
	{
		if (report == null)
		{
			return;
		}
		try
		{
			reportGenerator.closeReport(report);
		}
		catch (Exception e)
		{
			monitor.exceptionOccured(e);
		}
	}
}
