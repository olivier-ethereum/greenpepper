/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.samples.application.bank;

import java.math.BigDecimal;

/**
 * <p>Money class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Money
{
    /** Constant <code>ZERO</code> */
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal dollars;

    
    /**
     * <p>Constructor for Money.</p>
     *
     * @param dollars a {@link java.math.BigDecimal} object.
     */
    public Money(BigDecimal dollars)
    {
        this.dollars = dollars;
    }

    /**
     * <p>parse.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public static Money parse(String text)
    {
        return new Money( new BigDecimal( normalize( text ) ) );
    }

    private static String normalize(String text)
    {
        return text.replaceAll( "\\$", "").replaceAll( ",", "").replaceAll( "\\s", "");
    }

    /**
     * <p>dollars.</p>
     *
     * @param amount a float.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public static Money dollars(float amount)
    {
        return new Money( new BigDecimal( amount ) );
    }
    
    /**
     * <p>$.</p>
     *
     * @param amount a float.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public static Money $(float amount)
    {
        return dollars(amount);
    }

    /**
     * <p>zero.</p>
     *
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public static Money zero()
    {
        return dollars(0f);
    }

    /** {@inheritDoc} */
    public boolean equals(Object other)
    {
        if (other instanceof Money)
        {
            Money that = (Money) other;
            return this.dollars.compareTo( that.dollars ) == 0;
        }
        else
        {
            return false;
        }
    }

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return dollars.hashCode();
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString()
    {
        return "$" + dollars;
    }

    /**
     * <p>times.</p>
     *
     * @param multiplier a {@link java.math.BigDecimal} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money times(BigDecimal multiplier)
    {
        return new Money(dollars.multiply(multiplier));
    }

    /**
     * <p>times.</p>
     *
     * @param multiplier a float.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money times(float multiplier)
    {
        return new Money(dollars.multiply(new BigDecimal(multiplier)));
    }

    /**
     * <p>minus.</p>
     *
     * @param subtrahend a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money minus(Money subtrahend)
    {
        return new Money(dollars.subtract(subtrahend.dollars));
    }

    /**
     * <p>plus.</p>
     *
     * @param addend a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money plus(Money addend)
    {
        return new Money(dollars.add(addend.dollars));
    }

    /**
     * <p>greaterThan.</p>
     *
     * @param money a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a boolean.
     */
    public boolean greaterThan(Money money)
    {
        return this.dollars.compareTo( money.dollars ) >= 0;
    }

    /**
     * <p>strictlyGreaterThan.</p>
     *
     * @param money a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a boolean.
     */
    public boolean strictlyGreaterThan(Money money)
    {
        return this.dollars.compareTo( money.dollars ) > 0;
    }

    /**
     * <p>lowerThan.</p>
     *
     * @param money a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a boolean.
     */
    public boolean lowerThan(Money money)
    {
        return dollars.compareTo( money.dollars ) <= 0;
    }

    /**
     * <p>strictlyLowerThan.</p>
     *
     * @param money a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a boolean.
     */
    public boolean strictlyLowerThan(Money money)
    {
        return dollars.compareTo( money.dollars ) < 0;
    }
}
