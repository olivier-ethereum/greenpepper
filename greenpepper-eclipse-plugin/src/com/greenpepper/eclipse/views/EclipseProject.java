
package com.greenpepper.eclipse.views;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.properties.IRepositoryListView;
import com.greenpepper.eclipse.rpc.xmlrpc.EclipseXmlRpcGreenpepperClient;
import com.greenpepper.eclipse.rpc.xmlrpc.ServerPropertiesManagerImpl;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.util.StringUtil;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class EclipseProject
extends Node {
    private static final String[] DEFAULT_FILTERS = new String[]{"html", "htm"};
    private static final QualifiedName REMOTE_REPOSITORIES = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.REMOTE_REPOSITORIES");
    private static final QualifiedName LOCAL_REPOSITORIES = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.LOCAL_REPOSITORIES");
    private static final QualifiedName PROJECT_NAME = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.PROJECT_NAME");
    private static final QualifiedName SUT_NAME = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.SUT_NAME");
    private static final QualifiedName FILTER = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.FILTER");
    private static final String REPO_DEL = ";";
    private static final String REPO_ARGS_DEL = "@";
    private static final String FILTER_DEL = "@";
    private IProject iProject;
    private Project project;
    private SystemUnderTest systemUnderTest;
    private Set<IRepositoryListView> changeListeners = new HashSet<IRepositoryListView>();
    private SortedSet<Node> localRepositories;
    private SortedSet<Node> remoteRepositories;
    private SortedSet<String> filters;

    public EclipseProject(IProject iProject) {
        super(iProject.getName());
        this.iProject = iProject;
    }

    public void persist(String projectName, String sutName, Set<EclipseRepository> repositories) throws CoreException {
        this.iProject.getProject().setPersistentProperty(PROJECT_NAME, projectName);
        this.iProject.getProject().setPersistentProperty(SUT_NAME, sutName);
        this.persistRemoteRepositories(repositories);
    }

    public IProject getIProject() {
        return this.iProject;
    }

    public Project getProject() {
        if (this.project != null) {
            return this.project;
        }
        try {
            String projectName = this.iProject.getProject().getPersistentProperty(PROJECT_NAME);
            if (projectName != null) {
                this.project = Project.newInstance((String)projectName);
            }
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logInternalError(e);
        }
        return this.project;
    }

    public SystemUnderTest getSystemUnderTest() {
        if (this.systemUnderTest != null) {
            return this.systemUnderTest;
        }
        try {
            String sutName = this.iProject.getProject().getPersistentProperty(SUT_NAME);
            if (sutName != null) {
                this.systemUnderTest = SystemUnderTest.newInstance((String)sutName);
                this.getProject().addSystemUnderTest(this.systemUnderTest);
            }
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logInternalError(e);
        }
        return this.systemUnderTest;
    }

    public EclipseRepository getRepositoryNode(String repositoryUID) {
        if (repositoryUID == null) {
            return null;
        }
        for (Node repo : this.remoteRepositories) {
            if (!((EclipseRepository)repo).getRepository().getUid().equals(repositoryUID)) continue;
            return (EclipseRepository)repo;
        }
        return null;
    }

    public Node[] getLocalRepositories() {
        if (this.localRepositories == null) {
            this.loadLocalRepositories();
        }
        return this.localRepositories.toArray(new Node[this.localRepositories.size()]);
    }

    public Node[] getRemoteRepositories() {
        if (this.remoteRepositories == null) {
            this.loadRemoteRepositories();
        }
        return this.remoteRepositories.toArray(new Node[this.remoteRepositories.size()]);
    }

    public void setRemoteRepositories(Set<Repository> repositories) {
        this.remoteRepositories = new TreeSet<Node>();
        for (Repository repository : repositories) {
            repository.setUsername("");
            repository.setPassword("");
            EclipseRepository repo = new EclipseRepository(repository, true);
            this.remoteRepositories.add((Node)repo);
            Iterator<IRepositoryListView> iterator = this.changeListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().addRepository(repo);
            }
        }
    }

    public void run() {
    }

    public ILaunchConfiguration toLaunchConfiguration() {
        return null;
    }

    public void addLocalRepository(Repository repository) throws CoreException, GreenPepperServerException {
        if (this.localRepositories == null) {
            this.remoteRepositories = new TreeSet<Node>();
        }
        EclipseRepository repo = new EclipseRepository(repository, true);
        this.localRepositories.remove((Object)repo);
        if (this.localRepositories.add((Node)repo)) {
            this.persistLocalRepositories();
            Iterator<IRepositoryListView> iterator = this.changeListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().addRepository(repo);
            }
        }
    }

    public void removeLocalRepository(Repository repository) throws CoreException, GreenPepperServerException {
        if (this.localRepositories == null) {
            return;
        }
        EclipseRepository repo = new EclipseRepository(repository, true);
        if (this.localRepositories.remove((Object)repo)) {
            this.persistLocalRepositories();
            Iterator<IRepositoryListView> iterator = this.changeListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().removeRepository(repo);
            }
        }
    }

    public void removeChangeListener(IRepositoryListView viewer) {
        this.changeListeners.remove(viewer);
    }

    public void addChangeListener(IRepositoryListView viewer) {
        this.changeListeners.add(viewer);
    }

    public boolean isSupported(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (file.getName().indexOf(".") < 0) {
            return false;
        }
        if (this.filters == null) {
            this.loadFilters();
        }
        String[] temps = file.getName().split("\\.");
        return this.filters.contains(temps[temps.length - 1].toLowerCase());
    }

    public String toString() {
        String projectName = this.iProject.getProject().getName();
        return this.getSystemUnderTest() != null ? String.format("%s     (%s)", projectName, this.getSystemUnderTest().getName()) : projectName;
    }

    public int compareTo(Node obj) {
        if (!(obj instanceof EclipseProject)) {
            return super.compareTo(obj);
        }
        EclipseProject other = (EclipseProject)obj;
        if (this.hasChildren() && !other.hasChildren()) {
            return -1;
        }
        if (!this.hasChildren() && other.hasChildren()) {
            return 1;
        }
        return super.compareTo(obj);
    }

    public boolean equals(Object o) {
        if (!(o != null && o instanceof EclipseProject)) {
            return false;
        }
        EclipseProject nodeCompared = (EclipseProject)o;
        return this.iProject.equals((Object)nodeCompared.getIProject());
    }

    public int hashCode() {
        return this.iProject.hashCode();
    }

    protected SortedSet<Node> children() {
        if (this.children == null) {
            this.children = new TreeSet();
            this.children.addAll(Arrays.asList(this.getLocalRepositories()));
            this.children.addAll(Arrays.asList(this.getRemoteRepositories()));
        }
        return super.children();
    }

    protected String getImagePath() {
        return "icons/project.gif";
    }

    private void loadLocalRepositories() {
        this.localRepositories = new TreeSet<Node>();
        try {
            String[] repoDefs;
            String persistedRepos = this.iProject.getProject().getPersistentProperty(LOCAL_REPOSITORIES);
            if (StringUtil.isBlank((String)persistedRepos)) {
                return;
            }
            for (String repoDef : repoDefs = persistedRepos.split(";")) {
                StringTokenizer stk = new StringTokenizer(repoDef, "@");
                Repository repo = Repository.newInstance((String)stk.nextToken());
                repo.setProject(Project.newInstance((String)this.iProject.getProject().getName()));
                repo.setBaseTestUrl(stk.nextToken());
                RepositoryType type = RepositoryType.newInstance((String)"FILE");
                type.registerClassForEnvironment(FileSystemRepository.class.getName(), JAVA);
                repo.setType(type);
                EclipseRepository repoNode = new EclipseRepository(repo, true);
                repoNode.setParent((Node)this);
                this.localRepositories.add((Node)repoNode);
            }
        }
        catch (Exception e) {
            this.errorToNode(e, this.localRepositories);
        }
    }

    private void persistLocalRepositories() throws CoreException {
        StringBuilder sb = new StringBuilder("");
        for (Node repo : this.localRepositories) {
            sb.append(((EclipseRepository)repo).getRepository().getUid()).append("@");
            sb.append(((EclipseRepository)repo).getRepository().getBaseTestUrl());
            sb.append(";");
        }
        this.iProject.getProject().setPersistentProperty(LOCAL_REPOSITORIES, sb.toString());
    }

    private void loadRemoteRepositories() {
        try {
            String[] repoDefs;
            this.remoteRepositories = this.retrieveRepositories();
            String persistedRepos = this.iProject.getProject().getPersistentProperty(REMOTE_REPOSITORIES);
            if (StringUtil.isBlank((String)persistedRepos)) {
                return;
            }
            for (String repoDef : repoDefs = persistedRepos.split(";")) {
                StringTokenizer stk = new StringTokenizer(repoDef, "@");
                String uid = stk.nextToken();
                for (Node node : this.remoteRepositories) {
                    Repository repository;
                    if (!(node instanceof EclipseRepository) || !(repository = ((EclipseRepository)node).getRepository()).getUid().equals(uid)) continue;
                    repository.setUsername(stk.hasMoreTokens() ? stk.nextToken() : "");
                    repository.setPassword(stk.hasMoreTokens() ? StringUtil.unescapeSemiColon((String)stk.nextToken()) : "");
                }
            }
        }
        catch (Exception e) {
            this.errorToNode(e, this.remoteRepositories);
        }
    }

    private SortedSet<Node> retrieveRepositories() {
        TreeSet<Node> eRepositories = new TreeSet<Node>();
        if (this.getSystemUnderTest() == null) {
            return eRepositories;
        }
        try {
            EclipseXmlRpcGreenpepperClient greenPepperServer = new EclipseXmlRpcGreenpepperClient();
            Set<Repository> repositories = greenPepperServer.getAllRepositoriesForSystemUnderTest(this.getSystemUnderTest(), ServerPropertiesManagerImpl.IDENTIFIER);
            for (Repository repo : repositories) {
                repo.setUsername("");
                repo.setPassword("");
                EclipseRepository repoNode = new EclipseRepository(repo, false);
                repoNode.setParent((Node)this);
                eRepositories.add((Node)repoNode);
            }
        }
        catch (Exception e) {
            this.errorToNode(e, eRepositories);
        }
        return eRepositories;
    }

    private void persistRemoteRepositories(Set<EclipseRepository> repositories) throws CoreException {
        StringBuilder sb = new StringBuilder("");
        for (EclipseRepository repository : repositories) {
            sb.append(repository.getRepository().getUid()).append("@");
            sb.append(repository.getRepository().getUsername()).append("@");
            sb.append(StringUtil.escapeSemiColon((String)repository.getRepository().getPassword()));
            sb.append(";");
        }
        this.iProject.getProject().setPersistentProperty(REMOTE_REPOSITORIES, sb.toString());
    }

    private void errorToNode(Throwable t, Set<Node> nodeList) {
        String msg = t instanceof GreenPepperServerException ? GreenPepperMessages.getText(((GreenPepperServerException)t).getId()) : (StringUtil.isEmpty((String)t.getMessage()) ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_server_systemerror) : t.getMessage());
        GreenPepperEclipsePlugin.logInternalError(t);
        ErrorNode errorNode = new ErrorNode(msg, msg);
        errorNode.setParent((Node)this);
        nodeList.add((Node)errorNode);
    }

    private void loadFilters() {
        try {
            this.filters = new TreeSet<String>();
            String persistedFilters = this.getIProject().getProject().getPersistentProperty(FILTER);
            String[] filtersToAdd = StringUtil.isEmpty((String)persistedFilters) ? DEFAULT_FILTERS : persistedFilters.split("@");
            this.filters.addAll(Arrays.asList(filtersToAdd));
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.logInternalError(e);
        }
    }
}

