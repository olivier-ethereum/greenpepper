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
package com.greenpepper.maven.runner.resolver;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.maven.runner.resolver.CombinedResolver.MavenGAV;
import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;;

public class FileResolver implements CombinedResolver.Resolver {

    private static final Logger logger = LoggerFactory.getLogger(FileResolver.class);

    public final boolean canResolve(String value) {
        File projectFile = new File(value);
        return projectFile.isFile() && projectFile.exists();
    }

    public final MavenGAV resolve(String value) throws ResolverException {
        try {
            File file = new File(value);
            DefaultModelBuilderFactory builderFactory = new DefaultModelBuilderFactory();
            DefaultModelBuilder modelBuilder = builderFactory.newInstance();
            ModelBuildingRequest request = new DefaultModelBuildingRequest();
            request.setPomFile(file);
            ModelBuildingResult buildResult;
            buildResult = modelBuilder.build(request);
            Model effectiveModel = buildResult.getEffectiveModel();

            logger.debug("Got model Id '{}' from pom '{}'", effectiveModel, file.getAbsolutePath());
            MavenGAV mavenGAV = new MavenGAV();
            mavenGAV.setGroupId(effectiveModel.getGroupId());
            mavenGAV.setArtifactId(effectiveModel.getArtifactId());
            mavenGAV.setVersion(effectiveModel.getVersion());
            return mavenGAV;
        } catch (ModelBuildingException e) {
            throw new ResolverException("Impossible to resolve the Maven coordonates from the file :  " + value, e);
        }
    }
}
