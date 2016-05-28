
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.maven.runner.resolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.embedder.MavenEmbedderLogger;
public class ProjectFileResolver
{
    private final List<Resolver> resolvers = new ArrayList<Resolver>( 2 );
    private MavenGAV mavenGAV;

    /**
     * <p>Constructor for ProjectFileResolver.</p>
     *
     * @param embedder a {@link org.apache.maven.embedder.MavenEmbedder} object.
     * @param logger a {@link org.apache.maven.embedder.MavenEmbedderLogger} object.
     */
    public ProjectFileResolver(MavenEmbedder embedder, MavenEmbedderLogger logger)
    {
        resolvers.add( new FileResolver(embedder, logger ) );
        resolvers.add( new CoordinatesResolver( embedder, logger ) );
    }

    /**
     * <p>resolve.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.lang.Exception if any.
     */
    public File resolve(String value) throws Exception
    {
        for (Resolver resolver : resolvers)
        {
            if (resolver.canResolve( value ))
            {
                File resolved = resolver.resolve(value);
                mavenGAV = resolver.getMavenGAV();
                return resolved;
            }
        }

        throw new MavenEmbedderException( String.format( "Cannot resolve project dependency descriptor '%s'", value ) );
    }

    /**
     * <p>Getter for the field <code>mavenGAV</code>.</p>
     *
     * @return a {@link com.greenpepper.maven.runner.resolver.ProjectFileResolver.MavenGAV} object.
     * @since 4.0-beta3
     */
    public MavenGAV getMavenGAV() {
        return mavenGAV;
    }

    public static interface Resolver
    {
        boolean canResolve(String value);

        File resolve(String value) throws Exception;

        MavenGAV getMavenGAV();
    }
    
    public static class MavenGAV {

        private String groupId;
        private String artifactId;
        private String packaging;
        private String classifier;
        private String version;

        public MavenGAV() {
        }

        public MavenGAV(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setPackaging(String packaging) {
            this.packaging = packaging;
        }

        public String getClassifier() {
            return classifier;
        }

        public void setClassifier(String classifier) {
            this.classifier = classifier;
        }

        public String getPackaging() {
            return packaging;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
            result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
            result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
            result = prime * result + ((packaging == null) ? 0 : packaging.hashCode());
            result = prime * result + ((version == null) ? 0 : version.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MavenGAV other = (MavenGAV) obj;
            if (artifactId == null) {
                if (other.artifactId != null)
                    return false;
            } else if (!artifactId.equals(other.artifactId))
                return false;
            if (classifier == null) {
                if (other.classifier != null)
                    return false;
            } else if (!classifier.equals(other.classifier))
                return false;
            if (groupId == null) {
                if (other.groupId != null)
                    return false;
            } else if (!groupId.equals(other.groupId))
                return false;
            if (packaging == null) {
                if (other.packaging != null)
                    return false;
            } else if (!packaging.equals(other.packaging))
                return false;
            if (version == null) {
                if (other.version != null)
                    return false;
            } else if (!version.equals(other.version))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "MavenGAV{" +
                    "groupId='" + groupId + '\'' +
                    ", artifactId='" + artifactId + '\'' +
                    ", packaging='" + packaging + '\'' +
                    ", classifier='" + classifier + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}
