package com.greenpepper.confluence.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.Requirement;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.util.ExceptionUtils;
import com.greenpepper.util.HtmlUtil;

@SuppressWarnings("serial")
public class SpecificationAction extends AbstractGreenPepperAction
{
    protected Specification specification;
    
    private List<Reference> references;
    private List<Repository> repositories;
    private List<SystemUnderTest> projectSystemUnderTests;
    
    private String selectedSystemUnderTestInfo;
    private Execution execution;
    
    private String requirementName;
    private String sutName;
    private String sutProjectName;
    private String repositoryUid;
    private String sections;
    
    private boolean isMain;
    private boolean isSutEditable;
    protected boolean implemented;


	public String loadSpecification()
    {
        try
        {
            specification = gpUtil.getSpecification(spaceKey, getPage().getTitle());
        }
        catch (GreenPepperServerException e)
        {
            if(!e.getId().equals(GreenPepperServerErrorKey.SPECIFICATION_NOT_FOUND))
                addActionError(e.getId());
        }
        
        return SUCCESS; 
    }
    
    public String updateSelectedSystemUndertTest()
    {
        gpUtil.saveSelectedSystemUnderTestInfo(page, selectedSystemUnderTestInfo);
        return SUCCESS;
    }
    
    public String getSystemUndertTestSelection()
    {
        try
        {
            specification = gpUtil.getSpecification(spaceKey, getPage().getTitle());
            projectSystemUnderTests = gpUtil.getSystemsUnderTests(spaceKey);
        }
        catch (GreenPepperServerException e)
        {
            projectSystemUnderTests = new ArrayList<SystemUnderTest>();
            addActionError(e.getId());
        }
        
        return SUCCESS; 
    }

    public String addSystemUnderTest()
    {
        try 
        {
            SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
            sut.setProject(Project.newInstance(sutProjectName));
            sut.setRunner(Runner.newInstance(""));
            
            Specification specification = Specification.newInstance(getPage().getTitle());
            specification.setRepository(gpUtil.getHomeRepository(spaceKey));
            
            gpUtil.getGPServerService().addSpecificationSystemUnderTest(sut, specification);
        } 
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
        }
        
        return getSystemUndertTestSelection(); 
    }
    
    public String removeSystemUnderTest()
    {
        try 
        {
            SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
            sut.setProject(Project.newInstance(sutProjectName));
            sut.setRunner(Runner.newInstance(""));
            
            Specification specification = Specification.newInstance(getPage().getTitle());
            specification.setRepository(gpUtil.getHomeRepository(spaceKey));
            
            gpUtil.getGPServerService().removeSpecificationSystemUnderTest(sut, specification);
        } 
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
        }
        
        return getSystemUndertTestSelection(); 
    }
    
    public String run()
    {
		String locale = getLocale().getLanguage();

		SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
		sut.setProject(Project.newInstance(sutProjectName));
		sut.setRunner(Runner.newInstance(""));

		Specification spec = Specification.newInstance(getPage().getTitle());

		try
		{
			spec.setRepository(gpUtil.getHomeRepository(spaceKey));

        	execution = gpUtil.getGPServerService().runSpecification(sut, spec, implemented, locale);
        } 
        catch (GreenPepperServerException e) 
        {
            execution = Execution.error(spec, sut, null, e.getId());
        }
        catch (Exception e) 
        {
            execution = Execution.error(spec, sut, null, ExceptionUtils.stackTrace(e, "<br>", 15));
        }
        
        return SUCCESS;
    }
    
    public String retrieveReferenceList()
    {
        try 
        {
            references = gpUtil.getReferences(spaceKey, getPage().getTitle());
            if(isEditMode)
            {
                repositories = gpUtil.getRepositories(spaceKey);
                if(repositories.isEmpty())
                    throw new GreenPepperServerException("greenpepper.server.repositoriesnotfound", "");
                
                projectSystemUnderTests = gpUtil.getSystemsUnderTests(spaceKey);
                if(projectSystemUnderTests.isEmpty())
                    throw new GreenPepperServerException("greenpepper.server.sutsnotfound", "");
            }
        } 
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
            isEditMode = false;
        }

        return SUCCESS;
    }

    public String addReference()
    {        
        try 
        {
            gpUtil.getGPServerService().createReference(instanceOfReference());
        } 
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
        }
        
        return retrieveReferenceList(); 
    }
    
    public String removeReference()
    {
        isEditMode = true;
        
        try 
        {
            gpUtil.getGPServerService().removeReference(instanceOfReference());
        } 
        catch (GreenPepperServerException e) 
        {
            addActionError(e.getId());
        }
        
        return retrieveReferenceList(); 
    }
    
    public Specification getSpecification()
    {
        return specification;
    }

    public List<Repository> getRepositories()
    { 
        return repositories;
    }

    public List<SystemUnderTest> getProjectSystemUnderTests()
    { 
        return projectSystemUnderTests;
    }

    public Set<SystemUnderTest> getSpecificationSystemUnderTests()
    {
        if(specification == null) return new HashSet<SystemUnderTest>();
        return specification.getTargetedSystemUnderTests();
    }
    
    public boolean getIsSutEditable()
    {
        return this.isSutEditable;
    }
    
    public void setIsSutEditable(boolean isSutEditable)
    {
        this.isSutEditable = isSutEditable;
    }
    
    public boolean getImplemented() 
    {
		return implemented;
	}
    
    public void setImplemented(boolean implemented) 
    {
		this.implemented = implemented;
	}

    public String getRequirementName()
    {
        return requirementName;
    }

    public void setRequirementName(String requirementName)
    {
        this.requirementName = requirementName;
    }

    public void setSutInfo(String sutInfo)
    {
        StringTokenizer stk = new StringTokenizer(sutInfo, "@");
        this.sutProjectName = stk.nextToken();
        this.sutName = stk.nextToken();
    }

    public void setSutName(String sutName)
    {
        this.sutName = sutName;
    }

    public void setSutProjectName(String sutProjectName)
    {
        this.sutProjectName = sutProjectName;
    }

    public void setRepositoryUid(String repositoryUid)
    {
        this.repositoryUid = repositoryUid;
    }

    public void setSections(String sections)
    {
        this.sections = sections.trim();
    }
    
    public List<Reference> getReferences()
    {
        return references;
    }

    public SystemUnderTest getSelectedSystemUnderTest()
    {
        return gpUtil.getSelectedSystemUnderTest(page);
    }

    public boolean getIsExecutable()
    {
        return specification != null;
    }

    public Execution getExecution()
    {
        return execution;
    }

    public String getSelectedSystemUnderTestInfo()
    {
        if(selectedSystemUnderTestInfo != null){return selectedSystemUnderTestInfo;}
        selectedSystemUnderTestInfo = gpUtil.getSelectedSystemUnderTestInfo(page);
        return selectedSystemUnderTestInfo;
    }
    
    public void setSelectedSystemUnderTestInfo(String selectedSystemUnderTestInfo)
    {
        this.selectedSystemUnderTestInfo = selectedSystemUnderTestInfo;
    }
    
    public boolean getIsMain()
    {
        return isMain;
    }
    
    public void setIsMain(boolean isMain)
    {
        this.isMain = isMain;
    }

	public String getRenderedResults()
    {
        String results = execution.getResults();

		if (results != null)
		{
			results = results.replaceAll("greenpepper-manage-not-rendered", "greenpepper-manage");
			results = results.replaceAll("greenpepper-hierarchy-not-rendered", "greenpepper-hierarchy");
			results = results.replaceAll("greenpepper-children-not-rendered", "greenpepper-children");
			results = results.replaceAll("greenpepper-labels-not-rendered", "greenpepper-labels");
			results = results.replaceAll("greenpepper-group-not-rendered", "greenpepper-group");
			results = results.replaceAll("Unknown macro:", "");
			return HtmlUtil.cleanUpResults(results);
		}

		return null;
	}
    
    public boolean isInSpecificationSelection(SystemUnderTest systemUnderTest)
    {
        return gpUtil.isInSutList(systemUnderTest, specification.getTargetedSystemUnderTests());}
    
    /*********************  Utils  *********************/

    private Reference instanceOfReference()
			throws GreenPepperServerException
	{
        SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
        sut.setProject(Project.newInstance(sutProjectName));

        Specification specification = Specification.newInstance(getPage().getTitle());
        specification.setRepository(gpUtil.getHomeRepository(spaceKey));

        Requirement requirement = Requirement.newInstance(requirementName);
        requirement.setRepository(Repository.newInstance(repositoryUid));
          
        return Reference.newInstance(requirement, specification, sut, sections);
    }

	public int getNextFieldId()
	{
		return getFieldId() + 1;
	}
}
