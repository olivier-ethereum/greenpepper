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

package com.greenpepper.util.cli;

/**
 * <p>OptionBuilder class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class OptionBuilder
{
    /**
     * <p>create.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public static OptionBuilder create( String name )
    {
        return new OptionBuilder( name );
    }

    private final Option option;

    /**
     * <p>Constructor for OptionBuilder.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public OptionBuilder( String name )
    {
        option = new Option( name );
    }

    /**
     * <p>wantsArgument.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder wantsArgument( String value )
    {
        if (option.wantsArg()) throw new IllegalArgumentException( "Argument pattern given twice" );
        option.setArg( value );
        return this;
    }

    /**
     * <p>withShortForm.</p>
     *
     * @param shortForm a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder withShortForm( String shortForm )
    {
        if (option.getShortForm() != null) throw new IllegalArgumentException( "Short form given twice" );
        option.setShortForm( shortForm );
        return this;
    }

    /**
     * <p>withDescription.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder withDescription( String text )
    {
        if (option.getDescription() != null) throw new IllegalArgumentException( "Description given twice" );
        option.setDescription( text );
        return this;
    }

    /**
     * <p>withLongForm.</p>
     *
     * @param longForm a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder withLongForm( String longForm )
    {
        if (option.getLongForm() != null) throw new IllegalArgumentException( "Long form given twice" );
        option.setLongForm( longForm );
        return this;
    }

    /**
     * <p>defaultingTo.</p>
     *
     * @param value a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder defaultingTo( Object value )
    {
        option.setValue( value );
        return this;
    }

    /**
     * <p>asType.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder asType( Class type )
    {
        return convertedWith( CommandLine.converterFor( type ) );
    }

    /**
     * <p>convertedWith.</p>
     *
     * @param converter a {@link com.greenpepper.util.cli.Converter} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder convertedWith( Converter converter )
    {
        option.setConverter( converter );
        return this;
    }

    /**
     * <p>make.</p>
     *
     * @return a {@link com.greenpepper.util.cli.Option} object.
     */
    public Option make()
    {
        if (!option.isValid()) throw new IllegalArgumentException( "no switch given" );
        return option;
    }

    /**
     * <p>whenPresent.</p>
     *
     * @param stub a {@link com.greenpepper.util.cli.Option.Stub} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder whenPresent( Option.Stub stub )
    {
        option.setStub( stub );
        return this;
    }
}
