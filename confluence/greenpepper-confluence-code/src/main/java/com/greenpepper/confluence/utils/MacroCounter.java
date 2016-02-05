package com.greenpepper.confluence.utils;

/**
 * <p>MacroCounter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class MacroCounter
{
    private static MacroCounter labelCounter = new MacroCounter();
    private static int counter = 0;
    
    private MacroCounter(){}
    
    /**
     * <p>instance.</p>
     *
     * @return a {@link com.greenpepper.confluence.utils.MacroCounter} object.
     */
    public static MacroCounter instance() { return labelCounter; }
    
    /**
     * <p>getNextCount.</p>
     *
     * @return a int.
     */
    public synchronized int getNextCount() 
    { 
        if(counter > 999)
        {       
            counter = 0;
        }
        
        return counter++; 
    }
}
