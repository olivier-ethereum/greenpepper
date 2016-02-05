/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenpepper.maven;

import java.io.File;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.jar.JarArchiver;

/**
 * Base class for creating a jar from project classes.
 *
 * @author <a href="evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public abstract class AbstractJarMojo
    extends AbstractMojo
{

    private static final String[] DEFAULT_EXCLUDES = new String[]{"**/package.html"};

    private static final String[] DEFAULT_INCLUDES = new String[]{"**/**"};

    /**
     * Directory containing the generated JAR.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Name of the generated JAR.
     *
     * @parameter alias="jarName" expression="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * The Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver jarArchiver;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The maven archive configuration to use.
     *
     * See <a href="http://maven.apache.org/ref/current/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html">the Javadocs for MavenArchiveConfiguration</a>.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;
    
    /**
     * The Maven Session
     *
     * @required
     * @readonly
     * @parameter
     * expression="${session}"
     */
    private MavenSession mavenSession;

    /**
     * Whether creating the archive should be forced.
     * @parameter expression="${jar.forceCreation}" default-value="false"
     */
    private boolean forceCreation;

    /**
     * Return the specific output directory to serve as the root for the archive.
     *
     * @return a {@link java.io.File} object.
     */
    protected abstract File getClassesDirectory();

    /**
     * <p>Getter for the field <code>project</code>.</p>
     *
     * @return a {@link org.apache.maven.project.MavenProject} object.
     */
    protected final MavenProject getProject()
    {
        return project;
    }

    /**
     * Overload this to produce a test-jar, for example.
     *
     * @return a {@link java.lang.String} object.
     */
    protected abstract String getClassifier();

    /**
     * <p>getJarFile.</p>
     *
     * @param basedir a {@link java.io.File} object.
     * @param finalName a {@link java.lang.String} object.
     * @param classifier a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    protected static File getJarFile( File basedir, String finalName, String classifier )
    {
        if ( classifier == null )
        {
            classifier = "";
        }
        else if ( classifier.trim().length() > 0 && !classifier.startsWith( "-" ) )
        {
            classifier = "-" + classifier;
        }

        return new File( basedir, finalName + classifier + ".jar" );
    }

    /**
     * Generates the JAR.
     *
     * @return a {@link java.io.File} object.
     * @throws org.apache.maven.plugin.MojoExecutionException if any.
     */
    public File createArchive()
        throws MojoExecutionException
    {
        File jarFile = getJarFile( outputDirectory, finalName, getClassifier() );

        MavenArchiver archiver = new MavenArchiver();

        archiver.setArchiver( jarArchiver );

        archiver.setOutputFile( jarFile );

        archive.setForced( forceCreation );

        try
        {
            File contentDirectory = getClassesDirectory();
            if ( !contentDirectory.exists() )
            {
                getLog().warn( "JAR will be empty - no content was marked for inclusion!" );
            }
            else
            {
                archiver.getArchiver().addDirectory( contentDirectory, DEFAULT_INCLUDES, DEFAULT_EXCLUDES );
            }

            archiver.createArchive(mavenSession, project, archive);

            return jarFile;
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error assembling JAR", e );
        }
    }

    /**
     * Generates the JAR.
     *
     * @throws org.apache.maven.plugin.MojoExecutionException if any.
     */
    public void execute()
        throws MojoExecutionException
    {
        File jarFile = createArchive();

        String classifier = getClassifier();
        if ( classifier != null )
        {
            projectHelper.attachArtifact( getProject(), "jar", classifier, jarFile );
        }
        else
        {
            getProject().getArtifact().setFile( jarFile );
        }
    }
}
