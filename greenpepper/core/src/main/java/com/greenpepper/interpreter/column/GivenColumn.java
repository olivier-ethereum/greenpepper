
/**
 * <p>GivenColumn class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.call.ResultIs;
import com.greenpepper.reflect.Message;
public class GivenColumn extends Column
{
    private Message send;

    /**
     * <p>Constructor for GivenColumn.</p>
     *
     * @param send a {@link com.greenpepper.reflect.Message} object.
     * @throws java.lang.Exception if any.
     */
    public GivenColumn( Message send ) throws Exception
    {
        this.send = send;
    }

    /** {@inheritDoc} */
    public Statistics doCell( Example cell ) throws Exception
    {
    	Statistics stats = new Statistics();
        
    	Call call = new Call( send );
        call.will(Annotate.exception( cell )).when(ResultIs.exception());
        call.will(Compile.statistics(stats)).when(ResultIs.exception());
        call.execute( cell.getContent() );
        
        return stats;
    }
}
