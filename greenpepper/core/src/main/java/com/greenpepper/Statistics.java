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
package com.greenpepper;

import java.io.Serializable;

/**
 * <p>Statistics class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Statistics implements Serializable
{
	private static final long serialVersionUID = -1L;

    private int rightCount;
    private int wrongCount;
    private int exceptionCount;

    private int ignoredCount;

    /**
     * <p>Constructor for Statistics.</p>
     */
    public Statistics()
    {
        this( 0, 0, 0, 0 );
    }

    /**
     * <p>Constructor for Statistics.</p>
     *
     * @param right a int.
     * @param wrong a int.
     * @param exception a int.
     * @param ignored a int.
     */
    public Statistics( int right, int wrong, int exception, int ignored )
    {
        this.rightCount = right;
        this.wrongCount = wrong;
        this.exceptionCount = exception;
        this.ignoredCount = ignored;
    }

    /**
     * <p>exceptionCount.</p>
     *
     * @return a int.
     */
    public int exceptionCount()
    {
        return exceptionCount;
    }

    /**
     * <p>wrongCount.</p>
     *
     * @return a int.
     */
    public int wrongCount()
    {
        return wrongCount;
    }

    /**
     * <p>ignoredCount.</p>
     *
     * @return a int.
     */
    public int ignoredCount()
    {
        return ignoredCount;
    }

    /**
     * <p>rightCount.</p>
     *
     * @return a int.
     */
    public int rightCount()
    {
        return rightCount;
    }

    /**
     * <p>tally.</p>
     *
     * @param other a {@link com.greenpepper.Statistics} object.
     */
    public void tally( Statistics other )
    {
        rightCount += other.rightCount();
        wrongCount += other.wrongCount();
        ignoredCount += other.ignoredCount();
        exceptionCount += other.exceptionCount();
    }

    /**
     * <p>indicatesFailure.</p>
     *
     * @return a boolean.
     */
    public boolean indicatesFailure()
    {
        return wrongCount > 0 || exceptionCount > 0;
    }

    /**
     * <p>totalCount.</p>
     *
     * @return a int.
     */
    public int totalCount()
    {
        return rightCount() + wrongCount() + exceptionCount() + ignoredCount();
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString()
    {
        return String.format( "%d tests: %d right, %d wrong, %d ignored, %d exception(s)",
            totalCount(), rightCount(), wrongCount(), ignoredCount(), exceptionCount() );
    }

    /**
     * <p>right.</p>
     */
    public void right()
    {
        rightCount++;
    }

    /**
     * <p>wrong.</p>
     */
    public void wrong()
    {
        wrongCount++;
    }

    /**
     * <p>exception.</p>
     */
    public void exception()
    {
        exceptionCount++;
    }

    /**
     * <p>ignored.</p>
     */
    public void ignored()
    {
        ignoredCount++;
    }
    
    /**
     * <p>hasFailed.</p>
     *
     * @return a boolean.
     */
    public boolean hasFailed() 
    {
        return wrongCount() > 0 || exceptionCount() > 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Statistics)) return false;

        Statistics that = (Statistics) o;

        return exceptionCount == that.exceptionCount
               && ignoredCount == that.ignoredCount
               && rightCount == that.rightCount
               && wrongCount == that.wrongCount;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        int result;
        result = rightCount;
        result = 31 * result + wrongCount;
        result = 31 * result + exceptionCount;
        result = 31 * result + ignoredCount;
        return result;
    }
}
