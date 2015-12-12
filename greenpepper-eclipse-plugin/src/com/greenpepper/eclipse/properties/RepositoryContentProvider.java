/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.IStructuredContentProvider
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.jface.viewers.Viewer
 */
package com.greenpepper.eclipse.properties;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.properties.IRepositoryListView;
import com.greenpepper.eclipse.views.EclipseProject;
import com.greenpepper.eclipse.views.EclipseRepository;
import com.greenpepper.eclipse.views.Node;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class RepositoryContentProvider
implements IStructuredContentProvider,
IRepositoryListView {
    private TableViewer tableViewer;
    private EclipseProject project;
    private boolean local;

    public RepositoryContentProvider(TableViewer tableViewer, EclipseProject project, boolean local) {
        this.tableViewer = tableViewer;
        this.project = project;
        this.local = local;
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        if (newInput != null) {
            ((EclipseProject)newInput).addChangeListener((IRepositoryListView)this);
        }
        if (oldInput != null) {
            ((EclipseProject)oldInput).removeChangeListener((IRepositoryListView)this);
        }
    }

    public void dispose() {
        this.project.removeChangeListener((IRepositoryListView)this);
    }

    public Object[] getElements(Object parent) {
        try {
            return this.local ? this.project.getLocalRepositories() : this.project.getRemoteRepositories();
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logInternalError(e);
            return new Object[0];
        }
    }

    public void addRepository(EclipseRepository repository) {
        this.tableViewer.add((Object)repository);
        this.tableViewer.refresh();
    }

    public void removeRepository(EclipseRepository repository) {
        this.tableViewer.remove((Object)repository);
        this.tableViewer.refresh();
    }
}

