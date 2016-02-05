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

import com.greenpepper.util.CollectionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Option class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Option
{
    private String name;
    private String shortOpt;
    private String longOpt;
    private String description;
    private String arg;
    private Object value;
    private Converter converter = CommandLine.converterFor( String.class );
    private Stub stub = new DoNothing();

    /**
     * <p>Constructor for Option.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public Option( String name )
    {
        this.name = name;
    }

    /**
     * <p>isValid.</p>
     *
     * @return a boolean.
     */
    public boolean isValid()
    {
        return shortOpt != null || longOpt != null;
    }

    /**
     * <p>consume.</p>
     *
     * @param args a {@link java.util.List} object.
     * @throws com.greenpepper.util.cli.WrongOptionUsageException if any.
     */
    public void consume( List<String> args ) throws WrongOptionUsageException
    {
        if (wantsArg() && (args.isEmpty())) throw new WrongOptionUsageException( this );
        value = wantsArg() ? convert( CollectionUtil.shift( args ) ) : true;
    }

    private Object convert( String value ) throws WrongOptionUsageException
    {
        try
        {
            return converter.convert( value );
        }
        catch (Exception e)
        {
            throw new WrongOptionUsageException( this, e );
        }
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>getShortForm.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getShortForm()
    {
        return shortOpt;
    }

    /**
     * <p>setShortForm.</p>
     *
     * @param shortOpt a {@link java.lang.String} object.
     */
    public void setShortForm( String shortOpt )
    {
        this.shortOpt = shortOpt;
    }

    /**
     * <p>getLongForm.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLongForm()
    {
        return longOpt;
    }

    /**
     * <p>setLongForm.</p>
     *
     * @param longOpt a {@link java.lang.String} object.
     */
    public void setLongForm( String longOpt )
    {
        this.longOpt = longOpt;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>arg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getArg()
    {
        return arg;
    }

    /**
     * <p>Setter for the field <code>arg</code>.</p>
     *
     * @param arg a {@link java.lang.String} object.
     */
    public void setArg( String arg )
    {
        this.arg = arg;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a {@link java.lang.Object} object.
     */
    public void setValue( Object value )
    {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>converter</code>.</p>
     *
     * @return a {@link com.greenpepper.util.cli.Converter} object.
     */
    public Converter getConverter()
    {
        return converter;
    }

    /**
     * <p>Setter for the field <code>converter</code>.</p>
     *
     * @param converter a {@link com.greenpepper.util.cli.Converter} object.
     */
    public void setConverter( Converter converter )
    {
        this.converter = converter;
    }

    /**
     * <p>wantsArg.</p>
     *
     * @return a boolean.
     */
    public boolean wantsArg()
    {
        return arg != null;
    }

    void describeTo( StringBuilder sb )
    {
        if (shortOpt != null) sb.append( shortOpt );
        else sb.append( "  " );

        if (longOpt != null)
        {
            if (shortOpt != null) sb.append( ", " );
            else sb.append( "  " );
            sb.append( longOpt );
        }

        if (wantsArg()) sb.append( " " ).append( arg );

        if (description != null)
        {
            justify( sb );
            sb.append( description );
        }
    }

    private void justify( StringBuilder sb )
    {
        int padding = 30 - sb.length();
        if (padding <= 0)
        {
            sb.append( "\n" );
            padding = 30;
        }
        char[] filler = new char[padding];
        Arrays.fill( filler, ' ' );
        sb.append( filler );
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        describeTo( sb );
        return sb.toString();
    }

    /**
     * <p>Setter for the field <code>stub</code>.</p>
     *
     * @param stub a {@link com.greenpepper.util.cli.Option.Stub} object.
     */
    public void setStub( Stub stub )
    {
        this.stub = stub;
    }

    boolean wasGiven()
    {
        return value != null;
    }

    /**
     * <p>call.</p>
     */
    public void call()
    {
        stub.call( this );
    }

    public interface Stub
    {

        void call( Option option );
    }

    public static class DoNothing implements Stub
    {
        public void call( Option option )
        {
        }
    }
}

