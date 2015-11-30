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
package com.greenpepper.maven.runner.resolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.cli.ConsoleDownloadMonitor;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderLogger;
import org.apache.maven.model.Repository;
import org.apache.maven.project.MavenProject;

import com.greenpepper.maven.runner.resolver.ProjectFileResolver.MavenGAV;
import com.greenpepper.util.StringUtil;

public class CoordinatesResolver implements ProjectFileResolver.Resolver
{
    // A Maven coordinates : http://maven.apache.org/pom.html#Maven_Coordinates
    // groupId:artifactId[:packaging][:classifier]:version
    // packaging = [pom, jar, maven-plugin, ejb, war, ear, rar, par]

    private static final String REGEX = "(.*?):(.*?):(.*?:)?(.*?:)?(.*?)";

    private final Pattern coordinatesPattern = Pattern.compile(REGEX);

    private final MavenEmbedder embedder;
    private final MavenEmbedderLogger logger;

    private String groupId;
    private String artifactId;
    private String version;

    public CoordinatesResolver(MavenEmbedder embedder, MavenEmbedderLogger logger)
    {
        this.embedder = embedder;
        this.logger = logger;
    }

    public final boolean canResolve(String value)
    {
        return !StringUtil.isEmpty( value ) && coordinatesPattern.matcher( value ).find();
    }

    public final File resolve(String value) throws Exception
    {
        MavenGAV mavenGAV = resolveCoordinates( value );
        groupId = mavenGAV.getGroupId();
        artifactId = mavenGAV.getArtifactId();
        version = mavenGAV.getVersion();

        logger.info( String.format( "Using maven coordinates '%s:%s:%s' as project", groupId, artifactId, version ) );

		resolveTemporaryProjectFile();
		
        return resolveProjectFile();
    }

    private MavenGAV resolveCoordinates(String value) {
        Matcher matcher = coordinatesPattern.matcher(value);
        if (matcher.matches()) {
            String groupId = matcher.group(1);
            String artifactId = matcher.group(2);

            String packaging = StringUtils.strip(matcher.group(3), ":");
            String classifier = StringUtils.strip(matcher.group(4), ":");
            String version = matcher.group(5);
            logger.info(String.format("Using maven coordinates '%s:%s:%s:%s:%s' as project", groupId, artifactId, packaging, classifier, version));
            MavenGAV result = new MavenGAV(groupId, artifactId, version);
            result.setPackaging(packaging);
            result.setClassifier(classifier);
            return result;
        } 
        throw new IllegalArgumentException("The value is not resolveable by the pattern: " + REGEX);
    }

    private void resolveTemporaryProjectFile() throws Exception
    {
        File tmpProjectFile = createTemporaryPomFile( groupId, artifactId, version );
        MavenProject tmpProject = embedder.readProjectWithDependencies( tmpProjectFile, new ConsoleDownloadMonitor() );
        Artifact targetedArtifact = (Artifact) tmpProject.getDependencyArtifacts().iterator().next();
        version = targetedArtifact.getVersion();
    }

    private File resolveProjectFile() throws ArtifactResolutionException, ArtifactNotFoundException
    {
        Artifact artifact = embedder.createArtifact( groupId, artifactId, version, "runtime", "pom" );
        embedder.resolve( artifact, new ArrayList<Repository>(), embedder.getLocalRepository() );
        return artifact.getFile();
    }

    private File createTemporaryPomFile(String groupId, String artifactId, String version) throws IOException
    {
        File tempPomFile = File.createTempFile( "pom-", ".xml" );

        FileWriter pomWriter = null;
        try
        {
            pomWriter = new FileWriter( tempPomFile );
            pomWriter.write( toPOM( groupId, artifactId, version ) );
        }
        finally
        {
            tempPomFile.deleteOnExit();
            if (pomWriter != null)
            {
                pomWriter.close();
            }
        }

        return tempPomFile;
    }

    private String toPOM(String groupId, String artifactId, String version)
    {
        StringBuilder pom = new StringBuilder();

        pom.append( "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" );
        pom.append( "  <modelVersion>4.0.0</modelVersion>\n" );
        pom.append( "  <groupId>" ).append( "greenpepper" ).append( "</groupId>\n" );
        pom.append( "  <artifactId>" ).append( "greenpepper-maven-runner-resolver" ).append( "</artifactId>\n" );
        pom.append( "  <version>" ).append( "1.0" ).append( "</version>\n" );
        pom.append( "  <packaging>" ).append( "pom" ).append( "</packaging>\n" );
        pom.append( "  <dependencies>\n" );
        pom.append( "    <dependency>\n" );
        pom.append( "        <groupId>" ).append( groupId ).append( "</groupId>\n" );
        pom.append( "        <artifactId>" ).append( artifactId ).append( "</artifactId>\n" );
        pom.append( "        <version>" ).append( version ).append( "</version>\n" );
        pom.append( "    </dependency>\n" );
        pom.append( "  </dependencies>\n" );
        pom.append( "</project>" );

        return pom.toString();
    }
}