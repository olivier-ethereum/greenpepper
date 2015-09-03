/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.cli.MavenCli;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class CommandLineRunner {

    public static final String CWD = System.getProperty("user.dir");
    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);
    private ArgumentsParser argumentsParser;

    private PrintStream out;
    private PrintStream err;

    public CommandLineRunner() {
        this(System.out, System.err);
    }

    public CommandLineRunner(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
        argumentsParser = new ArgumentsParser(out);
    }

    public void run(String... args) throws ParseException, ResolverException, IOException, MavenExecutionException, DuplicateRealmException {
        CommandLine commandLine = argumentsParser.parse(args);

        if (commandLine != null) {
            setUpLogLevel(commandLine);
            Map<String, String> context = argumentsParser.createTemplatingContext(commandLine);
            File temporaryPom = createLaunchingPOM(context);

            if (System.getProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY) == null) {
                System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, CWD);
            }
            ClassWorld classWorld = new ClassWorld("greenpepper-maven-runner", MavenCli.class.getClassLoader());
            classWorld.newRealm("greenpepper-maven-runner.threadLoader", Thread.currentThread().getContextClassLoader());
            MavenCli mavenCli = new MavenCli(classWorld);
            String[] mavenArgs = new String[] {"-fn", "greenpepper:greenpepper-maven-plugin:run", "-f", temporaryPom.getAbsolutePath()};
            logger.debug("Launching Maven with args {}", Arrays.toString(mavenArgs));
            int mavenCLIResult = mavenCli.doMain(mavenArgs, CWD, out, err);
            if (mavenCLIResult != 0) {
                throw new MavenExecutionException("Failed to Run the maven command.", temporaryPom.getAbsoluteFile());
            }

        } else {
            logger.debug("The command line contains a --help option. Doing nothing.");
        }

    }

    private void setUpLogLevel(CommandLine commandLine) {
        if (commandLine.hasOption("vv")) {
            LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory(); 
            context.getLogger("com.greenpepper").setLevel(Level.TRACE);;
        }
        if (commandLine.hasOption("vvv")) {
            String loggername = commandLine.getOptionValue("vvv");
            LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory(); 
            context.getLogger(loggername).setLevel(Level.TRACE);;
        }
    }

    private File createLaunchingPOM(Map<String, String> context) throws IOException {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("resource.loader", "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.setProperty("classpath.resource.loader.cache", false);
        ve.init();
        Template template = ve.getTemplate("/greenpepper/resolver-pom.xml");
        VelocityContext velocityContext = new VelocityContext(context);
        File temporaryPom = File.createTempFile("resolver-pom-", ".xml", new File(CWD));
        temporaryPom.deleteOnExit();
        FileWriter writer = new FileWriter(temporaryPom.getAbsolutePath());
        template.merge(velocityContext, writer);
        writer.flush();
        return temporaryPom;
    }

}