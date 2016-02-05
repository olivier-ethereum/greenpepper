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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>CompositeSpecificationRunnerMonitor class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CompositeSpecificationRunnerMonitor implements SpecificationRunnerMonitor
{
    private final List<SpecificationRunnerMonitor> monitors;

    /**
     * <p>Constructor for CompositeSpecificationRunnerMonitor.</p>
     *
     * @param monitors a {@link com.greenpepper.runner.SpecificationRunnerMonitor} object.
     */
    public CompositeSpecificationRunnerMonitor( SpecificationRunnerMonitor... monitors )
    {
        this( Arrays.asList( monitors ) );
    }

    /**
     * <p>Constructor for CompositeSpecificationRunnerMonitor.</p>
     *
     * @param monitors a {@link java.util.List} object.
     */
    public CompositeSpecificationRunnerMonitor( List<SpecificationRunnerMonitor> monitors )
    {
        this.monitors = new ArrayList<SpecificationRunnerMonitor>();
        this.monitors.addAll( monitors );
    }

    /**
     * <p>Constructor for CompositeSpecificationRunnerMonitor.</p>
     */
    public CompositeSpecificationRunnerMonitor()
    {
        this( Collections.<SpecificationRunnerMonitor>emptyList() );
    }

    /**
     * <p>add.</p>
     *
     * @param monitor a {@link com.greenpepper.runner.SpecificationRunnerMonitor} object.
     */
    public void add( SpecificationRunnerMonitor monitor )
    {
        monitors.add( monitor );
    }


    /** {@inheritDoc} */
    public void testRunning( String location )
    {
        for (SpecificationRunnerMonitor monitor : monitors)
        {
            monitor.testRunning( location );
        }
    }

    /** {@inheritDoc} */
    public void testDone( int rightCount, int wrongCount, int exceptionCount, int ignoreCount )
    {
        for (SpecificationRunnerMonitor monitor : monitors)
        {
            monitor.testDone( rightCount, wrongCount, exceptionCount, ignoreCount );
        }
    }

    /** {@inheritDoc} */
    public void exceptionOccured( Throwable t )
    {
        for (SpecificationRunnerMonitor monitor : monitors)
        {
            monitor.exceptionOccured( t );
        }
    }
}
