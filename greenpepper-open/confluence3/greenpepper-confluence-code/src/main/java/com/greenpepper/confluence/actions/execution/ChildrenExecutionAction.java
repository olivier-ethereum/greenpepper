package com.greenpepper.confluence.actions.execution;

import java.util.List;

import com.atlassian.confluence.pages.Page;


@SuppressWarnings("serial")
public class ChildrenExecutionAction extends AbstractListExecutionAction
{
    protected boolean allChildren;

    @SuppressWarnings("unchecked")
    public void buildExecutableList()
    {
        fillExecutableList(page);
    }
    
    public boolean getAllChildren()
    {
        return allChildren;
    }
    
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
