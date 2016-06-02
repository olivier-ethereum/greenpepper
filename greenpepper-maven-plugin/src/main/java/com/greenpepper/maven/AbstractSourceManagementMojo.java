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

package com.greenpepper.maven;

import com.greenpepper.maven.plugin.utils.CompilationFailureException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.Compiler;
import org.codehaus.plexus.compiler.*;
import org.codehaus.plexus.compiler.manager.CompilerManager;
import org.codehaus.plexus.compiler.manager.NoSuchCompilerException;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

import java.io.File;
import java.util.*;

/**
 * <p>Abstract AbstractSourceManagementMojo class.</p>
 *
 */
public abstract class AbstractSourceManagementMojo   extends AbstractMojo {

    /**
     * The source directory containing the fixture sources to be compiled.
     *
     * @parameter default="${basedir}/src/fixture/java"
     * @required
     */
    protected File fixtureSourceDirectory;

    /**
     * The directory to run the compiler from if fork is true.
     *
     * @parameter default-value="${basedir}"
     * @required
     * @readonly
     */
    protected File basedir;

    public File getFixtureSourceDirectory() {
        return fixtureSourceDirectory;
    }

    public void setFixtureSourceDirectory(File fixtureSourceDirectory) {
        this.fixtureSourceDirectory = fixtureSourceDirectory;
    }

    public void setBasedir(File baseDir) {
        this.basedir = baseDir;
    }
}
