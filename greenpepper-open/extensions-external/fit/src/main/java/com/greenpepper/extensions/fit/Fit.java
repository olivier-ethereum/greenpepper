package com.greenpepper.extensions.fit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import fit.ActionFixture;
import fit.TimedActionFixture;

/**
 * <p>Fit class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Fit 
{
    /** Constant <code>format</code> */
    public static DateFormat format = new SimpleDateFormat("hh:mm:ss");
	
    
    /**
     * <p>isATimedActionFitInterpreter.</p>
     *
     * @param sud a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     * @param name a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isATimedActionFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {           
        	Object target = sud.getFixture(name).getTarget();
            if (target instanceof TimedActionFixture)
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
    
	/**
	 * <p>isAnActionFitInterpreter.</p>
	 *
	 * @param sud a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isAnActionFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {           
        	Object target = sud.getFixture(name).getTarget();
            if (target.getClass().equals(ActionFixture.class))
            	return false;
            if (target instanceof ActionFixture)
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
	
	/**
	 * <p>isAFitInterpreter.</p>
	 *
	 * @param sud a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isAFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {
        	Object target = sud.getFixture(name).getTarget();
            if (target instanceof fit.Fixture && !target.getClass().equals(ActionFixture.class))
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
}
