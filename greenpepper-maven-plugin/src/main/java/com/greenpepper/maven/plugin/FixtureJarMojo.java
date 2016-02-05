/*
 * Copyright 2001-2005 The Apache Software Foundation.
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
package com.greenpepper.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

import com.greenpepper.maven.AbstractJarMojo;

/**
 * Build a JAR of the test classes for the current project.
 *
 * @version $Id$
 * @goal fixture-jar
 * @phase post-integration-test
 * @author oaouattara
 */
public class FixtureJarMojo
    extends AbstractJarMojo
{

	/**
	 * Set this to 'true' to bypass greenpepper fixture jar process entirely.
	 * Its use is NOT RECOMMENDED, but quite convenient on occasion.
	 *
	 * @parameter expression="${maven.greenpepper.test.skip}" default-value="false"
	 */
	private boolean skip;

	/**
     * Directory containing the fixture classes.
     *
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    private File fixtureOutputDirectory;

	/** {@inheritDoc} */
	@Override
	public void execute() throws MojoExecutionException
	{
		if (skip)
		{
			getLog().info( "Not creating fixture jar." );
		}
		else
		{
			super.execute();
		}
	}

	/**
	 * <p>getClassifier.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getClassifier()
    {
        return "fixtures";
    }

    /**
     * Return the fixture-classes directory, to serve as the root of the fixtures jar.
     *
     * @return a {@link java.io.File} object.
     */
    protected File getClassesDirectory()
    {
        return fixtureOutputDirectory;
    }
}
