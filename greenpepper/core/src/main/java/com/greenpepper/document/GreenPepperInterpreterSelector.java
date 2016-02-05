package com.greenpepper.document;

import static com.greenpepper.util.CollectionUtil.toArray;
import static com.greenpepper.util.CollectionUtil.toList;

import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.GreenPepper;
import com.greenpepper.Interpreter;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.SkipInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExampleUtil;

/**
 * This class is responsible for selecting an interpreter from the first row of a table.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperInterpreterSelector implements InterpreterSelector
{
    protected final SystemUnderDevelopment systemUnderDevelopment;

    /**
     * <p>Constructor for GreenPepperInterpreterSelector.</p>
     *
     * @param systemUnderDevelopment a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public GreenPepperInterpreterSelector(SystemUnderDevelopment systemUnderDevelopment)
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    /** {@inheritDoc} */
    public Interpreter selectInterpreter(Example table)
    {
        Example cells = table.at(0, 0, 0);
        if (cells == null) return new SkipInterpreter();

        String interpreterName = cells.at(0).getContent();
        String[] fixtureAndParameters = fixtureAndParams(cells.at(1));
        try
        {
            Object[] args = CollectionUtil.isEmpty(fixtureAndParameters) ?
                    toArray(systemUnderDevelopment) : toArray(selectFixture(fixtureAndParameters));
            return GreenPepper.getInterpreter(interpreterName, systemUnderDevelopment.getClass(), args);
        }
        catch (Throwable t)
        {
            cells.at(0).annotate(Annotations.exception(t));
            return new SkipInterpreter(new Statistics(0, 0, 1, 0));
        }
    }

    private String[] fixtureAndParams(Example cell)
    {
        List<Example> list = CollectionUtil.even(ExampleUtil.asList(cell));
        return ExampleUtil.content(list);
    }

    private Fixture selectFixture(String[] fixtureAndParameters) throws Throwable
    {
        List<String> values = toList(fixtureAndParameters);
        String name = CollectionUtil.shift(values);
        return systemUnderDevelopment.getFixture(name, values.toArray( new String[values.size()] ) );
    }
}
