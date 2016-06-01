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

import com.greenpepper.document.CommentTableFilter;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.document.GreenPepperTableFilter;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.maven.AbstractCompilerMojo;
import com.greenpepper.maven.plugin.spy.SpySystemUnderDevelopment;
import com.greenpepper.maven.plugin.utils.CompilationFailureException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * <p>Generate a fixture from a Specifciation.</p>
 *
 * @goal generate-fixtures
 */
public class FixtureGeneratorMojo extends AbstractMojo {

    /**
     * @parameter property="greenpepper.specification"
     * @required
     */
    File specification;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            SpySystemUnderDevelopment spySut = new SpySystemUnderDevelopment();
            GreenPepperInterpreterSelector interpreterSelector = new GreenPepperInterpreterSelector(spySut);
            Document doc = HtmlDocumentBuilder.tablesAndLists().build(new FileReader(specification));
            doc.addFilter(new CommentTableFilter());
            doc.addFilter(new GreenPepperTableFilter(false));
            doc.execute(interpreterSelector);
        } catch (IOException e) {
            throw new MojoExecutionException("Generation Failed", e);
        }
    }
}
