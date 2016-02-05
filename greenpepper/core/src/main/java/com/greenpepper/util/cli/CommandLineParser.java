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
 * <p>CommandLineParser interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface CommandLineParser
{
    /**
     * <p>build.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.OptionBuilder} object.
     * @param options a {@link java.lang.String} object.
     */
    void build( OptionBuilder option, String... options );

    /**
     * <p>parse.</p>
     *
     * @param cli a {@link com.greenpepper.util.cli.CommandLine} object.
     * @param args an array of {@link java.lang.String} objects.
     * @throws com.greenpepper.util.cli.InvalidOptionException if any.
     * @throws com.greenpepper.util.cli.WrongOptionUsageException if any.
     */
    void parse( CommandLine cli, String[] args ) throws InvalidOptionException, WrongOptionUsageException;
}
