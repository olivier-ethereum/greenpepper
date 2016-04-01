
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.maven.runner;

import static com.greenpepper.util.CollectionUtil.toArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.cli.ConsoleDownloadMonitor;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.embedder.MavenEmbedderLogger;
import org.apache.maven.embedder.PlexusLoggerAdapter;
import org.apache.maven.model.Plugin;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.project.interpolation.StringSearchModelInterpolator;
import org.apache.maven.project.path.DefaultPathTranslator;
import org.apache.maven.settings.MavenSettingsBuilder;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.greenpepper.maven.runner.resolver.ProjectFileResolver;
import com.greenpepper.maven.runner.util.ReflectionUtils;
import com.greenpepper.util.cli.ArgumentMissingException;
public class CommandLineRunner {

    private static final String PLUGIN_KEY = "com.github.strator-dev.greenpepper:greenpepper-maven-plugin";
    private boolean isDebug = false;
    private String projectDependencyDescriptor;
    /** Constant <code>CWD="System.getProperty(user.dir)"</code> */
    public static final String CWD = System.getProperty("user.dir");
    @SuppressWarnings("serial")
    private ArrayList<String> scopes = new ArrayList<String>() {

        {
            add("runtime");
            add("compile");
        }
    };

    private Logger logger;
    private MavenEmbedder embedder;

    private MavenProject project;
    private List<Artifact> artifacts = new ArrayList<Artifact>();
    private ArgumentsParser argumentsParser;
    private ProjectFileResolver resolvers;

    /**
     * <p>Constructor for CommandLineRunner.</p>
     */
    public CommandLineRunner() {
        this(System.out);
    }

    /**
     * <p>Constructor for CommandLineRunner.</p>
     *
     * @param out a {@link java.io.PrintStream} object.
     */
    public CommandLineRunner(PrintStream out) {
        this.logger = new Logger(out);
        this.argumentsParser = new ArgumentsParser(out);
    }

    /**
     * <p>run.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @throws java.lang.Exception if any.
     */
    public void run(String... args) throws Exception {
        List<String> parameters = parseCommandLine(args);
        if (!parameters.isEmpty()) {
            runClassicRunner(parameters);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> parseCommandLine(String[] args) throws ArgumentMissingException, IOException, ParseException {
        List<String> parameters = new ArrayList<String>();
        CommandLine commandLine = argumentsParser.parse(args);
        if (commandLine != null) {
            Option[] options = commandLine.getOptions();
            for (Option option : options) {
                if ("v".equals(option.getOpt())) {
                    isDebug = true;
                    parameters.add("--debug");
                } else if ("p".equals(option.getOpt())) {
                    projectDependencyDescriptor = option.getValue();
                } else if ("m".equals(option.getOpt())) {
                    usingScopes(option.getValue());
                } else if ("o".equals(option.getOpt())) {
                    parameters.add("-" + option.getOpt());
                    parameters.add(option.getValue());
                } else if ("r".equals(option.getOpt())) {
                    parameters.add("-" + option.getOpt());
                    parameters.add(option.getValue());
                } else {
                    parameters.add("--" + option.getLongOpt());
                    if (option.hasArg()) {
                        parameters.add(option.getValue());
                    }
                }
            }
            parameters.addAll(commandLine.getArgList());  
        } 
        return parameters;            
    }

    private void usingScopes(String values) {
        StringTokenizer st = new StringTokenizer(values, ";", false);

        while (st.hasMoreTokens()) {
            String scope = st.nextToken();

            if (!scopes.contains(scope)) {
                scopes.add(scope);
            }
        }
    }

    private void runClassicRunner(List<String> args) throws Exception {
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

        initMavenEmbedder(originalClassLoader);

        resolveProject();

        ProjectFileResolver.MavenGAV mavenGAV = resolvers.getMavenGAV();
        if (StringUtils.isNotEmpty(mavenGAV.getClassifier())) {
            Artifact artifactWithClassifier = embedder.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(), project.getVersion(), mavenGAV.getPackaging(),
                    mavenGAV.getClassifier());
            resolve(artifactWithClassifier,artifacts);
        } else {
            resolveScopedArtifacts();

            resolveMavenPluginArtifact();

            resolveProjectArtifact();
        }

        URL[] classpaths = buildRuntimeClasspaths();

        URLClassLoader urlClassLoader = new URLClassLoader(classpaths, originalClassLoader);

        Thread.currentThread().setContextClassLoader(urlClassLoader);

        ReflectionUtils.setDebugEnabled(urlClassLoader, isDebug);
        ReflectionUtils.setSystemOutputs(urlClassLoader, logger.getOut(), System.err);

        Class<?> mainClass = urlClassLoader.loadClass("com.greenpepper.runner.Main");

        logger.debug("Invoking: com.greenpepper.runner.Main " + StringUtils.join(args, ' '));
        ReflectionUtils.invokeMain(mainClass, args);

        Thread.currentThread().setContextClassLoader(originalClassLoader);


    }

    private void resolveProject() throws Exception {
        File projectFile = resolveProjectFile();

        if (!projectFile.exists()) {
            throw new FileNotFoundException(String.format("Project dependency descriptor file not found: %s", projectFile.getAbsolutePath()));
        }

        project = readProjectWithDependencies(projectFile);
        
        interpolateProject();
        
    }

    private void interpolateProject() throws InitializationException, Exception, ModelInterpolationException {
        StringSearchModelInterpolator interpolator = new StringSearchModelInterpolator(new DefaultPathTranslator());
        interpolator.enableLogging(new PlexusLoggerAdapter(embedder.getLogger()));
        interpolator.initialize();
        ProjectBuilderConfiguration config =  new DefaultProjectBuilderConfiguration();
        config.setLocalRepository(getLocalRepository());
        config.setGlobalProfileManager(getProfileManager());
        interpolator.interpolate(project.getModel(), project.getBasedir(), config, logger.isDebugEnabled());
    }

    private MavenProject readProjectWithDependencies(File projectFile) throws Exception {
        return embedder.readProjectWithDependencies(projectFile, new ConsoleDownloadMonitor());
    }

    private File resolveProjectFile() throws Exception {
        resolvers = new ProjectFileResolver(embedder, logger);
        return resolvers.resolve(projectDependencyDescriptor);
    }

    @SuppressWarnings("unchecked")
    private void resolveScopedArtifacts() {
        artifacts = new ArrayList<Artifact>(project.getArtifacts().size());

        for (Iterator<Artifact> i = project.getArtifacts().iterator(); i.hasNext();) {
            Artifact a = i.next();

            if (scopes.contains(a.getScope())) {
                artifacts.add(a);
            }
        }
    }

    private void resolveProjectArtifact() {
        try {
            Artifact selfArtifact = project.getArtifact();

            if (selfArtifact.getFile() == null) {
                resolve(selfArtifact, artifacts);
            }
        } catch (Exception ex) {
            logger.error("Resolving project artifact", ex);
        }
    }

    private void resolveMavenPluginArtifact() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Artifact> plugins = project.getPluginArtifactMap();

            if (plugins.get(PLUGIN_KEY) != null) {
                Artifact fixtureArtifact = embedder.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging(),
                        "fixtures");

                resolve(fixtureArtifact, artifacts);
            }
        } catch (Exception ex) {
            logger.error("Resolving maven-plugin artifact", ex);
        }
    }

    private void resolve(Artifact artifact, List<Artifact> runtimeArtifacts) throws Exception {
        embedder.resolve(artifact, project.getRemoteArtifactRepositories(), embedder.getLocalRepository());

        if (artifact.getFile() != null) {
            runtimeArtifacts.add(artifact);
        }
    }

    private void initMavenEmbedder(ClassLoader classLoader) throws MavenEmbedderException {
        embedder = new MavenEmbedder();
        embedder.setClassLoader(classLoader);

        if (isDebug) {
            logger.setThreshold(MavenEmbedderLogger.LEVEL_DEBUG);
        }

        embedder.setLogger(logger);
        embedder.setAlignWithUserInstallation(true);

        embedder.start();

        initAuthenticationSettings();
    }

    private void initAuthenticationSettings() throws MavenEmbedderException {
        /**
         * GP-775 : MavenEmbedder does not read servers/mirrors configuration properly. Mimic the
         * resolveParameter
         * found in the DefaultMaven class to allow credentials to be taken into account.
         */
        try {
            WagonManager wagonManager = getWagonManager();
            Settings settings = getSettings();

            for (Object item : settings.getServers()) {
                Server server = (Server) item;

                wagonManager.addAuthenticationInfo(server.getId(), server.getUsername(), server.getPassword(), server.getPrivateKey(), server.getPassphrase());

                wagonManager.addPermissionInfo(server.getId(), server.getFilePermissions(), server.getDirectoryPermissions());

                if (server.getConfiguration() != null) {
                    wagonManager.addConfiguration(server.getId(), (Xpp3Dom) server.getConfiguration());
                }
            }

            for (Object item : settings.getMirrors()) {
                Mirror mirror = (Mirror) item;

                wagonManager.addMirror(mirror.getId(), mirror.getMirrorOf(), mirror.getUrl());
            }
        } catch (Exception ex) {
            throw new MavenEmbedderException("Failed to handle authentication settings", ex);
        }
    }

    private URL[] buildRuntimeClasspaths() throws IOException {
        final Map<String, File> actualClasspaths = getActualClasspaths();

        List<URL> classpaths = new ArrayList<URL>();

        for (Artifact artifact : artifacts) {
            File artifactFile = actualClasspaths.get(artifact.getFile().getName());

            if (artifactFile == null || !artifactFile.equals(artifact.getFile())) {
                logger.debug(String.format("Artifact: %s (%s)", artifact, artifact.getFile()));
                classpaths.add(artifact.getFile().toURI().toURL());
            }
        }
        
        // Add project folders, if any.
        String buildOutputDirectory = project.getBuild().getOutputDirectory();
        classpaths.add(new File(buildOutputDirectory).toURI().toURL());
        
        @SuppressWarnings("unchecked")
        List<Plugin> buildPlugins = project.getBuildPlugins();
        for (Plugin plugin : buildPlugins) {
            if (StringUtils.equals(PLUGIN_KEY,plugin.getKey())) {
                Xpp3Dom configuration = (Xpp3Dom)plugin.getConfiguration();
                if (configuration != null) {
                    Xpp3Dom fixtureOutputDirectory = configuration.getChild("fixtureOutputDirectory");
                    String relativeDirectory = fixtureOutputDirectory.getValue();
                    if (!StringUtils.isEmpty(relativeDirectory)){                            
                        File directory = FileUtils.getFile(project.getBasedir(), relativeDirectory);
                        classpaths.add(directory.toURI().toURL());
                    }
                }
                break;
            }
        }

        logger.debug("Classpath used: " + classpaths.toString(), null);
        return toArray(classpaths, URL.class);
    }

    private Map<String, File> getActualClasspaths() {
        Map<String, File> classpaths = new HashMap<String, File>();

        String[] javaClassPath = System.getProperty("java.class.path").split(System.getProperty("path.separator"));

        for (String classPath : javaClassPath) {
            File file = new File(classPath);
            classpaths.put(file.getName(), file);
        }

        return classpaths;
    }

    private WagonManager getWagonManager() throws Exception {
        return (WagonManager) ReflectionUtils.getDeclaredFieldValue(embedder, "wagonManager");
    }

    private Settings getSettings() throws Exception {
        MavenSettingsBuilder mavenSettingsBuilder = getMavenSettingsBuilder();
        return mavenSettingsBuilder.buildSettings();
    }

    private MavenSettingsBuilder getMavenSettingsBuilder() throws Exception {
        return (MavenSettingsBuilder) ReflectionUtils.getDeclaredFieldValue(embedder, "settingsBuilder");
    }
    
    private ArtifactRepository getLocalRepository() throws Exception {
        return (ArtifactRepository) ReflectionUtils.getDeclaredFieldValue(embedder, "localRepository");
    }
    
    private ProfileManager getProfileManager() throws Exception {
        return (ProfileManager) ReflectionUtils.getDeclaredFieldValue(embedder, "profileManager");
    }
}
