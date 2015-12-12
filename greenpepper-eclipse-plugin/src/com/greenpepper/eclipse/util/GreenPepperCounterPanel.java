/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.domain.Execution
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.greenpepper.eclipse.util;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

import com.greenpepper.eclipse.GreenPepperImages;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.server.domain.Execution;

public class GreenPepperCounterPanel
extends Composite {
    protected Text numberOfSuccess;
    protected Text numberOfFailures;
    protected Text numberOfErrors;
    protected Text numberOfRuns;
    protected int total = 0;
    protected int runned = 0;
    protected int success = 0;
    protected int failures = 0;
    protected int errors = 0;
    private Image successImage = GreenPepperImages.createImage("icons/greenpepper_12.gif");
    private Image failureImage = GreenPepperImages.createImage("icons/redpepper_12.gif");
    private Image errorImage = GreenPepperImages.createImage("icons/redpepper_12.gif");

    public GreenPepperCounterPanel(Composite parent) {
        super(parent, 64);
        GridLayout gridLayout = new GridLayout(9, false);
        gridLayout.marginWidth = 0;
        this.setLayout((Layout)gridLayout);
        this.numberOfSuccess = this.createLabel(GreenPepperMessages.greenpepper_counter_success, this.successImage, " 0 ");
        this.numberOfFailures = this.createLabel(GreenPepperMessages.greenpepper_counter_failures, this.errorImage, " 0 ");
        this.numberOfErrors = this.createLabel(GreenPepperMessages.greenpepper_counter_errors, this.errorImage, " 0 ");
    }

    private Text createLabel(String name, Image image, String init) {
        Label label = new Label((Composite)this, 0);
        if (image != null) {
            image.setBackground(label.getBackground());
            label.setImage(image);
        }
        label.setLayoutData((Object)new GridData(32));
        label = new Label((Composite)this, 0);
        label.setText(name);
        label.setLayoutData((Object)new GridData(32));
        Text value = new Text((Composite)this, 8);
        value.setText(init);
        value.setBackground(this.getDisplay().getSystemColor(22));
        value.setLayoutData((Object)new GridData(800));
        return value;
    }

    public void reset(int total) {
        this.total = total;
        this.runned = 0;
        this.success = 0;
        this.failures = 0;
        this.errors = 0;
        this.redrawLabels();
    }

    public void registerResults(Execution execution) {
        ++this.runned;
        this.success+=execution.getSuccess();
        this.failures+=execution.getFailures();
        this.errors+=execution.getErrors();
        this.errors+=execution.hasException() ? 1 : 0;
    }

    public void redrawLabels() {
        this.numberOfSuccess.setText(Integer.toString(this.success));
        this.numberOfFailures.setText(Integer.toString(this.failures));
        this.numberOfErrors.setText(Integer.toString(this.errors));
        this.redraw();
    }
}

