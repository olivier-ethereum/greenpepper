/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IResource
 *  org.eclipse.core.resources.IWorkspaceRoot
 *  org.eclipse.core.resources.ResourcesPlugin
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.debug.core.ILaunchConfiguration
 *  org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
 *  org.eclipse.debug.ui.AbstractLaunchConfigurationTab
 *  org.eclipse.jdt.core.IJavaElement
 *  org.eclipse.jdt.core.IJavaModel
 *  org.eclipse.jdt.core.IJavaProject
 *  org.eclipse.jdt.core.JavaCore
 *  org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants
 *  org.eclipse.jdt.launching.IVMInstall
 *  org.eclipse.jdt.launching.IVMInstallType
 *  org.eclipse.jdt.launching.JavaRuntime
 *  org.eclipse.jdt.ui.JavaElementLabelProvider
 *  org.eclipse.jface.dialogs.Dialog
 *  org.eclipse.jface.viewers.ILabelProvider
 *  org.eclipse.jface.viewers.ISelection
 *  org.eclipse.jface.viewers.IStructuredSelection
 *  org.eclipse.swt.events.ModifyEvent
 *  org.eclipse.swt.events.ModifyListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
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
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 *  org.eclipse.ui.IEditorInput
 *  org.eclipse.ui.IEditorPart
 *  org.eclipse.ui.IWorkbench
 *  org.eclipse.ui.IWorkbenchPage
 *  org.eclipse.ui.IWorkbenchWindow
 *  org.eclipse.ui.dialogs.ElementListSelectionDialog
 */
package com.greenpepper.eclipse.launcher;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.launcher.RunSpecificationLaunchConfigurationDelegate;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class MainTab
extends AbstractLaunchConfigurationTab {
    private static final String EMPTY_STRING = "";
    private Text projectInput;
    private Text specsInput;
    private Text repoUidInput;
    private Text repoClassInput;
    private Text repoUrlInput;
    private Text username;
    private Text password;
    private Button workingVersion;
    private Button projectButton;
    private ModifyListener fBasicModifyListener;
    private SelectionAdapter fSelectionListener;

    public MainTab() {
        this.fBasicModifyListener = new ModifyListener(){

            public void modifyText(ModifyEvent evt) {
                MainTab.this.updateLaunchConfigurationDialog();
            }
        };
        this.fSelectionListener = new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                Object source = e.getSource();
                if (source == MainTab.this.projectButton) {
                    MainTab.this.handleProjectButtonSelected();
                }
                if (source == MainTab.this.workingVersion) {
                    MainTab.this.updateLaunchConfigurationDialog();
                }
            }
        };
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, 0);
        this.setControl((Control)composite);
        GridLayout topLayout = new GridLayout(3, false);
        topLayout.horizontalSpacing = 10;
        composite.setLayout((Layout)topLayout);
        composite.setFont(parent.getFont());
        this.addProjectSection(composite);
        this.addRepositorySection(composite);
        this.addSpecificationSection(composite);
        Dialog.applyDialogFont((Control)parent);
    }

    private void addProjectSection(Composite composite) {
        GridData data = new GridData();
        Label projLabel = new Label(composite, 16384);
        projLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        projLabel.setText(GreenPepperMessages.greenpepper_run_project);
        projLabel.setLayoutData((Object)data);
        this.projectInput = new Text(composite, 2052);
        this.projectInput.setLayoutData((Object)new GridData(768));
        this.projectInput.addModifyListener(this.fBasicModifyListener);
        this.projectButton = this.createPushButton(composite, GreenPepperMessages.greenpepper_run_projectselect, null);
        this.projectButton.addSelectionListener((SelectionListener)this.fSelectionListener);
    }

    private void addRepositorySection(Composite composite) {
        GridData data = new GridData(4, 0, false, false);
        data = new GridData(4, 0, true, false);
        data.horizontalSpan = 2;
        Label repoUidLabel = new Label(composite, 16384);
        repoUidLabel.setText(GreenPepperMessages.greenpepper_run_repouid);
        repoUidLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        repoUidLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.repoUidInput = new Text(composite, 2052);
        this.repoUidInput.addModifyListener(this.fBasicModifyListener);
        this.repoUidInput.setLayoutData((Object)data);
        Label repoUrlLabel = new Label(composite, 16384);
        repoUrlLabel.setText(GreenPepperMessages.greenpepper_run_repourl);
        repoUrlLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        repoUrlLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.repoUrlInput = new Text(composite, 2052);
        this.repoUrlInput.addModifyListener(this.fBasicModifyListener);
        this.repoUrlInput.setLayoutData((Object)data);
        Label repoClassLabel = new Label(composite, 16384);
        repoClassLabel.setText(GreenPepperMessages.greenpepper_run_repomainclass);
        repoClassLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        repoClassLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.repoClassInput = new Text(composite, 2052);
        this.repoClassInput.addModifyListener(this.fBasicModifyListener);
        this.repoClassInput.setLayoutData((Object)data);
        Label usernameLabel = new Label(composite, 16384);
        usernameLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        usernameLabel.setText(GreenPepperMessages.greenpepper_project_username);
        usernameLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.username = new Text(composite, 2052);
        this.username.addModifyListener(this.fBasicModifyListener);
        this.username.setLayoutData((Object)data);
        Label passwordLabel = new Label(composite, 16384);
        passwordLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        passwordLabel.setText(GreenPepperMessages.greenpepper_project_password);
        passwordLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.password = new Text(composite, 4196352);
        this.password.addModifyListener(this.fBasicModifyListener);
        this.password.setLayoutData((Object)data);
    }

    private void addSpecificationSection(Composite composite) {
        GridData data = new GridData(4, 0, false, false);
        data = new GridData(4, 0, true, false);
        data.horizontalSpan = 2;
        Label specsLabel = new Label(composite, 16384);
        specsLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        specsLabel.setText(GreenPepperMessages.greenpepper_run_specifications);
        specsLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.specsInput = new Text(composite, 2052);
        this.specsInput.addModifyListener(this.fBasicModifyListener);
        this.specsInput.setLayoutData((Object)data);
        Label workingVersionLabel = new Label(composite, 16384);
        workingVersionLabel.setFont(new Font((Device)Display.getDefault(), "", 8, 1));
        workingVersionLabel.setText(GreenPepperMessages.greenpepper_run_working);
        workingVersionLabel.setLayoutData((Object)new GridData(4, 0, false, false));
        this.workingVersion = new Button(composite, 32);
        this.workingVersion.addSelectionListener((SelectionListener)this.fSelectionListener);
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
            this.projectInput.setText(projectName);
            String specs = configuration.getAttribute("specificationName", "");
            this.specsInput.setText(specs);
            boolean isCurrent = Boolean.valueOf(configuration.getAttribute("workingversion", "false"));
            this.workingVersion.setSelection(isCurrent);
            String repoUid = configuration.getAttribute("repoUID", "");
            this.repoUidInput.setText(repoUid);
            String repoClass = configuration.getAttribute("repoClass", "");
            this.repoClassInput.setText(repoClass);
            String repoUrl = configuration.getAttribute("baseURL", "");
            this.repoUrlInput.setText(repoUrl);
            String user = configuration.getAttribute("username", "");
            this.username.setText(user);
            String pwd = configuration.getAttribute("password", "");
            this.password.setText(pwd);
        }
        catch (CoreException e) {
            GreenPepperEclipsePlugin.logInternalError((Throwable)e);
        }
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, this.projectInput.getText());
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, RunSpecificationLaunchConfigurationDelegate.GREENPEPPER_COMMAND_LINE_RUNNER);
        configuration.setAttribute("specificationName", this.specsInput.getText());
        configuration.setAttribute("workingversion", String.valueOf(this.workingVersion.getSelection()));
        configuration.setAttribute("repoUID", this.repoUidInput.getText());
        configuration.setAttribute("repoClass", this.repoClassInput.getText());
        configuration.setAttribute("baseURL", this.repoUrlInput.getText());
        configuration.setAttribute("username", this.username.getText());
        configuration.setAttribute("password", this.password.getText());
    }

    public String getName() {
        return GreenPepperMessages.greenpepper_run_maintab;
    }

    public boolean isValid(ILaunchConfiguration launchConfig) {
        this.setErrorMessage(null);
        this.setMessage(null);
        String name = this.projectInput.getText().trim();
        if (name.length() == 0) {
            this.setErrorMessage(GreenPepperMessages.greenpepper_run_projectselecttitle);
            return false;
        }
        if (!ResourcesPlugin.getWorkspace().getRoot().getProject(name).exists()) {
            this.setErrorMessage(GreenPepperMessages.greenpepper_run_projectnotfound);
            return false;
        }
        if (this.isEmpty(this.repoUrlInput)) {
            this.setErrorMessage(GreenPepperMessages.greenpepper_run_repourlmissing);
            return false;
        }
        if (this.isEmpty(this.repoClassInput)) {
            this.setErrorMessage(GreenPepperMessages.greenpepper_run_repoclassmissing);
            return false;
        }
        if (this.isEmpty(this.specsInput)) {
            this.setErrorMessage(GreenPepperMessages.greenpepper_run_specmissing);
            return false;
        }
        return true;
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        IJavaElement je = this.getContext();
        if (je == null) {
            this.initializeHardCodedDefaults(configuration);
        } else {
            this.initializeDefaults(je, configuration);
        }
    }

    private void initializeDefaults(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
        this.initializeJavaProject(javaElement, config);
        this.initializeMainTypeAndName(javaElement, config);
        this.initializeHardCodedDefaults(config);
    }

    private void initializeMainTypeAndName(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, RunSpecificationLaunchConfigurationDelegate.GREENPEPPER_COMMAND_LINE_RUNNER);
    }

    private void initializeDefaultVM(ILaunchConfigurationWorkingCopy config) {
        IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
        if (vmInstall == null) {
        	//TODO : correction pour que le projet compil avant false il y avait null dans les 2 méthodes setattribute
            config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME, false);
            config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE, false);
        } else {
            config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME, vmInstall.getName());
            config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE, vmInstall.getVMInstallType().getId());
        }
    }

    private void initializeHardCodedDefaults(ILaunchConfigurationWorkingCopy config) {
        this.initializeDefaultVM(config);
    }

    private void handleProjectButtonSelected() {
        IJavaProject project = this.chooseGreenPepperProject();
        if (project == null) {
            return;
        }
        String projectName = project.getElementName();
        this.projectInput.setText(projectName);
    }

    private IJavaProject chooseGreenPepperProject() {
        JavaElementLabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(this.getShell(), (ILabelProvider)labelProvider);
        dialog.setTitle(GreenPepperMessages.greenpepper_run_projectselecttitle);
        dialog.setMessage(GreenPepperMessages.greenpepper_run_projectselectmsg);
        dialog.setElements(GreenPepperEclipsePlugin.getGreenPepperProjects().toArray());
        IJavaProject javaProject = this.getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[]{javaProject});
        }
        if (dialog.open() == 0) {
            return (IJavaProject)dialog.getFirstResult();
        }
        return null;
    }

    private IJavaProject getJavaProject() {
        String projectName = this.projectInput.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return this.getJavaModel().getJavaProject(projectName);
    }

    private IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    private IJavaModel getJavaModel() {
        return JavaCore.create((IWorkspaceRoot)this.getWorkspaceRoot());
    }

    protected IJavaElement getContext() {
        IWorkbenchPage page = GreenPepperEclipsePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IEditorPart part;
            IStructuredSelection ss;
            ISelection selection = page.getSelection();
            if (selection instanceof IStructuredSelection && !(ss = (IStructuredSelection)selection).isEmpty()) {
                Object obj = ss.getFirstElement();
                if (obj instanceof IJavaElement) {
                    return (IJavaElement)obj;
                }
                if (obj instanceof IResource) {
                    IJavaElement je = JavaCore.create((IResource)((IResource)obj));
                    if (je == null) {
                        IProject pro = ((IResource)obj).getProject();
                        je = JavaCore.create((IProject)pro);
                    }
                    if (je != null) {
                        return je;
                    }
                }
            }
            if ((part = page.getActiveEditor()) != null) {
                IEditorInput input = part.getEditorInput();
                return (IJavaElement)input.getAdapter((Class)IJavaElement.class);
            }
        }
        return null;
    }

    protected void initializeJavaProject(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
        IJavaProject javaProject = javaElement.getJavaProject();
        String name = null;
        if (javaProject != null && javaProject.exists()) {
            name = javaProject.getElementName();
        }
        config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
    }

    private boolean isEmpty(Text input) {
        if (input.getText() != null && input.getText().equals("")) {
            return true;
        }
        return false;
    }

}

