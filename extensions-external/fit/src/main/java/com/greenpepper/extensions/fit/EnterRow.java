package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.reflect.Fixture;

/**
 * <p>EnterRow class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class EnterRow extends FitDefaultRow 
{

	/**
	 * <p>Constructor for EnterRow.</p>
	 *
	 * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
	 * @param timed a {@link java.lang.Boolean} object.
	 */
	public EnterRow(Fixture fixture, Boolean timed) 
	{
		super(null, fixture, timed);
	}
	
	

	/** {@inheritDoc} */
	@Override
	public List<Example> actionCells(Example row) 
	{
		List<Example> enterRow = new ArrayList<Example>();
		enterRow.add(row.at(0, 1));
		enterRow.add(row.at(0, 2));
		return enterRow;
    }
}
