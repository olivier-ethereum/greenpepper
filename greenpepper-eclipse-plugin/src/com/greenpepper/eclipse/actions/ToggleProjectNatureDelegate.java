/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IProjectDescription
 *  org.eclipse.core.resources.IResource
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.IAdaptable
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.jface.action.IAction
 *  org.eclipse.jface.viewers.ISelection
 *  org.eclipse.jface.viewers.IStructuredSelection
 *  org.eclipse.ui.IWorkbenchWindow
 *  org.eclipse.ui.IWorkbenchWindowActionDelegate
 *  org.eclipse.ui.actions.ActionDelegate
 */
package com.greenpepper.eclipse.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;

public class ToggleProjectNatureDelegate
extends ActionDelegate
implements IWorkbenchWindowActionDelegate {
    private final Set<IProject> projects = new HashSet<IProject>();

    public void init(IWorkbenchWindow window) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.updateSelectedProjects(selection);
        if (this.projects.size() == 1) {
            boolean checked;
            boolean enabled;
            try {
                IProject project = this.projects.iterator().next();
                enabled = project.hasNature("org.eclipse.jdt.core.javanature") || project.hasNature("org.eclipse.php.core.PHPNature");
                checked = enabled && project.hasNature("com.greenpepper.eclipse.nature");
            }
            catch (CoreException e) {
                GreenPepperEclipsePlugin.logInternalError((Throwable)e);
                checked = false;
                enabled = false;
            }
            action.setEnabled(enabled);
            action.setChecked(checked);
        } else {
            action.setEnabled(false);
            action.setChecked(false);
        }
    }

    public void run(IAction action) {
        for (IProject project : this.projects) {
            IProjectDescription description;
            if (!project.isOpen()) continue;
            try {
                description = project.getDescription();
            }
            catch (CoreException e) {
                GreenPepperEclipsePlugin.logInternalError((Throwable)e);
                continue;
            }
            ArrayList<String> newIds = new ArrayList<String>();
            newIds.addAll(Arrays.asList(description.getNatureIds()));
            int index = newIds.indexOf("com.greenpepper.eclipse.nature");
            if (index == -1) {
                if (description.hasNature("org.eclipse.php.core.PHPNature") && !description.hasNature("org.eclipse.jdt.core.javanature")) {
                    newIds.add("org.eclipse.jdt.core.javanature");
                }
                newIds.add("com.greenpepper.eclipse.nature");
            } else {
                newIds.remove(index);
                if (description.hasNature("org.eclipse.php.core.PHPNature") && description.hasNature("org.eclipse.jdt.core.javanature")) {
                    int javaNatureIndex = newIds.indexOf("org.eclipse.jdt.core.javanature");
                    newIds.remove(javaNatureIndex);
                }
            }
            description.setNatureIds(newIds.toArray(new String[newIds.size()]));
            try {
                project.setDescription(description, null);
                continue;
            }
            catch (CoreException e) {
                GreenPepperEclipsePlugin.logInternalError((Throwable)e);
            }
        }
    }

    private void updateSelectedProjects(ISelection selection) {
        this.projects.clear();
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        Iterator iterator = ((IStructuredSelection)selection).iterator();
        while (iterator.hasNext()) {
            Object elem = iterator.next();
            if (!(elem instanceof IResource) && (!(elem instanceof IAdaptable) || !((elem = ((IAdaptable)elem).getAdapter((Class)IResource.class)) instanceof IResource))) continue;
            if (!(elem instanceof IProject) && !((elem = ((IResource)elem).getProject()) instanceof IProject)) continue;
            this.projects.add((IProject)elem);
        }
    }
}

