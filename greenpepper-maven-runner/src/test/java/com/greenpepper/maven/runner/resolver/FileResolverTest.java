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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.greenpepper.maven.runner.resolver.CombinedResolver.MavenGAV;
import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;

public class FileResolverTest
{
    private FileResolver resolver = new FileResolver();

    @Test
    public void cannotResolveDirectory()
    {
        assertFalse( resolver.canResolve( System.getProperty( "user.home" ) ) );
    }

    @Test
    public void cannotResolveANonExistingFile()
    {
        assertFalse( resolver.canResolve( "unknow-pom.xml" ) );
    }

    @Test
    public void canResolve() throws ResolverException
    {
        assertTrue( resolver.canResolve( "src/test/resources/pom.xml" ) );
        MavenGAV mavenGAV = new MavenGAV("greenpepper-open","greenpepper-samples","2.9");
        assertEquals( mavenGAV, resolver.resolve( "src/test/resources/pom.xml" ) );
    }
}
