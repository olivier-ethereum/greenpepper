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

package com.greenpepper.samples.application.mortgage;

import java.math.BigDecimal;

/**
 * <p>Ratio class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Ratio
{
    private BigDecimal numerator;
    private BigDecimal divisor;

    /**
     * <p>Constructor for Ratio.</p>
     *
     * @param value a {@link java.math.BigDecimal} object.
     * @param base a {@link java.math.BigDecimal} object.
     */
    public Ratio(BigDecimal value, BigDecimal base)
    {
        this.numerator = value;
        this.divisor = base;
    }

    /**
     * <p>percent.</p>
     *
     * @param value a long.
     * @return a {@link com.greenpepper.samples.application.mortgage.Ratio} object.
     */
    public static Ratio percent(long value)
    {
        return Ratio.of( value, 100 );
    }

    /**
     * <p>of.</p>
     *
     * @param value a long.
     * @param base a long.
     * @return a {@link com.greenpepper.samples.application.mortgage.Ratio} object.
     */
    public static Ratio of(long value, long base )
    {
        return new Ratio( BigDecimal.valueOf( value ), BigDecimal.valueOf( base ) );
    }

    /**
     * <p>applyTo.</p>
     *
     * @param value a {@link java.math.BigDecimal} object.
     * @return a {@link java.math.BigDecimal} object.
     */
    public BigDecimal applyTo(BigDecimal value)
    {
        return value.multiply( numerator ).divide( divisor );
    }

    /**
     * <p>inverse.</p>
     *
     * @return a {@link com.greenpepper.samples.application.mortgage.Ratio} object.
     */
    public Ratio inverse()
    {
        return new Ratio( divisor, numerator );
    }
}
