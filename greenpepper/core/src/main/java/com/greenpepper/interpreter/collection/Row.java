package com.greenpepper.interpreter.collection;

import static com.greenpepper.util.ExampleUtil.contentOf;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.expectation.Expectation;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.interpreter.HeaderForm;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.StringUtil;

/**
 * <p>Row class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Row
{
    private final Fixture fixture;
    private final Example headers;

    /**
     * <p>Constructor for Row.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     * @param headers a {@link com.greenpepper.Example} object.
     */
    public Row(Fixture fixture, Example headers)
    {
        this.headers = headers;
        this.fixture = fixture;
    }

    /**
     * <p>parse.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     * @param headers a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.interpreter.collection.Row} object.
     */
    public static Row parse(Fixture fixture, Example headers)
    {
        return new Row( fixture, headers );
    }

    /**
     * <p>matches.</p>
     *
     * @param cells a {@link com.greenpepper.Example} object.
     * @return a boolean.
     */
    public boolean matches(Example cells)
    {
        for (int i = 0; i < cells.remainings(); ++i)
        {
            Example cell = cells.at( i );

            if (i < headers.remainings())
            {
                if (HeaderForm.parse( contentOf( headers.at( i ) ) ).isExpected()) continue;
                if (StringUtil.isBlank( contentOf( cell ) )) continue;

                try
                {
                    Call call = new Call( fixture.check( contentOf( headers.at( i ) ) ) );
                    Expectation expectation = ShouldBe.literal( contentOf( cell ) );

                    Object result = call.execute();
                    if (!expectation.meets( result ))
                    {
                        return false;
                    }
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        }

        return true;
    }
}
