package com.greenpepper.interpreter;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.annotation.IgnoredAnnotation;
import com.greenpepper.annotation.RightAnnotation;
import com.greenpepper.annotation.WrongAnnotation;

/**
 * <p>MyOwnInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class MyOwnInterpreter extends AbstractInterpreter
{

    /**
     * <p>Constructor for MyOwnInterpreter.</p>
     *
     * @param systemUnderDevelopment a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public MyOwnInterpreter( SystemUnderDevelopment systemUnderDevelopment )
    {
        
    }
    
    /** {@inheritDoc} */
    public void interpret(Specification specification)
    {
        Statistics stats = new Statistics();
        Example table = specification.nextExample();
        
        for (Example row = table.at( 0, 1 ); row != null; row = row.nextSibling())
        {
            doRow( row );
        }
        
        specification.exampleDone(stats);
    }
    
    private void doRow( Example row )
    {
        if ("right".equals(row.firstChild().at(0).getContent()))
        {
            row.firstChild().at(1).annotate(new RightAnnotation());
        }
        
        if ("wrong".equals(row.firstChild().at(0).getContent()))
        {
            row.firstChild().at(1).annotate(new WrongAnnotation());
        }
        
        if ("ignore".equals(row.firstChild().at(0).getContent()))
        {
            row.firstChild().at(1).annotate(new IgnoredAnnotation(""));
        }
    }

}
