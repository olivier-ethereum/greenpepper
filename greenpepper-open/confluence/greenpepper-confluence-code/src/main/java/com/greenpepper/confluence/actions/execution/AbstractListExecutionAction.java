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

/**
 * <p>Abstract AbstractListExecutionAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
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
    
    /**
     * <p>Getter for the field <code>executableList</code>.</p>
     *
     * @return a {@link java.util.LinkedList} object.
     */
    public LinkedList<Page> getExecutableList()
    {
        if(executableList != null) return executableList;
        
        executableList = new LinkedList<Page>();        
        buildExecutableList();
        sortList();
        
        return executableList;
    }
    
    /**
     * <p>Getter for the field <code>forcedSystemUnderTests</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
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
    
    /**
     * <p>Getter for the field <code>forcedSuts</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getForcedSuts()
    {
        return forcedSuts;
    }
    
    /**
     * <p>Setter for the field <code>forcedSuts</code>.</p>
     *
     * @param forcedSuts a {@link java.lang.String} object.
     */
    public void setForcedSuts(String forcedSuts)
    {
        this.forcedSuts = forcedSuts;
    }

    /**
     * <p>Getter for the field <code>showList</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getShowList()
    {
        return showList;
    }

    /**
     * <p>Setter for the field <code>showList</code>.</p>
     *
     * @param showList a boolean.
     */
    public void setShowList(boolean showList)
    {
        this.showList = showList;
    }
    
	/**
	 * <p>isExecutable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isExecutable()
	{
		List<SystemUnderTest> forcedSystemUnderTests = getForcedSystemUnderTests();

		return (forcedSystemUnderTests != null && !forcedSystemUnderTests.isEmpty() && !getExecutableList().isEmpty());
	}

	/**
	 * <p>Getter for the field <code>reverse</code>.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean getReverse() 
	{
		return reverse;
	}

	/**
	 * <p>Setter for the field <code>reverse</code>.</p>
	 *
	 * @param reverse a {@link java.lang.Boolean} object.
	 */
	public void setReverse(Boolean reverse) 
	{
		this.reverse = reverse;
	}

	/**
	 * <p>Getter for the field <code>sortType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSortType() 
	{
		return sortType;
	}

	/**
	 * <p>Setter for the field <code>sortType</code>.</p>
	 *
	 * @param sortType a {@link java.lang.String} object.
	 */
	public void setSortType(String sortType) 
	{
		this.sortType = StringUtil.toEmptyIfNull(sortType);
	}

    /**
     * <p>buildExecutableList.</p>
     */
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

	/**
	 * <p>Getter for the field <code>openInSameWindow</code>.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean getOpenInSameWindow() {
		return openInSameWindow;
	}

	/**
	 * <p>Setter for the field <code>openInSameWindow</code>.</p>
	 *
	 * @param openInSameWindow a {@link java.lang.Boolean} object.
	 */
	public void setOpenInSameWindow(Boolean openInSameWindow) {
		this.openInSameWindow = openInSameWindow;
	}
}
