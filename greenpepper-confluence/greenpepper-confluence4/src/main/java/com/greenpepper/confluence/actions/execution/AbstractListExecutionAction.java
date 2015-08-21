package com.greenpepper.confluence.actions.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.util.ContentComparatorFactory;
import com.greenpepper.confluence.actions.SpecificationAction;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.util.StringUtil;

@SuppressWarnings("serial")
public abstract class AbstractListExecutionAction extends SpecificationAction
{
    protected LinkedList<Page> executableList;
    protected List<SystemUnderTest> forcedSystemUnderTests;
    private String forcedSuts;
    private String sortType = "title";
    private Boolean reverse = Boolean.FALSE;
    private Boolean openInSameWindow = Boolean.TRUE;
    
    private boolean showList;
    
    public LinkedList<Page> getExecutableList()
    {
        if(executableList != null) return executableList;
        
        executableList = new LinkedList<Page>();        
        buildExecutableList();
        sortList();
        
        return executableList;
    }
    
    public List<SystemUnderTest> getForcedSystemUnderTests()
    {
        try
        {
            if(forcedSystemUnderTests != null) return forcedSystemUnderTests;
            if(StringUtil.isEmpty(forcedSuts)) return gpUtil.getSystemsUnderTests(spaceKey);
            
            List<SystemUnderTest> forcedSystemUnderTests = new ArrayList<SystemUnderTest>();
            List<SystemUnderTest> projectSuts =  gpUtil.getSystemsUnderTests(spaceKey);
            for(String sutName : forcedSuts.split(","))
            {
                sutName = sutName.trim();
                for(SystemUnderTest sut : projectSuts)
                {
                    if(sut.getName().equals(sutName))
                    {
                        forcedSystemUnderTests.add(sut);
                    }
                }
            }
            
            return forcedSystemUnderTests;
        }
        catch (GreenPepperServerException e)
        {
            return new ArrayList<SystemUnderTest>();
        }
    }
    
    public String getForcedSuts()
    {
        return forcedSuts;
    }
    
    public void setForcedSuts(String forcedSuts)
    {
        this.forcedSuts = forcedSuts;
    }

    public boolean getShowList()
    {
        return showList;
    }

    public void setShowList(boolean showList)
    {
        this.showList = showList;
    }
    
	public boolean isExecutable()
	{
		List<SystemUnderTest> forcedSystemUnderTests = getForcedSystemUnderTests();

		return (forcedSystemUnderTests != null && !forcedSystemUnderTests.isEmpty() && !getExecutableList().isEmpty());
	}

	public Boolean getReverse() 
	{
		return reverse;
	}

	public void setReverse(Boolean reverse) 
	{
		this.reverse = reverse;
	}

	public String getSortType() 
	{
		return sortType;
	}

	public void setSortType(String sortType) 
	{
		this.sortType = StringUtil.toEmptyIfNull(sortType);
	}

    public abstract void buildExecutableList();

    @SuppressWarnings("unchecked")
	private void sortList()
    {
    	try
    	{
			if(StringUtil.isEmpty(sortType))
			{
				if(reverse) Collections.reverse(executableList);
			}
			else 
			{
				Collections.sort(executableList, ContentComparatorFactory.getComparator(sortType, reverse));
			}
    	}
	    catch(IllegalArgumentException e)
	    {
	    	// DONT SORT
	    }	    
    }

	public Boolean getOpenInSameWindow() {
		return openInSameWindow;
	}

	public void setOpenInSameWindow(Boolean openInSameWindow) {
		this.openInSameWindow = openInSameWindow;
	}
}
