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

/**
 * <p>WrongOptionUsageException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class WrongOptionUsageException extends ParseException
{
    private final Option opt;

    /**
     * <p>Constructor for WrongOptionUsageException.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.Option} object.
     */
    public WrongOptionUsageException( Option option )
    {
        this( option, null );
    }

    /**
     * <p>Constructor for WrongOptionUsageException.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.Option} object.
     * @param cause a {@link java.lang.Throwable} object.
     */
    public WrongOptionUsageException( Option option, Throwable cause )
    {
        super( cause );
        this.opt = option;
    }

    /**
     * <p>Getter for the field <code>opt</code>.</p>
     *
     * @return a {@link com.greenpepper.util.cli.Option} object.
     */
    public Option getOpt()
    {
        return opt;
    }

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage()
    {
        return "Wrong option usage: " + opt.toString();
    }
}
