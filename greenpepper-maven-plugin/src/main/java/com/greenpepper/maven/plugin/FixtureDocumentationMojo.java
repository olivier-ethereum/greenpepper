/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.maven.plugin;

import com.greenpepper.annotation.Fixture;
import com.greenpepper.document.CommentTableFilter;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.document.GreenPepperTableFilter;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.maven.AbstractSourceManagementMojo;
import com.greenpepper.maven.plugin.schemas.FixtureType;
import com.greenpepper.maven.plugin.schemas.Fixtures;
import com.greenpepper.maven.plugin.spy.FixtureGenerator;
import com.greenpepper.maven.plugin.spy.SpyFixture;
import com.greenpepper.maven.plugin.spy.SpySystemUnderDevelopment;
import com.greenpepper.maven.plugin.spy.impl.JavaFixtureGenerator;
import com.greenpepper.maven.plugin.utils.FixtureAnnotationClassScanner;
import com.greenpepper.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static java.io.File.separator;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.StringUtils.difference;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <p>Generates The documentation of your fixtures.</p>
 *
 * @goal documentation
 * @phase pre-integration-test
 * @requiresDependencyResolution test
 */
public class FixtureDocumentationMojo extends AbstractMojo {

    String packageToScan;

    /**
     * @parameter default-value="${project.build.directory}/greenpepper-documentation"
     * @required
     */
    File documentationDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            FixtureAnnotationClassScanner fixtureScanner = new FixtureAnnotationClassScanner();
            Fixtures scanned = fixtureScanner.scan(packageToScan);
            JAXBContext jaxbContext = JAXBContext.newInstance(Fixtures.class.getPackage().getName());
            File output = new File(documentationDirectory, "fixtures.xml");
            jaxbContext.createMarshaller().marshal(scanned, output);
        } catch (ClassNotFoundException e) {
            throw new MojoFailureException("Failed to generated the documentation descriptor",e);
        } catch (JAXBException e) {
            throw new MojoFailureException("Failed to generated the documentation descriptor",e);
        }
    }

}
