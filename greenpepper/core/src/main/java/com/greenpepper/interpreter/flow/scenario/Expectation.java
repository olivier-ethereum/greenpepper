
/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.interpreter.flow.scenario;

import com.greenpepper.expectation.DuckExpectation;
public class Expectation
{
    private String expected;
    private Object actual;
    private String describe;

    /**
     * <p>Constructor for Expectation.</p>
     */
    public Expectation()
    {
    }

    /**
     * <p>Constructor for Expectation.</p>
     *
     * @param expected a {@link java.lang.String} object.
     */
    public Expectation(String expected)
    {
        this.expected = expected;
    }

    /**
     * <p>meets.</p>
     *
     * @return a boolean.
     */
    public boolean meets()
    {
        DuckExpectation duck = DuckExpectation.create( getExpected() );
        return duck.meets( actual );
    }

    /**
     * <p>Getter for the field <code>expected</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExpected()
    {
        return expected;
    }

    /**
     * <p>Getter for the field <code>actual</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getActual()
    {
        return actual;
    }

    /**
     * <p>Setter for the field <code>actual</code>.</p>
     *
     * @param actual a {@link java.lang.Object} object.
     */
    public void setActual(Object actual)
    {
        this.actual = actual;
    }

    /**
     * <p>Getter for the field <code>describe</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescribe()
    {
        return describe == null ? String.format( "%s (and not %s)", getActual(), getExpected() ) : describe;
    }

    /**
     * <p>Setter for the field <code>describe</code>.</p>
     *
     * @param describe a {@link java.lang.String} object.
     */
    public void setDescribe(String describe)
    {
        this.describe = describe;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format( "Expectation: { expected: %s, actual: %s }", expected, actual );
    }
}
