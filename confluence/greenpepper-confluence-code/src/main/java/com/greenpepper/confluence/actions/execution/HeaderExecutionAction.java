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

/**
 * <p>HeaderExecutionAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class HeaderExecutionAction extends ChildrenExecutionAction
{   
	private Boolean hasChildren;
    private Boolean doExecuteChildren;
    private boolean pepperize;
    private boolean retrieveBody;

	/**
	 * <p>loadHeader.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String loadHeader()
    {
        retrieveReferenceList();
        loadSpecification(); 
    	return SUCCESS;
    }
    
    /**
     * <p>setAsImplemented.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String setAsImplemented()
    {
    	gpUtil.saveImplementedVersion(getPage(), getPage().getVersion());
        return loadHeader();
    }
    
    /**
     * <p>revert.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String revert()
    {
    	gpUtil.revertImplementation(getPage());
        return loadHeader();
    }
    
    /**
     * <p>greenPepperize.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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
    
    /**
     * <p>updateExecuteChildren.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String updateExecuteChildren()
    {
        gpUtil.saveExecuteChildren(page, doExecuteChildren);
        return SUCCESS;
    }
    
    /**
     * <p>getForcedSystemUnderTests.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<SystemUnderTest> getForcedSystemUnderTests()
    {
        return null;
    }
    
    /**
     * <p>getCanBeImplemented.</p>
     *
     * @return a boolean.
     */
    public boolean getCanBeImplemented()
    {
    	return gpUtil.canBeImplemented(getPage());
    }
    
    /**
     * <p>getCanBeReverted.</p>
     *
     * @return a boolean.
     */
    public boolean getCanBeReverted()
    {
    	return  getPreviousImplementedVersion() != null;
    }
    
    /**
     * <p>getImplementedVersion.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getImplementedVersion()
    {
    	return gpUtil.getImplementedVersion(getPage());
    }
    
    /**
     * <p>getPreviousImplementedVersion.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getPreviousImplementedVersion()
    {
    	return gpUtil.getPreviousImplementedVersion(getPage());
    }
    
    /**
     * <p>getRenderedContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
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

    /**
     * <p>getExecutableList.</p>
     *
     * @return a {@link java.util.LinkedList} object.
     */
    @SuppressWarnings("unchecked")
    public LinkedList<Page> getExecutableList()
    {
        if(!getDoExecuteChildren()) 
        	return new LinkedList<Page>();
        
        return super.getExecutableList();
    }
    
    /**
     * <p>Getter for the field <code>hasChildren</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getHasChildren()
    {
    	if(hasChildren != null) 
    		return hasChildren;
    	
    	hasChildren = !implemented && !super.getExecutableList().isEmpty();
    	return hasChildren;
    }
    
    /**
     * <p>Getter for the field <code>doExecuteChildren</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getDoExecuteChildren()
    {
        if(doExecuteChildren != null)
        	return doExecuteChildren;
        
        doExecuteChildren = getHasChildren() && gpUtil.getExecuteChildren(page);
        return doExecuteChildren; 
    }

    /**
     * <p>Setter for the field <code>doExecuteChildren</code>.</p>
     *
     * @param doExecuteChildren a boolean.
     */
    public void setDoExecuteChildren(boolean doExecuteChildren)
    {
        this.doExecuteChildren = doExecuteChildren;
    }
    
    /**
     * <p>getExecutionUID.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExecutionUID()
    {
        return "HEADER";
    }
    
    /**
     * <p>getIsSutEditable.</p>
     *
     * @return a boolean.
     */
    public boolean getIsSutEditable()
    {
        return true;
    }
    
    /**
     * <p>getAllChildren.</p>
     *
     * @return a boolean.
     */
    public boolean getAllChildren()
    {
        return true;
    }
    
    /**
     * <p>getIsSelfIncluded.</p>
     *
     * @return a boolean.
     */
    public boolean getIsSelfIncluded()
    {
        return true;
    }

    /**
     * <p>isPepperize.</p>
     *
     * @return a boolean.
     */
    public boolean isPepperize()
    {
        return pepperize;
    }

    /**
     * <p>Setter for the field <code>pepperize</code>.</p>
     *
     * @param pepperize a boolean.
     */
    public void setPepperize(boolean pepperize)
    {
        this.pepperize = pepperize;
    }
        
    /**
     * <p>Getter for the field <code>retrieveBody</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getRetrieveBody() 
    {
		return retrieveBody;
	}

	/**
	 * <p>Setter for the field <code>retrieveBody</code>.</p>
	 *
	 * @param retrieveBody a boolean.
	 */
	public void setRetrieveBody(boolean retrieveBody) 
	{
		this.retrieveBody = retrieveBody;
	}
    
    /**
     * <p>isImplementationDue.</p>
     *
     * @return a boolean.
     */
    public boolean isImplementationDue()
    {
    	return gpUtil.isImplementationDue(getPage());
    }
}
