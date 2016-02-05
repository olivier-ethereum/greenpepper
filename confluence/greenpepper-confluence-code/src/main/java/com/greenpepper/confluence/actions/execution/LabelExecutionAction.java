package com.greenpepper.confluence.actions.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.pages.Page;

/**
 * <p>LabelExecutionAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class LabelExecutionAction extends AbstractListExecutionAction
{
    private String labels;
    private boolean searchQuery;
    
    /**
     * <p>buildExecutableList.</p>
     */
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
    
    /**
     * <p>Getter for the field <code>labels</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLabels()
    {
        return labels;
    }
    
    /**
     * <p>Setter for the field <code>labels</code>.</p>
     *
     * @param labels a {@link java.lang.String} object.
     */
    public void setLabels(String labels)
    {
        this.labels = labels.replaceAll("&amp;", "&");
    }

    /**
     * <p>Getter for the field <code>searchQuery</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getSearchQuery()
    {
        return searchQuery;
    }

    /**
     * <p>Setter for the field <code>searchQuery</code>.</p>
     *
     * @param searchQuery a boolean.
     */
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
