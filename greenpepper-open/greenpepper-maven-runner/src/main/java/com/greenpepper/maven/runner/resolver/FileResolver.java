
/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 * <p>
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.maven.runner.resolver;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderLogger;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;

import java.io.File;
public class FileResolver implements ProjectFileResolver.Resolver {
    private MavenEmbedder embedder;
    private MavenEmbedderLogger logger;
    private ProjectFileResolver.MavenGAV mavenGAV;

    /**
     * <p>Constructor for FileResolver.</p>
     *
     * @param mavenEmbedder a {@link org.apache.maven.embedder.MavenEmbedder} object.
     * @param mavenEmbedderLogger a {@link org.apache.maven.embedder.MavenEmbedderLogger} object.
     */
    public FileResolver(MavenEmbedder mavenEmbedder, MavenEmbedderLogger mavenEmbedderLogger) {
        embedder = mavenEmbedder;
        logger = mavenEmbedderLogger;
    }

    /** {@inheritDoc} */
    public boolean canResolve(String value) {
        File projectFile = new File(value);
        return projectFile.isFile() && projectFile.exists();
    }

    /** {@inheritDoc} */
    public final File resolve(String value) throws ProjectBuildingException {
        File projectFile = new File(value);
        MavenProject mavenProject = embedder.readProject(projectFile);
        mavenGAV = new ProjectFileResolver.MavenGAV(mavenProject.getGroupId(),mavenProject.getArtifactId(),mavenProject.getVersion());
        mavenGAV.setPackaging(mavenProject.getPackaging());
        return projectFile;
    }

    /** {@inheritDoc} */
    @Override
    public ProjectFileResolver.MavenGAV getMavenGAV() {
        return mavenGAV;
    }
}
