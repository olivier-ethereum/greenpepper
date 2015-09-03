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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.maven.runner.resolver.CombinedResolver.MavenGAV;
import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;
import com.greenpepper.util.StringUtil;

public class CoordinatesResolver implements CombinedResolver.Resolver {
    // A Maven coordinates : http://maven.apache.org/pom.html#Maven_Coordinates
    // groupId:artifactId[:packaging][:classifier]:version
    // packaging = [pom, jar, maven-plugin, ejb, war, ear, rar, par]

    private static final String REGEX = "(.*?):(.*?):(.*?:)?(.*?:)?(.*?)";

    private final Pattern coordinatesPattern = Pattern.compile(REGEX);

    private final Logger logger = LoggerFactory.getLogger(CoordinatesResolver.class);

    public final boolean canResolve(String value) {
        return !StringUtil.isEmpty(value) && coordinatesPattern.matcher(value).find();
    }

    public final MavenGAV resolve(String value) throws ResolverException {
        return resolveCoordinates(value);
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

}