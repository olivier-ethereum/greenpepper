/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.ISelection
 *  org.eclipse.jface.viewers.ISelectionChangedListener
 *  org.eclipse.jface.viewers.IStructuredSelection
 *  org.eclipse.jface.viewers.SelectionChangedEvent
 *  org.eclipse.jface.viewers.TreeViewer
 */
package com.greenpepper.eclipse.views;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.util.IExecutionListener;
import com.greenpepper.eclipse.views.EclipseSpecification;
import com.greenpepper.eclipse.views.Node;
import com.greenpepper.eclipse.views.RepositoryView;
import com.greenpepper.eclipse.views.RootNode;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;

public class SpecificationsSelection
implements ISelectionChangedListener {
    private RepositoryView repositoryView;
    private LinkedList<Node> list = new LinkedList();
    private boolean fromSingleSelection;

    public SpecificationsSelection(RepositoryView repositoryView) {
        this.repositoryView = repositoryView;
    }

    public void selectionChanged(SelectionChangedEvent event) {
        this.list = new LinkedList();
        IStructuredSelection oSelection = (IStructuredSelection)this.repositoryView.getTree().getSelection();
        this.fromSingleSelection = oSelection.size() == 1;
        Iterator iter = oSelection.iterator();
        while (iter.hasNext()) {
            this.list.addAll(((Node)iter.next()).hierarchy());
        }
    }

    public boolean fromSingleSelection() {
        return this.fromSingleSelection;
    }

    public boolean hasCurrentVersionsUsed() {
        for (Node specification : this.list) {
            if (!specification.isUsingCurrentVersion()) continue;
            return true;
        }
        return false;
    }

    public boolean hasCurrentVersionsUnused() {
        for (Node specification : this.list) {
            if (!specification.canBeImplemented() || specification.isUsingCurrentVersion()) continue;
            return true;
        }
        return false;
    }

    public boolean hasExecutableSpecs() {
        for (Node specification : this.list) {
            if (!specification.isExecutable()) continue;
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public int executionCount() {
        int counter = 0;
        for (Node specification : this.list) {
            if (!specification.isExecutable()) continue;
            ++counter;
        }
        return counter;
    }

    public boolean isValid() {
        if (this.fromSingleSelection) {
            return true;
        }
        for (Node specification : this.list) {
            if (!specification.isExecutable()) continue;
            return true;
        }
        return false;
    }

    public Node first() {
        if (this.isEmpty()) {
            return null;
        }
        return this.list.iterator().next();
    }

    public void execute(boolean includeChildren) {
        this.repositoryView.getTreeRoot().reset(true);
        if (!includeChildren) {
            this.launchExecution(this.first());
        } else {
            for (Node node : this.list) {
                this.launchExecution(node);
            }
        }
    }

    public void switchVersion(boolean toCurrent, boolean includeChildren) {
        this.repositoryView.getTreeRoot().reset(true);
        if (!includeChildren) {
            this.switchVersion(this.first(), toCurrent);
        } else {
            for (Node node : this.list) {
                if (!this.switchVersion(node, toCurrent)) continue;
                this.repositoryView.getTree().expandToLevel((Object)node.getParent(), 1);
            }
        }
        this.repositoryView.getTree().refresh();
    }

    public void tagToImplementedVersion(boolean includeChildren) {
        this.repositoryView.getTreeRoot().reset(true);
        if (!includeChildren) {
            this.tagToImplementedVersion(this.first());
        } else {
            for (Node node : this.list) {
                if (!this.tagToImplementedVersion(node)) continue;
                this.repositoryView.getTree().expandToLevel((Object)node.getParent(), 1);
            }
        }
    }

    private void launchExecution(Node node) {
        if (!node.isExecutable()) {
            return;
        }
        this.repositoryView.getTree().expandToLevel((Object)node.getParent(), 1);
        node.addExecutionListener((IExecutionListener)this.repositoryView);
        Thread execution = new Thread((Runnable)node);
        execution.start();
    }

    private boolean switchVersion(Node node, boolean toCurrent) {
        return node.setUsingCurrentVersion(toCurrent);
    }

    private boolean tagToImplementedVersion(Node node) {
        if (!(node instanceof EclipseSpecification || node.isExecutable())) {
            return false;
        }
        try {
            boolean tagged = ((EclipseSpecification)node).setAsImplemeted();
            this.repositoryView.getTree().refresh();
            return tagged;
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logError(e.getMessage(), e);
            return false;
        }
    }
}

