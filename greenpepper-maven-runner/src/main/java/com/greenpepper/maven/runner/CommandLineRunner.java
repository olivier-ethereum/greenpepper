/**
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
package com.greenpepper.maven.runner;

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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.cli.ConsoleDownloadMonitor;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.embedder.MavenEmbedderLogger;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.MavenSettingsBuilder;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.greenpepper.maven.runner.resolver.ProjectFileResolver;
import com.greenpepper.maven.runner.util.ReflectionUtils;
import static com.greenpepper.util.CollectionUtil.toArray;
import com.greenpepper.util.cli.ArgumentMissingException;

public class CommandLineRunner
{
    private boolean isDebug = false;
    private String projectDependencyDescriptor;
	private ArrayList<String> scopes = new ArrayList<String>() {{ add("runtime"); add("compile"); }};

    private Logger logger;
    private MavenEmbedder embedder;

    private MavenProject project;
    private List<Artifact> artifacts;

    public CommandLineRunner()
    {
        this( System.out );
    }

    public CommandLineRunner(PrintStream out)
    {
        this.logger = new Logger( out );
    }

    public void run(String... args)
            throws Exception
    {
        List<String> parameters = parseCommandLine( args );

        if (projectDependencyDescriptor == null)
        {
            throw new ArgumentMissingException( "pdd" );
        }

        runClassicRunner( parameters );
    }

    private List<String> parseCommandLine(String[] args)
            throws ArgumentMissingException, IOException
    {
        List<String> parameters = new ArrayList<String>();
        int index = 0;

        while (index < args.length)
        {
            if (isParameter( args[index], "debug" ))
            {
                isDebug = true;
                parameters.add( "--debug" );
            }
            else if (isParameter( args[index], "pdd" ))
            {
                projectDependencyDescriptor = args[index + 1];
                index++;
            }
			else if (isParameter(args[index], "scope"))
			{
				usingScopes(args[index+1]);
				index++;
			}
            else
            {
                parameters.add( args[index] );
            }

            index++;
        }

        return parameters;
    }

    private boolean isParameter(String parameter, String expected)
    {
        return parameter.startsWith( "-" ) && parameter.indexOf( expected ) != -1;
    }

	private void usingScopes(String values)
	{
		StringTokenizer st = new StringTokenizer(values, ";", false);

		while (st.hasMoreTokens())
		{
			String scope = st.nextToken();

			if (!scopes.contains(scope))
			{
				scopes.add(scope);
			}
		}
	}

    private void runClassicRunner(List<String> args)
            throws Exception
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        initMavenEmbedder( classLoader );

        resolveProject();

        resolveScopedArtifacts();

        resolveMavenPluginArtifact();

        resolveProjectArtifact();

        URL[] classpaths = buildRuntimeClasspaths();

        URLClassLoader urlClassLoader = new URLClassLoader( classpaths, classLoader );

        Thread.currentThread().setContextClassLoader( urlClassLoader );

        ReflectionUtils.setDebugEnabled( urlClassLoader, isDebug );
        ReflectionUtils.setSystemOutputs( urlClassLoader, logger.getOut(), System.err );

        Class mainClass = urlClassLoader.loadClass( "com.greenpepper.runner.Main" );

        ReflectionUtils.invokeMain( mainClass, args );
    }

    private void resolveProject() throws Exception
    {
        File projectFile = resolveProjectFile();

        if (!projectFile.exists())
        {
            throw new FileNotFoundException( String.format( "Project dependency descriptor file not found: %s",
                    projectFile.getAbsolutePath() ) );
        }

        project = readProjectWithDependencies( projectFile );
    }

    private MavenProject readProjectWithDependencies(File projectFile) throws Exception
    {
        return embedder.readProjectWithDependencies( projectFile, new ConsoleDownloadMonitor() );
    }

    private File resolveProjectFile() throws Exception
    {
        ProjectFileResolver resolvers = new ProjectFileResolver( embedder, logger );
        return resolvers.resolve( projectDependencyDescriptor );
    }

    @SuppressWarnings("unchecked")
    private void resolveScopedArtifacts()
    {
		artifacts = new ArrayList(project.getArtifacts().size());

		for (Iterator i = project.getArtifacts().iterator(); i.hasNext();)
		{
			Artifact a = (Artifact)i.next();

			if (scopes.contains(a.getScope()))
			{
				artifacts.add(a);
			}
		}
    }

    private void resolveProjectArtifact()
    {
        try
        {
            Artifact selfArtifact = project.getArtifact();

            if (selfArtifact.getFile() == null)
            {
                resolve( selfArtifact, artifacts);
            }
        }
        catch (Exception ex)
        {
            logger.error( "Resolving project artifact", ex );
        }
    }

    private void resolveMavenPluginArtifact()
    {
        try
        {
            Map plugins = project.getPluginArtifactMap();

            if (plugins.get( "greenpepper:greenpepper-maven-plugin" ) != null)
            {
                Artifact fixtureArtifact = embedder.createArtifactWithClassifier(
                        project.getGroupId(), project.getArtifactId(),
                        project.getVersion(), project.getPackaging(),
                        "fixtures" );

                resolve( fixtureArtifact, artifacts);
            }
        }
        catch (Exception ex)
        {
            logger.error( "Resolving maven-plugin artifact", ex );
        }
    }

    private void resolve(Artifact artifact, List<Artifact> runtimeArtifacts)
            throws Exception
    {
        embedder.resolve( artifact, project.getRemoteArtifactRepositories(), embedder.getLocalRepository() );

        if (artifact.getFile() != null)
        {
            runtimeArtifacts.add( artifact );
        }
    }

    private void initMavenEmbedder(ClassLoader classLoader)
            throws MavenEmbedderException
    {
        embedder = new MavenEmbedder();
        embedder.setClassLoader( classLoader );

        if (isDebug)
        {
            logger.setThreshold( MavenEmbedderLogger.LEVEL_DEBUG );
        }

        embedder.setLogger( logger );
        embedder.setAlignWithUserInstallation( true );

        embedder.start();

        initAuthenticationSettings();
    }

    private void initAuthenticationSettings()
            throws MavenEmbedderException
    {
        /**
         * GP-775 : MavenEmbedder does not read servers/mirrors configuration properly.  Mimic the resolveParameter
         * found in the DefaultMaven class to allow credentials to be taken into account.
         */
        try
        {
            WagonManager wagonManager = getWagonManager();
            Settings settings = getSettings();

            for (Object item : settings.getServers())
            {
                Server server = (Server) item;

                wagonManager.addAuthenticationInfo( server.getId(), server.getUsername(), server.getPassword(),
                        server.getPrivateKey(), server.getPassphrase() );

                wagonManager.addPermissionInfo( server.getId(), server.getFilePermissions(),
                        server.getDirectoryPermissions() );

                if (server.getConfiguration() != null)
                {
                    wagonManager.addConfiguration( server.getId(), (Xpp3Dom) server.getConfiguration() );
                }
            }

            for (Object item : settings.getMirrors())
            {
                Mirror mirror = (Mirror) item;

                wagonManager.addMirror( mirror.getId(), mirror.getMirrorOf(), mirror.getUrl() );
            }
        }
        catch (Exception ex)
        {
            throw new MavenEmbedderException( "Failed to handle authentication settings", ex );
        }
    }

    @SuppressWarnings("unchecked")
    private URL[] buildRuntimeClasspaths()
            throws IOException
    {
        final Map<String, File> actualClasspaths = getActualClasspaths();

        List<URL> classpaths = new ArrayList<URL>();

        for (Artifact artifact : artifacts)
        {
            File artifactFile = actualClasspaths.get( artifact.getFile().getName() );

            if (artifactFile == null || !artifactFile.equals( artifact.getFile() ))
            {
                logger.debug( String.format( "Artifact: %s (%s)", artifact, artifact.getFile() ) );
                classpaths.add( artifact.getFile().toURI().toURL() );
            }
        }

        return toArray( classpaths, URL.class );
    }

    private Map<String, File> getActualClasspaths()
    {
        Map<String, File> classpaths = new HashMap<String, File>();

        String[] javaClassPath = System.getProperty( "java.class.path" ).split( System.getProperty( "path.separator" ) );

        for (String classPath : javaClassPath)
        {
            File file = new File( classPath );
            classpaths.put( file.getName(), file );
        }

        return classpaths;
    }

    private WagonManager getWagonManager()
            throws Exception
    {
        return (WagonManager) ReflectionUtils.getDeclaredFieldValue( embedder, "wagonManager" );
    }

    private Settings getSettings()
            throws Exception
    {
        MavenSettingsBuilder mavenSettingsBuilder = getMavenSettingsBuilder();
        return mavenSettingsBuilder.buildSettings();
    }

    private MavenSettingsBuilder getMavenSettingsBuilder()
            throws Exception
    {
        return (MavenSettingsBuilder) ReflectionUtils.getDeclaredFieldValue( embedder, "settingsBuilder" );
    }
}