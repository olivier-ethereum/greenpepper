/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.document.CommentTableFilter
 *  com.greenpepper.document.Document
 *  com.greenpepper.document.ExampleFilter
 *  com.greenpepper.document.GreenPepperInterpreterSelector
 *  com.greenpepper.document.GreenPepperTableFilter
 *  com.greenpepper.document.InterpreterSelector
 *  com.greenpepper.html.HtmlDocumentBuilder
 *  com.greenpepper.report.XmlReport
 *  com.greenpepper.repository.DocumentRepository
 *  com.greenpepper.server.domain.Document
 *  com.greenpepper.server.domain.DocumentNode
 *  com.greenpepper.server.domain.EnvironmentType
 *  com.greenpepper.server.domain.Execution
 *  com.greenpepper.server.domain.Project
 *  com.greenpepper.server.domain.Repository
 *  com.greenpepper.server.domain.RepositoryType
 *  com.greenpepper.server.domain.Specification
 *  com.greenpepper.server.domain.SystemUnderTest
 *  com.greenpepper.systemunderdevelopment.SystemUnderDevelopment
 *  com.greenpepper.util.IOUtil
 *  org.eclipse.core.resources.IContainer
 *  org.eclipse.core.resources.IFile
 *  org.eclipse.core.resources.IFolder
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.core.runtime.NullProgressMonitor
 *  org.eclipse.debug.core.DebugPlugin
 *  org.eclipse.debug.core.ILaunch
 *  org.eclipse.debug.core.ILaunchConfiguration
 *  org.eclipse.debug.core.ILaunchConfigurationType
 *  org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
 *  org.eclipse.debug.core.ILaunchManager
 *  org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants
 *  org.eclipse.ui.IWorkbench
 *  org.eclipse.ui.browser.IWebBrowser
 *  org.eclipse.ui.browser.IWorkbenchBrowserSupport
 */
package com.greenpepper.eclipse.views;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.greenpepper.document.CommentTableFilter;
import com.greenpepper.document.Document;
import com.greenpepper.document.ExampleFilter;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.document.GreenPepperTableFilter;
import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.fixture.SpyFixture;
import com.greenpepper.eclipse.fixture.SpySystemUnderDevelopment;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.launcher.RunSpecificationLaunchConfigurationDelegate;
import com.greenpepper.eclipse.util.IExecutionListener;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.report.XmlReport;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.IOUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class EclipseSpecification
extends Node {
    private static final String HTML = ".html";
    private static final String XML = ".xml";
    private SpySystemUnderDevelopment spySut;
    private String repositoryUID;
    private boolean openAfterExecution;
    private Specification specification;
    private String name;
    protected Set<IExecutionListener> executionListeners = new HashSet<IExecutionListener>();

    public EclipseSpecification(String name, boolean executable, boolean canBeImplemented, boolean usingCurrentVersion) {
        super(name, executable, true, canBeImplemented);
        this.name = name;
        this.usingCurrentVersion = usingCurrentVersion;
    }

    public static EclipseSpecification build(DocumentNode node, boolean currentVersion) {
        return new EclipseSpecification(node.getTitle(), node.isExecutable(), node.canBeImplemented(), currentVersion);
    }

    public static EclipseSpecification build(IProject iProject, ILaunchConfiguration configuration) throws CoreException {
        EclipseProject projectNode = new EclipseProject(iProject);
        Project project = Project.newInstance((String)"N/A");
        Repository repository = Repository.newInstance((String)configuration.getAttribute("repoUID", "NONE"));
        repository.setName("");
        repository.setBaseTestUrl(configuration.getAttribute("baseURL", ""));
        repository.setUsername(configuration.getAttribute("username", ""));
        repository.setPassword(configuration.getAttribute("password", ""));
        RepositoryType type = RepositoryType.newInstance((String)"N/A");
        type.registerClassForEnvironment(configuration.getAttribute("repoClass", ""), JAVA);
        repository.setType(type);
        repository.setProject(project);
        boolean local = Boolean.valueOf(configuration.getAttribute("local", "false"));
        EclipseRepository repositoryNode = new EclipseRepository(repository, local);
        boolean currentVersion = Boolean.valueOf(configuration.getAttribute("workingversion", "false"));
        EclipseSpecification specNode = new EclipseSpecification(configuration.getAttribute("specificationName", ""), true, currentVersion, currentVersion);
        projectNode.addChild((Node)repositoryNode);
        repositoryNode.addChild((Node)specNode);
        return specNode;
    }

    public void openAfterExecution() {
        this.openAfterExecution = true;
    }

    public Specification getSpecification() {
        if (this.specification != null) {
            return this.specification;
        }
        this.specification = Specification.newInstance((String)this.name);
        this.specification.setRepository(this.getRepository());
        return this.specification;
    }

    public Execution getLastExecution() {
        return (Execution)this.getSpecification().getExecutions().iterator().next();
    }

    public boolean setAsImplemeted() throws Exception {
        if (!(this.canBeImplemented && this.usingCurrentVersion)) {
            return false;
        }
        DocumentRepository docRepository = this.getRepository().asDocumentRepository(JAVA);
        docRepository.setDocumentAsImplemeted(this.name);
        this.setUsingCurrentVersion(false);
        this.canBeImplemented = false;
        return true;
    }

    public void run() {
        try {
            if (!this.executable) {
                return;
            }
            this.toLaunchConfiguration().launch("run", (IProgressMonitor)this, true);
        }
        catch (Exception e) {
            this.error(e);
            this.notifyAllExecutionListeners();
        }
    }

    public void show(boolean reload) throws Exception {
        if (!this.isOpenable()) {
            return;
        }
        if (reload) {
            this.clean(false);
        }
        if (this.getSpecification().getExecutions().isEmpty()) {
            this.openSpecification();
        } else {
            this.openResult();
        }
    }

    public void showRemote() throws Exception {
        if (this.getRepositoryNode().isLocal()) {
            return;
        }
        String remoteUrl = this.buildRemoteUrl();
        String id = String.valueOf(this.buildEditorName()) + " - Remote";
        IWorkbenchBrowserSupport browserSupport = GreenPepperEclipsePlugin.getDefault().getWorkbench().getBrowserSupport();
        IWebBrowser browser = browserSupport.createBrowser(32, id, id, remoteUrl);
        browser.openURL(new URL(remoteUrl));
    }

    public void executed() {
        try {
            try {
                StringReader resultsReader = new StringReader(IOUtil.readContent((File)new File(this.reportPath())));
                XmlReport report = XmlReport.parse((Reader)resultsReader);
                Execution execution = Execution.newInstance((Specification)this.getSpecification(), (SystemUnderTest)null, (XmlReport)report);
                if (!execution.hasException()) {
                    execution.setResults(report.getResults(0));
                }
                this.getSpecification().addExecution(execution);
                if (this.openAfterExecution) {
                    this.openResult();
                }
            }
            catch (Exception e) {
                this.error(e);
                this.notifyAllExecutionListeners();
            }
        }
        finally {
            this.notifyAllExecutionListeners();
        }
    }

    public void error(Throwable t) {
        GreenPepperEclipsePlugin.logInternalError(t);
        this.getSpecification().addExecution(Execution.error((Specification)this.getSpecification(), (SystemUnderTest)null, (String)null, (String)t.getMessage()));
        if (this.openAfterExecution) {
            try {
                this.openResult();
            }
            catch (Exception v6352) {}
        }
    }

    public String asCmdLineOptions() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(this.loadSpecification().getAbsolutePath()).append("\" \"");
        sb.append(this.reportPath()).append("\" ");
        sb.append("--xml");
        return sb.toString();
    }

    public ILaunchConfiguration toLaunchConfiguration() throws CoreException {
        DebugPlugin plugin = DebugPlugin.getDefault();
        ILaunchManager lm = plugin.getLaunchManager();
        ILaunchConfigurationType t = lm.getLaunchConfigurationType("com.greenpepper.eclipse.launcher.RunSpecification");
        ILaunchConfigurationWorkingCopy wc = t.newInstance(null, this.name);
        Repository repository = this.getRepository();
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, this.getProjectNode().getIProject().getName());
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, RunSpecificationLaunchConfigurationDelegate.GREENPEPPER_COMMAND_LINE_RUNNER);
        wc.setAttribute("specificationName", this.name);
        wc.setAttribute("workingversion", String.valueOf(this.usingCurrentVersion));
        wc.setAttribute("repoUID", repository.getUid());
        wc.setAttribute("baseURL", repository.getBaseTestUrl());
        String repositoryTypeClass = repository.getType().getRepositoryTypeClass(JAVA);
        wc.setAttribute("repoClass", repositoryTypeClass);
        boolean isLocal = this.getRepositoryNode().isLocal();
        wc.setAttribute("local", String.valueOf(isLocal));
        wc.setAttribute("username", repository.getUsername());
        wc.setAttribute("password", repository.getPassword());
        ILaunchConfiguration config = wc.doSave();
        return config;
    }

    public EclipseRepository getForcedRepositoryNode() {
        if (this.repositoryUID == null) {
            return null;
        }
        return this.getProjectNode().getRepositoryNode(this.repositoryUID);
    }

    public void setForcedRepositoryUID(String forcedRepositoryUID) {
        this.repositoryUID = forcedRepositoryUID;
    }

    public String getName() {
        return this.name;
    }

    public SpySystemUnderDevelopment getSpySut() {
        if (this.spySut != null) {
            return this.spySut;
        }
        try {
            this.spySut = new SpySystemUnderDevelopment();
            GreenPepperInterpreterSelector interpreterSelector = new GreenPepperInterpreterSelector((SystemUnderDevelopment)this.spySut);
            Document doc = HtmlDocumentBuilder.tablesAndLists().build((Reader)new FileReader(this.loadSpecification()));
            doc.addFilter((ExampleFilter)new CommentTableFilter());
            doc.addFilter((ExampleFilter)new GreenPepperTableFilter(GreenPepperEclipsePlugin.isLazyMode(this.getProjectNode().getIProject())));
            doc.execute((InterpreterSelector)interpreterSelector);
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logError("Failed to retrieve Fixtures from spec", e);
        }
        return this.spySut;
    }

    public HashMap<String, SpyFixture> getFixtures() {
        HashMap<String, SpyFixture> hashMap;
        if (this.getSpySut() != null) {
            hashMap = this.getSpySut().getFixtures();
        } else {
            hashMap = new HashMap<String, SpyFixture>();
        }
        return hashMap;
    }

    public void clean(boolean andChildren) {
        try {
            this.reset(false);
            this.spySut = null;
            new File(this.specificationPath()).delete();
            new File(this.reportPath()).delete();
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logWarning(String.valueOf(GreenPepperMessages.greenpepper_repoview_cleanspecfailed) + " (" + e.getMessage() + ")");
        }
        if (andChildren) {
            for (Node child : this.children()) {
                child.clean(andChildren);
            }
        }
    }

    public void reset(boolean andChildren) {
        if (this.getSpecification() != null) {
            this.getSpecification().setExecutions(new HashSet());
            this.executionListeners.clear();
        }
        if (andChildren) {
            for (Node child : this.children()) {
                child.reset(andChildren);
            }
        }
    }

    public String toString() {
        if (this.name.indexOf("\\") < 0 && this.name.indexOf("/") < 0) {
            return this.name;
        }
        if (this.name.lastIndexOf("\\") > this.name.lastIndexOf("/")) {
            return this.name.substring(this.name.lastIndexOf(92) + 1);
        }
        return this.name.substring(this.name.lastIndexOf(47) + 1);
    }

    protected String getImagePath() {
        String addin = "";
        if (this.canBeImplemented && this.isExecutable()) {
            addin = "_diff";
        }
        if (this.usingCurrentVersion && this.isExecutable()) {
            addin = "_working";
        }
        return String.format("%s%s/%s%s.gif", "icons/", this.getRepository().getType().getName(), this.getStatus(), addin);
    }

    protected Repository getRepository() {
        return this.getRepositoryNode().getRepository();
    }

    protected String getStatus() {
        Set executions = this.getSpecification().getExecutions();
        if (executions.isEmpty()) {
            return this.executable ? "executable" : "notexecutable";
        }
        return ((Execution)executions.iterator().next()).getStatus();
    }

    private String specificationPath() {
        return new File(System.getProperty("java.io.tmpdir"), this.getFileName("specification", ".html")).getAbsolutePath();
    }

    private String resultPath() {
        return new File(System.getProperty("java.io.tmpdir"), this.getFileName("results", ".html")).getAbsolutePath();
    }

    private String reportPath() {
        return new File(System.getProperty("java.io.tmpdir"), this.getFileName("report", ".xml")).getAbsolutePath();
    }

    private String getFileName(String type, String extension) {
        String prefix = this.getRepository().getUid().replaceAll("\\\\", "_").replaceAll("/", "_").replaceAll("-", "_");
        String altName = this.name.replaceAll("\\\\", "_").replaceAll("/", "_").replaceAll("\"", "''");
        return String.format("%s_%s_%s%s", prefix, altName, type, extension);
    }

    private void openSpecification() throws Exception {
        GreenPepperEclipsePlugin.openInEditor(this.buildEditorName(), this.toResource(this.loadSpecification()));
    }

    private void openResult() throws Exception {
        GreenPepperEclipsePlugin.openInEditor(this.buildEditorName(), this.toResource(this.loadResult()));
    }

    private String buildEditorName() {
        return String.format("%s/%s", this.getRepository().getUid(), this.getSpecification().getName());
    }

    private File loadSpecification() throws Exception {
        File specFile;
        Repository repository = this.getRepository();
        if (repository == null) {
            return null;
        }
        specFile = new File(this.specificationPath());
        DocumentRepository docRepository = repository.asDocumentRepository(JAVA);
        Document document = docRepository.loadDocument(String.valueOf(this.name) + (this.usingCurrentVersion ? "?implemented=false" : ""));
        PrintWriter pw = new PrintWriter(specFile);
        try {
            document.print(pw);
        }
        finally {
            IOUtil.closeQuietly((Closeable)pw);
        }
        return specFile;
    }

    private File loadResult() throws Exception {
        File resultFile;
        resultFile = new File(this.resultPath());
        FileWriter fw = new FileWriter(resultFile);
        try {
            Execution exe = (Execution)this.getSpecification().getExecutions().iterator().next();
            fw.write(exe.hasException() ? exe.getExecutionErrorId() : exe.getResults());
        }
        finally {
            IOUtil.closeQuietly((Closeable)fw);
        }
        return resultFile;
    }

    private String buildRemoteUrl() throws Exception {
        Specification specification = this.getSpecification();
        String remoteUrl = specification.getRepository().getType().resolveName((com.greenpepper.server.domain.Document)specification);
        return remoteUrl.replaceAll("\"", "%22");
    }

    private IFile toResource(File file) throws Exception {
        try {
            return this.createOrUpdateResource(file);
        }
        catch (CoreException v6840) {
            this.getProjectNode().getIProject().refreshLocal(1, (IProgressMonitor)new NullProgressMonitor());
            return this.createOrUpdateResource(file);
        }
    }

    private IFile createOrUpdateResource(File file) throws Exception {
        FileInputStream is = null;
        try {
            IFolder dir = this.getProjectNode().getIProject().getFolder("GreenPepper");
            if (!dir.exists()) {
                dir.create(true, true, (IProgressMonitor)new NullProgressMonitor());
            }
            IFile resource = dir.getFile(this.getFileName("gp", ".html"));
            is = new FileInputStream(file);
            if (!resource.exists()) {
                resource.create((InputStream)is, true, (IProgressMonitor)new NullProgressMonitor());
            } else {
                resource.setContents((InputStream)is, true, false, (IProgressMonitor)new NullProgressMonitor());
            }
            IFile iFile = resource;
            return iFile;
        }
        finally {
            IOUtil.closeQuietly((Closeable)is);
        }
    }
}

