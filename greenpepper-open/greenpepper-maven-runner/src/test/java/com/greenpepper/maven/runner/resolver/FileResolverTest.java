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

import java.io.File;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.junit.Test;

public class FileResolverTest
{
    private FileResolver resolver;

    @Test
    public void cannotResolveDirectory()
    {
        resolver = new FileResolver(null, null);
        assertFalse( resolver.canResolve( System.getProperty( "user.home" ) ) );
    }

    @Test
    public void cannotResolveANonExistingFile()
    {
        resolver = new FileResolver(null, null);
        assertFalse( resolver.canResolve( "unknow-pom.xml" ) );
    }

    @Test
    public void canResolve() throws ProjectBuildingException {
        MavenEmbedder mavenEmbedder = createMock(MavenEmbedder.class);
        MavenProject mavenProject = new MavenProject();
        expect(mavenEmbedder.readProject(anyObject(File.class))).andReturn(mavenProject);
        resolver = new FileResolver(mavenEmbedder, null);
        replay(mavenEmbedder);

        assertTrue( resolver.canResolve( "src/test/resources/pom.xml" ) );
        File expected = new File( "src/test/resources/pom.xml" );
        assertEquals( expected, resolver.resolve( "src/test/resources/pom.xml" ) );
        verify(mavenEmbedder);
    }
}
