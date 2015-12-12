/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup
 *  org.eclipse.debug.ui.CommonTab
 *  org.eclipse.debug.ui.ILaunchConfigurationDialog
 *  org.eclipse.debug.ui.ILaunchConfigurationTab
 *  org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab
 *  org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab
 *  org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab
 */
package com.greenpepper.eclipse.launcher;

import com.greenpepper.eclipse.launcher.MainTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

public class RunSpecificationTabGroup
extends AbstractLaunchConfigurationTabGroup {
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[]{new MainTab(), new JavaArgumentsTab(), new JavaJRETab(), new JavaClasspathTab(), new CommonTab()};
        this.setTabs(tabs);
    }
}

