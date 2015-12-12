/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.domain.Execution
 *  com.greenpepper.server.domain.ReferenceNode
 *  com.greenpepper.server.domain.Repository
 *  com.greenpepper.server.domain.RepositoryType
 *  com.greenpepper.server.domain.Specification
 *  com.greenpepper.util.StringUtil
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.debug.core.ILaunchConfiguration
 */
package com.greenpepper.eclipse.views;

import com.greenpepper.eclipse.fixture.SpyFixture;
import com.greenpepper.eclipse.fixture.SpySystemUnderDevelopment;
import com.greenpepper.eclipse.util.IExecutionListener;
import com.greenpepper.eclipse.views.EclipseProject;
import com.greenpepper.eclipse.views.EclipseRepository;
import com.greenpepper.eclipse.views.EclipseSpecification;
import com.greenpepper.eclipse.views.Node;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.ReferenceNode;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.util.StringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class EclipseReference
extends EclipseSpecification {
    private EclipseSpecification referencedpecification;
    private String repositoryUID;
    private String section;

    private EclipseReference(String name, String repositoryUID, boolean executable) {
        super(name, executable, false, false);
        this.repositoryUID = repositoryUID;
    }

    public static EclipseReference build(ReferenceNode node) {
        EclipseReference reference = new EclipseReference(node.getTitle(), node.getRepositoryUID(), node.isExecutable());
        reference.setSection(node.getSection());
        return reference;
    }

    public String getSection() {
        return this.section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private EclipseSpecification getReferencedSpecification() {
        if (this.referencedpecification != null) {
            return this.referencedpecification;
        }
        EclipseRepository repository = this.getProjectNode().getRepositoryNode(this.repositoryUID);
        this.referencedpecification = (EclipseSpecification)repository.getChildNode(this.getName());
        return this.referencedpecification;
    }

    public void openAfterExecution() {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().openAfterExecution();
    }

    public Specification getSpecification() {
        if (this.getReferencedSpecification() == null) {
            return null;
        }
        return this.getReferencedSpecification().getSpecification();
    }

    public Execution getLastExecution() {
        if (this.getReferencedSpecification() == null) {
            return null;
        }
        return (Execution)this.getReferencedSpecification().getSpecification().getExecutions().iterator().next();
    }

    public boolean setAsImplemeted() throws Exception {
        if (this.getReferencedSpecification() == null) {
            return false;
        }
        return this.getReferencedSpecification().setAsImplemeted();
    }

    public void run() {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().run();
    }

    public void addExecutionListener(IExecutionListener executionListener) {
        this.getReferencedSpecification().addExecutionListener(executionListener);
    }

    public void show(boolean reload) throws Exception {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().show(reload);
    }

    public void executed() {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().executed();
    }

    public void error(Throwable t) {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().error(t);
    }

    public String asCmdLineOptions() throws Exception {
        if (this.getReferencedSpecification() == null) {
            return null;
        }
        return this.getReferencedSpecification().asCmdLineOptions();
    }

    public ILaunchConfiguration toLaunchConfiguration() throws CoreException {
        if (this.getReferencedSpecification() == null) {
            return null;
        }
        return this.getReferencedSpecification().toLaunchConfiguration();
    }

    public void clean(boolean andChildren) {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().clean(andChildren);
    }

    public void reset(boolean andChildren) {
        if (this.getReferencedSpecification() == null) {
            return;
        }
        this.getReferencedSpecification().reset(andChildren);
    }

    public boolean isExecutable() {
        if (this.getReferencedSpecification() == null) {
            return false;
        }
        return this.executable;
    }

    public boolean isOpenable() {
        if (this.getReferencedSpecification() == null) {
            return false;
        }
        return this.getReferencedSpecification().isOpenable();
    }

    public boolean canBeImplemented() {
        if (this.getReferencedSpecification() == null) {
            return false;
        }
        if (this.isExecutable() && this.getReferencedSpecification().canBeImplemented()) {
            return true;
        }
        return false;
    }

    public boolean setUsingCurrentVersion(boolean usingCurrentVersion) {
        if (!this.canBeImplemented()) {
            return false;
        }
        return this.getReferencedSpecification().setUsingCurrentVersion(usingCurrentVersion);
    }

    public boolean isUsingCurrentVersion() {
        if (this.getReferencedSpecification() == null) {
            return false;
        }
        return this.getReferencedSpecification().isUsingCurrentVersion();
    }

    public SpySystemUnderDevelopment getSpySut() {
        if (this.getReferencedSpecification() == null) {
            return null;
        }
        return this.getReferencedSpecification().getSpySut();
    }

    public HashMap<String, SpyFixture> getFixtures() {
        if (this.getReferencedSpecification() == null) {
            return new HashMap<String, SpyFixture>();
        }
        return this.getReferencedSpecification().getFixtures();
    }

    public String toString() {
        if (StringUtil.isBlank((String)this.section)) {
            return this.getName();
        }
        return String.format("%s  [%s]", this.getName(), this.section);
    }

    protected Repository getRepository() {
        if (this.getReferencedSpecification() == null) {
            return this.getProjectNode().getRepositoryNode(this.repositoryUID).getRepository();
        }
        return this.getRepositoryNode().getRepository();
    }

    protected String getImagePath() {
        String addin = "";
        if (this.canBeImplemented() && this.isExecutable()) {
            addin = "_diff";
        }
        if (this.isUsingCurrentVersion() && this.isExecutable()) {
            addin = "_working";
        }
        return String.format("%s%s/%s%s.gif", "icons/", this.getRepository().getType().getName(), this.getStatus(), addin);
    }

    protected String getStatus() {
        if (this.getReferencedSpecification() == null) {
            return "notfound";
        }
        Set executions = this.getReferencedSpecification().getSpecification().getExecutions();
        if (executions.isEmpty()) {
            return this.isExecutable() ? "executable" : "notexecutable";
        }
        return ((Execution)executions.iterator().next()).getStatus();
    }
}

