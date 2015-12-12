/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.debug.core.ILaunchConfiguration
 */
package com.greenpepper.eclipse.views;

import com.greenpepper.eclipse.views.Node;
import org.eclipse.debug.core.ILaunchConfiguration;

public class RootNode
extends Node {
    public RootNode() {
        super("ROOT");
    }

    public boolean isOpenable() {
        return false;
    }

    public String toString() {
        return "ROOT";
    }

    public void run() {
    }

    public ILaunchConfiguration toLaunchConfiguration() {
        return null;
    }

    protected String getImagePath() {
        return null;
    }
}

