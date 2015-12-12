/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.debug.core.ILaunchConfiguration
 */
package com.greenpepper.eclipse.views;

import com.greenpepper.eclipse.views.EclipseSpecification;
import org.eclipse.debug.core.ILaunchConfiguration;

public class ErrorNode
extends EclipseSpecification {
    private String error;

    public ErrorNode(String name, String error) {
        super(name, false, false, false);
        this.error = error;
    }

    public String toString() {
        return this.error;
    }

    protected String getImagePath() {
        return "icons/error.gif";
    }

    public void clean(boolean andChildren) {
    }

    public void reset(boolean andChildren) {
    }

    public ILaunchConfiguration toLaunchConfiguration() {
        return null;
    }

    public void run() {
    }
}

