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

import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.StringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>PosixParser class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class PosixParser implements CommandLineParser
{
    /**
     * <p>build.</p>
     *
     * @param option a {@link com.greenpepper.util.cli.OptionBuilder} object.
     * @param options a {@link java.lang.String} object.
     */
    public void build( OptionBuilder option, String... options )
    {
        for (String opt : options)
        {
            Matcher matcher = Pattern.compile( "(--[^\\s]*)(\\s(.+))?" ).matcher( opt );
            if (matcher.matches())
            {
                option.withLongForm( matcher.group( 1 ) );
                if (!StringUtil.isBlank( matcher.group( 3 ) )) option.wantsArgument( matcher.group( 3 ) );
                continue;
            }

            matcher = Pattern.compile( "(-.)(\\s+(.+))?" ).matcher( opt );
            if (matcher.matches())
            {
                option.withShortForm( matcher.group( 1 ) );
                if (!StringUtil.isBlank( matcher.group( 3 ) )) option.wantsArgument( matcher.group( 3 ) );
                continue;
            }

            option.withDescription( opt );
        }
    }

    /**
     * <p>parse.</p>
     *
     * @param cli a {@link com.greenpepper.util.cli.CommandLine} object.
     * @param args an array of {@link java.lang.String} objects.
     * @throws com.greenpepper.util.cli.InvalidOptionException if any.
     * @throws com.greenpepper.util.cli.WrongOptionUsageException if any.
     */
    public void parse( CommandLine cli, String[] args ) throws InvalidOptionException, WrongOptionUsageException
    {
        List<String> tokens = CollectionUtil.toList( args );
        while (!tokens.isEmpty())
        {
            String token = CollectionUtil.shift( tokens );
            if (Pattern.compile( "(--.+)" ).matcher( token ).lookingAt())
            {
                cli.getOptionWithLongForm( token ).consume( tokens );
            }
            else if (Pattern.compile( "(-.)" ).matcher( token ).lookingAt())
            {
                cli.getOptionWithShortForm( token ).consume( tokens );
            }
            else
            {
                cli.addArgument( token );
            }
        }
    }
}
