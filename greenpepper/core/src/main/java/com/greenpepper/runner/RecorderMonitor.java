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

package com.greenpepper.runner;

import com.greenpepper.Statistics;

/**
 * <p>RecorderMonitor class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class RecorderMonitor implements SpecificationRunnerMonitor
{
    private Statistics statistics = new Statistics();
    private Throwable exception;
	private int locationCount = 0;

	/** {@inheritDoc} */
	public void testRunning( String location )
    {
		locationCount++;
	}

    /** {@inheritDoc} */
    public void testDone( int rightCount, int wrongCount, int exceptionCount, int ignoreCount )
    {
        Statistics stats = new Statistics( rightCount, wrongCount, exceptionCount, ignoreCount );
        statistics.tally( stats );
    }

    /** {@inheritDoc} */
    public void exceptionOccured( Throwable t )
    {
        exception = t;
    }

    /**
     * <p>Getter for the field <code>statistics</code>.</p>
     *
     * @return a {@link com.greenpepper.Statistics} object.
     */
    public Statistics getStatistics()
    {
        return statistics;
    }

    /**
     * <p>Getter for the field <code>exception</code>.</p>
     *
     * @return a {@link java.lang.Throwable} object.
     */
    public Throwable getException()
    {
        return exception;
    }

    /**
     * <p>hasException.</p>
     *
     * @return a boolean.
     */
    public boolean hasException()
    {
        return exception != null;
    }

    /**
     * <p>hasTestFailures.</p>
     *
     * @return a boolean.
     */
    public boolean hasTestFailures()
    {
        return statistics.indicatesFailure();
    }

	/**
	 * <p>Getter for the field <code>locationCount</code>.</p>
	 *
	 * @return a int.
	 */
	public int getLocationCount()
	{
		return locationCount;
	}
}
