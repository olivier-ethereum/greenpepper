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

package com.greenpepper.maven.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;

import com.greenpepper.maven.AbstractCompilerMojo;
import com.greenpepper.maven.plugin.utils.CompilationFailureException;

/**
 * <p>FixtureCompilerMojo class.</p>
 *
 * @goal compile
 * @phase pre-integration-test
 * @requiresDependencyResolution compile
 * @description Compiles fixture test sources
 * @author oaouattara
 * @version $Id: $Id
 */
public class FixtureCompilerMojo
    extends AbstractCompilerMojo
{
    /**
     * Set this to 'true' to bypass greenpepper fixture compilation process entirely.
     * Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.greenpepper.test.skip}" default-value="false"
     */
    private boolean skip;

    /**
     * Project fixture classpath.
     *
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * The directory where compiled fixture classes go.
     *
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    private File fixtureOutputDirectory;

    /**
     * A list of inclusion filters for the compiler.
     *
     * @parameter
     */
    private Set testIncludes = new HashSet();

    /**
     * A list of exclusion filters for the compiler.
     *
     * @parameter
     */
    private Set testExcludes = new HashSet();

	/** {@inheritDoc} */
	@Override
	public void execute()
        throws MojoExecutionException, CompilationFailureException
    {
        if ( skip )
        {
            getLog().info( "Not compiling fixture sources." );
        }
        else
        {
            getLog().info( "Compiling fixture sources at " + fixtureSourceDirectory.getAbsolutePath());
            getLog().info( "to directory " + fixtureOutputDirectory.getAbsolutePath());
            super.execute();
        }
    }

    /**
     * <p>getCompileSourceRoots.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    protected List getCompileSourceRoots()
    {
        List roots = new ArrayList();
        roots.add(fixtureSourceDirectory.getAbsolutePath());
        return roots;
    }

    /**
     * <p>Getter for the field <code>classpathElements</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List getClasspathElements()
    {
        return classpathElements;
    }

    /**
     * <p>getOutputDirectory.</p>
     *
     * @return a {@link java.io.File} object.
     */
    protected File getOutputDirectory()
    {
        return fixtureOutputDirectory;
    }

    /**
     * <p>getSourceInclusionScanner.</p>
     *
     * @param staleMillis a int.
     * @return a {@link org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner} object.
     */
    @SuppressWarnings("unchecked")
    protected SourceInclusionScanner getSourceInclusionScanner( int staleMillis )
    {
        SourceInclusionScanner scanner = null;

        if ( testIncludes.isEmpty() && testExcludes.isEmpty() )
        {
            scanner = new StaleSourceScanner( staleMillis );
        }
        else
        {
            if ( testIncludes.isEmpty() )
            {
                testIncludes.add( "**/*.java" );
            }
            scanner = new StaleSourceScanner( staleMillis, testIncludes, testExcludes );
        }

        return scanner;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    protected SourceInclusionScanner getSourceInclusionScanner( String inputFileEnding )
    {
        SourceInclusionScanner scanner = null;

        if ( testIncludes.isEmpty() && testExcludes.isEmpty() )
        {
            testIncludes = Collections.singleton( "**/*." + inputFileEnding );
            scanner = new SimpleSourceInclusionScanner( testIncludes, Collections.EMPTY_SET );
        }
        else
        {
            if ( testIncludes.isEmpty() )
            {
                testIncludes.add( "**/*." + inputFileEnding );
            }
            scanner = new SimpleSourceInclusionScanner( testIncludes, testExcludes );
        }

        return scanner;
    }}
