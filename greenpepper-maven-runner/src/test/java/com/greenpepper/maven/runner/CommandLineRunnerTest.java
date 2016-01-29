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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.embedder.MavenEmbedderException;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.GreenPepperCore;

public class CommandLineRunnerTest
{

    private CommandLineRunner runner;

    @Before
    public void setUp()
            throws Exception
    {
        runner = new CommandLineRunner();
    }

    @Test
    public void commandLineWithoutPDDParameterMustFail() throws Exception
    {
        try
        {
            runner.run( "" );

            fail();
        }
        catch (MissingOptionException ex)
        {
            // Expected
        }
    }

    @Test
    public void withANonExistingPomFile() throws Exception
    {
        try
        {
            runner.run( "--pdd", "unknown-pom.xml", "--debug", "test.html" );

            fail();
        }
        catch (MavenEmbedderException ex)
        {
            // Expected
            assertThat( ex.getMessage(), containsString( "Cannot resolve project dependency descriptor 'unknown-pom.xml'" ) );
        }
    }

    @Test
    public void usingUnreacheableMavenCoordinates() throws Exception
    {
        try
        {
            runner.run( "--pdd", "greenpepper:unknown:"+ GreenPepperCore.VERSION , "test.html");

            fail();
        }
        catch (AbstractArtifactResolutionException ex)
        {
            // Expected
        }
    }

    @Test
    public void usingLiveMavenCoordinatesWithFixedVersion() throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner( new PrintStream( byteOut ) );
        runner.run( "--debug", "--pdd", "com.github.strator-dev.greenpepper:greenpepper-confluence-demo:pom:" + GreenPepperCore.VERSION,
                "src/test/resources/collection.html", "-o", "target/reports", "--xml" );
        String output = byteOut.toString();
        assertThat( output, containsString( "Artifact: com.github.strator-dev.greenpepper:greenpepper-confluence-demo:jar:fixtures:" + GreenPepperCore.VERSION ) );
        assertThat( output, containsString( "Artifact: com.github.strator-dev.greenpepper:greenpepper-confluence-demo:jar:" + GreenPepperCore.VERSION ) );
        assertThat( output, containsString( "Running collection.html" ) );
        assertThat( output, containsString( "38 tests: 38 right, 0 wrong, 0 ignored, 0 exception(s)" ) );
    }

    @Test
    public void usingLiveMavenCoordinatesWithRangeVersion() throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner( new PrintStream( byteOut ) );
        runner.run( "--debug", "--pdd", "com.github.strator-dev.greenpepper:greenpepper-confluence-demo:pom:[3.2,)",
                "src/test/resources/bank.html", "-o", "target/reports", "--xml" );
        String output = byteOut.toString();
        assertThat( output, containsString( "Artifact: com.github.strator-dev.greenpepper:greenpepper-confluence-demo:jar:fixtures:" ) );
        assertThat( output, containsString( "Artifact: com.github.strator-dev.greenpepper:greenpepper-confluence-demo:jar:" ) );
        assertThat( output, containsString( "Running bank.html" ) );
        assertThat( output, containsString( "17 tests: 17 right, 0 wrong, 0 ignored, 0 exception(s)" ) );
    }

    @Test
    public void usingMavenCoordinatesWithClassifier() throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner( new PrintStream( byteOut ) );
        runner.run( "--debug", "--pdd", "com.github.strator-dev.greenpepper:greenpepper-client:jar:complete:" + GreenPepperCore.VERSION,
                "src/test/resources/collection.html", "-o", "target/reports", "--xml" );
        String output = byteOut.toString();
        assertThat( output, containsString( "Artifact: com.github.strator-dev.greenpepper:greenpepper-client:jar:complete:" ) );
        assertFalse(StringUtils.contains(output, "Artifact: com.github.strator-dev.greenpepper:greenpepper-client:jar:" + GreenPepperCore.VERSION));
    }

    @Test(expected = ArtifactNotFoundException.class)
    public void usingMavenCoordinatesWithClassifierNotExisting() throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner( new PrintStream( byteOut ) );
        runner.run( "--debug", "--pdd", "com.github.strator-dev.greenpepper:greenpepper-confluence-demo:jar:complete:" + GreenPepperCore.VERSION,
                "src/test/resources/collection.html", "-o", "target/reports", "--xml" );
    }

}
