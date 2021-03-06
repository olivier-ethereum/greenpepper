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

package com.greenpepper.seeds;

import com.greenpepper.reflect.Type;
import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.systemunderdevelopment.FixtureTypeLoaderChain;

public class FindingFixtureFixture
{
    private final com.greenpepper.reflect.TypeLoader typeLoader;
    private static final String YES = "yes";
    private static final String NO = "no";

    public String fixtureName;
    public String packageToImport;
    public boolean tryImport = false;

    public FindingFixtureFixture()
    {
        typeLoader = new FixtureTypeLoaderChain( Thread.currentThread().getContextClassLoader() );
        typeLoader.searchPackage("com.greenpepper.seeds");
    }

    public String found()
    {
        try
        {
            return typeFound(typeLoader);
        }
        catch (NoClassDefFoundError e)
        {
        	return NO;
        }
    }

    public String foundNoImport()
    {
        packageToImport = null;
        return foundWithTheSpecifiedPackage();
    }

    public String foundWithTheSpecifiedPackage()
    {
        TypeLoader loader;
        try
        {
            loader = new FixtureTypeLoaderChain( Thread.currentThread().getContextClassLoader() );
            if (packageToImport != null) loader.searchPackage(packageToImport);
            return typeFound(loader);
        }
        catch (NoClassDefFoundError e) 
        {
            return NO;
        }
    }

    private String typeFound(TypeLoader loader)
    {
        Type type = loader.loadType(fixtureName);
        return (type != null ? YES : NO);
    }
}
