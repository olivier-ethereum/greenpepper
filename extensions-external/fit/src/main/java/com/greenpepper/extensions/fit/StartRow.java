package com.greenpepper.extensions.fit;

import com.greenpepper.Specification;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.reflect.Fixture;

/**
 * <p>StartRow class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class StartRow implements Row 
{
	/**
	 * <p>Constructor for StartRow.</p>
	 *
	 * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
	 * @param timed a {@link java.lang.Boolean} object.
	 */
	public StartRow(Fixture fixture, Boolean timed) {}
	
	/** {@inheritDoc} */
	public void interpret(Specification row) 
	{
		row.nextExample();
	}
}
