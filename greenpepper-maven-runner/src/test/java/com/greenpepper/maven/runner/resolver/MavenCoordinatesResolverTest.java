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
 */
package com.greenpepper.maven.runner.resolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MavenCoordinatesResolverTest
{
    private CoordinatesResolver resolver = new CoordinatesResolver();

    @Test
    public void canResolve()
    {
        assertFalse( resolver.canResolve( null ) );
        assertFalse( resolver.canResolve( "" ) );
        assertFalse( resolver.canResolve( "not containing semi colon" ) );
        assertFalse( resolver.canResolve( "C:/Faking Window Filename" ) );
        assertTrue( resolver.canResolve( "groupId:artifiactId:version" ) );
        assertTrue( resolver.canResolve( "groupId:artifiactId:classifier:version" ) );
        assertTrue( resolver.canResolve( "groupId:artifiactId:packaging:classifier:version" ) );
    }
}
