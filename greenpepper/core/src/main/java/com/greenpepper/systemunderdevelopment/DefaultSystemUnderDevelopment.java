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

package com.greenpepper.systemunderdevelopment;

import com.greenpepper.document.Document;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Type;
import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.reflect.TypeNotFoundException;

/**
 * <p>DefaultSystemUnderDevelopment class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DefaultSystemUnderDevelopment implements SystemUnderDevelopment
{
    private final TypeLoader<?> typeLoader;

    /**
     * <p>Constructor for DefaultSystemUnderDevelopment.</p>
     */
    public DefaultSystemUnderDevelopment()
    {
        this( Thread.currentThread().getContextClassLoader() );
    }

    /**
     * <p>Constructor for DefaultSystemUnderDevelopment.</p>
     *
     * @param classLoader a {@link java.lang.ClassLoader} object.
     */
    public DefaultSystemUnderDevelopment( java.lang.ClassLoader classLoader )
    {
        this( new FixtureTypeLoaderChain( classLoader ) );
    }

    /**
     * <p>Constructor for DefaultSystemUnderDevelopment.</p>
     *
     * @param typeLoader a {@link com.greenpepper.reflect.TypeLoader} object.
     */
    public DefaultSystemUnderDevelopment( TypeLoader<?> typeLoader)
    {
        this.typeLoader = typeLoader;
    }

    /**
     * Creates a new instance of a fixture class using a set of parameters.
     *
     * @param name   the name of the class to instantiate
     * @param params the parameters (constructor arguments)
     * @return a new instance of the fixtureClass with fields populated using
     *         Constructor
     * @throws Exception if any.
     * @throws java.lang.Throwable if any.
     */
    public Fixture getFixture( String name, String... params ) throws Throwable
    {
        Type<?> type = loadType( name );
        Object target = type.newInstanceUsingCoercion( params );
        return new DefaultFixture( target );
    }

    /**
     * <p>loadType.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Type} object.
     */
    protected Type<?> loadType( String name )
    {
        Type<?> type = typeLoader.loadType( name );
        if (type == null) throw new TypeNotFoundException( name , this.getClass());
        return type;
    }

    /** {@inheritDoc} */
    public void addImport( String packageName )
    {
        typeLoader.searchPackage(packageName);
    }

    /** {@inheritDoc} */
    public void onEndDocument(Document document)
    {
    }

    /** {@inheritDoc} */
    public void onStartDocument(Document document)
    {
    }
}
