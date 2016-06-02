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
import com.greenpepper.maven.plugin.spy.FixtureGenerator;
import com.greenpepper.maven.plugin.spy.SpyFixture;
import com.greenpepper.maven.plugin.spy.SpySystemUnderDevelopment;
import com.greenpepper.maven.plugin.utils.CompilationFailureException;
import com.greenpepper.util.ClassUtils;
import org.apache.commons.lang3.Validate;
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

import static org.apache.commons.lang3.StringUtils.isEmpty;

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

    /**
     * The fixture generator class. It should implement {@link com.greenpepper.maven.plugin.spy.FixtureGenerator}
     *
     * @parameter property="greenpepper.fixturegenerator.class"
     *              default-value="com.greenpepper.maven.plugin.spy.impl.JavaFixtureGenerator"
     * @required
     */
    String fixtureGeneratorClass;

    FixtureGenerator fixtureGenerator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            validateConfiguration();

            SpySystemUnderDevelopment spySut = new SpySystemUnderDevelopment();
            GreenPepperInterpreterSelector interpreterSelector = new GreenPepperInterpreterSelector(spySut);
            Document doc = HtmlDocumentBuilder.tablesAndLists().build(new FileReader(specification));
            doc.addFilter(new CommentTableFilter());
            doc.addFilter(new GreenPepperTableFilter(false));
            doc.execute(interpreterSelector);

            HashMap<String, SpyFixture> fixtures = spySut.getFixtures();
            for (String fixtureName : fixtures.keySet()) {
                SpyFixture spyFixture = fixtures.get(fixtureName);
                String classSource = fixtureGenerator.generateFixture(spyFixture, spySut);
                System.out.println(classSource);
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Generation Failed", e);
        } catch (IllegalAccessException e) {
            throw new MojoExecutionException("Generation Failed", e);
        } catch (InstantiationException e) {
            throw new MojoExecutionException("Generation Failed", e);
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Generation Failed", e);
        }
    }

    private void validateConfiguration() throws MojoFailureException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (isEmpty(fixtureGeneratorClass)) {
            throw new MojoFailureException("The fixtureGeneratorClass can't be null!");
        }
        Class<FixtureGenerator> fixtureGeneratorClazz = ClassUtils.loadClass(fixtureGeneratorClass);
        fixtureGenerator = fixtureGeneratorClazz.newInstance();
    }
}
