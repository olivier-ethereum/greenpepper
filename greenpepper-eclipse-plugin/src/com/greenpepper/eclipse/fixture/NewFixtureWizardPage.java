/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.core.runtime.IStatus
 *  org.eclipse.core.runtime.Status
 *  org.eclipse.jdt.core.IField
 *  org.eclipse.jdt.core.IJavaElement
 *  org.eclipse.jdt.core.IMethod
 *  org.eclipse.jdt.core.IType
 *  org.eclipse.jdt.ui.wizards.NewClassWizardPage
 *  org.eclipse.jdt.ui.wizards.NewTypeWizardPage
 *  org.eclipse.jdt.ui.wizards.NewTypeWizardPage$ImportsManager
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Font
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 */
package com.greenpepper.eclipse.fixture;

import com.greenpepper.eclipse.fixture.Constructor;
import com.greenpepper.eclipse.fixture.Method;
import com.greenpepper.eclipse.fixture.Property;
import com.greenpepper.eclipse.fixture.SpyFixture;
import com.greenpepper.eclipse.views.EclipseSpecification;
import java.util.HashMap;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

public class NewFixtureWizardPage
extends NewClassWizardPage {
    private final EclipseSpecification specification;
    private Combo fTypeNameDialogField;
    private String typeName;

    public NewFixtureWizardPage(EclipseSpecification specification) {
        this.setTitle("New Fixture class");
        this.setDescription("Create a new Fixture class");
        this.specification = specification;
    }

    protected IStatus typeNameChanged() {
        return Status.OK_STATUS;
    }

    protected void createTypeNameControls(Composite composite, int nColumns) {
        if (this.specification.getFixtures().isEmpty()) {
            this.error(composite, nColumns);
        } else {
            this.createTypeNameSection(composite, nColumns);
        }
    }

    public String getTypeName() {
        return this.typeName;
    }

    protected void createTypeMembers(IType type, NewTypeWizardPage.ImportsManager imports, IProgressMonitor monitor) throws CoreException {
        try {
            SpyFixture fixture = this.specification.getFixtures().get(this.typeName);
            for (Constructor constructor : fixture.getConstructors()) {
                type.createMethod(constructor.toCode(), null, true, monitor);
            }
            for (Method method : fixture.getMethods()) {
                type.createMethod(method.toCode(), null, true, monitor);
            }
            for (Property property : fixture.getProperties()) {
                type.createField(property.toCode(), null, true, monitor);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTypeNameSection(Composite composite, int nColumns) {
        Label label = new Label(composite, 0);
        GridData gd = new GridData(256);
        gd.horizontalSpan = 1;
        label.setLayoutData((Object)gd);
        label.setText("Fixture Name:");
        this.fTypeNameDialogField = new Combo(composite, 8);
        GridData gd2 = new GridData();
        gd2.grabExcessHorizontalSpace = false;
        gd2.horizontalSpan = nColumns - 1;
        this.fTypeNameDialogField.setLayoutData((Object)gd2);
        this.fTypeNameDialogField.setLayout((Layout)new GridLayout(nColumns, false));
        try {
            for (String name : this.specification.getFixtures().keySet()) {
                this.fTypeNameDialogField.add(name);
            }
            this.fTypeNameDialogField.select(0);
            this.typeName = this.fTypeNameDialogField.getText();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.fTypeNameDialogField.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                NewFixtureWizardPage.access$1(NewFixtureWizardPage.this, NewFixtureWizardPage.this.fTypeNameDialogField.getText());
            }
        });
    }

    private void error(Composite composite, int nColumns) {
        this.updateStatus(Status.CANCEL_STATUS);
        Label space = new Label(composite, 0);
        GridData gd = new GridData(256);
        gd.horizontalSpan = 1;
        space.setLayoutData((Object)gd);
        Label label = new Label(composite, 0);
        gd = new GridData(256);
        gd.horizontalSpan = nColumns - 1;
        label.setLayoutData((Object)gd);
        label.setText("NO FIXTURE TO CREATE !");
        label.setFont(new Font((Device)Display.getDefault(), "", 12, 1));
        label.setForeground(new Color((Device)Display.getDefault(), 200, 0, 0));
        this.setErrorMessage("NO FIXTURE TO CREATE !");
        this.typeName = "NO FIXTURE TO CREATE !";
    }

    static /* synthetic */ void access$1(NewFixtureWizardPage newFixtureWizardPage, String string) {
        newFixtureWizardPage.typeName = string;
    }

}

