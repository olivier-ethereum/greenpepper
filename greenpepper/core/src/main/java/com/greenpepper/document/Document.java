package com.greenpepper.document;

import static com.greenpepper.GreenPepper.canContinue;

import java.io.PrintWriter;

import com.greenpepper.Example;
import com.greenpepper.Interpreter;
import com.greenpepper.Statistics;
import com.greenpepper.TimeStatistics;

/**
 * <p>Document class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Document
{
    private final String type;
    private final Example start;
    private final Statistics stats;
	private final TimeStatistics timeStats;
    private final CompositeFilter filters;
	private String[] sections;
	private final String name;
	private final String externalLink;
    private String uri;

    private SpecificationListener listener = new NullSpecificationListener();

    /**
     * <p>html.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.document.Document} object.
     */
    public static Document html( Example example )
    {
        return new Document( "html", example );
    }

	/**
	 * <p>html.</p>
	 *
	 * @param example a {@link com.greenpepper.Example} object.
	 * @param name a {@link java.lang.String} object.
	 * @param externalLink a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.document.Document} object.
	 */
	public static Document html( Example example, String name, String externalLink )
	{
		return new Document( "html", example, name, externalLink );
	}

    /**
     * <p>text.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.document.Document} object.
     */
    public static Document text( Example example )
    {
        return new Document( "txt", example );
    }

	/**
	 * <p>Constructor for Document.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 * @param example a {@link com.greenpepper.Example} object.
	 */
	public Document( String type, Example example )
	{
		this( type, example, null, null );
	}

    /**
     * <p>Constructor for Document.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @param example a {@link com.greenpepper.Example} object.
     * @param name a {@link java.lang.String} object.
     * @param externalLink a {@link java.lang.String} object.
     */
    public Document( String type, Example example, String name, String externalLink )
    {
        this.type = type;
        this.start = example;
		this.name = name;
		this.externalLink = externalLink;
        this.stats = new Statistics();
		this.timeStats = new TimeStatistics();
        this.filters = new CompositeFilter();
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType()
    {
        return type;
    }

	/**
	 * <p>Getter for the field <code>sections</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getSections()
	{
		return sections;
	}

	/**
	 * <p>Setter for the field <code>sections</code>.</p>
	 *
	 * @param sections an array of {@link java.lang.String} objects.
	 */
	public void setSections(String[] sections)
	{
		this.sections = sections;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * <p>Getter for the field <code>externalLink</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getExternalLink()
	{
		return externalLink;
	}

	/**
	 * <p>setSpecificationListener.</p>
	 *
	 * @param listener a {@link com.greenpepper.document.SpecificationListener} object.
	 */
	public void setSpecificationListener( SpecificationListener listener )
    {
        this.listener = listener;
    }

    /**
     * <p>execute.</p>
     *
     * @param interpreterSelector a {@link com.greenpepper.document.InterpreterSelector} object.
     */
    public void execute(InterpreterSelector interpreterSelector)
    {
        AbstractSpecification spec = new FilteredSpecification( start );

        while (spec.hasMoreExamples() && canContinue( stats ))
        {
            Interpreter interpreter = interpreterSelector.selectInterpreter( spec.peek() );
            interpreter.interpret( spec );
        }
    }

    /**
     * <p>print.</p>
     *
     * @param writer a {@link java.io.PrintWriter} object.
     */
    public void print( PrintWriter writer )
    {
        start.print( writer );
    }

    /**
     * <p>tally.</p>
     *
     * @param statistics a {@link com.greenpepper.Statistics} object.
     */
    public void tally( Statistics statistics )
    {
        stats.tally( statistics );
    }

    /**
     * <p>Getter for the field <code>uri</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUri() {
        return uri;
    }

    /**
     * <p>Setter for the field <code>uri</code>.</p>
     *
     * @param uri a {@link java.lang.String} object.
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    public class FilteredSpecification extends AbstractSpecification
    {
        public FilteredSpecification(Example start)
        {
            super();
            
            setStart( start );
        }

        protected Example peek()
        {
            return new EagerFilter( filters ).filter( cursor.nextSibling() );
        }

        public void exampleDone( Statistics statistics )
        {
            tally( statistics );
            listener.exampleDone( cursor, statistics );
        }
    }

    /**
     * Notify the registered listener that the specification is done
     */
    public void done()
    {
        listener.specificationDone( start, stats );
    }

    /**
     * <p>addFilter.</p>
     *
     * @param filter a {@link com.greenpepper.document.ExampleFilter} object.
     */
    public void addFilter( ExampleFilter filter )
    {
        filters.add( filter );
    }

    /**
     * <p>getStatistics.</p>
     *
     * @return a {@link com.greenpepper.Statistics} object.
     */
    public Statistics getStatistics()
    {
        return stats;
    }

	/**
	 * <p>getTimeStatistics.</p>
	 *
	 * @return a {@link com.greenpepper.TimeStatistics} object.
	 */
	public TimeStatistics getTimeStatistics()
	{
		return timeStats;
	}
}
