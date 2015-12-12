/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.resources.IFile
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.core.runtime.IStatus
 *  org.eclipse.core.runtime.Status
 *  org.eclipse.ui.IWorkbench
 *  org.eclipse.ui.PlatformUI
 *  org.eclipse.ui.browser.IWebBrowser
 *  org.eclipse.ui.browser.IWorkbenchBrowserSupport
 *  org.eclipse.ui.progress.UIJob
 */
package com.greenpepper.eclipse.util;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import java.net.URI;
import java.net.URL;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.progress.UIJob;

public class OpenInEditorJob
extends UIJob {
    private final IFile ifile;

    public OpenInEditorJob(String name, IFile ifile) {
        super(name);
        this.ifile = ifile;
    }

    public IStatus runInUIThread(IProgressMonitor monitor) {
        try {
            IWorkbench workbench = PlatformUI.getWorkbench();
            IWorkbenchBrowserSupport browserSupport = workbench.getBrowserSupport();
            String id = this.ifile.getRawLocationURI().getPath();
            IWebBrowser browser = browserSupport.createBrowser(32, id, this.getName(), this.getName());
            browser.openURL(this.ifile.getRawLocationURI().toURL());
        }
        catch (Exception ex) {
            GreenPepperEclipsePlugin.logInternalError(ex);
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }
}

