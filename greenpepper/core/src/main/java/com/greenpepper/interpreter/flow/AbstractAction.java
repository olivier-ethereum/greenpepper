package com.greenpepper.interpreter.flow;

import static com.greenpepper.util.CollectionUtil.filter;
import static com.greenpepper.util.CollectionUtil.joinAsString;
import static com.greenpepper.util.CollectionUtil.toArray;

import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.util.NotBlank;

/**
 * <p>Abstract AbstractAction class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractAction
{
    private final Iterable<String> cells;

    /**
     * <p>Constructor for AbstractAction.</p>
     *
     * @param cells a {@link java.lang.Iterable} object.
     */
    public AbstractAction( Iterable<String> cells )
    {
        this.cells = cells;
    }

    /**
     * <p>Getter for the field <code>cells</code>.</p>
     *
     * @return a {@link java.lang.Iterable} object.
     */
    protected Iterable<String> getCells()
    {
    	return this.cells;
    }
    
    /**
     * <p>checkAgainst.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     * @return a {@link com.greenpepper.Call} object.
     * @throws java.lang.Exception if any.
     */
    public Call checkAgainst( Fixture fixture ) throws Exception
    {
        return call( fixture.check( name() ) );
    }

    /**
     * <p>call.</p>
     *
     * @param message a {@link com.greenpepper.reflect.Message} object.
     * @return a {@link com.greenpepper.Call} object.
     * @throws com.greenpepper.reflect.NoSuchMessageException if any.
     */
    protected Call call( Message message ) throws NoSuchMessageException
    {
        Call call = new Call( message );
        call.addInput( arguments() );
        return call;
    }

    /**
     * <p>keywords.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected abstract List<String> keywords();

    /**
     * <p>parameters.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected abstract List<String> parameters();

    /**
     * <p>arguments.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] arguments()
    {
        return toArray( parameters() );
    }

    /**
     * <p>name.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String name()
    {
        return joinAsString( toArray( filter ( keywords(), new NotBlank() ) ), " ");
    }
    
}
