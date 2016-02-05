package com.greenpepper.interpreter.flow.action;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.AbstractAction;
import com.greenpepper.util.ExampleUtil;

/**
 * <p>Action class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Action extends AbstractAction
{
    /**
     * <p>Constructor for Action.</p>
     *
     * @param cells a {@link java.lang.Iterable} object.
     */
    public Action(Iterable<String> cells)
    {
		super(cells);
	}

	/**
	 * <p>parse.</p>
	 *
	 * @param cells a {@link java.util.List} object.
	 * @return a {@link com.greenpepper.interpreter.flow.action.Action} object.
	 */
	public static Action parse(List<Example> cells)
    {
        return new Action( ExampleUtil.contentAsList(cells) );
	}

	/**
	 * <p>keywords.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	protected List<String> keywords()
    {
        return cellsAsList().subList(0, 1);
    }

    /**
     * <p>parameters.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List<String> parameters()
    {   
        List<String> cellsAsList = cellsAsList();
		return cellsAsList.subList(1, cellsAsList.size());
    }

	private List<String> cellsAsList()
    {
		List<String> parameters = new ArrayList<String>();
    	for (String cell : getCells()) {
			parameters.add(cell);
		}
		return parameters;
	}
}
