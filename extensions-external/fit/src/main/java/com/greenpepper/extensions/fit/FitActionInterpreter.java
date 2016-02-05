package com.greenpepper.extensions.fit;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.AbstractFlowInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;

/**
 * <p>FitActionInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class FitActionInterpreter extends AbstractFlowInterpreter
{
	private SystemUnderDevelopment sud;
	private FitActionRowSelector selector;
	
	/**
	 * <p>Constructor for FitActionInterpreter.</p>
	 *
	 * @param sud a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
	 * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
	 * @param timed a boolean.
	 */
	public FitActionInterpreter(SystemUnderDevelopment sud, Fixture fixture, boolean timed)
	{
        selector = new FitActionRowSelector(sud, fixture, timed);
        setRowSelector(selector);
		this.sud = sud;
	}

	/** {@inheritDoc} */
	@Override
    protected Example firstRowOf(Example next)
    {
		if(Fit.isAFitInterpreter(sud, ExampleUtil.contentOf(next.firstChild())))
			return next.at( 0, 0 );
		
		return next.at( 0, 1 );
	}
	
	/**
	 * <p>getRowSelector.</p>
	 *
	 * @return a {@link com.greenpepper.extensions.fit.FitActionRowSelector} object.
	 */
	protected FitActionRowSelector getRowSelector()
	{
		return selector;
	}
}
