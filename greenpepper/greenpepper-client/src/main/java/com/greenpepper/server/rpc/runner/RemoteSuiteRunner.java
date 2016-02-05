
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.greenpepper.runner.SpecificationRunner;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.rpc.runner.report.ReportGenerator;
public class RemoteSuiteRunner
		implements SpecificationRunner
{
	private XmlRpcRemoteRunner xmlRpcRemoteRunner;
	private SpecificationRunnerMonitor monitor;
	private String project;
	private String systemUnderTest;
	private String repositoryId;

	private final RemoteDocumentRunner documentRunner;

	/**
	 * <p>Constructor for RemoteSuiteRunner.</p>
	 */
	public RemoteSuiteRunner()
	{
		this.documentRunner = new RemoteDocumentRunner();
	}

	/**
	 * <p>Setter for the field <code>xmlRpcRemoteRunner</code>.</p>
	 *
	 * @param xmlRpcRemoteRunner a {@link com.greenpepper.server.rpc.runner.XmlRpcRemoteRunner} object.
	 */
	public void setXmlRpcRemoteRunner(XmlRpcRemoteRunner xmlRpcRemoteRunner)
	{
		this.xmlRpcRemoteRunner = xmlRpcRemoteRunner;
		documentRunner.setXmlRpcRemoteRunner(xmlRpcRemoteRunner);
	}

	/**
	 * <p>Setter for the field <code>monitor</code>.</p>
	 *
	 * @param monitor a {@link com.greenpepper.runner.SpecificationRunnerMonitor} object.
	 */
	public void setMonitor(SpecificationRunnerMonitor monitor)
	{
		this.monitor = monitor;
		documentRunner.setMonitor(monitor);
	}

	/**
	 * <p>Setter for the field <code>project</code>.</p>
	 *
	 * @param project a {@link java.lang.String} object.
	 */
	public void setProject(String project)
	{
		this.project = project;
		documentRunner.setProject(project);
	}

	/**
	 * <p>Setter for the field <code>systemUnderTest</code>.</p>
	 *
	 * @param systemUnderTest a {@link java.lang.String} object.
	 */
	public void setSystemUnderTest(String systemUnderTest)
	{
		this.systemUnderTest = systemUnderTest;
		documentRunner.setSystemUnderTest(systemUnderTest);
	}

	/**
	 * <p>Setter for the field <code>repositoryId</code>.</p>
	 *
	 * @param repositoryId a {@link java.lang.String} object.
	 */
	public void setRepositoryId(String repositoryId)
	{
		this.repositoryId = repositoryId;
		documentRunner.setRepositoryId(repositoryId);
	}

	/**
	 * <p>setReportGenerator.</p>
	 *
	 * @param reportGenerator a {@link com.greenpepper.server.rpc.runner.report.ReportGenerator} object.
	 */
	public void setReportGenerator(ReportGenerator reportGenerator)
	{
		documentRunner.setReportGenerator(reportGenerator);
	}

	/**
	 * <p>setLocale.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 */
	public void setLocale(Locale locale)
	{
		documentRunner.setLocale(locale);
	}

	/** {@inheritDoc} */
	public void run(String source, String destination)
	{
		try
		{
			SystemUnderTest sut = SystemUnderTest.newInstance(systemUnderTest);
			sut.setProject(Project.newInstance(project));

			Repository repository = Repository.newInstance(repositoryId);

			DocumentNode documentNode = xmlRpcRemoteRunner.getSpecificationHierarchy(repository, sut);
			List<DocumentNode> executableSpecs = extractExecutableSpecifications(documentNode);

			if (executableSpecs.isEmpty())
			{
				monitor.testRunning(getLocation(source, '/'));
				monitor.testDone(0, 0, 0, 0);
			}
			else
			{
				for (DocumentNode spec : executableSpecs)
				{
					documentRunner.run(spec.getTitle(), destination);
				}
			}
		}
		catch (Throwable t)
		{
			monitor.exceptionOccured(t);
		}
	}

	private String getLocation(String specificationName, char separator)
	{
		return repositoryId + separator + specificationName;
	}

	private List<DocumentNode> extractExecutableSpecifications(DocumentNode node)
	{
		List<DocumentNode> specifications = new ArrayList<DocumentNode>();

		if (node.isExecutable())
		{
			specifications.add(node);
		}

		for (DocumentNode childNode : node.getChildren())
		{
			specifications.addAll(extractExecutableSpecifications(childNode));
		}

		return specifications;
	}
}
