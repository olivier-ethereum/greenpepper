/*
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package com.greenpepper.runner.ant;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.StringDecoder;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.LoaderUtils;
import org.apache.xmlrpc.XmlRpcClient;

import com.greenpepper.GreenPepper;
import com.greenpepper.GreenPepperCore;
import com.greenpepper.util.DuckType;

/**
 * <p>AntTaskRunner class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class AntTaskRunner extends Task
{
	private boolean failOnError = false;
	private boolean suite = false;
	private String input;
	private File output;
	private String outputType;
	private SectionElement section;
	private SystemUnderDevelopmentElement systemDevelopment;
	private RepositoryElement repository;
	private boolean stopOnFirstFailure = false;
	private String locale;
	private String selector;
	private boolean debug = false;
    private Path classpath;
    private Path runtimeClasspath;

	/**
	 * <p>Constructor for AntTaskRunner.</p>
	 */
	public AntTaskRunner()
	{
        super();
	}

    /**
     * <p>isFailOnError.</p>
     *
     * @return a boolean.
     */
    public boolean isFailOnError()
    {
        return failOnError;
    }

    /**
     * <p>Setter for the field <code>failOnError</code>.</p>
     *
     * @param failOnError a boolean.
     */
    public void setFailOnError(boolean failOnError)
	{
		this.failOnError = failOnError;
	}

    /**
     * <p>isSuite.</p>
     *
     * @return a boolean.
     */
    public boolean isSuite()
    {
        return suite;
    }

    /**
     * <p>Setter for the field <code>suite</code>.</p>
     *
     * @param suite a boolean.
     */
    public void setSuite(boolean suite)
	{
		this.suite = suite;
	}

    /**
     * <p>Getter for the field <code>input</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInput()
    {
        return input;
    }

    /**
     * <p>Setter for the field <code>input</code>.</p>
     *
     * @param input a {@link java.lang.String} object.
     */
    public void setInput(String input)
	{
		this.input = input;
	}

    /**
     * <p>Getter for the field <code>output</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getOutput()
    {
        return output;
    }

    /**
     * <p>Setter for the field <code>output</code>.</p>
     *
     * @param output a {@link java.io.File} object.
     */
    public void setOutput(File output)
	{
		this.output = output;
	}

    /**
     * <p>Getter for the field <code>outputType</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutputType()
    {
        return outputType;
    }

    /**
     * <p>Setter for the field <code>outputType</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setOutputType(String type) {
		this.outputType = type.equalsIgnoreCase("xml") ? "xml": null;
	}

    /**
     * <p>Getter for the field <code>section</code>.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.SectionElement} object.
     */
    public SectionElement getSection()
    {
        return section;
    }

    /**
     * <p>Setter for the field <code>section</code>.</p>
     *
     * @param section a {@link com.greenpepper.runner.ant.SectionElement} object.
     */
    public void setSection(SectionElement section)
    {
        this.section = section;
    }

    /**
     * <p>createSection.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.SectionElement} object.
     */
    public SectionElement createSection()
	{
		if (getSection() != null)
        {
			throw new IllegalArgumentException("Section already defined!");
		}

		section = new SectionElement();
		return section;
	}

    /**
     * <p>Getter for the field <code>systemDevelopment</code>.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.SystemUnderDevelopmentElement} object.
     */
    public SystemUnderDevelopmentElement getSystemDevelopment()
    {
        return systemDevelopment;
    }

    /**
     * <p>createSystemUnderDevelopment.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.SystemUnderDevelopmentElement} object.
     */
    public SystemUnderDevelopmentElement createSystemUnderDevelopment()
    {
		if (systemDevelopment != null)
        {
			throw new IllegalArgumentException("SystemUnderDevelopment already defined!");
		}

		systemDevelopment = new SystemUnderDevelopmentElement();
		return systemDevelopment;
	}

    /**
     * <p>Getter for the field <code>repository</code>.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.RepositoryElement} object.
     */
    public RepositoryElement getRepository()
    {
        return repository;
    }

    /**
     * <p>createRepository.</p>
     *
     * @return a {@link com.greenpepper.runner.ant.RepositoryElement} object.
     */
    public RepositoryElement createRepository()
    {
		if (repository != null)
        {
			throw new IllegalArgumentException("Repository already defined!");
		}

		repository = new RepositoryElement();
		return repository;
	}

	/**
	 * <p>isStopOnFirstFailure.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isStopOnFirstFailure()
	{
		return stopOnFirstFailure;
	}

	/**
	 * <p>Setter for the field <code>stopOnFirstFailure</code>.</p>
	 *
	 * @param stopOnFirstFailure a boolean.
	 */
	public void setStopOnFirstFailure(boolean stopOnFirstFailure)
	{
		this.stopOnFirstFailure = stopOnFirstFailure;
	}

	/**
	 * <p>Getter for the field <code>locale</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLocale()
	{
		return locale;
	}

	/**
	 * <p>Setter for the field <code>locale</code>.</p>
	 *
	 * @param locale a {@link java.lang.String} object.
	 */
	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	/**
	 * <p>Getter for the field <code>selector</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSelector()
	{
		return selector;
	}

	/**
	 * <p>Setter for the field <code>selector</code>.</p>
	 *
	 * @param selector a {@link java.lang.String} object.
	 */
	public void setSelector(String selector)
	{
		this.selector = selector;
	}

	/**
	 * <p>isDebug.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDebug()
	{
		return debug;
	}

	/**
	 * <p>Setter for the field <code>debug</code>.</p>
	 *
	 * @param debug a boolean.
	 */
	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

    /**
     * <p>createClasspath.</p>
     *
     * @return a {@link org.apache.tools.ant.types.Path} object.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath;
    }

	/**
	 * <p>execute.</p>
	 *
	 * @throws org.apache.tools.ant.BuildException if any.
	 */
	public void execute()
			throws BuildException
	{
		try
		{
			verboseHeaderToLog();

			List<String> args = buildCommandLine();

			verboseCommandLineToLog(args);

			doRun(args);

		}
        catch (BuildException ex) {
            throw ex;
        }
		catch (Exception ex)
		{
			throw new BuildException("AntTaskRunner", ex);
		}
	}

	private void verboseHeaderToLog()
	{
		log("\n-------------------------------------------------------", Project.MSG_INFO);
		log(" G R E E N  P E P P E R  S P E C I F I C A T I O N S   ", Project.MSG_INFO);
		log("-------------------------------------------------------\n", Project.MSG_INFO);
	}

	private List<String> buildCommandLine()
	{
		String currentDirectory = getCurrentDirectory();

		log(String.format("CurrentDir \"%s\"", currentDirectory), Project.MSG_VERBOSE);
		log(String.format("BaseDir \"%s\"", getProject().getBaseDir()), Project.MSG_VERBOSE);

        if (createRuntimeClasspath() != null && createRuntimeClasspath().size() > 0) {
            log("Classpath :", Project.MSG_VERBOSE);
            for (String path : createRuntimeClasspath().list()) {
                log(String.format("\t%s", path), Project.MSG_VERBOSE);
            }
        }

		List<String> args = new ArrayList<String>();

		if (getLocale() != null)
		{
			args.add("--locale");
			args.add(getLocale());
			log(String.format("Locale \"%s\"", getLocale()), Project.MSG_VERBOSE);
		}

		if (getSelector() != null)
		{
			args.add("--selector");
			args.add(getSelector());
			log(String.format("Selector \"%s\"", getSelector()), Project.MSG_VERBOSE);
		}

		if (getSystemDevelopment() != null)
        {
			args.add("-f");
			args.add(getSystemDevelopment().toArgument(this));
		}

		if (getRepository() != null)
        {
			args.add("-r");
			args.add(getRepository().toArgument(this));
		}

		String outputLocation = currentDirectory;

		if (getOutput() != null)
        {
			outputLocation = getOutput().getAbsolutePath();
		}

		log(String.format("Output \"%s\"", outputLocation), Project.MSG_VERBOSE);
		args.add("-o");
		args.add(outputLocation);

		if (isSuite())
		{
			log(String.format("Suite ? \"%s\"", isSuite()), Project.MSG_VERBOSE);
			args.add("-s");
		}

		if (getSection() != null)
		{
			args.add("-t");
			args.add(getSection().toArgument(this));
		}

		if (getOutputType() != null)
		{
			args.add("--xml");
		}

		if (isStopOnFirstFailure())
		{
			args.add("--stop");
		}

		if (isDebug())
		{
			args.add("--debug");
		}

		log(String.format("Input \"%s\"", getInput()), Project.MSG_VERBOSE);

		args.add(getInput());
		return args;
	}

	private String getCurrentDirectory()
	{
		return System.getProperty("user.dir");
	}

	private void verboseCommandLineToLog(List<String> args)
	{
		for(String arg : args)
		{
			log(arg, Project.MSG_VERBOSE);
		}
	}

	private void doRun(List<String> args)
			throws Exception
	{
        AntClassLoader classLoader = new AntClassLoader( null, createRuntimeClasspath(), false );

        try
        {
            classLoader.setThreadContextLoader();

            log(classLoader.toString(), Project.MSG_DEBUG);

            AntTaskRunnerMonitor recorderMonitor = new AntTaskRunnerMonitor();
            Object runnerInstance = createRunner( classLoader , new AntTaskRunnerLogger(this), recorderMonitor );
            CommandLineRunnerMirror runner = DuckType.implement( CommandLineRunnerMirror.class, runnerInstance );

            String[] arguments = new String[args.size()];
            args.toArray(arguments);

            runner.run(arguments);

            log(String.format("Results: %s for %s specification(s)",
                recorderMonitor.getStatistics().toString(),
                recorderMonitor.getLocationCount()), Project.MSG_INFO);

            checkResults(recorderMonitor.hasException(), "Some greenpepper tests did not run");
            checkResults(recorderMonitor.hasTestFailures(), "There were greenpepper tests failures");
        }
        finally
        {
            classLoader.resetThreadContextLoader();
        }
    }

    private Path createRuntimeClasspath()
    {
        if (runtimeClasspath == null)
        {
            runtimeClasspath = new Path( getProject() );
            runtimeClasspath.append( createClasspath() );

            appendGreenPepperCoreClasspath( runtimeClasspath );
            appendXmlRpcClasspath( runtimeClasspath );
        }

        return runtimeClasspath;
    }

    private void appendGreenPepperCoreClasspath(Path path)
    {
        appendClasspath( path, AntTaskRunner.class, String.format( "greenpepper-extensions-java-%s.jar", GreenPepperCore.VERSION ) );
        appendClasspath( path, GreenPepper.class, String.format( "greenpepper-core-%s.jar", GreenPepperCore.VERSION ) );
    }

    private void appendXmlRpcClasspath(Path path)
    {
        appendClasspath( path, XmlRpcClient.class, "xmlrpc-2.0.1.jar" );
        appendClasspath( path, StringDecoder.class, "commons-codec-1.3.jar" );
    }

    private void appendClasspath(Path path, Class c, String expectedJar)
    {

        File location = LoaderUtils.getClassSource( c );

        if (location != null)
        {
            path.createPath().setLocation( location );

            log( String.format( "Implicitly adding '%s' to CLASSPATH", location ), Project.MSG_VERBOSE );
        }
        else
        {
            log( String.format( "Couldn't found classpath for '%s'", c ), Project.MSG_WARN );
            log( String.format( "Make sure you have '%s' in the <classpath/> of taskdef of GreenPepper", expectedJar ) );
        }
    }

    private Object createRunner(ClassLoader classLoader, CommandLineRunnerMirror.CommandLineLogger logger,
                                CommandLineRunnerMirror.CommandLineMonitor monitor)
    {
        try
        {
            Class runnerClass = classLoader.loadClass( "com.greenpepper.runner.ant.CommandLineRunnerMirrorImpl" );

            if (runnerClass.getClassLoader() != classLoader) {
                throw new BuildException("Overdelegating loader", getLocation());
            }

            Constructor runnerCtr = runnerClass.getConstructor( Object.class, Object.class );

            return runnerCtr.newInstance( logger, monitor );
        }
        catch (Exception ex)
        {
            throw new BuildException( ex, getLocation() );
        }
    }

    private void checkResults(boolean state, String message)
	{
		if (state)
		{
			if (isFailOnError())
			{
				throw new BuildException(message);
			}
			else
			{
				log(String.format("\nWarning : %s", message), Project.MSG_WARN);
			}
		}
	}
}
