/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.runtime.IStatus
 *  org.eclipse.core.runtime.Status
 *  org.eclipse.jface.action.IAction
 *  org.eclipse.ui.IViewPart
 *  org.eclipse.ui.IWorkbenchPage
 *  org.eclipse.ui.IWorkbenchWindow
 *  org.eclipse.ui.IWorkbenchWindowActionDelegate
 *  org.eclipse.ui.PlatformUI
 *  org.eclipse.ui.actions.ActionDelegate
 */
package com.greenpepper.eclipse.actions;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

public class RepositoryViewAction
extends ActionDelegate
implements IWorkbenchWindowActionDelegate {
    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return;
        }
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        if (activePage == null) {
            return;
        }
        try {
            activePage.showView("com.greenpepper.eclipse.views.RepositoryView");
        }
        catch (Exception e) {
            GreenPepperEclipsePlugin.log((IStatus)new Status(1, "com.greenpepper.eclipse", 4, GreenPepperMessages.greenpepper_server_generalexeerror, (Throwable)e));
        }
    }
}

