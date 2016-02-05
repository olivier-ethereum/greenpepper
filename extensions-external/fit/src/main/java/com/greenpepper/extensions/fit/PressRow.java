package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.reflect.Fixture;

/**
 * <p>PressRow class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PressRow extends FitDefaultRow 
{
	/**
	 * <p>Constructor for PressRow.</p>
	 *
	 * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
	 * @param timed a {@link java.lang.Boolean} object.
	 */
	public PressRow(Fixture fixture, Boolean timed) 
	{
		super(null, fixture, timed);
	}

	/** {@inheritDoc} */
	@Override
	public List<Example> actionCells(Example row) 
	{
		List<Example> pressRow = new ArrayList<Example>();
		pressRow.add(row.at(0, 1));
		return pressRow;
    }
}
