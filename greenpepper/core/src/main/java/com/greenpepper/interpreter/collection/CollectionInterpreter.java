/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.interpreter.collection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import static com.greenpepper.GreenPepper.canContinue;
import static com.greenpepper.GreenPepper.shouldStop;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.TypeConversion;
import com.greenpepper.annotation.Annotations;
import static com.greenpepper.annotation.Annotations.exception;
import static com.greenpepper.annotation.Annotations.ignored;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.interpreter.AbstractInterpreter;
import com.greenpepper.interpreter.HeaderForm;
import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.util.StringUtil;

// TODO: STATS compile stats here and in derived classes ( no test for that yet )
/**
 * <p>Abstract CollectionInterpreter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class CollectionInterpreter extends AbstractInterpreter
{
    private final Fixture fixture;
    protected final Statistics stats = new Statistics();

    /**
     * <p>statistics.</p>
     *
     * @return a {@link com.greenpepper.Statistics} object.
     */
    public Statistics statistics()
    {
        return this.stats;
    }

    /**
     * <p>Constructor for CollectionInterpreter.</p>
     *
     * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
     */
    protected CollectionInterpreter( Fixture fixture )
    {
        this.fixture = fixture;
    }

    /**
     * <p>toList.</p>
     *
     * @param results a {@link java.lang.Object} object.
     * @return a {@link java.util.List} object.
     */
    protected List<?> toList( Object results )
    {
        if (results instanceof Object[])
        {
            return Arrays.asList( (Object[]) results );
        }
        if (results instanceof Collection)
        {
            return new ArrayList<Object>( (Collection<?>) results );
        }
        return null;
    }

    /**
     * <p>executeRow.</p>
     *
     * @param valuesRow a {@link com.greenpepper.Example} object.
     * @param headers a {@link com.greenpepper.Example} object.
     * @param rowFixtureAdapter a {@link com.greenpepper.reflect.Fixture} object.
     */
    protected void executeRow( Example valuesRow, Example headers, Fixture rowFixtureAdapter )
    {
        stats.right();
        valuesRow.annotate( Annotations.right() );

        for (int i = 0; i != valuesRow.remainings(); ++i)
        {
            Example cell = valuesRow.at( i );

            if (i < headers.remainings())
            {
                try
                {
                    Call call = new Call( rowFixtureAdapter.check( headers.at( i ).getContent() ) );

                    if (!StringUtil.isBlank( cell.getContent() ))
                    {
                        call.expect( cell.getContent() );
                    }
                    call.will( Annotate.withDetails( cell ) );
                    
                    if (HeaderForm.parse(headers.at( i ).getContent()).isExpected())
                        call.will( Compile.statistics( stats ) );
                    
                    call.execute();
                }
                catch (Exception e)
                {
                    cell.annotate( exception( e ) );
                    stats.exception();
                }
            }
            else
            {
                cell.annotate( ignored( cell.getContent() ) );
            }
        }
    }

    /**
     * <p>addSurplusRow.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @param headers a {@link com.greenpepper.Example} object.
     * @param rowFixtureAdapter a {@link com.greenpepper.reflect.Fixture} object.
     */
    protected void addSurplusRow( Example example, Example headers, Fixture rowFixtureAdapter)
    {
        Example row = example.addSibling();

        for (int i = 0; i < headers.remainings(); i++)
        {
            Example cell = row.addChild();
            try
            {
                Call call = new Call( rowFixtureAdapter.check( headers.at( i ).getContent() ) );

                Object actual = call.execute();

                cell.setContent( TypeConversion.toString(actual));
                cell.annotate( Annotations.surplus() );
                if (i == 0) // Notify test listener on first cell only
                {
                    stats.wrong();
                }
            }
            catch (Exception e)
            {
                // TODO: STATS count stats?
                cell.annotate( ignored( e ) );
            }
        }
    }

    /**
     * <p>missingRow.</p>
     *
     * @param row a {@link com.greenpepper.Example} object.
     */
    protected void missingRow( Example row )
    {
        Example firstChild = row.firstChild();

        firstChild.annotate( Annotations.missing() );
        stats.wrong();

        if (firstChild.hasSibling())
        {
            for (Example cell : firstChild.nextSibling())
            {
                cell.annotate( Annotations.missing() );
            }
        }
    }

    private List<?> getCollectionProvider()
    {
        Object target = fixture.getTarget();

        for (Method method : target.getClass().getMethods())
        {
            if (method.isAnnotationPresent( CollectionProvider.class ))
            {
                return toList(invoke( target, method ));
            }
        }
        return null;
    }

    private Object invoke( Object target, Method method )
    {
        try
        {
            return method.invoke( target );
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * <p>getFixtureList.</p>
     *
     * @return a {@link java.util.List} object.
     * @throws java.lang.Exception if any.
     */
    public List<Fixture> getFixtureList() throws Exception
    {
        List results = getCollectionProvider();

        if (results == null)
        {
            results = toList( fixture.getTarget() );
        }

        if (results == null)
        {
            try
            {
                Call query = new Call( fixture.check( "query" ) );
                results = toList( query.execute() );
            }
            catch (NoSuchMessageException e)
            {}
        }

        if (results == null)
            throw new IllegalArgumentException( "results parameter is neither an Object[] nor a Collection" );

        List<Fixture> fixtures = new ArrayList<Fixture>();
        for (Object object : results)
        {
            fixtures.add( fixture.fixtureFor( object ) );
        }

        return fixtures;
    }

    /**
     * <p>mustProcessMissing.</p>
     *
     * @return a boolean.
     */
    protected boolean mustProcessMissing()
    {
        return false;
    }

    /**
     * <p>mustProcessSurplus.</p>
     *
     * @return a boolean.
     */
    protected boolean mustProcessSurplus()
    {
        return false;
    }

    /** {@inheritDoc} */
    public void interpret( Specification specification )
    {
        Example table = specification.nextExample();
        execute( table.at( 0, 1 ) );
        specification.exampleDone( stats );
    }

    /**
     * <p>execute.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     */
    public void execute( Example example )
    {
        try
        {
            List<Fixture> fixtures = getFixtureList();

            Example headers = example.at( 0, 0 );

            RowFixtureSplitter splitter = new RowFixtureSplitter();

            splitter.split( example.at( 1 ), fixtures, headers );

            for (RowFixture rowFixture : splitter.getMatch())
            {
				Example row = rowFixture.getRow();
                executeRow( row.firstChild(), headers, rowFixture.getAdapter() );

				if (shouldStop( stats ))
				{
					row.addChild().annotate(Annotations.stopped());
					break;
				}
            }

            if (mustProcessMissing() && canContinue( stats ))
            {
                for (Example row : splitter.getMissing())
                {
                    missingRow( row );

					if (shouldStop( stats ))
					{
						row.addChild().annotate(Annotations.stopped());
						break;
					}
                }
            }

            if (mustProcessSurplus() && canContinue( stats ))
            {
                for (Fixture adapter : splitter.getSurplus())
                {
                    addSurplusRow( example, headers, adapter);

					if (shouldStop( stats ))
					{
						example.lastSibling().addChild().annotate(Annotations.stopped());
						break;
					}
                }
            }
        }
        catch (Exception e)
        {
            stats.exception();
            example.firstChild().annotate( exception( e ) );

			if (shouldStop( stats ))
			{
				example.addChild().annotate(Annotations.stopped());
			}
        }
    }

	/**
	 * <p>applyRowStatistic.</p>
	 *
	 * @param rowStats a {@link com.greenpepper.Statistics} object.
	 */
	protected void applyRowStatistic(Statistics rowStats)
	{
		if (rowStats.exceptionCount() > 0)
		{
			stats.exception();
		}
		else if (rowStats.wrongCount() > 0)
		{
			stats.wrong();
		}
		else if (rowStats.rightCount() > 0)
		{
			stats.right();
		}
		else
		{
			stats.ignored();
		}
	}
}
