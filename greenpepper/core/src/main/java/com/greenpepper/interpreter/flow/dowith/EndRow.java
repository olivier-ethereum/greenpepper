package com.greenpepper.interpreter.flow.dowith;

import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.SyntaxException;
import com.greenpepper.util.CollectionUtil;

/**
 * This row with the 'END' keyword will stop the flow.
 * @author wattazoum
 *
 */
public class EndRow implements Row
{

    /**
     * 
     * @param fixture
     */
    public EndRow(Fixture fixture) {
        
    }
    
    /**
     * Cases of failures : 
     * <ul>
     *  <li>this row is not the last row of the Specification table</li>
     *  <li>there is more than one cell in this row</li>
     * </ul>
     */
    @Override
    public void interpret(Specification table)
    {
        Example row = table.nextExample();
        Statistics statistics = new Statistics(0, 0, 0, 0);
        if (table.hasMoreExamples()) {
            SyntaxException e = new SyntaxException("end", "This keyword should end the table");
            statistics.exception();
            CollectionUtil.first( keywordCells(row) ).annotate( Annotations.exception( e ) );
        }
        if (row.at(0, 1) != null) {            
            SyntaxException e = new SyntaxException("end", "This keyword doesn't take any argument");
            statistics.exception();
            CollectionUtil.first( keywordCells(row) ).annotate( Annotations.exception( e ) );
        }
        table.exampleDone(statistics);
    }

    private List<Example> keywordCells(Example row)
    {
        return CollectionUtil.even( row.firstChild() );
    }
}
