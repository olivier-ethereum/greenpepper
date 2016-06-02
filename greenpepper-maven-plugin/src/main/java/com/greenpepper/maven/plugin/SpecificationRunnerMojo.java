/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.FileSystemRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.greenpepper.Statistics;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.runner.CompositeSpecificationRunnerMonitor;
import com.greenpepper.runner.RecorderMonitor;
import com.greenpepper.util.IOUtil;
import org.apache.maven.project.MavenProject;

/**
 * <p>SpecificationRunnerMojo class.</p>
 *
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 * @description Runs GreenPepper specifications
 * @author oaouattara
 * @version $Id: $Id
 */
public class SpecificationRunnerMojo extends AbstractMojo {

    /**
     * Set this to 'true' to bypass greenpepper tests entirely.
     * Its use is NOT RECOMMENDED, but quite convenient on occasion.
     * @parameter expression="${maven.greenpepper.test.skip}" default-value="false"
     */
    private boolean skip;

    /**
     * Project fixture classpath.
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    List<String> classpathElements;

    /**
     * The directory where compiled fixture classes go.
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    File fixtureOutputDirectory;

    /**
     * The SystemUnderDevelopment class to use
     * @parameter default-value=
     *            "com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment"
     * @required
     */
    String systemUnderDevelopment;

    /**
     * @parameter expression="${project.build.directory}/greenpepper-reports"
     * @required
     */
    File reportsDirectory;

    /**
     * @parameter expression="${maven.greenpepper.reports.type}" default-value="html"
     */
    String reportsType;

    /**
     * @parameter property="greenpepper.repositories"
     * @required
     */
    ArrayList<Repository> repositories;

    /**
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    List<Artifact> pluginDependencies;

    /**
     * Set this to 'true' to stop the execution on a failure.
     * @parameter expression="${maven.greenpepper.test.stop}" default-value="false"
     */
    boolean stopOnFirstFailure;

    /**
     * Set the locale for the execution.
     * @parameter expression="${maven.greenpepper.locale}"
     */
    String locale;

    /**
     * Set the Selector class.
     * @parameter expression="${maven.greenpepper.selector}"
     *            default-value="com.greenpepper.document.GreenPepperInterpreterSelector"
     */
    String selector;

    /**
     * Set the Debug mode.
     * @parameter expression="${maven.greenpepper.debug}" default-value="false"
     */
    boolean debug;

    /**
     * Set this to true to ignore a failure during testing.
     * Its use is NOT RECOMMENDED, but quite convenient on occasion.
     * @parameter expression="${maven.greenpepper.test.failure.ignore}" default-value="false"
     */
    boolean testFailureIgnore;

    /**
     * Set this to a Specification name to run only this test.
     * The test is searched inside the default repository.
     *
     * @parameter property="gp.test"
     */
    String testSpecification;

    /**
     * Set this to a Specification name to run only this test.
     * The test is searched inside the default repository.
     * @parameter expression="${gp.testOutput}"
     */
    String testSpecificationOutput;

    /**
     * Set this to a Repository name defined in the pom.xml.
     * This option is only used in case <code>-Dgp.test</code> is used.
     *
     * @parameter property="gp.repo"
     */
    String selectedRepository;

    /**
     * @component
     */
    protected MavenProject project;

    Statistics statistics;

    boolean testFailed;
    boolean exceptionOccured;

    /**
     * <p>Constructor for SpecificationRunnerMojo.</p>
     */
    public SpecificationRunnerMojo() {
        this.statistics = new Statistics();
        this.repositories = new ArrayList<Repository>();
    }

    /**
     * <p>addRepository.</p>
     *
     * @param repository a {@link com.greenpepper.maven.plugin.Repository} object.
     */
    public void addRepository(Repository repository) {
        repositories.add(repository);
    }

    /**
     * <p>execute.</p>
     *
     * @throws org.apache.maven.plugin.MojoExecutionException if any.
     * @throws org.apache.maven.plugin.MojoFailureException if any.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Not executing specifications.");
        } else {
            prepareReportsDir();
            printBanner();
            try {
                runAllTests();
            } finally {
                printFooter();
            }
            checkTestsResults();
        }
    }

    private void checkTestsResults() throws MojoExecutionException, MojoFailureException {
        if (exceptionOccured)
            notifyExceptionsOccured();
        if (testFailed)
            notifyTestsFailed();
    }

    private void notifyExceptionsOccured() throws MojoExecutionException {
        if (testFailureIgnore) {
            getLog().error("Some greenpepper tests did not run\n");
        } else {
            throw new MojoExecutionException("Some greenpepper tests did not run");
        }
    }

    private void notifyTestsFailed() throws MojoFailureException {
        if (testFailureIgnore) {
            getLog().error("There were greenpepper tests failures\n");
        } else {
            throw new MojoFailureException("There were greenpepper tests failures");
        }
    }

    private void printBanner() {
        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println(" G R E E N  P E P P E R  S P E C I F I C A T I O N S ");
        System.out.println("-----------------------------------------------------");
        System.out.println();
    }

    private void runAllTests() throws MojoExecutionException, MojoFailureException {
        if (StringUtils.isNotEmpty(testSpecification)) {
            // Locate default repository
            Repository defaultRepository = null;
            if (repositories.size() == 1) {
                defaultRepository = repositories.get(0);
            } else {
                boolean repositorySelected = StringUtils.isNotEmpty(selectedRepository);
                if (repositorySelected) {
                    for (Repository repository : repositories) {
                        if (StringUtils.equalsIgnoreCase(selectedRepository, repository.getName())) {
                            defaultRepository = repository;
                            break;
                        }
                    }
                    if (defaultRepository == null) {
                        throw new MojoExecutionException(String.format("Repository '%s' not found in the list of repository.", selectedRepository));
                    }
                } else {
                    for (Repository repository : repositories) {
                        if (repository.isDefault()) {
                            defaultRepository = repository;
                            break;
                        }
                    }
                }
            }
            if (defaultRepository == null) {
                throw new MojoExecutionException("A default repository should be set when using '-Dgp.test='. Use '-Dgp.repo=' or specify it in the pom.xml");
            }

            // Run the test
            runSingleTest(defaultRepository, testSpecification);

        } else {
            for (Repository repository : repositories) {
                if (shouldStop()) {
                    break;
                }

                runAllIn(repository);
            }
        }
    }

    private void runAllIn(Repository repository) throws MojoExecutionException, MojoFailureException {
        runTestsIn(repository);
        runSuitesIn(repository);
    }

    private void runSuitesIn(Repository repository) throws MojoExecutionException, MojoFailureException {
        for (String suite : repository.getSuites()) {
            if (shouldStop()) {
                break;
            }

            String repoCmdOption = repository.getType() + (repository.getRoot() != null ? ";" + repository.getRoot() : "");
            String outputDir = new File(reportsDirectory, repository.getName()).getAbsolutePath();

            List<String> args = args("-f", systemUnderDevelopment, "-s", "-r", repoCmdOption, "-o", outputDir, suite);

            run(args);
        }
    }

    private void runTestsIn(Repository repository) throws MojoExecutionException, MojoFailureException {
        for (String test : repository.getTests()) {
            if (shouldStop()) {
                break;
            }

            runSingleTest(repository, test);
        }
    }

    private void runSingleTest(Repository repository, String test) throws MojoExecutionException, MojoFailureException {
        String repoCmdOption;
        boolean managingFileSystem = false;
        try {
            DocumentRepository documentRepository = repository.getDocumentRepository();
            managingFileSystem = documentRepository instanceof FileSystemRepository;
        } catch (Exception e) {
            throw new MojoFailureException("Unable to get the document repository", e);
        }
        if (managingFileSystem) {
            File projectBasedir = project.getBasedir();
            repoCmdOption = repository.getType() + ";";
            if (repository.getRoot() != null){
                File relativeRoot = new File(repository.getRoot());
                File absoluteDir;
                if (relativeRoot.getAbsoluteFile().compareTo(relativeRoot) == 0) {
                    absoluteDir = relativeRoot;
                } else {
                    absoluteDir = new File(projectBasedir,repository.getRoot());
                }
                repoCmdOption += absoluteDir.getAbsolutePath();
            } else {
                repoCmdOption += projectBasedir.getAbsolutePath();
            }
        } else {
            repoCmdOption = repository.getType() + (repository.getRoot() != null ? ";" + repository.getRoot() : "");
        }

        String outputDir = new File(reportsDirectory, repository.getName()).getAbsolutePath();

        List<String> args = args("-f", systemUnderDevelopment, "-r", repoCmdOption, "-o", outputDir, test);

        run(args);
    }

    private void run(List<String> args) throws MojoExecutionException, MojoFailureException {
        DynamicCoreInvoker runner = new DynamicCoreInvoker(createClassLoader());
        CompositeSpecificationRunnerMonitor monitors = new CompositeSpecificationRunnerMonitor();
        monitors.add(new LoggerMonitor(getLog()));
        RecorderMonitor recorder = new RecorderMonitor();
        monitors.add(recorder);
        runner.setMonitor(monitors);

        try {
            runner.run(toArray(args));
        } catch (Exception e) {
            exceptionOccured = true;
            throw new MojoExecutionException("Unable to run tests", e);
        }

        exceptionOccured |= recorder.hasException();
        testFailed |= recorder.hasTestFailures();
        statistics.tally(recorder.getStatistics());
    }

    private void printFooter() {
        System.out.println();
        System.out.println("Results:");
        System.out.println(statistics);
        System.out.println();
    }

    private ClassLoader createClassLoader() throws MojoExecutionException {
        List<URL> urls = new ArrayList<URL>();
        if (classpathElements != null) {
            for (String classpathElement : classpathElements) {
                urls.add(toURL(new File(classpathElement)));
            }
        }

        urls.add(toURL(fixtureOutputDirectory));

        if (!containsGreenPepperCore(urls)) {
            urls.add(getDependencyURL("greenpepper-core"));
        }

        urls.add(getDependencyURL("greenpepper-extensions-java"));
        urls.add(getDependencyURL("xmlrpc"));
        urls.add(getDependencyURL("commons-codec"));
        urls.add(getDependencyURL("slf4j-api"));
        urls.add(getDependencyURL("jcl-over-slf4j"));

        URL[] classpath = urls.toArray(new URL[urls.size()]);

        return new URLClassLoader(classpath, ClassLoader.getSystemClassLoader());
    }

    private URL getDependencyURL(String name) throws MojoExecutionException {
        if (pluginDependencies != null && !pluginDependencies.isEmpty()) {
            for (Artifact artifact : pluginDependencies) {
                if (artifact.getArtifactId().equals(name) && artifact.getType().equals("jar"))
                    return toURL(artifact.getFile());
            }
        }
        throw new MojoExecutionException("Dependency not found: " + name);
    }

    private URL toURL(File f) throws MojoExecutionException {
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Invalid dependency: " + f.getAbsolutePath(), e);
        }
    }

    private boolean containsGreenPepperCore(List<URL> urls) {

        for (URL url : urls) {

            if (url.getFile().indexOf("greenpepper-core") != -1 && url.getFile().endsWith(".jar")) {
                return true;
            }
        }

        return false;
    }

    private void prepareReportsDir() throws MojoExecutionException {
        if (StringUtils.isAnyEmpty(testSpecification, testSpecificationOutput)) {
            try {
                IOUtil.createDirectoryTree(reportsDirectory);
            } catch (IOException e) {
                throw new MojoExecutionException("Could not create reports directory: " + reportsDirectory.getAbsolutePath());
            }
        }

    }

    private boolean shouldStop() {
        return stopOnFirstFailure && statistics.indicatesFailure();
    }

    private List<String> args(String... args) {
        List<String> arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(args));
        
        if (StringUtils.isNoneEmpty(testSpecification, testSpecificationOutput)){
            arguments.add(testSpecificationOutput);
        }

        if (!StringUtils.isEmpty(locale)) {
            arguments.add("--locale");
            arguments.add(locale);
        }

        if (!StringUtils.isEmpty(selector) && !GreenPepperInterpreterSelector.class.getName().equals(selector)) {
            arguments.add("--selector");
            arguments.add(selector);
        }

        if ("xml".equalsIgnoreCase(reportsType)) {
            arguments.add("--xml");
        }

        if (stopOnFirstFailure) {
            arguments.add("--stop");
        }

        if (debug) {
            arguments.add("--debug");
        }

        return arguments;
    }

    private String[] toArray(List<String> args) {
        String[] arguments = new String[args.size()];
        args.toArray(arguments);
        return arguments;
    }
}
