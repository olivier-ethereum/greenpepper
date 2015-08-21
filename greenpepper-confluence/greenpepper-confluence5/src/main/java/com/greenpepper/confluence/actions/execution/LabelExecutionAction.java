package com.greenpepper.confluence.actions.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.pages.Page;

@SuppressWarnings("serial")
public class LabelExecutionAction extends AbstractListExecutionAction
{
    private String labels;
    private boolean searchQuery;
    
    public void buildExecutableList()
    {
        for(String labelExp : labels.split(","))
        {
            List<Page> pages = getExpLabeledPages(labelExp);                
            for(Page page : pages)
            {
				if (page.getSpaceKey().equals(spaceKey)
						&& !executableList.contains(page)
						&& gpUtil.isExecutable(page))
				{
					executableList.add(page);
				}
            }
        }
    }
    
    public String getLabels()
    {
        return labels;
    }
    
    public void setLabels(String labels)
    {
        this.labels = labels.replaceAll("&amp;", "&");
    }

    public boolean getSearchQuery()
    {
        return searchQuery;
    }

    public void setSearchQuery(boolean searchQuery)
    {
        this.searchQuery = searchQuery;
    }
    
    private List<Page> getExpLabeledPages(String labelExp)
    {
        StringTokenizer stk = new StringTokenizer(labelExp, "&");
        int tokens = stk.countTokens();
        if(tokens <= 1)
        {
            return getLabeledPages(labelExp.trim());
        }
        
        List<Page> pages = getLabeledPages(stk.nextToken().trim());
        for(int i = 1; i < tokens; i++)
        {
            pages.retainAll(getLabeledPages(stk.nextToken().trim()));
        }
        
        return pages;
    }
    
    @SuppressWarnings("unchecked")
    private List<Page> getLabeledPages(String label)
    {
        Label labelObject = gpUtil.getLabelManager().getLabel(label.trim());
        if(labelObject != null)
        {
            return gpUtil.getLabelManager().getContent(labelObject);  
        }
        
        return new ArrayList<Page>();
    }
}
