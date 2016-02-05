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
package com.greenpepper.expectation;

import com.greenpepper.util.Factory;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>ShouldBe class.</p>
 *
 * @version $Revision: $ $Date: $
 * @author oaouattara
 */
public final class ShouldBe
{
    /** Constant <code>TRUE</code> */
    public static final Expectation TRUE = new EqualExpectation(true);

    /** Constant <code>FALSE</code> */
    public static final Expectation FALSE = new EqualExpectation(false);

    /** Constant <code>NULL</code> */
    public static final Expectation NULL = new NullExpectation();

    private static List<Factory<Expectation>> factories = new LinkedList<Factory<Expectation>>();

    static
    {
        register(IsInstanceExpectation.class);
        register(ErrorExpectation.class);
        register(NullExpectation.class);
    }

    private ShouldBe()
    {
    }

    /**
     * <p>register.</p>
     *
     * @param factoryClass a {@link java.lang.Class} object.
     */
    public static void register(Class<? extends Expectation> factoryClass)
    {
        factories.add(new Factory<Expectation>(factoryClass));
    }

    /**
     * <p>equal.</p>
     *
     * @param o a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.expectation.Expectation} object.
     */
    public static Expectation equal(Object o)
    {
        return new EqualExpectation(o);
    }

    /**
     * <p>instanceOf.</p>
     *
     * @param c a {@link java.lang.Class} object.
     * @return a {@link com.greenpepper.expectation.Expectation} object.
     */
    public static Expectation instanceOf(Class c)
    {
        return new IsInstanceExpectation(c);
    }

    /**
     * <p>literal.</p>
     *
     * @param expected a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.expectation.Expectation} object.
     */
    public static Expectation literal(String expected)
    {
        if (expected == null)
            throw new NullPointerException("expected");

        for (int i = factories.size() - 1; i >= 0; i--)
        {
            Factory<Expectation> factory = factories.get(i);
            Expectation expectation = factory.newInstance(expected);
            if (expectation != null)
                return expectation;
        }

        return new DuckExpectation(expected);
    }

    /**
     * <p>either.</p>
     *
     * @param expectation a {@link com.greenpepper.expectation.Expectation} object.
     * @return a {@link com.greenpepper.expectation.Either} object.
     */
    public static Either either(Expectation expectation)
    {
        return new Either(expectation);
    }

	/**
	 * <p>not.</p>
	 *
	 * @param expectation a {@link com.greenpepper.expectation.Expectation} object.
	 * @return a {@link com.greenpepper.expectation.Expectation} object.
	 */
	public static Expectation not(Expectation expectation)
	{
		return new NotExpectation(expectation);
	}
}
