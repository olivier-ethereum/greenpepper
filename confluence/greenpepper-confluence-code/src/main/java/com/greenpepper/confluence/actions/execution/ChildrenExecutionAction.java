package com.greenpepper.confluence.actions.execution;

import java.util.List;

import com.atlassian.confluence.pages.Page;


/**
 * <p>ChildrenExecutionAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ChildrenExecutionAction extends AbstractListExecutionAction
{
    protected boolean allChildren;

    /**
     * <p>buildExecutableList.</p>
     */
    @SuppressWarnings("unchecked")
    public void buildExecutableList()
    {
        fillExecutableList(page);
    }
    
    /**
     * <p>Getter for the field <code>allChildren</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getAllChildren()
    {
        return allChildren;
    }
    
    /**
     * <p>Setter for the field <code>allChildren</code>.</p>
     *
     * @param allChildren a boolean.
     */
    public void setAllChildren(boolean allChildren)
    {
        this.allChildren = allChildren;
    }

    private void fillExecutableList(Page page)
    {
        List<Page> pageChildren = getPermittedChildren(page);
        for(Page child : pageChildren)
        {
            if(gpUtil.isExecutable(child))
            {
                executableList.add(child);
            }
            if(getAllChildren()) 
            {
                fillExecutableList(child);
            }
        } 
    }
}
