/*
 * Decompiled with CFR 0_100.
 * Could not load the following classes:
 * com.greenpepper.server.domain.Execution
 * com.greenpepper.server.domain.Repository
 * com.greenpepper.server.domain.RepositoryType
 * com.greenpepper.server.domain.Specification
 * org.eclipse.core.resources.IProject
 * org.eclipse.core.resources.IWorkspaceRoot
 * org.eclipse.core.resources.ResourcesPlugin
 * org.eclipse.core.runtime.CoreException
 * org.eclipse.core.runtime.IProgressMonitor
 * org.eclipse.core.runtime.IStatus
 * org.eclipse.core.runtime.NullProgressMonitor
 * org.eclipse.core.runtime.Platform
 * org.eclipse.core.runtime.Status
 * org.eclipse.core.runtime.jobs.ILock
 * org.eclipse.core.runtime.jobs.Job
 * org.eclipse.jdt.core.IJavaProject
 * org.eclipse.jdt.core.JavaCore
 * org.eclipse.jdt.internal.ui.JavaPlugin
 * org.eclipse.jdt.internal.ui.wizards.NewClassCreationWizard
 * org.eclipse.jdt.ui.wizards.NewClassWizardPage
 * org.eclipse.jface.action.Action
 * org.eclipse.jface.action.IAction
 * org.eclipse.jface.action.IContributionItem
 * org.eclipse.jface.action.IMenuListener
 * org.eclipse.jface.action.IMenuManager
 * org.eclipse.jface.action.IToolBarManager
 * org.eclipse.jface.action.MenuManager
 * org.eclipse.jface.action.Separator
 * org.eclipse.jface.dialogs.MessageDialog
 * org.eclipse.jface.preference.PreferenceDialog
 * org.eclipse.jface.resource.ImageDescriptor
 * org.eclipse.jface.viewers.DoubleClickEvent
 * org.eclipse.jface.viewers.IBaseLabelProvider
 * org.eclipse.jface.viewers.IContentProvider
 * org.eclipse.jface.viewers.IDoubleClickListener
 * org.eclipse.jface.viewers.ISelection
 * org.eclipse.jface.viewers.ISelectionChangedListener
 * org.eclipse.jface.viewers.ISelectionProvider
 * org.eclipse.jface.viewers.IStructuredContentProvider
 * org.eclipse.jface.viewers.IStructuredSelection
 * org.eclipse.jface.viewers.ITreeContentProvider
 * org.eclipse.jface.viewers.LabelProvider
 * org.eclipse.jface.viewers.SelectionChangedEvent
 * org.eclipse.jface.viewers.StructuredSelection
 * org.eclipse.jface.viewers.TreeSelection
 * org.eclipse.jface.viewers.TreeViewer
 * org.eclipse.jface.viewers.Viewer
 * org.eclipse.jface.window.IShellProvider
 * org.eclipse.jface.window.SameShellProvider
 * org.eclipse.jface.wizard.IWizard
 * org.eclipse.jface.wizard.WizardDialog
 * org.eclipse.swt.custom.SashForm
 * org.eclipse.swt.custom.ViewForm
 * org.eclipse.swt.graphics.Image
 * org.eclipse.swt.graphics.Point
 * org.eclipse.swt.layout.GridData
 * org.eclipse.swt.layout.GridLayout
 * org.eclipse.swt.widgets.Composite
 * org.eclipse.swt.widgets.Control
 * org.eclipse.swt.widgets.Display
 * org.eclipse.swt.widgets.Layout
 * org.eclipse.swt.widgets.Menu
 * org.eclipse.swt.widgets.Shell
 * org.eclipse.swt.widgets.Tree
 * org.eclipse.ui.IActionBars
 * org.eclipse.ui.IViewPart
 * org.eclipse.ui.IViewSite
 * org.eclipse.ui.IWorkbench
 * org.eclipse.ui.IWorkbenchPage
 * org.eclipse.ui.IWorkbenchPartSite
 * org.eclipse.ui.IWorkbenchWindow
 * org.eclipse.ui.IWorkbenchWindowActionDelegate
 * org.eclipse.ui.dialogs.PropertyDialogAction
 * org.eclipse.ui.part.PageBook
 * org.eclipse.ui.part.ViewPart
 * org.eclipse.ui.progress.IWorkbenchSiteProgressService
 * org.eclipse.ui.progress.UIJob
 */
package com.greenpepper.eclipse.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.NewClassCreationWizard;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.progress.UIJob;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.GreenPepperImages;
import com.greenpepper.eclipse.fixture.NewFixtureWizardPage;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.util.GreenPepperCounterPanel;
import com.greenpepper.eclipse.util.GreenPepperProgressBar;
import com.greenpepper.eclipse.util.IExecutionListener;

public class RepositoryView extends ViewPart implements IExecutionListener {

    static final int REFRESH_INTERVAL = 450;
    private TreeViewer viewer;
    private SpecificationsSelection specificationList;
    private RootNode invisibleRoot;
    private UpdateUIJob updateJob;
    private GreenPepperIsRunningJob gpIsRunningJob;
    private ILock gpIsRunningLock;
    private boolean isDisposed = false;
    private IToolBarManager toolBar;
    private IMenuManager viewMenu;
    private Action executeAction;
    private Action executeAllAction;
    private Action workingVersionAction;
    private Action allCurrentVersionAction;
    private Action implementedVersionAction;
    private Action allImplementedVersionAction;
    private Action tagVersionAction;
    private Action tagAllVersionAction;
    private Action openAction;
    private Action openRemoteAction;
    private Action refreshTreeAction;
    private Action doubleClickAction;
    private Action createFixtureAction;
    private Action showPropertiesAction;
    private Map<String, Action> actions = new HashMap<String, Action>();
    private GreenPepperCounterPanel counterPane;
    private GreenPepperProgressBar progressBar;
    private SashForm sashForm;
    private PageBook viewerbook;
    private boolean hasErrors = false;
    private int ticksDone = 0;
    private int maxTicks = 0;

    public TreeViewer getTree() {
        return this.viewer;
    }

    public RootNode getTreeRoot() {
        return this.invisibleRoot;
    }

    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout((Layout) gridLayout);
        this.configureToolBar();
        this.createProgressCountPanel(parent);
        this.createViewSection(parent);
        this.defineDoubleClickAction();
        this.initContextMenu();
    }

    public void setFocus() {
        this.viewer.getControl().setFocus();
    }

    public synchronized void dispose() {
        this.isDisposed = true;
    }

    private void configureToolBar() {
        IActionBars actionBars = this.getViewSite().getActionBars();
        this.toolBar = actionBars.getToolBarManager();
        this.viewMenu = actionBars.getMenuManager();
        this.createVersionSwitcherActions();
        this.createExecuteActions();
        this.createTagActions();
        this.createReloadAction();
        this.createOpenRemoteAction();
        this.createRefreshTreeAction();
        this.createFixtureAction();
        this.createShowPropertiesAction();
        this.toolBar.add((IAction) this.implementedVersionAction);
        this.toolBar.add((IAction) this.workingVersionAction);
        this.toolBar.add((IContributionItem) new Separator());
        this.toolBar.add((IAction) this.openAction);
        this.toolBar.add((IAction) this.openRemoteAction);
        this.toolBar.add((IAction) this.executeAction);
        this.toolBar.add((IContributionItem) new Separator());
        this.toolBar.add((IAction) this.tagVersionAction);
        this.toolBar.add((IContributionItem) new Separator());
        this.toolBar.add((IAction) this.refreshTreeAction);
        this.viewMenu.add((IAction) this.allImplementedVersionAction);
        this.viewMenu.add((IAction) this.allCurrentVersionAction);
        this.toolBar.add((IContributionItem) new Separator());
        this.viewMenu.add((IAction) this.executeAllAction);
        this.viewMenu.add((IContributionItem) new Separator());
        this.viewMenu.add((IAction) this.tagAllVersionAction);
        this.enableActions();
    }

    private void initContextMenu() {
        MenuManager menuMgr = new MenuManager("PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener((IMenuListener) new IMenuListener() {

            public void menuAboutToShow(IMenuManager popupMenu) {
                RepositoryView.this.fillPopupMenu(popupMenu);
            }
        });
        Menu menu = menuMgr.createContextMenu((Control) this.viewerbook);
        this.viewer.getTree().setMenu(menu);
    }

    private void createProgressCountPanel(Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        composite.setLayout((Layout) layout);
        this.counterPane = new GreenPepperCounterPanel(composite);
        this.counterPane.setLayoutData((Object) new GridData(768));
        this.progressBar = new GreenPepperProgressBar(composite);
        this.progressBar.setLayoutData((Object) new GridData(768));
        composite.setLayoutData((Object) new GridData(768));
    }

    private void createViewSection(Composite parent) {
        this.sashForm = new SashForm(parent, 512);
        this.sashForm.setLayoutData(new GridData(1808));

        ViewForm top = new ViewForm(this.sashForm, 0);

        Composite empty = new Composite(top, 0);
        empty.setLayout(new Layout() {

            protected void layout(Composite composite, boolean flushCache) {
            }

            protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
                return new Point(1, 1);
            }
        });
        top.setTopLeft(empty);
        this.viewerbook = new PageBook(top, 0);
        this.viewer = new TreeViewer(this.viewerbook, 770);
        this.specificationList = new SpecificationsSelection(this);
        this.viewer.setContentProvider(new ViewContentProvider());
        this.viewer.setLabelProvider(new ViewLabelProvider());
        this.viewer.setInput(getViewSite());
        this.viewer.setAutoExpandLevel(0);
        this.viewer.addSelectionChangedListener(this.specificationList);
        this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (RepositoryView.this.inSession()) {
                    return;
                }
                RepositoryView.this.enableActions();
            }
        });
        this.viewerbook.showPage(this.viewer.getTree());
        top.setContent(this.viewerbook);
    }

    private void createVersionSwitcherActions() {
        this.implementedVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_switchtoimplemented) {

            public void run() {
                RepositoryView.this.specificationList.switchVersion(false, false);
                RepositoryView.this.enableActions();
            }
        };
        this.implementedVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_switchtoimplemented_tooltip);
        this.implementedVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/implemented.png"));
        this.implementedVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/implemented_disabled.png"));
        this.implementedVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.SwitchToImplementedCopy.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.SwitchToImplementedCopy.action", this.implementedVersionAction);
        this.allImplementedVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_switchalltoimplemented) {

            public void run() {
                RepositoryView.this.specificationList.switchVersion(false, true);
                RepositoryView.this.enableActions();
            }
        };
        this.allImplementedVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_switchalltoimplemented_tooltip);
        this.allImplementedVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/implemented.png"));
        this.allImplementedVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/implemented_disabled.png"));
        this.allImplementedVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.SwitchAllToImplementedCopy.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.SwitchAllToImplementedCopy.action", this.allImplementedVersionAction);
        this.workingVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_switchtoworking) {

            public void run() {
                RepositoryView.this.specificationList.switchVersion(true, false);
                RepositoryView.this.enableActions();
            }
        };
        this.workingVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_switchtoworking_tooltip);
        this.workingVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/working.png"));
        this.workingVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/working_disabled.png"));
        this.workingVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.SwitchToWorkingCopy.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.SwitchToWorkingCopy.action", this.workingVersionAction);
        this.allCurrentVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_switchalltoworking) {

            public void run() {
                RepositoryView.this.specificationList.switchVersion(true, true);
                RepositoryView.this.enableActions();
            }
        };
        this.allCurrentVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_switchalltoworking_tooltip);
        this.allCurrentVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/working.png"));
        this.allCurrentVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/working_disabled.png"));
        this.allCurrentVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.SwitchAllToWorkingCopy.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.SwitchAllToWorkingCopy.action", this.allCurrentVersionAction);
    }

    private void createFixtureAction() {
        this.createFixtureAction = new Action(GreenPepperMessages.greenpepper_repoview_createfixture) {

            public void run() {
                RepositoryView.this.generateFixture();
            }
        };
        this.createFixtureAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/fixture.gif"));
        this.createFixtureAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_createfixture_tooltip);
        this.createFixtureAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.CreateFixture.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.CreateFixture.action", this.createFixtureAction);
    }

    private void createShowPropertiesAction() {
        this.showPropertiesAction = new Action(GreenPepperMessages.greenpepper_repoview_properties) {

            public void run() {
                RepositoryView.this.showProperties();
            }
        };
        this.showPropertiesAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_properties_tooltip);
        this.showPropertiesAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.ShowProjectProperties.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.ShowProjectProperties.action", this.showPropertiesAction);
    }

    private void createExecuteActions() {
        this.executeAction = new Action(GreenPepperMessages.greenpepper_repoview_execute) {

            public void run() {
                RepositoryView.this.executeSelection(false);
            }
        };
        this.executeAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_execute_tooltip);
        this.executeAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/document_execute_enabled.png"));
        this.executeAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/document_execute_disabled.png"));
        this.executeAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.ExecuteDocument.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.ExecuteDocument.action", this.executeAction);
        this.executeAllAction = new Action(GreenPepperMessages.greenpepper_repoview_executeall) {

            public void run() {
                RepositoryView.this.executeSelection(true);
            }
        };
        this.executeAllAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_executeall_tooltip);
        this.executeAllAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/branch_execute_enabled.png"));
        this.executeAllAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/branch_execute_disabled.png"));
        this.executeAllAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.ExecuteAllDocument.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.ExecuteAllDocument.action", this.executeAllAction);
    }

    private void createTagActions() {
        this.tagVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_tagimplemented) {

            public void run() {
                RepositoryView.this.specificationList.tagToImplementedVersion(false);
                RepositoryView.this.enableActions();
            }
        };
        this.tagVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_tagimplemented_tooltip);
        this.tagVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/tagimplemented.png"));
        this.tagVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/tagimplemented_disabled.png"));
        this.tagVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.TagDocumentAsImplemented.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.TagDocumentAsImplemented.action", this.tagVersionAction);
        this.tagAllVersionAction = new Action(GreenPepperMessages.greenpepper_repoview_tagallimplemented) {

            public void run() {
                RepositoryView.this.specificationList.tagToImplementedVersion(true);
                RepositoryView.this.enableActions();
            }
        };
        this.tagAllVersionAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_tagallimplemented_tooltip);
        this.tagAllVersionAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/tagimplemented.png"));
        this.tagAllVersionAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/tagimplemented_disabled.png"));
        this.tagAllVersionAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.TagAllDocumentAsImplemented.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.TagAllDocumentAsImplemented.action", this.tagAllVersionAction);
    }

    private void createReloadAction() {
        this.openAction = new Action(GreenPepperMessages.greenpepper_repoview_opendocument) {

            public void run() {
                RepositoryView.this.show(true);
            }
        };
        this.openAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_opendocument_tooltip);
        this.openAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/document_view_enabled.png"));
        this.openAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/document_view_disabled.png"));
        this.openAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.ReloadDocument.command");
    }

    private void createOpenRemoteAction() {
        this.openRemoteAction = new Action(GreenPepperMessages.greenpepper_repoview_openremotedocument) {

            public void run() {
                RepositoryView.this.showRemote();
            }
        };
        this.openRemoteAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_openremotedocument_tooltip);
        this.openRemoteAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/remotedocument_view_enabled.png"));
        this.openRemoteAction.setDisabledImageDescriptor(GreenPepperImages.getImageDescriptor("icons/remotedocument_view_disabled.png"));
        this.openRemoteAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.OpenRemoteDocument.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.OpenRemoteDocument.action", this.openRemoteAction);
    }

    private void createRefreshTreeAction() {
        this.refreshTreeAction = new Action(GreenPepperMessages.greenpepper_repoview_refreshtree) {

            public void run() {
                if (RepositoryView.this.inSession()) {
                    return;
                }
                RepositoryView.this.refreshTree();
            }
        };
        this.refreshTreeAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_refreshtree_tooltip);
        this.refreshTreeAction.setImageDescriptor(GreenPepperImages.getImageDescriptor("icons/refresh_tree.gif"));
        this.refreshTreeAction.setActionDefinitionId("com.greenpepper.eclipse.repositoryView.ReloadRepositories.command");
        this.actions.put("com.greenpepper.eclipse.repositoryView.ReloadRepositories.action", this.refreshTreeAction);
    }

    private void defineDoubleClickAction() {
        this.doubleClickAction = new Action() {

            public void run() {
                RepositoryView.this.show(false);
            }
        };
        this.viewer.addDoubleClickListener((IDoubleClickListener) new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                if (RepositoryView.this.inSession()) {
                    return;
                }
                RepositoryView.this.doubleClickAction.run();
            }
        });
        this.actions.put("com.greenpepper.eclipse.repositoryView.ReloadDocument.action", this.doubleClickAction);
    }

    public synchronized void executed(Node node) {
        EclipseSpecification spec = (EclipseSpecification) node;
        this.counterPane.registerResults(spec.getLastExecution());
        this.hasErrors = this.hasErrors ? this.hasErrors : spec.getLastExecution().hasFailed();
        ++this.ticksDone;
        if (!this.inSession()) {
            this.stopUpdateJobs();
        }
    }

    public void refreshTree() {
        ((ViewContentProvider) this.viewer.getContentProvider()).refresh();
    }

    private void show(boolean reload) {
        IStructuredSelection oSelection = (IStructuredSelection) this.viewer.getSelection();
        Node node = (Node) oSelection.getFirstElement();
        if (!(node instanceof EclipseSpecification)) {
            return;
        }
        try {
            ((EclipseSpecification) node).show(reload);
            this.viewer.refresh();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(GreenPepperMessages.greenpepper_server_filefailed).append(":   ").append(node.toString());
            if (e.getMessage() != null) {
                sb.append("\n\nCause:\n").append(e.getMessage());
            }
            MessageDialog.openInformation((Shell) this.viewer.getControl().getShell(), (String) "Error", (String) sb.toString());
            GreenPepperEclipsePlugin.logInternalError(e);
        }
    }

    private void showRemote() {
        IStructuredSelection oSelection = (IStructuredSelection) this.viewer.getSelection();
        Node node = (Node) oSelection.getFirstElement();
        if (!(node instanceof EclipseSpecification)) {
            return;
        }
        try {
            ((EclipseSpecification) node).showRemote();
            this.viewer.refresh();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(GreenPepperMessages.greenpepper_server_filefailed).append(":   ").append(node.toString());
            if (e.getMessage() != null) {
                sb.append("\n\nCause:\n").append(e.getMessage());
            }
            MessageDialog.openInformation((Shell) this.viewer.getControl().getShell(), (String) "Error", (String) sb.toString());
            GreenPepperEclipsePlugin.logInternalError(e);
        }
    }

    protected boolean inSession() {
        if (this.maxTicks != this.ticksDone) {
            return true;
        }
        return false;
    }

    private void generateFixture() {
        if (this.inSession()) {
            return;
        }
        IStructuredSelection oSelection = (IStructuredSelection) this.viewer.getSelection();
        EclipseSpecification specification = (EclipseSpecification) oSelection.getFirstElement();
        StructuredSelection project = new StructuredSelection(new Object[] {JavaCore.create((IProject) specification.getProjectNode().getIProject()).getJavaProject()});
        NewFixtureWizardPage wizPage = new NewFixtureWizardPage(specification);
        wizPage.init((IStructuredSelection) project);
        NewClassCreationWizard wiz = new NewClassCreationWizard((NewClassWizardPage) wizPage, true);
        wiz.setDefaultPageImageDescriptor(GreenPepperImages.getImageDescriptor("icons/new_fixture.png"));
        wiz.setWindowTitle(GreenPepperMessages.greenpepper_fixture_generation);
        wiz.init(JavaPlugin.getActivePage().getWorkbenchWindow().getWorkbench(), null);
        WizardDialog dialog = new WizardDialog(this.getSite().getWorkbenchWindow().getShell(), (IWizard) wiz);
        dialog.open();
    }

    private void showProperties() {
        if (inSession()) {
            return;
        }
        EclipseProject selectedProject = (EclipseProject) ((TreeSelection) this.viewer.getSelection()).getFirstElement();
        final ISelection selectedRealProject = new StructuredSelection(new Object[] {JavaCore.create(selectedProject.getIProject()).getJavaProject()});

        PropertyDialogAction dialog = new PropertyDialogAction(new SameShellProvider(getSite().getShell()), new ISelectionProvider() {

            public void addSelectionChangedListener(ISelectionChangedListener listener) {
            }

            public ISelection getSelection() {
                return selectedRealProject;
            }

            public void removeSelectionChangedListener(ISelectionChangedListener listener) {
            }

            public void setSelection(ISelection selection) {
            }
        });
        dialog.createDialog().open();
    }

    private synchronized void executeSelection(boolean includeChildren) {
        if (this.inSession()) {
            return;
        }
        this.hasErrors = false;
        this.ticksDone = 0;
        this.maxTicks = includeChildren ? this.specificationList.executionCount() : 1;
        this.counterPane.reset(this.maxTicks);
        if (!this.inSession()) {
            return;
        }
        this.postSyncProcessChanges();
        if (this.updateJob != null) {
            return;
        }
        this.specificationList.execute(includeChildren);
        this.gpIsRunningJob = new GreenPepperIsRunningJob("GP_WRAPPER_JOB");
        this.gpIsRunningLock = Platform.getJobManager().newLock();
        this.gpIsRunningLock.acquire();
        this.getProgressService().schedule((Job) this.gpIsRunningJob);
        this.updateJob = new UpdateUIJob("GP_JOB");
        this.updateJob.schedule(450);
    }

    private void stopUpdateJobs() {
        if (this.updateJob != null) {
            this.updateJob.stop();
            this.updateJob = null;
        }
        if (this.gpIsRunningJob != null && this.gpIsRunningLock != null) {
            this.gpIsRunningLock.release();
            this.gpIsRunningJob = null;
        }
        this.postSyncProcessChanges();
    }

    private void postSyncProcessChanges() {
        this.postSyncRunnable(new Runnable() {

            public void run() {
                RepositoryView.this.processChangesInUI();
            }
        });
    }

    private IWorkbenchSiteProgressService getProgressService() {
        Object siteService = this.getSite().getAdapter((Class) IWorkbenchSiteProgressService.class);
        if (siteService != null) {
            return (IWorkbenchSiteProgressService) siteService;
        }
        return null;
    }

    private void processChangesInUI() {
        if (this.sashForm.isDisposed()) {
            return;
        }
        this.viewer.refresh();
        this.progressBar.reset(this.hasErrors, false, this.ticksDone, this.maxTicks);
        this.counterPane.redrawLabels();
    }

    private void postSyncRunnable(Runnable r) {
        if (this.isDisposed || this.counterPane.isDisposed()) {
            return;
        }
        this.getViewSite().getShell().getDisplay().syncExec(r);
    }

    private void enableActions() {
        Node first = this.specificationList != null ? this.specificationList.first() : null;
        boolean cond = !this.inSession() && this.specificationList != null && this.specificationList.fromSingleSelection();
        this.implementedVersionAction.setEnabled(cond && first.isUsingCurrentVersion());
        this.workingVersionAction.setEnabled(cond && !first.isUsingCurrentVersion() && first.canBeImplemented());
        this.openAction.setEnabled(cond && first.isOpenable());
        this.openRemoteAction.setEnabled(cond && first.isOpenable() && !first.getRepositoryNode().isLocal());
        this.syncOpenRemoteAction(first);
        this.executeAction.setEnabled(cond && first.isExecutable());
        this.tagVersionAction.setEnabled(cond && first.isUsingCurrentVersion());
        this.refreshTreeAction.setEnabled(true);
        cond = !this.inSession() && this.specificationList != null
                && (!this.specificationList.fromSingleSelection() || first instanceof EclipseProject || first instanceof EclipseRepository || first.hasChildren());
        this.allImplementedVersionAction.setEnabled(cond && this.specificationList.hasCurrentVersionsUsed());
        this.allCurrentVersionAction.setEnabled(cond && this.specificationList.hasCurrentVersionsUnused());
        this.executeAllAction.setEnabled(cond && this.specificationList.hasExecutableSpecs());
        this.tagAllVersionAction.setEnabled(cond && this.specificationList.hasCurrentVersionsUsed());
    }

    private void fillPopupMenu(IMenuManager popupMenu) {
        if (this.inSession()) {
            return;
        }
        popupMenu.removeAll();
        if (!this.specificationList.isValid()) {
            return;
        }
        if (this.specificationList.fromSingleSelection()) {
            this.fillSingleNodeMenu(popupMenu, this.specificationList.first());
        } else {
            this.fillMultipleMenu(popupMenu);
        }
    }

    private void fillSingleNodeMenu(IMenuManager popupMenu, Node node) {
        if (node instanceof ErrorNode) {
            return;
        }
        if (node instanceof EclipseProject || node instanceof EclipseRepository) {
            this.fillMultipleMenu(popupMenu);
            if (node instanceof EclipseProject) {
                popupMenu.add((IContributionItem) new Separator());
                popupMenu.add((IAction) this.showPropertiesAction);
                popupMenu.add((IContributionItem) new Separator());
            }
        } else {
            EclipseSpecification spec = (EclipseSpecification) node;
            if (spec.isUsingCurrentVersion()) {
                popupMenu.add((IAction) this.implementedVersionAction);
                popupMenu.add((IContributionItem) new Separator());
            } else if (spec.canBeImplemented()) {
                popupMenu.add((IAction) this.workingVersionAction);
                popupMenu.add((IContributionItem) new Separator());
            }
            if (spec.isExecutable()) {
                popupMenu.add((IAction) this.executeAction);
            }
            popupMenu.add((IAction) this.openAction);
            if (!spec.getRepositoryNode().isLocal()) {
                popupMenu.add((IAction) this.openRemoteAction);
            }
            if (spec.isExecutable()) {
                popupMenu.add((IContributionItem) new Separator());
                popupMenu.add((IAction) this.createFixtureAction);
            }
            if (spec.isExecutable() && spec.isUsingCurrentVersion()) {
                popupMenu.add((IContributionItem) new Separator());
                popupMenu.add((IAction) this.tagVersionAction);
            }
            if (spec.hasChildren()) {
                MenuManager subMenu = new MenuManager(GreenPepperMessages.greenpepper_repoview_branchmenu);
                this.fillMultipleMenu((IMenuManager) subMenu);
                popupMenu.add((IContributionItem) new Separator());
                popupMenu.add((IContributionItem) subMenu);
            }
        }
    }

    private void fillMultipleMenu(IMenuManager popupMenu) {
        boolean hasCurrentVersionsUsed = this.specificationList.hasCurrentVersionsUsed();
        boolean hasCurrentVersionsUnused = this.specificationList.hasCurrentVersionsUnused();
        boolean hasExecutableSpecs = this.specificationList.hasExecutableSpecs();
        if (hasCurrentVersionsUsed) {
            popupMenu.add((IAction) this.allImplementedVersionAction);
        }
        if (hasCurrentVersionsUnused) {
            popupMenu.add((IAction) this.allCurrentVersionAction);
        }
        if (hasCurrentVersionsUsed || hasCurrentVersionsUnused) {
            popupMenu.add((IContributionItem) new Separator());
        }
        if (hasExecutableSpecs) {
            popupMenu.add((IAction) this.executeAllAction);
        }
        if (hasExecutableSpecs) {
            popupMenu.add((IContributionItem) new Separator());
        }
        if (hasCurrentVersionsUsed) {
            popupMenu.add((IAction) this.tagAllVersionAction);
        }
    }

    private void syncOpenRemoteAction(Node node) {
        EclipseSpecification spec;
        this.openRemoteAction.setText(GreenPepperMessages.greenpepper_repoview_openremotedocument);
        this.openRemoteAction.setToolTipText(GreenPepperMessages.greenpepper_repoview_openremotedocument_tooltip);
        if (node instanceof EclipseSpecification && !(spec = (EclipseSpecification) node).getRepositoryNode().isLocal()) {
            String target = this.capitalize(spec.getSpecification().getRepository().getType().getName());
            this.openRemoteAction.setText(String.format("%s %s", GreenPepperMessages.greenpepper_repoview_openremotedocument_in, target));
            this.openRemoteAction.setToolTipText(String.format("%s %s", GreenPepperMessages.greenpepper_repoview_openremotedocument_in_tooltip, target));
        }
    }

    private String capitalize(String text) {
        StringBuilder buf = new StringBuilder(text.length());
        buf.append(text.substring(0, 1).toUpperCase());
        buf.append(text.substring(1).toLowerCase());
        return buf.toString();
    }

    static /* synthetic */ void access$1(RepositoryView repositoryView, RootNode rootNode) {
        repositoryView.invisibleRoot = rootNode;
    }

    private class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

        private ViewContentProvider() {
        }

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent.equals(RepositoryView.this.getViewSite())) {
                if (RepositoryView.this.invisibleRoot == null) {
                    RepositoryView.this.invisibleRoot = new RootNode();
                    loadProjectsHierarchyIntoTree();
                }
                return getChildren(RepositoryView.this.invisibleRoot);
            }
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            if ((child instanceof Node)) {
                return ((Node) child).getParent();
            }
            return null;
        }

        public Object[] getChildren(Object parent) {
            return ((Node) parent).getChildren();
        }

        public boolean hasChildren(Object parent) {
            return ((Node) parent).hasChildren();
        }

        public void refresh() {
            Node[] arrayOfNode;
            int j = (arrayOfNode = RepositoryView.this.invisibleRoot.getChildren()).length;
            for (int i = 0; i < j; i++) {
                Node child = arrayOfNode[i];
                try {
                    child.getProjectNode().getIProject().refreshLocal(1, new NullProgressMonitor());
                } catch (CoreException e) {
                    GreenPepperEclipsePlugin.logInternalError(e);
                }
                RepositoryView.this.invisibleRoot.removeChild(child);
            }
            RepositoryView.this.viewer.remove(RepositoryView.this.invisibleRoot);
            loadProjectsHierarchyIntoTree();
            RepositoryView.this.viewer.setSelection(null);
            RepositoryView.this.viewer.refresh();
        }

        private void loadProjectsHierarchyIntoTree() {
            try {
                IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
                IProject[] allProjects = wsRoot.getProjects();
                IProject[] arrayOfIProject1;
                int j = (arrayOfIProject1 = allProjects).length;
                for (int i = 0; i < j; i++) {
                    IProject project = arrayOfIProject1[i];
                    if ((project.isOpen()) && (project.getProject().hasNature("com.greenpepper.eclipse.nature"))) {
                        EclipseProject node = new EclipseProject(project.getProject());

                        RepositoryView.this.invisibleRoot.addChild(node);
                    }
                }
            } catch (Exception jme) {
                GreenPepperEclipsePlugin.logInternalError(jme);
            }
        }
    }

    private class ViewLabelProvider extends LabelProvider {

        private ViewLabelProvider() {
        }

        public String getText(Object obj) {
            return obj.toString();
        }

        public Image getImage(Object obj) {
            return ((Node) obj).getImage();
        }
    }

    private class UpdateUIJob extends UIJob {

        private boolean running = true;

        public UpdateUIJob(String name) {
            super(name);
            setSystem(true);
        }

        public IStatus runInUIThread(IProgressMonitor monitor) {
            if ((!RepositoryView.this.isDisposed) && (!RepositoryView.this.counterPane.isDisposed())) {
                RepositoryView.this.processChangesInUI();
            }
            schedule(450L);
            return Status.OK_STATUS;
        }

        public void stop() {
            this.running = false;
        }

        public boolean shouldSchedule() {
            return this.running;
        }
    }

    private class GreenPepperIsRunningJob extends Job {

        public GreenPepperIsRunningJob(String name) {
            super(name);
            setSystem(true);
        }

        public IStatus run(IProgressMonitor monitor) {
            RepositoryView.this.gpIsRunningLock.acquire();
            return Status.OK_STATUS;
        }

        public boolean belongsTo(Object family) {
            return family == "GREENPEPPER";
        }
    }

    public static class RepositoryViewActionDelegate implements IWorkbenchWindowActionDelegate {

        private IWorkbenchWindow window;
        private RepositoryView view;

        public void dispose() {
            this.window = null;
        }

        public void init(IWorkbenchWindow window) {
            this.window = window;
        }

        public void run(IAction action) {
            if (getView() != null) {
                Action actionToRun = (Action) getView().actions.get(action.getId());
                if ((actionToRun != null) && (actionToRun.isEnabled())) {
                    actionToRun.run();
                }
            }
        }

        public void selectionChanged(IAction action, ISelection selection) {
        }

        private RepositoryView getView() {
            if (this.view == null) {
                this.view = ((RepositoryView) this.window.getActivePage().findView("com.greenpepper.eclipse.views.RepositoryView"));
            }
            return this.view;
        }
    }

}
