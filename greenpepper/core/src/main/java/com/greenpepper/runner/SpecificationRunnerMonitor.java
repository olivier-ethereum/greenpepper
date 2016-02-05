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

/**
 * <p>SpecificationRunnerMonitor interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface SpecificationRunnerMonitor
{
    /**
     * <p>testRunning.</p>
     *
     * @param location a {@link java.lang.String} object.
     */
    void testRunning( String location );

    /**
     * <p>testDone.</p>
     *
     * @param rightCount a int.
     * @param wrongCount a int.
     * @param exceptionCount a int.
     * @param ignoreCount a int.
     */
    void testDone( int rightCount, int wrongCount, int exceptionCount, int ignoreCount );

    /**
     * <p>exceptionOccured.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     */
    void exceptionOccured( Throwable t );
}
