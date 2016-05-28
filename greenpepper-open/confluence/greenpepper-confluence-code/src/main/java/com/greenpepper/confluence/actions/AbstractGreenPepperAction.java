package com.greenpepper.confluence.actions;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.velocity.htmlsafe.HtmlSafe;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

/**
 * <p>Abstract AbstractGreenPepperAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractGreenPepperAction extends ConfluenceActionSupport
{    
    protected ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();

    protected String bulkUID = "PAGE";
    protected String executionUID;  
    protected int fieldId;
    protected String spaceKey;
    protected Long pageId;
    protected Page page;

    protected Boolean canEdit;
    protected boolean refreshAll;
    protected boolean isEditMode;

    private String pageConent; 
    
    /**
     * <p>Getter for the field <code>bulkUID</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBulkUID()
    {
        return bulkUID;
    }

    /**
     * <p>Setter for the field <code>bulkUID</code>.</p>
     *
     * @param bulkUID a {@link java.lang.String} object.
     */
    public void setBulkUID(String bulkUID)
    {
        this.bulkUID = bulkUID;
    }

    /**
     * <p>Getter for the field <code>executionUID</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExecutionUID()
    {
        return executionUID;
    }
    
    /**
     * <p>Setter for the field <code>executionUID</code>.</p>
     *
     * @param executionUID a {@link java.lang.String} object.
     */
    public void setExecutionUID(String executionUID)
    {
        this.executionUID = executionUID;
    }

    /**
     * <p>Getter for the field <code>fieldId</code>.</p>
     *
     * @return a int.
     */
    public int getFieldId()
    {
        return fieldId;
    }
    
    /**
     * <p>Setter for the field <code>fieldId</code>.</p>
     *
     * @param fieldId a int.
     */
    public void setFieldId(int fieldId)
    {
        this.fieldId = fieldId;
    }
    
    /**
     * <p>Getter for the field <code>page</code>.</p>
     *
     * @return a {@link com.atlassian.confluence.pages.Page} object.
     */
    public Page getPage()
    {
        return page;
    }
    
    /**
     * <p>Setter for the field <code>page</code>.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     */
    public void setPage(Page page)
    {
        this.page = page;
    }

    /**
     * <p>Getter for the field <code>spaceKey</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSpaceKey()
    {
        return this.spaceKey;
    }
    
    /**
     * <p>Setter for the field <code>spaceKey</code>.</p>
     *
     * @param spaceKey a {@link java.lang.String} object.
     */
    public void setSpaceKey(String spaceKey) 
    {
        this.spaceKey = spaceKey;
    }
    
    /**
     * <p>Getter for the field <code>pageId</code>.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getPageId()
    {        
        return this.pageId;
    }
    
    /**
     * <p>Setter for the field <code>pageId</code>.</p>
     *
     * @param pageId a {@link java.lang.Long} object.
     * @throws java.io.UnsupportedEncodingException if any.
     */
    public void setPageId(Long pageId) throws UnsupportedEncodingException
    {
		page = gpUtil.getPageManager().getPage(pageId);
		this.pageId = pageId;
    }
    
    /**
     * <p>Getter for the field <code>isEditMode</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getIsEditMode()
    {
        return this.isEditMode;
    }
    
    /**
     * <p>Setter for the field <code>isEditMode</code>.</p>
     *
     * @param isEditMode a boolean.
     */
    public void setIsEditMode(boolean isEditMode)
    {
        this.isEditMode = isEditMode;
    }

    /**
     * <p>Getter for the field <code>refreshAll</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getRefreshAll()
    {
        return refreshAll;
    }

    /**
     * <p>Setter for the field <code>refreshAll</code>.</p>
     *
     * @param refreshAll a boolean.
     */
    public void setRefreshAll(boolean refreshAll)
    {
        this.refreshAll = refreshAll;
    }
    
    /**
     * <p>getPageContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPageContent()
    {
        if(pageConent != null){return pageConent;}
        pageConent = gpUtil.getPageContent(page);
        return pageConent;
    }

    /**
     * <p>getPermittedChildren.</p>
     *
     * @param page a {@link com.atlassian.confluence.pages.Page} object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public List<Page> getPermittedChildren(Page page)
    {
        return gpUtil.getContentPermissionManager().getPermittedChildren(page, getRemoteUser());
    }

	/** {@inheritDoc} */
	@HtmlSafe
    public String getText(String key)
    {
        return gpUtil.getText(key);
    }
    
    /**
     * <p>Getter for the field <code>canEdit</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getCanEdit()
    {
    	if(canEdit != null) return canEdit;
    	canEdit = gpUtil.canEdit(page);
    	return canEdit;
    }
    
    /**
     * <p>Setter for the field <code>canEdit</code>.</p>
     *
     * @param canEdit a boolean.
     */
    public void setCanEdit(boolean canEdit)
    {
        this.canEdit = canEdit;
    }
    
    /** {@inheritDoc} */
    public void addActionError(String msg)
    {
        if(!hasActionErrors())
            super.addActionError(msg);
    }
}
