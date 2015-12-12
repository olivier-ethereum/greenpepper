/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IProjectNature
 *  org.eclipse.core.runtime.CoreException
 */
package com.greenpepper.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class GreenPepperNature
implements IProjectNature {
    private IProject project;
    public static final String NATURE_ID = "com.greenpepper.eclipse.nature";

    public void configure() throws CoreException {
    }

    public void deconfigure() throws CoreException {
    }

    public IProject getProject() {
        return this.project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }
}

