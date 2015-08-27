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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.embedder.MavenEmbedderLogger;

public class ProjectFileResolver
{
    private final List<Resolver> resolvers = new ArrayList<Resolver>( 2 );

    public ProjectFileResolver(MavenEmbedder embedder, MavenEmbedderLogger logger)
    {
        resolvers.add( new FileResolver() );
        resolvers.add( new CoordinatesResolver( embedder, logger ) );
    }

    public File resolve(String value) throws Exception
    {
        for (Resolver resolver : resolvers)
        {
            if (resolver.canResolve( value ))
            {
                return resolver.resolve( value );
            }
        }

        throw new MavenEmbedderException( String.format( "Cannot resolve project dependency descriptor '%s'", value ) );
    }

    public static interface Resolver
    {
        boolean canResolve(String value);

        File resolve(String value) throws Exception;
    }
}
