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

package com.greenpepper.util.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>CommandLine class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CommandLine
{
    private static final Map<Class, Converter> converters = new HashMap<Class, Converter>();

    static
    {
        registerConverter( String.class, new StringConverter() );
        registerConverter( Integer.class, new IntegerConverter() );
        registerConverter( int.class, new IntegerConverter() );
        registerConverter( Locale.class, new LocaleConverter() );
        registerConverter( File.class, new FileConverter() );
        registerConverter( Class.class, new ClassConverter() );
    }

    /**
     * <p>registerConverter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param converter a {@link com.greenpepper.util.cli.Converter} object.
     */
    public static void registerConverter( Class type, Converter converter )
    {
        converters.put( type, converter );
    }

    /**
     * <p>converterFor.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @return a {@link com.greenpepper.util.cli.Converter} object.
     */
    public static Converter converterFor( Class type )
    {
        if (!converters.containsKey( type ))
            throw new IllegalArgumentException( "Don't know type: " + type );

        return converters.get( type );
    }

    private final List<String> arguments;
    private final List<Option> options;

    private CommandLineParser parser = new PosixParser();
    private String banner = "";

    /**
     * <p>Constructor for CommandLine.</p>
     */
    public CommandLine()
    {
        arguments = new ArrayList<String>();
        options = new ArrayList<Option>();
    }

    /**
     * <p>Setter for the field <code>banner</code>.</p>
     *
     * @param banner a {@link java.lang.String} object.
     */
    public void setBanner( String banner )
    {
        this.banner = banner;
    }

    /**
     * <p>setMode.</p>
     *
     * @param parser a {@link com.greenpepper.util.cli.CommandLineParser} object.
     */
    public void setMode( CommandLineParser parser )
    {
        this.parser = parser;
    }

    /**
     * <p>buildOption.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param options a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public OptionBuilder buildOption( String name, String... options )
    {
        OptionBuilder option = OptionBuilder.create( name );
        parser.build( option, options );
        return option;
    }

    /**
     * <p>defineOption.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.OptionBuilder} object.
     */
    public void defineOption( OptionBuilder option )
    {
        define( option.make() );
    }

    /**
     * <p>define.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.Option} object.
     */
    public void define( Option option )
    {
        options.add( option );
    }

    /**
     * <p>parse.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return an array of {@link java.lang.String} objects.
     * @throws com.greenpepper.util.cli.ParseException if any.
     */
    public String[] parse( String... args ) throws ParseException
    {
        parser.parse( this, args );
        callStubs();
        return getArguments();
    }

    /**
     * <p>addArgument.</p>
     *
     * @param arg a {@link java.lang.String} object.
     */
    public void addArgument( String arg )
    {
        arguments.add( arg );
    }

    /**
     * <p>getArgumentCount.</p>
     *
     * @return a int.
     */
    public int getArgumentCount()
    {
        return arguments.size();
    }

    /**
     * <p>Getter for the field <code>arguments</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getArguments()
    {
        return arguments.toArray( new String[arguments.size()] );
    }

    /**
     * <p>getArgument.</p>
     *
     * @param index a int.
     * @return a {@link java.lang.String} object.
     */
    public String getArgument( int index )
    {
        if (index >= arguments.size()) return null;
        return arguments.get( index );
    }

    /**
     * <p>hasOptionValue.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean hasOptionValue( String name )
    {
        return getOptionValues().containsKey( name );
    }

    /**
     * <p>getOptionValues.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, ? extends Object> getOptionValues()
    {
        Map<String, Object> opts = new HashMap<String, Object>();
        for (Option opt : options)
        {
            if (opt.wasGiven()) opts.put( opt.getName(), opt.getValue() );
        }

        return opts;
    }

    /**
     * <p>getOptionValue.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getOptionValue( String name )
    {
        if (!hasOptionValue( name )) throw new IllegalArgumentException( "Don't know option: " + name );
        return getOptionValues().get( name );
    }

    /**
     * <p>usage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String usage()
    {
        StringBuilder sb = new StringBuilder( "Usage: " ).append( banner );
        if (options.isEmpty()) return sb.toString();

        sb.append( "\n\n" ).append( "Options:" );
        for (Option option : options)
        {
            sb.append( "\n" ).append( option );
        }

        return sb.toString();
    }

    /**
     * <p>getOptionWithShortForm.</p>
     *
     * @param shortForm a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.Option} object.
     * @throws com.greenpepper.util.cli.InvalidOptionException if any.
     */
    public Option getOptionWithShortForm( String shortForm ) throws InvalidOptionException
    {
        for (Option option : options)
        {
            if (shortForm.equals( option.getShortForm() )) return option;
        }

        throw new InvalidOptionException( shortForm );
    }

    /**
     * <p>getOptionWithLongForm.</p>
     *
     * @param longForm a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.util.cli.Option} object.
     * @throws com.greenpepper.util.cli.InvalidOptionException if any.
     */
    public Option getOptionWithLongForm( String longForm ) throws InvalidOptionException
    {
        for (Option option : options)
        {
            if (longForm.equals( option.getLongForm() )) return option;
        }

        throw new InvalidOptionException( longForm );
    }

    private void callStubs()
    {
        for (Option option : options)
        {
            if (option.wasGiven()) option.call();
        }
    }
}
