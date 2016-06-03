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
import com.greenpepper.maven.AbstractSourceManagementMojo;
import com.greenpepper.maven.plugin.spy.FixtureGenerator;
import com.greenpepper.maven.plugin.spy.SpyFixture;
import com.greenpepper.maven.plugin.spy.SpySystemUnderDevelopment;
import com.greenpepper.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.StringUtils.difference;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <p>Generate a fixture from a Specifciation.</p>
 *
 * @goal generate-fixtures
 */
public class FixtureGeneratorMojo extends AbstractSourceManagementMojo {

    /**
     * A file on the disk from which to generate the Fixture.
     *
     * @parameter property="greenpepper.specification"
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

    /**
     * @parameter property="greenpepper.repositories"
     */
    ArrayList<Repository> repositories;

    /**
     * Set this to a Repository name defined in the pom.xml.
     * This option is only used in case <code>-Dgp.test</code> is used.
     *
     * @parameter property="gp.repo"
     */
    String selectedRepository;

    /**
     * Set this to a Specification name to use this specification for fixture generation.
     * The test is searched inside the default repository.
     *
     * @parameter property="gp.test"
     */
    String specificationName;

    private FixtureGenerator fixtureGenerator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            validateConfiguration();

            if (specification != null) {
                Document doc = HtmlDocumentBuilder.tablesAndLists().build(new FileReader(specification));
                generateFixturesForDocument(doc);
            } else {
                boolean atLeastOneRepositoryProcessed = false;
                boolean specificationFound = false;
                try {
                    for (Repository repository : repositories) {
                        if (isNotEmpty(selectedRepository)) {
                            if (StringUtils.equals(selectedRepository, repository.getName())) {
                                specificationFound = processRepository(repository);
                                atLeastOneRepositoryProcessed = true;
                            } else {
                                getLog().debug(format("Skipping repository '%s', selected is '%s' ", repository.getName(), selectedRepository));
                            }
                        } else {
                            specificationFound = processRepository(repository);
                        }
                        if (specificationFound) {
                            getLog().debug("Already found the required specification. We stop here.");
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new MojoExecutionException("Error running the Goal", e);
                }
                if (isNotEmpty(selectedRepository) && !atLeastOneRepositoryProcessed) {
                    throw new MojoExecutionException("No repository could match your requirements");
                }
                if (!specificationFound) {
                    throw new MojoExecutionException(format("The specification %s has not been found", specificationName));
                }
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Generation Failed", e);
        }
    }

    private boolean processRepository(Repository repository) throws Exception {
        try {
            Document document = repository.getDocumentRepository().loadDocument(specificationName);
            generateFixturesForDocument(document);
            return true;
        } catch (Exception e) {
            getLog().debug("failed to load the document from repository : " +  e.getMessage());
            return false;
        }
    }

    private void generateFixturesForDocument(Document doc) throws Exception {
        SpySystemUnderDevelopment spySut = new SpySystemUnderDevelopment();
        GreenPepperInterpreterSelector interpreterSelector = new GreenPepperInterpreterSelector(spySut);
        doc.addFilter(new CommentTableFilter());
        doc.addFilter(new GreenPepperTableFilter(false));
        doc.execute(interpreterSelector);

        HashMap<String, SpyFixture> fixtures = spySut.getFixtures();
        for (String fixtureName : fixtures.keySet()) {
            SpyFixture spyFixture = fixtures.get(fixtureName);
            FixtureGenerator.Result result = fixtureGenerator.generateFixture(spyFixture, spySut, getFixtureSourceDirectory());
            File classSource = result.getFixtureFile();
            switch (result.getAction()) {
                case CREATED:
                    getLog().info(format("\t %s: %s", "Generated", difference(basedir.getAbsolutePath(), classSource.getAbsolutePath())));
                    break;
                case UPDATED:
                    getLog().info(format("\t %s: %s", "Updated", difference(basedir.getAbsolutePath(), classSource.getAbsolutePath())));
                    break;
                case NONE:
                    getLog().debug(format("\t %s: %s", "Nothing done for", difference(basedir.getAbsolutePath(), classSource.getAbsolutePath())));
                    break;
            }
            if (getLog().isDebugEnabled()){
                getLog().debug(format("\t Code of fixtureName :\n %s", readFileToString(classSource)));
            }
        }
    }

    private void validateConfiguration() throws MojoFailureException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (isEmpty(fixtureGeneratorClass)) {
            throw new MojoFailureException("The fixtureGeneratorClass can't be null!");
        }
        Class<FixtureGenerator> fixtureGeneratorClazz = ClassUtils.loadClass(fixtureGeneratorClass);
        fixtureGenerator = fixtureGeneratorClazz.newInstance();

        if (specification != null && !specification.isFile()) {
            throw new MojoFailureException("The specified file doesn't exist : " + specification);
        }
        if (specification == null) {
            if (repositories == null || repositories.isEmpty()) {
                throw new MojoFailureException("When not using a File to generate the fixtures, you need to set some repositories");
            }
            if (isEmpty(specificationName)) {
                throw new MojoFailureException("When not using a File to generate the fixtures, you need to set a specification name to search for in the repositories");
            }
        }
    }
}
