/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.GreenPepperServerException
 *  com.greenpepper.util.StringUtil
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.resources.IProjectNature
 *  org.eclipse.core.resources.IResource
 *  org.eclipse.core.runtime.IAdaptable
 *  org.eclipse.core.runtime.QualifiedName
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Font
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 *  org.eclipse.ui.dialogs.PropertyPage
 */
package com.greenpepper.eclipse.properties;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.util.StringUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class AdvancedPropertyPage
extends PropertyPage {
    private Text factoryClassInput;
    private Text factoryArgsInput;
    private Text vmArgsInput;
    private Text additionalArgsInput;
    private Label errorLabel;

    public void performApply() {
        this.performOk();
    }

    public boolean performOk() {
        try {
            IResource resource = (IResource)this.getElement().getAdapter((Class)IResource.class);
            resource.setPersistentProperty(GreenPepperEclipsePlugin.FACTORY_CLASS, this.factoryClassInput.getText().trim());
            resource.setPersistentProperty(GreenPepperEclipsePlugin.FACTORY_ARGS, this.factoryArgsInput.getText().trim());
            resource.setPersistentProperty(GreenPepperEclipsePlugin.VM_ARGS, this.vmArgsInput.getText().trim());
            resource.setPersistentProperty(GreenPepperEclipsePlugin.ADDITIONAL_ARGS, this.additionalArgsInput.getText().trim());
            return true;
        }
        catch (Exception e) {
            return this.error(e);
        }
    }

    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout((Layout)layout);
        composite.setLayoutData((Object)new GridData());
        this.addFactoryClassSection(composite);
        this.addFactoryArgsSection(composite);
        this.addSeparator(composite);
        this.addAdditionalArgsSection(composite);
        this.addSeparator(composite);
        this.addVMArgsSection(composite);
        this.addErrorSection(composite);
        this.initInputs();
        return composite;
    }

    private void addFactoryClassSection(Composite parent) {
        Label titleLabel = new Label(parent, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_factoryclasstitle);
        GridData titleData = new GridData(0);
        titleData.horizontalSpan = 2;
        titleLabel.setLayoutData((Object)titleData);
        Label descLabel = new Label(parent, 64);
        descLabel.setText(GreenPepperMessages.greenpepper_project_factoryclassdesc);
        GridData descData = new GridData(0);
        descData.horizontalSpan = 2;
        descLabel.setLayoutData((Object)descData);
        Label factoryClassLabel = new Label(parent, 16448);
        factoryClassLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        factoryClassLabel.setText(GreenPepperMessages.greenpepper_project_factoryclass);
        this.factoryClassInput = new Text(parent, 2052);
        this.factoryClassInput.setLayoutData((Object)new GridData(4, 0, false, false));
    }

    private void addFactoryArgsSection(Composite parent) {
        Label factoryArgsLabel = new Label(parent, 16448);
        factoryArgsLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        factoryArgsLabel.setText(GreenPepperMessages.greenpepper_project_factoryargs);
        this.factoryArgsInput = new Text(parent, 2052);
        this.factoryArgsInput.setLayoutData((Object)new GridData(4, 0, false, false));
    }

    private void addVMArgsSection(Composite parent) {
        Label titleLabel = new Label(parent, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_vmargstitle);
        GridData titleData = new GridData(0);
        titleData.horizontalSpan = 2;
        titleLabel.setLayoutData((Object)titleData);
        Label descLabel = new Label(parent, 64);
        descLabel.setText(GreenPepperMessages.greenpepper_project_vmargsdesc);
        GridData descData = new GridData(0);
        descData.horizontalSpan = 2;
        descLabel.setLayoutData((Object)descData);
        Label vmArgsLabel = new Label(parent, 16448);
        vmArgsLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        vmArgsLabel.setText(GreenPepperMessages.greenpepper_project_vmargs);
        this.vmArgsInput = new Text(parent, 2052);
        this.vmArgsInput.setLayoutData((Object)new GridData(4, 0, false, false));
    }

    private void addAdditionalArgsSection(Composite parent) {
        Label titleLabel = new Label(parent, 64);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_project_additionalargstitle);
        GridData titleData = new GridData(0);
        titleData.horizontalSpan = 2;
        titleLabel.setLayoutData((Object)titleData);
        Label descLabel = new Label(parent, 64);
        descLabel.setText(GreenPepperMessages.greenpepper_project_additionalargsdesc);
        GridData descData = new GridData(0);
        descData.horizontalSpan = 2;
        descLabel.setLayoutData((Object)descData);
        Label additionalArgsLabel = new Label(parent, 16448);
        additionalArgsLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        additionalArgsLabel.setText(GreenPepperMessages.greenpepper_project_additionalargs);
        this.additionalArgsInput = new Text(parent, 2052);
        this.additionalArgsInput.setLayoutData((Object)new GridData(4, 0, false, false));
    }

    private void addErrorSection(Composite composite) {
        GridData errorData = new GridData(0);
        errorData.horizontalSpan = 2;
        this.errorLabel = new Label(composite, 64);
        this.errorLabel.setFont(new Font((Device)Display.getDefault(), "", 9, 1));
        this.errorLabel.setForeground(new Color((Device)Display.getDefault(), 200, 0, 0));
        this.errorLabel.setText("\t\t\t\t\t\t\t");
        this.errorLabel.setLayoutData((Object)errorData);
    }

    private void addSeparator(Composite parent) {
        GridData data = new GridData(0);
        data.horizontalSpan = 2;
        Label separator = new Label(parent, 0);
        separator.setLayoutData((Object)data);
    }

    private void initInputs() {
        boolean isPHPNatureActive = this.isProjectHasPHPNature();
        String factoryClassValue = GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.FACTORY_CLASS);
        if (factoryClassValue == null && isPHPNatureActive) {
            factoryClassValue = "com.greenpepper.phpsud.PHPSud";
        }
        this.factoryClassInput.setText(StringUtil.toEmptyIfNull((String)factoryClassValue));
        String factoryArgs = GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.FACTORY_ARGS);
        if (factoryArgs == null && isPHPNatureActive) {
            factoryArgs = "default;.;init.php";
        }
        this.factoryArgsInput.setText(StringUtil.toEmptyIfNull((String)factoryArgs));
        this.additionalArgsInput.setText(StringUtil.toEmptyIfNull((String)GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.ADDITIONAL_ARGS)));
        this.vmArgsInput.setText(StringUtil.toEmptyIfNull((String)GreenPepperEclipsePlugin.getEclipseProjectValue(this.getElement(), GreenPepperEclipsePlugin.VM_ARGS)));
    }

    private boolean error(Throwable t) {
        GreenPepperEclipsePlugin.logInternalError(t);
        String msg = t instanceof GreenPepperServerException ? GreenPepperMessages.getText(((GreenPepperServerException)t).getId()) : (StringUtil.isEmpty((String)t.getMessage()) ? GreenPepperMessages.getText(GreenPepperMessages.greenpepper_server_systemerror) : t.getMessage());
        this.errorLabel.setText(msg);
        this.errorLabel.redraw();
        return false;
    }

    private boolean isProjectHasPHPNature() {
        try {
            if (this.getElement() instanceof IProject) {
                IProject project = (IProject)this.getElement();
                IProjectNature nature = project.getNature("org.eclipse.php.core.PHPNature");
                if (nature != null) {
                    return true;
                }
                return false;
            }
        }
        catch (Exception ex) {
            GreenPepperEclipsePlugin.logInternalError(ex);
        }
        return false;
    }
}

