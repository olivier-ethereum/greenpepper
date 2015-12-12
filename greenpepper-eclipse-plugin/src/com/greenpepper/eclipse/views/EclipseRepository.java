/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.repository.FileSystemRepository
 *  com.greenpepper.server.GreenPepperServerException
 *  com.greenpepper.server.domain.DocumentNode
 *  com.greenpepper.server.domain.Project
 *  com.greenpepper.server.domain.ReferenceNode
 *  com.greenpepper.server.domain.Repository
 *  com.greenpepper.server.domain.RepositoryType
 *  com.greenpepper.server.domain.SystemUnderTest
 *  com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller
 *  com.greenpepper.util.StringUtil
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.QualifiedName
 *  org.eclipse.debug.core.ILaunchConfiguration
 */
package com.greenpepper.eclipse.views;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.rpc.xmlrpc.EclipseXmlRpcGreenpepperClient;
import com.greenpepper.eclipse.rpc.xmlrpc.ServerPropertiesManagerImpl;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.domain.ReferenceNode;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.util.StringUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class EclipseRepository
extends Node
implements IWorkingStatusListener {
    private static final String SPECS_DEL = ";";
    private boolean local;
    private Set<String> workingSpecs;
    private Repository repository;

    public EclipseRepository(Repository repository, boolean local) {
        super(repository.getName());
        this.repository = repository;
        this.local = local;
    }

    public boolean isLocal() {
        return this.local;
    }

    public String getProjectName() {
        return this.repository.getProject().getName();
    }

    public Repository getRepository() {
        return this.repository;
    }

    public String getRepositoryName() {
        return this.repository.getName();
    }

    public String getBaseTestUrl() {
        return this.repository.getBaseTestUrl();
    }

    public String getUsername() {
        return this.repository.getUsername();
    }

    public String getPassword() {
        return this.repository.getPassword();
    }

    public void workingStatusChanged(Node node) {
        if (node.isUsingCurrentVersion()) {
            this.workingSpecs.add(node.toString());
        } else {
            this.workingSpecs.remove(node.toString());
        }
        this.persistWorkingSpecs();
    }

    public void run() {
    }

    public ILaunchConfiguration toLaunchConfiguration() {
        return null;
    }

    public String toString() {
        if (!this.local) {
            return this.repository.getName();
        }
        String url = this.repository.getBaseTestUrl().replace('\\', '/');
        String[] parts = url.split("/");
        if (parts.length < 5) {
            return this.repository.getBaseTestUrl();
        }
        return String.valueOf(parts[0]) + File.separator + parts[1] + File.separator + "..." + File.separator + parts[parts.length - 1];
    }

    public int compareTo(Node o) {
        EclipseRepository other = (EclipseRepository)o;
        if (this.local == other.local) {
            return super.compareTo(o);
        }
        if (this.local) {
            return 1;
        }
        return -1;
    }

    public boolean equals(Object o) {
        if (!(o != null && o instanceof EclipseRepository)) {
            return false;
        }
        EclipseRepository nodeCompared = (EclipseRepository)o;
        if (this.repository.getUid().equals(nodeCompared.getRepository().getUid()) && this.local == nodeCompared.local) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.repository.getUid().hashCode();
    }

    protected String getImagePath() {
        return String.format("%s%s/repository" + (this.local ? "_local" : "") + ".gif", "icons/", this.repository.getType().getName());
    }

    protected SortedSet<Node> children() {
        if (this.children == null) {
            this.load();
        }
        return super.children();
    }

    private void load() {
        try {
            this.children = new TreeSet();
            DocumentNode hierarchy = this.retrieveHierarchy();
            for (DocumentNode child : hierarchy.getChildren()) {
                Node branch = this.buildBranch((Node)this, child);
                if (branch == null) continue;
                this.addChild(branch);
            }
        }
        catch (GreenPepperServerException e) {
            GreenPepperEclipsePlugin.logError(GreenPepperMessages.getText(e.getId()), (Throwable)e);
            this.addChild((Node)new ErrorNode("", GreenPepperMessages.getText(e.getId())));
        }
    }

    private Node buildBranch(Node parent, DocumentNode docNode) {
        EclipseReference node;
        File file = new File(this.getBaseTestUrl(), docNode.getTitle());
        if (this.local && !this.getProjectNode().isSupported(file)) {
            return null;
        }
        if (docNode instanceof ReferenceNode) {
            node = EclipseReference.build((ReferenceNode)docNode);
        } else {
            node = (EclipseReference) EclipseSpecification.build(docNode, this.wasUsingCurrentVersion(docNode));
            for (DocumentNode child : docNode.getChildren()) {
                Node branch = this.buildBranch((Node)node, child);
                if (branch == null) continue;
                node.addChild(branch);
            }
        }
        node.addWorkingStatusListener((IWorkingStatusListener)this);
        parent.addChild((Node)node);
        return node;
    }

    private DocumentNode retrieveHierarchy() throws GreenPepperServerException {
        DocumentNode hierarchy = null;
        if (this.local) {
            try {
                FileSystemRepository fileRepo = new FileSystemRepository(new String[]{this.getBaseTestUrl()});
                hierarchy = XmlRpcDataMarshaller.toDocumentNode(new Vector(fileRepo.listDocumentsInHierarchy()));
            }
            catch (Exception e) {
                throw new GreenPepperServerException(StringUtil.isEmpty((String)e.getMessage()) ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_server_systemerror) : e.getMessage(), "");
            }
        } else {
            EclipseXmlRpcGreenpepperClient greenPepperServer = new EclipseXmlRpcGreenpepperClient();
            hierarchy = greenPepperServer.getSpecificationHierarchy(this.repository, this.getProjectNode().getSystemUnderTest(), ServerPropertiesManagerImpl.IDENTIFIER);
        }
        if (!(hierarchy != null && hierarchy.hasChildren())) {
            String msg = String.format("%s (%s)", hierarchy.getTitle(), GreenPepperMessages.getText(GreenPepperMessages.greenpepper_project_empty));
            throw new GreenPepperServerException(msg, "");
        }
        return hierarchy;
    }

    private boolean wasUsingCurrentVersion(DocumentNode docNode) {
        if (this.workingSpecs == null) {
            this.loadWorkingSpecs();
        }
        if (this.workingSpecs.contains(docNode.getTitle()) && docNode.isExecutable() && docNode.canBeImplemented()) {
            return true;
        }
        this.workingSpecs.remove(docNode.getTitle());
        return false;
    }

    private void loadWorkingSpecs() {
        try {
            String[] specs;
            this.workingSpecs = new HashSet<String>();
            String persistedWorkingSpecs = this.getProjectNode().getIProject().getPersistentProperty(new QualifiedName("com.greenpepper.eclipse", this.repository.getUid()));
            if (StringUtil.isBlank((String)persistedWorkingSpecs)) {
                return;
            }
            for (String spec : specs = persistedWorkingSpecs.split(";")) {
                this.workingSpecs.add(spec);
            }
        }
        catch (CoreException e) {
            GreenPepperEclipsePlugin.logInternalError((Throwable)e);
        }
    }

    private void persistWorkingSpecs() {
        try {
            StringBuilder sb = new StringBuilder("");
            for (Node node : this.hierarchy()) {
                if (!(node instanceof EclipseSpecification) || !node.isUsingCurrentVersion()) continue;
                sb.append(node.toString());
                sb.append(";");
            }
            this.getProjectNode().getIProject().getProject().setPersistentProperty(new QualifiedName("com.greenpepper.eclipse", this.repository.getUid()), sb.toString());
        }
        catch (CoreException e) {
            GreenPepperEclipsePlugin.logInternalError((Throwable)e);
        }
    }
}

