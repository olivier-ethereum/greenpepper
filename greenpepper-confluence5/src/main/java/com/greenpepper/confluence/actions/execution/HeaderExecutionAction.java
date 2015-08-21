package com.greenpepper.confluence.actions.execution;

import java.util.LinkedList;
import java.util.List;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.DefaultConversionContext;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.ContentTree;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.PageContext;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;

@SuppressWarnings("serial")
public class HeaderExecutionAction extends ChildrenExecutionAction
{   
	private Boolean hasChildren;
    private Boolean doExecuteChildren;
    private boolean pepperize;
    private boolean retrieveBody;

	public String loadHeader()
    {
        retrieveReferenceList();
        loadSpecification(); 
    	return SUCCESS;
    }
    
    public String setAsImplemented()
    {
    	gpUtil.saveImplementedVersion(getPage(), getPage().getVersion());
        return loadHeader();
    }
    
    public String revert()
    {
    	gpUtil.revertImplementation(getPage());
        return loadHeader();
    }
    
    public String greenPepperize()
    {
        if(pepperize)
        {
            try 
            {
				Specification spec = Specification.newInstance(getPage().getTitle());
				spec.setRepository(gpUtil.getHomeRepository(spaceKey));

				specification = gpUtil.getGPServerService().createSpecification(spec);
                return loadHeader();
            } 
            catch (GreenPepperServerException e) 
            {
                addActionError(e.getId());
            }
        }
        else
        {
            try 
            {
				Specification spec = Specification.newInstance(getPage().getTitle());
				spec.setRepository(gpUtil.getHomeRepository(spaceKey));
				
				// Clean spec
                gpUtil.getGPServerService().removeSpecification(spec);
                gpUtil.saveExecuteChildren(page, false);
            	gpUtil.saveImplementedVersion(getPage(), null);
            	gpUtil.savePreviousImplementedVersion(getPage(), null);
                specification = null;
            } 
            catch (GreenPepperServerException e) 
            {
                addActionError(e.getId());
                return loadHeader();
            }
        }

        return SUCCESS;
    }
    
    public String updateExecuteChildren()
    {
        gpUtil.saveExecuteChildren(page, doExecuteChildren);
        return SUCCESS;
    }
    
    public List<SystemUnderTest> getForcedSystemUnderTests()
    {
        return null;
    }
    
    public boolean getCanBeImplemented()
    {
    	return gpUtil.canBeImplemented(getPage());
    }
    
    public boolean getCanBeReverted()
    {
    	return  getPreviousImplementedVersion() != null;
    }
    
    public Integer getImplementedVersion()
    {
    	return gpUtil.getImplementedVersion(getPage());
    }
    
    public Integer getPreviousImplementedVersion()
    {
    	return gpUtil.getPreviousImplementedVersion(getPage());
    }
    
    public String getRenderedContent()
    {
    	String content;
    	
    	try 
    	{
    		content = gpUtil.getPageContent(getPage(), implemented);
    	}
    	catch(GreenPepperServerException e)
    	{
    		content = "";
    	}
    	
    	return gpUtil.getViewRenderer().render(content, new DefaultConversionContext(getPage().toPageContext()));
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Page> getExecutableList()
    {
        if(!getDoExecuteChildren()) 
        	return new LinkedList<Page>();
        
        return super.getExecutableList();
    }
    
    public boolean getHasChildren()
    {
    	if(hasChildren != null) 
    		return hasChildren;
    	
    	hasChildren = !implemented && !super.getExecutableList().isEmpty();
    	return hasChildren;
    }
    
    public boolean getDoExecuteChildren()
    {
        if(doExecuteChildren != null)
        	return doExecuteChildren;
        
        doExecuteChildren = getHasChildren() && gpUtil.getExecuteChildren(page);
        return doExecuteChildren; 
    }

    public void setDoExecuteChildren(boolean doExecuteChildren)
    {
        this.doExecuteChildren = doExecuteChildren;
    }
    
    public String getExecutionUID()
    {
        return "HEADER";
    }
    
    public boolean getIsSutEditable()
    {
        return true;
    }
    
    public boolean getAllChildren()
    {
        return true;
    }
    
    public boolean getIsSelfIncluded()
    {
        return true;
    }

    public boolean isPepperize()
    {
        return pepperize;
    }

    public void setPepperize(boolean pepperize)
    {
        this.pepperize = pepperize;
    }
        
    public boolean getRetrieveBody() 
    {
		return retrieveBody;
	}

	public void setRetrieveBody(boolean retrieveBody) 
	{
		this.retrieveBody = retrieveBody;
	}
    
    public boolean isImplementationDue()
    {
    	return gpUtil.isImplementationDue(getPage());
    }
}
