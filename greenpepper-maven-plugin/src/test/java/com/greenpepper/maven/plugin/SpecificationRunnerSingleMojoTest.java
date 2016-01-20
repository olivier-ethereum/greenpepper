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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.greenpepper.util.URIUtil;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

public class SpecificationRunnerSingleMojoTest extends AbstractMojoTestCase
{
    private SpecificationRunnerMojo mojo;

    public void testReadSinglePomWithSingleTestExecutionConfiguration() throws Exception {
        URL pomPath = SpecificationRunnerSingleMojoTest.class.getResource( "pom-single.xml");
        mojo = (SpecificationRunnerMojo) lookupMojo( "run", URIUtil.decoded(pomPath.getPath()) );
        mojo.project = new MavenProjectStub();
        mojo.project.setBasedir(new File("."));
        mojo.classpathElements = new ArrayList<String>( );
        String core = dependency( "greenpepper-core.jar" ).getAbsolutePath();
        mojo.classpathElements.add( core );
        mojo.classpathElements.add( dependency( "guice-1.0.jar" ).getAbsolutePath());
        
        mojo.pluginDependencies = new ArrayList<Artifact>();
        mojo.pluginDependencies.add( new DependencyArtifact( "commons-codec", dependency( "commons-codec-1.3.jar" )) );
        mojo.pluginDependencies.add( new DependencyArtifact( "xmlrpc", dependency( "xmlrpc-2.0.1.jar" )) ); 
        File extension = dependency( "greenpepper-extensions-java.jar" );
        mojo.pluginDependencies.add( new DependencyArtifact( "greenpepper-extensions-java", extension  ));
        mojo.pluginDependencies.add( new DependencyArtifact( "slf4j-api", dependency("slf4j-api-1.6.1.jar")));
        mojo.pluginDependencies.add( new DependencyArtifact( "jcl-over-slf4j", dependency("jcl-over-slf4j-1.6.1.jar")));

        mojo.execute();
       
    }
    private File localDir() throws URISyntaxException {
        return spec("spec.html").getParentFile();
    }

    private File dependency(String name) throws URISyntaxException
    {
        return new File( classesOutputDir(), name );
    }

    private File classesOutputDir()
        throws URISyntaxException
    {
        return localDir().getParentFile().getParentFile().getParentFile().getParentFile();
    }

    private File spec(String name) throws URISyntaxException
    {
        return new File( URIUtil.decoded(SpecificationRunnerSingleMojoTest.class.getResource( name ).getPath()) );
    }


}