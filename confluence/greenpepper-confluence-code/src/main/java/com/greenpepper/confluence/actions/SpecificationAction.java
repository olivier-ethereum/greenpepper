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

/**
 * <p>SpecificationAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
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


	/**
	 * <p>loadSpecification.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
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
    
    /**
     * <p>updateSelectedSystemUndertTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String updateSelectedSystemUndertTest()
    {
        gpUtil.saveSelectedSystemUnderTestInfo(page, selectedSystemUnderTestInfo);
        return SUCCESS;
    }
    
    /**
     * <p>getSystemUndertTestSelection.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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

    /**
     * <p>addSystemUnderTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>removeSystemUnderTest.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>run.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>retrieveReferenceList.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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

    /**
     * <p>addReference.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>removeReference.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>Getter for the field <code>specification</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Specification} object.
     */
    public Specification getSpecification()
    {
        return specification;
    }

    /**
     * <p>Getter for the field <code>repositories</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Repository> getRepositories()
    { 
        return repositories;
    }

    /**
     * <p>Getter for the field <code>projectSystemUnderTests</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<SystemUnderTest> getProjectSystemUnderTests()
    { 
        return projectSystemUnderTests;
    }

    /**
     * <p>getSpecificationSystemUnderTests.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<SystemUnderTest> getSpecificationSystemUnderTests()
    {
        if(specification == null) return new HashSet<SystemUnderTest>();
        return specification.getTargetedSystemUnderTests();
    }
    
    /**
     * <p>Getter for the field <code>isSutEditable</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getIsSutEditable()
    {
        return this.isSutEditable;
    }
    
    /**
     * <p>Setter for the field <code>isSutEditable</code>.</p>
     *
     * @param isSutEditable a boolean.
     */
    public void setIsSutEditable(boolean isSutEditable)
    {
        this.isSutEditable = isSutEditable;
    }
    
    /**
     * <p>Getter for the field <code>implemented</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getImplemented() 
    {
		return implemented;
	}
    
    /**
     * <p>Setter for the field <code>implemented</code>.</p>
     *
     * @param implemented a boolean.
     */
    public void setImplemented(boolean implemented) 
    {
		this.implemented = implemented;
	}

    /**
     * <p>Getter for the field <code>requirementName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRequirementName()
    {
        return requirementName;
    }

    /**
     * <p>Setter for the field <code>requirementName</code>.</p>
     *
     * @param requirementName a {@link java.lang.String} object.
     */
    public void setRequirementName(String requirementName)
    {
        this.requirementName = requirementName;
    }

    /**
     * <p>setSutInfo.</p>
     *
     * @param sutInfo a {@link java.lang.String} object.
     */
    public void setSutInfo(String sutInfo)
    {
        StringTokenizer stk = new StringTokenizer(sutInfo, "@");
        this.sutProjectName = stk.nextToken();
        this.sutName = stk.nextToken();
    }

    /**
     * <p>Setter for the field <code>sutName</code>.</p>
     *
     * @param sutName a {@link java.lang.String} object.
     */
    public void setSutName(String sutName)
    {
        this.sutName = sutName;
    }

    /**
     * <p>Setter for the field <code>sutProjectName</code>.</p>
     *
     * @param sutProjectName a {@link java.lang.String} object.
     */
    public void setSutProjectName(String sutProjectName)
    {
        this.sutProjectName = sutProjectName;
    }

    /**
     * <p>Setter for the field <code>repositoryUid</code>.</p>
     *
     * @param repositoryUid a {@link java.lang.String} object.
     */
    public void setRepositoryUid(String repositoryUid)
    {
        this.repositoryUid = repositoryUid;
    }

    /**
     * <p>Setter for the field <code>sections</code>.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public void setSections(String sections)
    {
        this.sections = sections.trim();
    }
    
    /**
     * <p>Getter for the field <code>references</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Reference> getReferences()
    {
        return references;
    }

    /**
     * <p>getSelectedSystemUnderTest.</p>
     *
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public SystemUnderTest getSelectedSystemUnderTest()
    {
        return gpUtil.getSelectedSystemUnderTest(page);
    }

    /**
     * <p>getIsExecutable.</p>
     *
     * @return a boolean.
     */
    public boolean getIsExecutable()
    {
        return specification != null;
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
     * <p>Getter for the field <code>selectedSystemUnderTestInfo</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSelectedSystemUnderTestInfo()
    {
        if(selectedSystemUnderTestInfo != null){return selectedSystemUnderTestInfo;}
        selectedSystemUnderTestInfo = gpUtil.getSelectedSystemUnderTestInfo(page);
        return selectedSystemUnderTestInfo;
    }
    
    /**
     * <p>Setter for the field <code>selectedSystemUnderTestInfo</code>.</p>
     *
     * @param selectedSystemUnderTestInfo a {@link java.lang.String} object.
     */
    public void setSelectedSystemUnderTestInfo(String selectedSystemUnderTestInfo)
    {
        this.selectedSystemUnderTestInfo = selectedSystemUnderTestInfo;
    }
    
    /**
     * <p>Getter for the field <code>isMain</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getIsMain()
    {
        return isMain;
    }
    
    /**
     * <p>Setter for the field <code>isMain</code>.</p>
     *
     * @param isMain a boolean.
     */
    public void setIsMain(boolean isMain)
    {
        this.isMain = isMain;
    }

	/**
	 * <p>getRenderedResults.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
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
    
    /**
     * <p>isInSpecificationSelection.</p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return a boolean.
     */
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

	/**
	 * <p>getNextFieldId.</p>
	 *
	 * @return a int.
	 */
	public int getNextFieldId()
	{
		return getFieldId() + 1;
	}
}
