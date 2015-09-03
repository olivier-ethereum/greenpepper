/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.maven.runner;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.cli.ParseException;
import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.GreenPepperCore;
import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class CommandLineRunnerTest {

    private CommandLineRunner runner;

    @Before
    public void setUp() throws Exception {
        runner = new CommandLineRunner();
    }

    @Test
    public void commandLineWithoutPDDParameterMustFail() throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        runner.run();
        assertTrue(byteOut.toString().contains("usage:"));
    }

    @Test(expected = ResolverException.class)
    public void withANonExistingPomFile() throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        runner.run("--pdd", "unknown-pom.xml", "--debug", "src/test/resources/collection.html");
    }

    @Test
    public void usingUnreacheableMavenCoordinates() throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        runner.run("--pdd", "greenpepper:unknown:" + GreenPepperCore.VERSION, "src/test/resources/collection.html");
    }

    @Test
    public void usingLiveMavenCoordinatesWithFixedVersionButDependenciesIssues() throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.WARN);;
        runner = new CommandLineRunner(System.out, System.err);
        runner.run("--debug", "--pdd", "greenpepper:greenpepper-confluence-demo:pom:" + GreenPepperCore.VERSION, "src/test/resources/collection.html", "-o", "target/reports",
                "--xml", "-vv");
        String output = byteOut.toString();
        assertThat(output, containsString("Running src/test/resources/collection.html"));
        assertThat(output, containsString("6 tests: 0 right, 0 wrong, 0 ignored, 6 exception(s)"));
    }

    @Test
    public void usingLiveMavenCoordinatesWithFixedVersionAndArtifact() throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        runner.run("--debug", "--pdd", "greenpepper:greenpepper-confluence-demo:jar:fixtures:" + GreenPepperCore.VERSION, "src/test/resources/collection.html", "-o",
                "target/reports", "--xml");
        String output = byteOut.toString();
        assertThat(output, containsString("Running src/test/resources/collection.html"));
        assertThat(output, containsString("38 tests: 38 right, 0 wrong, 0 ignored, 0 exception(s)"));
    }

    @Test
    public void usingLiveMavenCoordinatesWithRangeVersion() throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        runner = new CommandLineRunner(new PrintStream(byteOut), System.err);
        runner.run("--debug", "--pdd", "greenpepper:greenpepper-confluence-demo:pom:fixtures:[2.8,)", "src/test/resources/bank.html", "-o", "target/reports", "--xml");
        String output = byteOut.toString();
        assertThat(output, containsString("Running src/test/resources/bank.html"));
        assertThat(output, containsString("17 tests: 17 right, 0 wrong, 0 ignored, 0 exception(s)"));
    }

}
