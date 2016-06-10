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

/**
 * <p>Property class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Property
{
    private Money marketValue;

    /**
     * <p>valued.</p>
     *
     * @param marketValue a {@link com.greenpepper.samples.application.mortgage.Money} object.
     * @return a {@link com.greenpepper.samples.application.mortgage.Property} object.
     */
    public static Property valued(Money marketValue)
    {
        return new Property(marketValue);
    }

    /**
     * <p>Constructor for Property.</p>
     *
     * @param marketValue a {@link com.greenpepper.samples.application.mortgage.Money} object.
     */
    public Property(Money marketValue)
    {
        this.marketValue = marketValue;
    }

    /**
     * <p>Getter for the field <code>marketValue</code>.</p>
     *
     * @return a {@link com.greenpepper.samples.application.mortgage.Money} object.
     */
    public Money getMarketValue()
    {
        return marketValue;
    }
}
