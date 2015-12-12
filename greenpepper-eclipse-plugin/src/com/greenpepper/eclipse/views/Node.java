/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.domain.EnvironmentType
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.IAdaptable
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.debug.core.ILaunchConfiguration
 *  org.eclipse.swt.graphics.Image
 */
package com.greenpepper.eclipse.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.graphics.Image;

import com.greenpepper.eclipse.GreenPepperImages;
import com.greenpepper.eclipse.util.IExecutionListener;
import com.greenpepper.server.domain.EnvironmentType;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public abstract class Node
implements Comparable<Node>,
Runnable,
IProgressMonitor,
IAdaptable {
    protected static final EnvironmentType JAVA = EnvironmentType.newInstance((String)"JAVA");
    protected boolean executable;
    protected boolean openable;
    protected boolean canBeImplemented;
    protected boolean usingCurrentVersion;
    protected Node parent;
    protected SortedSet<Node> children;
    private String name;
    protected Set<IWorkingStatusListener> workingStatusListeners = new HashSet<IWorkingStatusListener>();
    protected Set<IExecutionListener> executionListeners = new HashSet<IExecutionListener>();

    public Node(String name) {
        this(name, false, false, false);
    }

    public Node(String name, boolean executable, boolean openable, boolean canBeImplemented) {
        this.name = name;
        this.executable = executable;
        this.openable = openable;
        this.canBeImplemented = canBeImplemented;
    }

    public EclipseProject getProjectNode() {
        if (this instanceof RootNode) {
            return null;
        }
        if (this instanceof EclipseProject) {
            return (EclipseProject)this;
        }
        return this.parent != null ? this.parent.getProjectNode() : null;
    }

    public EclipseRepository getRepositoryNode() {
        if (this instanceof RootNode) {
            return null;
        }
        if (this instanceof EclipseRepository) {
            return (EclipseRepository)this;
        }
        return this.parent != null ? this.parent.getRepositoryNode() : null;
    }

    public Node getChildNode(String nodeName) {
        if (this.name.equals(nodeName)) {
            return this;
        }
        for (Node child : this.children()) {
            Node node = child.getChildNode(nodeName);
            if (node == null) continue;
            return node;
        }
        return null;
    }

    public List<Node> hierarchy() {
        ArrayList<Node> specs = new ArrayList<Node>();
        specs.add(this);
        for (Node child : this.children()) {
            specs.addAll(child.hierarchy());
        }
        return specs;
    }

    public Image getImage() {
        return GreenPepperImages.createImage(this.getImagePath());
    }

    public boolean isExecutable() {
        return this.executable;
    }

    public boolean isOpenable() {
        return this.openable;
    }

    public boolean canBeImplemented() {
        if (this.executable && this.canBeImplemented) {
            return true;
        }
        return false;
    }

    public boolean isUsingCurrentVersion() {
        return this.usingCurrentVersion;
    }

    public boolean setUsingCurrentVersion(boolean usingCurrentVersion) {
        if (!this.canBeImplemented) {
            return false;
        }
        this.usingCurrentVersion = usingCurrentVersion;
        this.notifyAllWorkingStatusListener();
        this.clean(false);
        return true;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node[] getChildren() {
        return this.children().toArray(new Node[this.children.size()]);
    }

    public boolean hasChildren() {
        if (this.children().size() > 0) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() throws Exception {
        return this.children().isEmpty();
    }

    public boolean addChild(Node child) {
        child.setParent(this);
        return this.children().add(child);
    }

    public boolean removeChild(Node child) {
        child.setParent(null);
        return this.children().remove((Object)child);
    }

    public int compareTo(Node other) {
        return this.toString().compareToIgnoreCase(other.toString());
    }

    public boolean equals(Object o) {
        if (!(o != null && o instanceof Node)) {
            return false;
        }
        Node nodeCompared = (Node)o;
        if (this.toString().equals(nodeCompared.toString())) {
            return this.parent != null ? this.getParent().equals((Object)nodeCompared.getParent()) : true;
        }
        return false;
    }

    public int hashCode() {
        return this.toString().hashCode() + (this.parent != null ? this.parent.hashCode() : 0);
    }

    public void addExecutionListener(IExecutionListener executionListener) {
        this.executionListeners.add(executionListener);
    }

    public void notifyAllExecutionListeners() {
        for (IExecutionListener listener : this.executionListeners) {
            listener.executed(this);
        }
    }

    public void addWorkingStatusListener(IWorkingStatusListener workingStatusListener) {
        this.workingStatusListeners.add(workingStatusListener);
    }

    public void notifyAllWorkingStatusListener() {
        for (IWorkingStatusListener listener : this.workingStatusListeners) {
            listener.workingStatusChanged(this);
        }
    }

    public void clean(boolean andChildren) {
        if (this.children == null) {
            return;
        }
        for (Node child : this.children) {
            child.clean(true);
        }
    }

    public void reset(boolean andChildren) {
        if (this.children == null) {
            return;
        }
        for (Node child : this.children) {
            child.reset(true);
        }
    }

    public String getName() {
        return this.name;
    }

    protected SortedSet<Node> children() {
        if (this.children == null) {
            this.children = new TreeSet<Node>();
        }
        return this.children;
    }

    public abstract ILaunchConfiguration toLaunchConfiguration() throws CoreException;

    protected abstract String getImagePath();

    public Object getAdapter(Class key) {
        return null;
    }

    public void beginTask(String name, int totalWork) {
    }

    public void internalWorked(double work) {
    }

    public boolean isCanceled() {
        return false;
    }

    public void setCanceled(boolean value) {
    }

    public void setTaskName(String name) {
    }

    public void subTask(String name) {
    }

    public void worked(int work) {
    }

    public void done() {
    }
}

