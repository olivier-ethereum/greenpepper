package com.greenpepper.interpreter.flow;

import static com.greenpepper.GreenPepper.canContinue;
import static com.greenpepper.GreenPepper.shouldStop;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.AbstractInterpreter;
import com.greenpepper.util.ExampleUtil;

/**
 * Interprets a Command table specifications.
 * <p/>
 * Process a table containing a series of command.
 * Each line of table correspond to a command and its parameters.
 */
public class AbstractFlowInterpreter extends AbstractInterpreter
{
    private int startRow;
    private RowSelector rowSelector;

    public void interpret( Specification specification )
    {
		Statistics stats = new Statistics();
		
        skipFirstRowOfNextTable();

        while (specification.hasMoreExamples() && canContinue( stats ))
        {
            Example next = specification.nextExample();
            if (indicatesEndOfFlow( next ))
            {
                specification.exampleDone( new Statistics() );
                return;
            }

            Table table = new Table( firstRowOf(next) );
            execute(table);
            specification.exampleDone( table.getStatistics() );
			stats.tally( table.getStatistics() );

            includeFirstRowOfNextTable();
            /*
             *  In case there was a 'END' keyword sticked in the table.
             */
            Example lastCells = next.firstChild().lastSibling().firstChild();
            boolean indicatesEnd = "end".equalsIgnoreCase( ExampleUtil.contentOf( lastCells ) );
            if (indicatesEnd) {
                specification.exampleDone( new Statistics() );
                return;
            }
        }
    }

    protected void setRowSelector( RowSelector rowSelector )
    {
        this.rowSelector = rowSelector;
    }

    protected Example firstRowOf(Example next)
    {
        return next.at( 0, startRow );
    }

    private void execute(Table table)
    {
        while (table.hasMoreExamples() && canContinue( table.getStatistics() ))
        {
			Example example = table.peek();

            Row row = rowSelector.select( example );

			row.interpret( table );

			if (shouldStop( table.getStatistics() ))
			{
				example.firstChild().lastSibling().addSibling().annotate(Annotations.stopped());
			}
        }
    }

    private void includeFirstRowOfNextTable()
    {
        startRow = 0;
    }

    /**
     * The first Row is the header with the keyword
     */
    private void skipFirstRowOfNextTable()
    {
        startRow = 1;
    }

    private boolean indicatesEndOfFlow( Example table )
    {
        return "end".equalsIgnoreCase( ExampleUtil.contentOf( table.at( 0, 0, 0 ) ) );
    }
}
