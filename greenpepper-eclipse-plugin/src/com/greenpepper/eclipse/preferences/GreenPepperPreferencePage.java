/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.GreenPepperServerException
 *  org.eclipse.jface.preference.FieldEditor
 *  org.eclipse.jface.preference.FieldEditorPreferencePage
 *  org.eclipse.jface.preference.IPreferenceStore
 *  org.eclipse.jface.preference.StringFieldEditor
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Font
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.ui.IWorkbench
 *  org.eclipse.ui.IWorkbenchPreferencePage
 */
package com.greenpepper.eclipse.preferences;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.rpc.xmlrpc.EclipseXmlRpcGreenpepperClient;
import com.greenpepper.server.GreenPepperServerException;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class GreenPepperPreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage,
SelectionListener {
    private Button testButton;
    private Label resultLabel;
    private StringFieldEditor urlEditor;
    private StringFieldEditor handlerEditor;

    public GreenPepperPreferencePage() {
        super(1);
        this.setPreferenceStore(GreenPepperEclipsePlugin.getDefault().getPreferenceStore());
    }

    public boolean performOk() {
        return super.performOk();
    }

    public void performApply() {
        super.performApply();
    }

    public void init(IWorkbench workbench) {
    }

    public void createFieldEditors() {
        Composite composite = new Composite(this.getFieldEditorParent(), 0);
        GridLayout layout = new GridLayout();
        composite.setLayout((Layout)layout);
        GridData data = new GridData(4);
        data.grabExcessHorizontalSpace = true;
        composite.setLayoutData((Object)data);
        this.addTitleSection(composite);
        this.addInfoHeaderSection(composite);
        this.addSpacer(composite);
        this.addXmlRpcFields(composite);
        this.addSpacer(composite);
        this.addTestSection(composite);
        this.addSpacer(composite);
        this.addResultSection(composite);
        this.testButton.addSelectionListener((SelectionListener)this);
    }

    private void addXmlRpcFields(Composite composite) {
        this.urlEditor = new StringFieldEditor("GREENPEPPER_URL", GreenPepperMessages.greenpepper_configuration_url, composite);
        this.handlerEditor = new StringFieldEditor("GREENPEPPER_HANDLER", GreenPepperMessages.greenpepper_configuration_handler, composite);
        this.addField((FieldEditor)this.urlEditor);
        this.addField((FieldEditor)this.handlerEditor);
    }

    private void addTitleSection(Composite composite) {
        GridData data = new GridData();
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;
        Label titleLabel = new Label(composite, 0);
        titleLabel.setFont(new Font((Device)Display.getDefault(), "", 11, 1));
        titleLabel.setForeground(new Color((Device)Display.getDefault(), 0, 102, 0));
        titleLabel.setText(GreenPepperMessages.greenpepper_configuration_title);
        titleLabel.setLayoutData((Object)data);
    }

    private void addInfoHeaderSection(Composite composite) {
        GridData data = new GridData();
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;
        Label infoLabel = new Label(composite, 64);
        infoLabel.setText(GreenPepperMessages.greenpepper_configuration_propertiesdesc);
        infoLabel.setLayoutData((Object)data);
    }

    private void addSpacer(Composite composite) {
        GridData spacerGd = new GridData();
        spacerGd.horizontalSpan = 2;
        new Label(composite, 0).setLayoutData((Object)spacerGd);
    }

    private void addTestSection(Composite composite) {
        new org.eclipse.swt.widgets.Label(composite, 16777232);
        this.testButton = new Button(composite, 131072);
        this.testButton.setText("  " + GreenPepperMessages.greenpepper_configuration_testconnection + "  ");
        GridData testGd = new GridData();
        testGd.horizontalAlignment = 3;
        testGd.horizontalSpan = 1;
        this.testButton.setLayoutData((Object)testGd);
    }

    private void addResultSection(Composite composite) {
        this.resultLabel = new Label(composite, 16777236);
        this.resultLabel.setFont(new Font((Device)Display.getDefault(), "", 12, 1));
        GridData resultGd = new GridData();
        resultGd.horizontalAlignment = 4;
        resultGd.horizontalSpan = 3;
        this.resultLabel.setLayoutData((Object)resultGd);
    }

    public void widgetSelected(SelectionEvent event) {
        try {
            EclipseXmlRpcGreenpepperClient gpUtil = new EclipseXmlRpcGreenpepperClient();
            gpUtil.testConnection(this.urlEditor.getStringValue(), this.handlerEditor.getStringValue());
            this.resultLabel.setFont(new Font((Device)Display.getDefault(), "", 12, 1));
            this.resultLabel.setForeground(new Color((Device)Display.getDefault(), 140, 192, 109));
            this.resultLabel.setText(GreenPepperMessages.greenpepper_configuration_connected);
        }
        catch (GreenPepperServerException e) {
            this.resultLabel.setFont(new Font((Device)Display.getDefault(), "", 10, 1));
            this.resultLabel.setForeground(new Color((Device)Display.getDefault(), 200, 0, 0));
            this.resultLabel.setText(GreenPepperMessages.getText(e.getId()));
        }
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }
}

