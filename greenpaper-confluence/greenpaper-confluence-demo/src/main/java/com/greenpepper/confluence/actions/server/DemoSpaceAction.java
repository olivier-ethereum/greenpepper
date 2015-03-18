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
package com.greenpepper.confluence.actions.server;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.atlassian.confluence.core.ConfluenceEntityObject;
import com.atlassian.confluence.importexport.DefaultImportContext;
import com.atlassian.confluence.importexport.ImportExportException;
import com.atlassian.confluence.importexport.ImportExportManager;
import com.atlassian.confluence.importexport.ImportedObjectPostProcessor;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.User;
import com.greenpepper.confluence.demo.phonebook.PhoneBookSystemUnderDevelopment;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.server.rpc.RpcServerService;
import com.greenpepper.util.I18nUtil;
import com.greenpepper.util.StringUtil;

public class DemoSpaceAction
		extends GreenPepperServerAction
{

	private static final String DEMO_NAME = "GreenPepper Demo";
	private static final String DEMO_SUT_NAME = "Demo";
	private static final String DEMO_SPACE_KEY = "GREENPEPPERDEMO";
	private static final String PHONEBOOK_SUT_NAME = DEMO_SUT_NAME + " - PhoneBook";

	private static final String RESOURCE_BUNDLE = InstallationAction.class.getName();
	private final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
	private ResourceBundle resourceBundle;

	private ImportExportManager importExportManager;

	private String username;
	private String pwd;

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = StringUtil.toNullIfEmpty(pwd.trim());
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = StringUtil.toNullIfEmpty(username.trim());
	}

	public String doGetDemo()
	{
		if (!gpUtil.isServerReady())
		{
			addActionError(ConfluenceGreenPepper.SERVER_NOCONFIGURATION);
			return SUCCESS;
		}

		return SUCCESS;
	}

	public boolean isDemoSpaceExist()
	{
		try
		{
			return getDemoSpace() != null;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public String doCreateDemoSpace()
	{
		try
		{
			if (getUsername() != null)
			{
				gpUtil.verifyCredentials(getUsername(), getPwd());
			}

			doImportDemoSite();

			Space demoSpace = getDemoSpace();

			if (demoSpace == null)
			{
				throw new GreenPepperServerException("greenpepper.demo.importfail", "Importing the demo site fail!");	
			}

			Repository demoRepository = doRegisterSpace(demoSpace);

			doAddDemoSUT(demoRepository);
			doAddPhoneBookSUT(demoRepository);

			doGreenPepperizedPage(demoSpace, demoRepository);

			doAddRemoteUserToGreenPepperUserGroup();
		}
		catch (GreenPepperServerException ex)
		{
			addActionError(ex.getId());
			doRemoveDemoSpace();
		}
		catch (Exception ex)
		{
			addActionError(ex.getMessage());
			doRemoveDemoSpace();
		}

		return SUCCESS;
	}

	private Repository doRegisterSpace(Space demoSpace)
			throws GreenPepperServerException
	{
		Repository demoRepository = getDemoRepository();

		if (demoRepository != null) return demoRepository;

		Project demoProject = getDemoProject();

		String uid = gpUtil.getSettingsManager().getGlobalSettings().getSiteTitle() + "-" + demoSpace.getKey();

		demoRepository = Repository.newInstance(uid);

		demoRepository.setProject(demoProject);
		demoRepository.setType(RepositoryType.newInstance("CONFLUENCE"));
		demoRepository.setName(DEMO_NAME);
		demoRepository.setContentType(ContentType.TEST);
		demoRepository.setBaseUrl(gpUtil.getBaseUrl());
		demoRepository.setUsername(getUsername());
		demoRepository.setPassword(getPwd());

		demoRepository.setBaseRepositoryUrl(getDemoSpaceUrl());

		String baseTestUrl = String.format("%s/rpc/xmlrpc?handler=%s#%s", gpUtil.getBaseUrl(),
										   RpcServerService.SERVICE_HANDLER, demoSpace.getKey());
		demoRepository.setBaseTestUrl(baseTestUrl);

		return getService().registerRepository(demoRepository);
	}

	private void doAddDemoSUT(Repository demoRepository)
			throws GreenPepperServerException
	{
		SystemUnderTest demoSut = getSUT(demoRepository, DEMO_SUT_NAME);

		if (demoSut == null)
		{
			demoSut = SystemUnderTest.newInstance(DEMO_SUT_NAME);

			demoSut.setRunner(getJavaRunner());
			demoSut.setProject(getDemoProject());

			gpUtil.getGPServerService().createSystemUnderTest(demoSut, demoRepository);
		}
	}

	private void doAddPhoneBookSUT(Repository demoRepository)
			throws GreenPepperServerException
	{
		SystemUnderTest phoneBookSut = getSUT(demoRepository, PHONEBOOK_SUT_NAME);

		if (phoneBookSut == null)
		{
			phoneBookSut = SystemUnderTest.newInstance(PHONEBOOK_SUT_NAME);

			phoneBookSut.setFixtureFactory(PhoneBookSystemUnderDevelopment.class.getName());
			phoneBookSut.setRunner(getJavaRunner());
			phoneBookSut.setProject(getDemoProject());

			gpUtil.getGPServerService().createSystemUnderTest(phoneBookSut, demoRepository);
		}
	}

	@SuppressWarnings("unchecked")
	private void doGreenPepperizedPage(Space demoSpace, Repository demoRepository)
			throws GreenPepperServerException
	{
		List<Page> demoPages = gpUtil.getPageManager().getPages(demoSpace, true);

		for (Page demoPage : demoPages)
		{
			if (demoSpace.getHomePage().getId() != demoPage.getId()
					&& !demoPage.getTitle().endsWith(".java"))
			{
				doGreenPepperizedPage(demoRepository, demoPage);
			}
		}
	}

	private void doGreenPepperizedPage(Repository demoRepository, Page page)
			throws GreenPepperServerException
	{
		Specification spec = Specification.newInstance(page.getTitle());
		spec.setRepository(demoRepository);

		spec = gpUtil.getGPServerService().createSpecification(spec);

		if (page.getTitle().equals("PhoneBook"))
		{
			SystemUnderTest phoneBookSut = getSUT(demoRepository, PHONEBOOK_SUT_NAME);
			gpUtil.getGPServerService().addSpecificationSystemUnderTest(phoneBookSut, spec);

			SystemUnderTest demoSut = getSUT(demoRepository, DEMO_SUT_NAME);
			gpUtil.getGPServerService().removeSpecificationSystemUnderTest(demoSut, spec);
		}
	}

	private void doAddRemoteUserToGreenPepperUserGroup()
	{
		final User remoteUser = this.getRemoteUser();

		if (!gpUtil.getGreenPepperUserGroup().hasMembership(remoteUser))
		{
			gpUtil.getGreenPepperUserGroup().addMembership(remoteUser);
		}
	}

	private Space getDemoSpace()
	{
		return gpUtil.getSpaceManager().getSpace(DEMO_SPACE_KEY);
	}

	private SystemUnderTest getSUT(Repository demoRepository, String name)
			throws GreenPepperServerException
	{
		List<SystemUnderTest> suts = gpUtil.getGPServerService().getSystemUnderTestsOfAssociatedProject(demoRepository.getUid());

		for (SystemUnderTest sut : suts)
		{
			if (sut.getName().equals(name))
			{
				return sut;
			}
		}

		return null;
	}

	private Repository getDemoRepository()
			throws GreenPepperServerException
	{
		List<Repository> repositories = gpUtil.getGPServerService().getAllSpecificationRepositories();

		for (Repository repository : repositories)
		{
			if (repository.getName().equals(DEMO_NAME))
			{
				return repository;
			}
		}

		return null;
	}

	private Project getDemoProject()
			throws GreenPepperServerException
	{
		List<Project> projects = gpUtil.getGPServerService().getAllProjects();

		for (Project project : projects)
		{
			if (project.getName().equals(DEMO_NAME))
			{
				return project;
			}
		}

		return Project.newInstance(DEMO_NAME);
	}

	private Runner getJavaRunner()
			throws GreenPepperServerException
	{
		List<Runner> runners = gpUtil.getGPServerService().getAllRunners();

		for (Runner runner : runners)
		{
			if (runner.getName().startsWith("GPCore JAVA v."))
			{
				return runner;
			}
		}

		Runner runner = Runner.newInstance("Java");
		runner.setEnvironmentType(EnvironmentType.newInstance("JAVA"));
		return runner;
	}

	private void doImportDemoSite()
			throws FileNotFoundException, ImportExportException
	{
		URL demoSiteZipUrl = DemoSpaceAction.class.getResource("/com/greenpepper/confluence/demo/demo-site.zip");

		if (demoSiteZipUrl == null)
		{
			throw new FileNotFoundException("Cannot find demo-site.zip");
		}

		DefaultImportContext ctx = new DefaultImportContext(demoSiteZipUrl, null);
		final Date importStart = new Date();

		ctx.setPostProcessor(new ImportedObjectPostProcessor()
		{
			public boolean process(Object obj)
			{
				if (obj instanceof ConfluenceEntityObject)
				{
					ConfluenceEntityObject entityObject = (ConfluenceEntityObject)obj;

					// Make pages appear in recent updated in preference to non-pages.
					if (entityObject instanceof Page)
					{
						entityObject.setLastModificationDate(new Date());
					}
					else
					{
						entityObject.setLastModificationDate(importStart);
					}

					return true;
				}

				return false;
			}
		});

		getImportExportManager().importAs(ImportExportManager.TYPE_ALL_DATA, ctx);
	}

	public String doRemoveDemoSpace()
	{
		try
		{
			Space demoSpace = getDemoSpace();

			if (demoSpace != null)
			{
				gpUtil.getSpaceManager().removeSpace(demoSpace);
			}

			gpUtil.getGPServerService().removeProject(getDemoProject(), true);
		}
		catch (Exception ex)
		{
			addActionError(ex.getMessage());
		}

		return SUCCESS;
	}

	public String getDemoSpaceUrl()
	{
		Space demoSpace = getDemoSpace();

		return String.format("%s/display/%s", gpUtil.getBaseUrl(), demoSpace.getKey());
	}

	/**
	 * Custom I18n. Based on WebWork i18n.
	 *
	 * @param key Key
	 * @return the i18nzed message. If none found key is returned.
	 */
	@HtmlSafe
	public String getText(String key)
	{
		String text = super.getText(key);

		if (text.equals(key))
		{
			text = I18nUtil.getText(key, getResourceBundle());
		}

		return text;
	}

	private ResourceBundle getResourceBundle() {

		if (resourceBundle == null)
		{
			Locale locale = threadLocale.get();
			if (locale == null)
			{
				locale = getLocale();
				threadLocale.set(locale == null ? Locale.ENGLISH : locale);
			}

			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
		}

		return resourceBundle;
	}

	public ImportExportManager getImportExportManager()
    {
		if (importExportManager == null)
		{
			importExportManager = (ImportExportManager)ContainerManager.getComponent("importExportManager");
		}

		return importExportManager;
    }

    public void setImportExportManager(ImportExportManager importExportManager)
    {
        this.importExportManager = importExportManager;
    }

	public boolean isAllowRemoteApiAnonymous()
	{
		return gpUtil.getSettingsManager().getGlobalSettings().isAllowRemoteApiAnonymous();
	}

	public String getGeneralConfigSecurityRemoteApiUrl()
	{
		return String.format("%s/admin/editgeneralconfig.action#security",
							 gpUtil.getSettingsManager().getGlobalSettings().getBaseUrl());		
	}
}