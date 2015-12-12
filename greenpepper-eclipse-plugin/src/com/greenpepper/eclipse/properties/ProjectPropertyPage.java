/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.GreenPepperServerException
 *  com.greenpepper.server.domain.Project
 *  com.greenpepper.server.domain.Repository
 *  com.greenpepper.server.domain.SystemUnderTest
 *  com.greenpepper.util.StringUtil
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IResource
 *  org.eclipse.core.runtime.IAdaptable
 *  org.eclipse.core.runtime.QualifiedName
 *  org.eclipse.jface.preference.DirectoryFieldEditor
 *  org.eclipse.jface.viewers.CellEditor
 *  org.eclipse.jface.viewers.IBaseLabelProvider
 *  org.eclipse.jface.viewers.ICellModifier
 *  org.eclipse.jface.viewers.IContentProvider
 *  org.eclipse.jface.viewers.ITableLabelProvider
 *  org.eclipse.jface.viewers.LabelProvider
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.jface.viewers.TextCellEditor
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Font
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableColumn
 *  org.eclipse.swt.widgets.TableItem
 *  org.eclipse.ui.dialogs.PropertyPage
 */
package com.greenpepper.eclipse.properties;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.properties.RepositoryContentProvider;
import com.greenpepper.eclipse.rpc.xmlrpc.EclipseXmlRpcGreenpepperClient;
import com.greenpepper.eclipse.rpc.xmlrpc.ServerPropertiesManagerImpl;
import com.greenpepper.eclipse.views.EclipseProject;
import com.greenpepper.eclipse.views.EclipseRepository;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.util.StringUtil;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectPropertyPage
extends PropertyPage {
    public static final String PP_ID = "com.greenpepper.eclipse.properties.ProjectPropertyPage";
    private Combo projectCombo;
    private Combo sutCombo;
    private String[] columnNames = new String[]{"projectName", "repositoryName", "username", "password"};
    private TableViewer tableViewer;
    private Table table;
    private DirectoryFieldEditor corePath;
    private Label errorLabel;
    private EclipseProject project;
    private Set<Project> projects;
    private Set<Repository> repositories;
    private Set<SystemUnderTest> suds;

    public ProjectPropertyPage() {
        this.noDefaultAndApplyButton();
    }

    public boolean performOk() {
        try {
            IResource resource = (IResource)this.getElement().getAdapter((Class)IResource.class);
            resource.setPersistentProperty(GreenPepperEclipsePlugin.CORE, this.corePath.getStringValue().trim());
            if (this.projectCombo.getSelectionIndex() >= 0) {
                this.validate();
                String projectName = this.projectCombo.getItem(this.projectCombo.getSelectionIndex());
                String sutName = this.sutCombo.getItem(this.sutCombo.getSelectionIndex());
                HashSet<EclipseRepository> repositories = new HashSet<EclipseRepository>();
                for (TableItem repoItem : this.table.getItems()) {
                    repositories.add((EclipseRepository)repoItem.getData());
                }
                this.project.persist(projectName, sutName, repositories);
            }
            GreenPepperEclipsePlugin.notifyRepositoryView();
            return true;
        }
        catch (Exception e) {
            return this.error(e);
        }
    }

    public Composite createContents(Composite parent) {
        IResource resource = (IResource)this.getElement().getAdapter((Class)IResource.class);
        this.project = new EclipseProject(resource.getProject());
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        composite.setLayout((Layout)layout);
        GridData data = new GridData(4, 4, true, true);
        composite.setLayoutData((Object)data);
        this.addIdentificationSection(composite);
        new org.eclipse.swt.widgets.Label(composite, 0);
        this.addRunnerSection(composite);
        new org.eclipse.swt.widgets.Label(composite, 0);
        this.addErrorSection(composite);
        this.init();
        return composite;
    }

    private void init() {
        this.loadProjects();
        if (GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.CORE) != null) {
            this.corePath.setStringValue(GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.CORE));
        }
        if (this.table.getItemCount() == 0) {
            this.loadRepositories();
        }
    }

    private void addIdentificationSection(Composite composite) {
        this.addInputTitle(composite);
        this.addInputLabelsSection(composite);
        this.addTable(composite);
    }

    private void addInputTitle(Composite parent) {
        Label titleLabel = new Label(parent, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_sudselectiontitle);
        Label infoLabel = new Label(parent, 64);
        infoLabel.setText(GreenPepperMessages.greenpepper_project_sudselectiondesc);
        infoLabel.setLayoutData((Object)new GridData(768));
    }

    private void addInputLabelsSection(Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout((Layout)layout);
        GridData labelData = new GridData(0);
        labelData.widthHint = 175;
        GridData comboData = new GridData(0);
        comboData.widthHint = 285;
        Label projectLabel = new Label(composite, 16384);
        projectLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        projectLabel.setText(GreenPepperMessages.greenpepper_project_name);
        projectLabel.setLayoutData((Object)labelData);
        this.projectCombo = new Combo(composite, 2076);
        this.projectCombo.setLayoutData((Object)comboData);
        this.projectCombo.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                ProjectPropertyPage.this.errorLabel.setText("");
                ProjectPropertyPage.this.loadSuds();
                ProjectPropertyPage.this.loadRepositories();
            }
        });
        Label repositoryLabel = new Label(composite, 16384);
        repositoryLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        repositoryLabel.setText(GreenPepperMessages.greenpepper_project_sutname);
        repositoryLabel.setLayoutData((Object)labelData);
        this.sutCombo = new Combo(composite, 2076);
        this.sutCombo.setLayoutData((Object)comboData);
        this.sutCombo.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                ProjectPropertyPage.this.errorLabel.setText("");
            }
        });
    }

    private void addTable(Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        composite.setLayout((Layout)layout);
        GridData tableData = new GridData();
        tableData.heightHint = 75;
        this.table = new Table(composite, 100866);
        this.table.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        this.table.setLinesVisible(true);
        this.table.setHeaderVisible(true);
        this.table.setLayoutData((Object)tableData);
        TableColumn column = new TableColumn(this.table, 16388, 0);
        column.setText(GreenPepperMessages.greenpepper_project_name);
        column.setWidth(130);
        column = new TableColumn(this.table, 16388, 1);
        column.setText(GreenPepperMessages.greenpepper_project_repository_name);
        column.setWidth(150);
        column = new TableColumn(this.table, 16388, 2);
        column.setText(GreenPepperMessages.greenpepper_project_username);
        column.setWidth(95);
        column = new TableColumn(this.table, 16388, 3);
        column.setText(GreenPepperMessages.greenpepper_project_password);
        column.setWidth(95);
        Label infoLabel = new Label(composite, 64);
        infoLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 3));
        infoLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        infoLabel.setText(GreenPepperMessages.greenpepper_project_remotetabledesc);
        this.tableViewer = new TableViewer(this.table);
        this.tableViewer.setUseHashlookup(true);
        this.tableViewer.setColumnProperties(this.columnNames);
        CellEditor[] editors = new CellEditor[this.columnNames.length];
        editors[0] = new TextCellEditor((Composite)this.table, 0);
        editors[1] = new TextCellEditor((Composite)this.table, 0);
        editors[2] = new TextCellEditor((Composite)this.table, 0);
        editors[3] = new TextCellEditor((Composite)this.table, 4194304);
        this.tableViewer.setCellEditors(editors);
        this.tableViewer.setContentProvider((IContentProvider)new RepositoryContentProvider(this.tableViewer, this.project, false));
        this.tableViewer.setLabelProvider((IBaseLabelProvider)new RemoteRepositoryItemLabelProvider());
        this.tableViewer.setCellModifier((ICellModifier)new RemoteRepositoryModifier());
        this.tableViewer.setInput((Object)this.project);
    }

    private void addRunnerSection(Composite composite) {
        Label titleLabel = new Label(composite, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_coretitle);
        Label descLabel = new Label(composite, 64);
        descLabel.setLayoutData((Object)new GridData(768));
        descLabel.setText(GreenPepperMessages.greenpepper_project_coredesc);
        GridData data = new GridData(16);
        data.grabExcessVerticalSpace = false;
        data.widthHint = 495;
        Composite coreComposite = new Composite(composite, 16);
        coreComposite.setLayout((Layout)new GridLayout());
        coreComposite.setLayoutData((Object)data);
        this.corePath = new DirectoryFieldEditor("GreenPepper core location", GreenPepperMessages.greenpepper_project_coredirectory, coreComposite);
        this.corePath.setStringValue("");
    }

    private void addErrorSection(Composite composite) {
        this.errorLabel = new Label(composite, 64);
        this.errorLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        this.errorLabel.setForeground(new Color((Device)Display.getDefault(), 200, 0, 0));
        this.errorLabel.setText("\t\t\t\t\t\t\t");
    }

    private void loadProjects() {
        try {
            EclipseXmlRpcGreenpepperClient greenPepperServer = new EclipseXmlRpcGreenpepperClient();
            this.projects = greenPepperServer.getAllProjects(ServerPropertiesManagerImpl.IDENTIFIER);
            int index = -1;
            for (Project prj : this.projects) {
                ++index;
                this.projectCombo.add(prj.getName());
                if (this.project.getProject() != null && this.project.getProject().getName().equals(prj.getName())) {
                    this.projectCombo.select(index);
                }
                this.projectCombo.select(Math.max(this.projectCombo.getSelectionIndex(), 0));
                this.loadSuds();
            }
        }
        catch (Exception e) {
            this.error(e);
        }
    }

    private void loadSuds() {
        try {
            this.sutCombo.removeAll();
            String selectedProject = this.projectCombo.getItem(this.projectCombo.getSelectionIndex());
            EclipseXmlRpcGreenpepperClient greenPepperServer = new EclipseXmlRpcGreenpepperClient();
            this.suds = greenPepperServer.getSystemUnderTestsOfProject(selectedProject, ServerPropertiesManagerImpl.IDENTIFIER);
            int index = -1;
            for (SystemUnderTest sud : this.suds) {
                ++index;
                this.sutCombo.add(sud.getName());
                if (this.project.getSystemUnderTest() == null || !this.project.getSystemUnderTest().getName().equals(sud.getName())) continue;
                this.sutCombo.select(index);
            }
            this.sutCombo.select(Math.max(this.sutCombo.getSelectionIndex(), 0));
        }
        catch (Exception e) {
            this.error(e);
        }
    }

    private void loadRepositories() {
        try {
            this.table.clearAll();
            this.validate();
            EclipseXmlRpcGreenpepperClient greenPepperServer = new EclipseXmlRpcGreenpepperClient();
            SystemUnderTest sut = SystemUnderTest.newInstance((String)this.sutCombo.getItem(this.sutCombo.getSelectionIndex()));
            sut.setProject(Project.newInstance((String)this.projectCombo.getItem(this.projectCombo.getSelectionIndex())));
            this.repositories = greenPepperServer.getAllRepositoriesForSystemUnderTest(sut, ServerPropertiesManagerImpl.IDENTIFIER);
            this.project.setRemoteRepositories(this.repositories);
            this.tableViewer.refresh();
        }
        catch (Exception e) {
            this.error(e);
        }
    }

    private void validate() throws GreenPepperServerException {
        if (this.sutCombo.getSelectionIndex() < 0) {
            throw new GreenPepperServerException(GreenPepperMessages.greenpepper_server_sutnotfound, "no sud");
        }
    }

    private boolean error(Throwable t) {
        GreenPepperEclipsePlugin.logInternalError(t);
        String msg = t instanceof GreenPepperServerException ? GreenPepperMessages.getText(((GreenPepperServerException)t).getId()) : (StringUtil.isEmpty((String)t.getMessage()) ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_server_systemerror) : t.getMessage());
        this.errorLabel.setText(msg);
        this.errorLabel.redraw();
        return false;
    }

    class RemoteRepositoryItemLabelProvider
    extends LabelProvider
    implements ITableLabelProvider {
        RemoteRepositoryItemLabelProvider() {
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = "";
            if (!(element instanceof EclipseRepository)) {
                return result;
            }
            EclipseRepository repo = (EclipseRepository)element;
            boolean anon = StringUtil.isEmpty((String)repo.getUsername());
            switch (columnIndex) {
                case 0: {
                    result = repo.getProjectName();
                    break;
                }
                case 1: {
                    result = repo.getRepositoryName();
                    break;
                }
                case 2: {
                    result = anon ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_project_anonymous) : repo.getUsername();
                    break;
                }
                case 3: {
                    result = anon ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_project_anonymous) : repo.getPassword().replaceAll(".", "x");
                    break;
                }
            }
            return result;
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

    class RemoteRepositoryModifier
    implements ICellModifier {
        RemoteRepositoryModifier() {
        }

        public boolean canModify(Object element, String property) {
            if (property.equals("username")) {
                return true;
            }
            Repository repository = ((EclipseRepository)element).getRepository();
            if (property.equals("password") && !StringUtil.isEmpty((String)repository.getUsername())) {
                return true;
            }
            return false;
        }

        public Object getValue(Object element, String property) {
            if (property.equals("username")) {
                return ((EclipseRepository)element).getUsername();
            }
            if (property.equals("password")) {
                return ((EclipseRepository)element).getPassword();
            }
            return "";
        }

        public void modify(Object element, String property, Object value) {
            Repository repository = ((EclipseRepository)((TableItem)element).getData()).getRepository();
            if (property.equals("username")) {
                repository.setUsername((String)value);
                if (StringUtil.isEmpty((String)((String)value))) {
                    repository.setPassword("");
                }
            }
            if (property.equals("password") && !StringUtil.isEmpty((String)repository.getUsername())) {
                repository.setPassword((String)value);
            }
            ProjectPropertyPage.this.tableViewer.refresh();
        }
    }

}

