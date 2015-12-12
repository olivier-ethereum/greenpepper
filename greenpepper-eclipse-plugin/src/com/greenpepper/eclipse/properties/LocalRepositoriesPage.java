/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.GreenPepperServerException
 *  com.greenpepper.server.domain.Project
 *  com.greenpepper.server.domain.Repository
 *  com.greenpepper.util.StringUtil
 *  com.greenpepper.util.URIUtil
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IResource
 *  org.eclipse.core.runtime.IAdaptable
 *  org.eclipse.jface.preference.DirectoryFieldEditor
 *  org.eclipse.jface.viewers.CellEditor
 *  org.eclipse.jface.viewers.IBaseLabelProvider
 *  org.eclipse.jface.viewers.IContentProvider
 *  org.eclipse.jface.viewers.ISelection
 *  org.eclipse.jface.viewers.IStructuredSelection
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
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableColumn
 *  org.eclipse.ui.dialogs.PropertyPage
 */
package com.greenpepper.eclipse.properties;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.PropertyPage;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.views.EclipseProject;
import com.greenpepper.eclipse.views.EclipseRepository;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.util.StringUtil;
import com.greenpepper.util.URIUtil;

public class LocalRepositoriesPage
extends PropertyPage {
    private Table table;
    private TableViewer tableViewer;
    private EclipseProject projectNode;
    private Label errorLabel;
    private Button add;
    private Button delete;
    private DirectoryFieldEditor path;
    private String[] columnNames = new String[]{"baseTestUrl"};

    public LocalRepositoriesPage() {
        this.noDefaultAndApplyButton();
    }

    public boolean performOk() {
        return true;
    }

    public void addRepository(Repository repository) {
        try {
            this.projectNode.addLocalRepository(repository);
            GreenPepperEclipsePlugin.notifyRepositoryView();
        }
        catch (Exception e) {
            this.error(e);
        }
    }

    public void removeRepository(Repository repository) {
        try {
            this.projectNode.removeLocalRepository(repository);
            GreenPepperEclipsePlugin.notifyRepositoryView();
        }
        catch (Exception e) {
            this.error(e);
        }
    }

    public Composite createContents(Composite parent) {
        IResource resource = (IResource)this.getElement().getAdapter((Class)IResource.class);
        this.projectNode = new EclipseProject(resource.getProject());
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout(5, false);
        composite.setLayout((Layout)layout);
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        composite.setLayoutData((Object)data);
        this.addRepositoryListSection(composite);
        this.addSpacer(composite, 1);
        this.addRepositoriesInputSection(composite);
        this.addErrorSection(composite);
        this.tableViewer = this.createTableViewer();
        return composite;
    }

    protected TableViewer createTableViewer() {
        TableViewer tableViewer = new TableViewer(this.table);
        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(this.columnNames);
        CellEditor[] editors = new CellEditor[this.columnNames.length];
        editors[0] = new TextCellEditor((Composite)this.table, 0);
        tableViewer.setCellEditors(editors);
        tableViewer.setContentProvider((IContentProvider)new RepositoryContentProvider(tableViewer, this.projectNode, true));
        tableViewer.setLabelProvider((IBaseLabelProvider)new LocalRepositoryItemLabelProvider());
        tableViewer.setInput((Object)this.projectNode);
        return tableViewer;
    }

    protected Table createTable(Composite composite) {
        Table table = new Table(composite, 101122);
        table.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        table.setLinesVisible(false);
        table.setHeaderVisible(true);
        table.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selections;
                LocalRepositoriesPage.this.delete.setEnabled(!(selections = (IStructuredSelection)LocalRepositoriesPage.this.tableViewer.getSelection()).isEmpty());
            }
        });
        TableColumn column = new TableColumn(table, 16388, 0);
        column.setText(GreenPepperMessages.greenpepper_project_basetesturl);
        column.setWidth(470);
        return table;
    }

    protected void addInputSection(Composite composite) {
        GridData data = new GridData(16);
        data.grabExcessVerticalSpace = false;
        data.widthHint = 450;
        Composite pathComposite = new Composite(composite, 16);
        pathComposite.setLayout((Layout)new GridLayout());
        pathComposite.setLayoutData((Object)data);
        this.path = new DirectoryFieldEditor("Local path", GreenPepperMessages.greenpepper_project_directory, pathComposite);
        this.path.setStringValue("");
        this.add = new Button(composite, 0);
        this.add.setText(GreenPepperMessages.greenpepper_project_add);
        this.add.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        this.add.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                File selectedFile = new File(LocalRepositoriesPage.this.path.getStringValue());
                if (!selectedFile.isDirectory()) {
                    return;
                }
                Repository repo = Repository.newInstance((String)URIUtil.flatten((String)selectedFile.getAbsolutePath()));
                repo.setBaseTestUrl(selectedFile.getAbsolutePath());
                repo.setProject(Project.newInstance((String)LocalRepositoriesPage.this.projectNode.toString()));
                LocalRepositoriesPage.this.addRepository(repo);
                LocalRepositoriesPage.this.path.setStringValue("");
            }
        });
    }

    private void addRepositoryListSection(Composite composite) {
        this.addRepositoryListTitleSection(composite);
        this.addRepositoryListDescSection(composite);
        this.addSpacer(composite, 5);
        this.addTable(composite);
        this.createRemoveAction(composite);
    }

    private void addRepositoryListTitleSection(Composite composite) {
        Label tableTitleLabel = new Label(composite, 16448);
        tableTitleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        tableTitleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        tableTitleLabel.setText(GreenPepperMessages.greenpepper_project_localtabletitle);
        GridData tableTitleGd = new GridData();
        tableTitleGd.horizontalSpan = 5;
        tableTitleLabel.setLayoutData((Object)tableTitleGd);
    }

    private void addRepositoryListDescSection(Composite composite) {
        Label infoLabel = new Label(composite, 64);
        infoLabel.setText(GreenPepperMessages.greenpepper_project_localtabledesc);
        GridData data = new GridData();
        data.horizontalSpan = 5;
        infoLabel.setLayoutData((Object)data);
    }

    private void addTable(Composite composite) {
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = false;
        gridData.heightHint = 100;
        gridData.horizontalSpan = 5;
        this.table = this.createTable(composite);
        this.table.setLayoutData((Object)gridData);
    }

    private void createRemoveAction(Composite composite) {
        this.delete = new Button(composite, 16777224);
        this.delete.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        this.delete.setText(GreenPepperMessages.greenpepper_project_remove);
        GridData data = new GridData(16777216);
        data.widthHint = 490;
        data.horizontalSpan = 5;
        this.delete.setLayoutData((Object)data);
        this.delete.setEnabled(false);
        this.delete.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                LocalRepositoriesPage.this.delete.setEnabled(false);
                IStructuredSelection selections = (IStructuredSelection)LocalRepositoriesPage.this.tableViewer.getSelection();
                
                @SuppressWarnings("unchecked")
                Iterator<EclipseRepository> iterator = selections.iterator();
                while (iterator.hasNext()) {
                    EclipseRepository repo = iterator.next();
                    if (repo == null) continue;
                    LocalRepositoriesPage.this.removeRepository(repo.getRepository());
                }
            }
        });
    }

    private void addRepositoriesInputSection(Composite composite) {
        this.addInfoInputTitleSection(composite);
        this.addInfoInputDescSection(composite);
        this.addSpacer(composite, 5);
        this.addInputSection(composite);
    }

    private void addInfoInputTitleSection(Composite composite) {
        Label titleLabel = new Label(composite, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_localinputtitle);
        GridData data = new GridData();
        data.horizontalSpan = 5;
        titleLabel.setLayoutData((Object)data);
    }

    private void addInfoInputDescSection(Composite composite) {
        Label infoLabel = new Label(composite, 64);
        infoLabel.setText(GreenPepperMessages.greenpepper_project_localinputdesc);
        GridData data = new GridData();
        data.horizontalSpan = 5;
        infoLabel.setLayoutData((Object)data);
    }

    private void addErrorSection(Composite composite) {
        GridData errorData = new GridData(0);
        errorData.horizontalSpan = 5;
        this.errorLabel = new Label(composite, 64);
        this.errorLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        this.errorLabel.setForeground(new Color((Device)Display.getDefault(), 200, 0, 0));
        this.errorLabel.setText("\t\t\t\t\t\t\t");
        this.errorLabel.setLayoutData((Object)errorData);
    }

    private void addSpacer(Composite composite, int hrzSpan) {
        GridData spacerGd = new GridData();
        new Label(composite, 0).setLayoutData((Object)spacerGd);
        spacerGd.horizontalSpan = hrzSpan;
    }

    private boolean error(Throwable t) {
        GreenPepperEclipsePlugin.logInternalError(t);
        String msg = t instanceof GreenPepperServerException ? GreenPepperMessages.getText(((GreenPepperServerException)t).getId()) : (StringUtil.isEmpty((String)t.getMessage()) ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_server_systemerror) : t.getMessage());
        this.errorLabel.setText(msg);
        this.errorLabel.redraw();
        return false;
    }

    class LocalRepositoryItemLabelProvider
    extends LabelProvider
    implements ITableLabelProvider {
        LocalRepositoryItemLabelProvider() {
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = "";
            if (!(element instanceof EclipseRepository)) {
                return result;
            }
            EclipseRepository repo = (EclipseRepository)element;
            switch (columnIndex) {
                case 0: {
                    result = repo.getBaseTestUrl();
                    break;
                }
            }
            return result;
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

}

