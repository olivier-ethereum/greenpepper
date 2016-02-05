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

package com.greenpepper.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.Statistics;

/**
 * <p>LoggerMonitor class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class LoggerMonitor implements SpecificationRunnerMonitor
{
    private Log log;

    /**
     * <p>Constructor for LoggerMonitor.</p>
     *
     * @param log a {@link org.apache.maven.plugin.logging.Log} object.
     */
    public LoggerMonitor(Log log)
    {
        this.log = log;
    }

    /** {@inheritDoc} */
    public void testRunning(String name)
    {
        log.info( "Running " + name );
    }

    /** {@inheritDoc} */
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount)
    {
        Statistics stats = new Statistics( rightCount, wrongCount, exceptionCount, ignoreCount );
        log.info( stats.toString() + (stats.hasFailed() ? " <<< FAILURE! " : "") );
    }

    /** {@inheritDoc} */
    public void exceptionOccured(Throwable t)
    {
        log.error( "Error running specification", t );
    }
}
