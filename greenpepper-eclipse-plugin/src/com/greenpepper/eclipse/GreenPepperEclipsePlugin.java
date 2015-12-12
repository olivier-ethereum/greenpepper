package com.greenpepper.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.util.OpenInEditorJob;
import com.greenpepper.eclipse.views.RepositoryView;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class GreenPepperEclipsePlugin extends AbstractUIPlugin {
    public static final String ID = "com.greenpepper.eclipse";
    public static final QualifiedName VM_ARGS = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.VM_ARGS");
    public static final QualifiedName ADDITIONAL_ARGS = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.ADDITIONAL_ARGS");
    public static final QualifiedName CORE = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.CORE");
    public static final QualifiedName FACTORY_CLASS = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.FACTORY_CLASS");
    public static final QualifiedName FACTORY_ARGS = new QualifiedName("com.greenpepper.eclipse", "com.greenpepper.eclipse.properties.FACTORY_ARGS");
    public static final String PHP_NATURE_ID = "org.eclipse.php.core.PHPNature";
    private static GreenPepperEclipsePlugin plugin;

    public GreenPepperEclipsePlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static GreenPepperEclipsePlugin getDefault() {
        return plugin;
    }

    public static String getUniqueIdentifier() {
        return "com.greenpepper.eclipse";
    }

    public static void log(IStatus status) {
        GreenPepperEclipsePlugin.getDefault().getLog().log(status);
    }

    public static void logInternalError(Throwable e) {
        GreenPepperEclipsePlugin.log((IStatus)new Status(4, "com.greenpepper.eclipse", 150, GreenPepperMessages.greenpepper_server_generalexeerror, e));
    }

    public static void logError(String message, Throwable e) {
        GreenPepperEclipsePlugin.log((IStatus)new Status(4, "com.greenpepper.eclipse", 4, message, e));
    }

    public static void logWarning(String message) {
        GreenPepperEclipsePlugin.log((IStatus)new Status(2, "com.greenpepper.eclipse", 0, message, null));
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return GreenPepperEclipsePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    public static IWorkbenchPage getActivePage() {
        IWorkbenchWindow w = GreenPepperEclipsePlugin.getActiveWorkbenchWindow();
        if (w != null) {
            return w.getActivePage();
        }
        return null;
    }

    public static void openInEditor(String title, IFile file) throws PartInitException {
        new OpenInEditorJob(title, file).run((IProgressMonitor)new NullProgressMonitor());
    }

    public static List<IProject> getGreenPepperProjects() {
        ArrayList<IProject> projects = new ArrayList<IProject>();
        try {
            IProject[] allProjects;
            for (IProject aProject : allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
                if (!aProject.isOpen() || !aProject.getProject().hasNature("com.greenpepper.eclipse.nature")) continue;
                projects.add(aProject);
            }
        }
        catch (Exception jme) {
            GreenPepperEclipsePlugin.logInternalError(jme);
        }
        return projects;
    }

    public static String getEclipseProjectValue(IAdaptable element, QualifiedName name) {
        try {
            IResource resource = (IResource)element.getAdapter((Class)IResource.class);
            return resource.getPersistentProperty(name);
        }
        catch (CoreException e) {
            GreenPepperEclipsePlugin.logInternalError((Throwable)e);
            return null;
        }
    }

    public static boolean isLazyMode(IProject javaProject) {
        try {
            String additionalArgs = javaProject.getProject().getPersistentProperty(ADDITIONAL_ARGS);
            if (additionalArgs != null && additionalArgs.matches("--lazy")) {
                return true;
            }
            return false;
        }
        catch (CoreException v129) {
            return false;
        }
    }

    public static void notifyRepositoryView() {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return;
        }
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        RepositoryView view = (RepositoryView)activePage.findView("com.greenpepper.eclipse.views.RepositoryView");
        if (view != null) {
            view.refreshTree();
        }
    }
}

