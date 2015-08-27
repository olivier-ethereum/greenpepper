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
package com.greenpepper.maven.runner;

import java.io.PrintStream;

import org.apache.maven.embedder.AbstractMavenEmbedderLogger;

public class Logger extends AbstractMavenEmbedderLogger
{
    private final PrintStream out;

    public Logger(PrintStream out)
    {
        this.out = out;
    }

	public final PrintStream getOut() {
		return out;
	}

	public final void debug(String message, Throwable throwable)
    {
        if (isDebugEnabled())
        {
            out.print( "[ maven embedder DEBUG] " );
            out.println( message );

            if (null != throwable)
            {
                throwable.printStackTrace( out );
            }
        }
    }

    public final void info(String message, Throwable throwable)
    {
        if (isInfoEnabled())
        {
            out.print( "[ maven embedder INFO] " );
            out.println( message );

            if (null != throwable)
            {
                throwable.printStackTrace( out );
            }
        }
    }

    public final void warn(String message, Throwable throwable)
    {
        if (isWarnEnabled())
        {
            out.print( "[ maven embedder WARNING] " );
            out.println( message );

            if (null != throwable)
            {
                throwable.printStackTrace( out );
            }
        }
    }

    public final void error(String message, Throwable throwable)
    {
        if (isErrorEnabled())
        {
            out.print( "[ maven embedder ERROR] " );
            out.println( message );

            if (null != throwable)
            {
                throwable.printStackTrace( out );
            }
        }
    }

    public final void fatalError(String message, Throwable throwable)
    {
        if (isFatalErrorEnabled())
        {
            out.print( "[ maven embedder FATAL ERROR] " );
            out.println( message );

            if (null != throwable)
            {
                throwable.printStackTrace( out );
            }
        }
    }
}