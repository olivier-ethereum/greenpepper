/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.runner.Main
 *  com.greenpepper.server.GreenPepperServerException
 *  com.greenpepper.util.StringUtil
 *  org.eclipse.core.resources.IProject
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.core.runtime.QualifiedName
 *  org.eclipse.core.runtime.SubProgressMonitor
 *  org.eclipse.debug.core.DebugEvent
 *  org.eclipse.debug.core.DebugPlugin
 *  org.eclipse.debug.core.IDebugEventSetListener
 *  org.eclipse.debug.core.ILaunch
 *  org.eclipse.debug.core.ILaunchConfiguration
 *  org.eclipse.debug.core.model.IDebugTarget
 *  org.eclipse.debug.core.model.IProcess
 *  org.eclipse.jdt.core.IJavaProject
 *  org.eclipse.jdt.launching.JavaLaunchDelegate
 */
package com.greenpepper.eclipse.launcher;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import com.greenpepper.eclipse.i18n.GreenPepperMessages;
import com.greenpepper.eclipse.views.EclipseSpecification;
import com.greenpepper.runner.Main;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.util.StringUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class RunSpecificationLaunchConfigurationDelegate
extends JavaLaunchDelegate
implements IDebugEventSetListener {
    public static final String ID_RUN_SPECIFICATION_CFG = "com.greenpepper.eclipse.launcher.RunSpecification";
    public static final String REPOSITORY_UID = "repoUID";
    public static final String REPOSITORY_CLASS = "repoClass";
    public static final String REPOSITORY_BASEURL = "baseURL";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOCAL = "local";
    public static final String SPECIFICATIONS = "specificationName";
    public static final String OPEN = "open";
    public static final String WORKING_VERSION = "workingversion";
    public static final String GREENPEPPER_COMMAND_LINE_RUNNER = Main.class.getName();
    private static Map<ILaunch, EclipseSpecification> launchedSpecs = new HashMap<ILaunch, EclipseSpecification>();
    private ILaunch launch;

    public synchronized void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        try {
            try {
                this.launch = launch;
                if (!launchedSpecs.keySet().contains((Object)launch)) {
                    DebugPlugin.getDefault().addDebugEventListener((IDebugEventSetListener)this);
                    if (monitor instanceof SubProgressMonitor) {
                        SubProgressMonitor m = (SubProgressMonitor)monitor;
                        if (m.getWrappedProgressMonitor() instanceof EclipseSpecification) {
                            launchedSpecs.put(launch, (EclipseSpecification)m.getWrappedProgressMonitor());
                        } else {
                            EclipseSpecification spec = EclipseSpecification.build(this.getJavaProject(configuration).getProject(), configuration);
                            spec.openAfterExecution();
                            launchedSpecs.put(launch, spec);
                        }
                    }
                }
                super.launch(configuration, mode, launch, monitor);
            }
            catch (CoreException e) {
                this.cleanup(launch);
                throw e;
            }
        }
        finally {
            launch = null;
        }
    }

    public void handleDebugEvents(DebugEvent[] events) {
        block3 : for (int i = 0; i < events.length; ++i) {
            DebugEvent event = events[i];
            Object eventSource = event.getSource();
            switch (event.getKind()) {
                case 8: {
                    if (eventSource == null) continue block3;
                    ILaunch launch = null;
                    if (eventSource instanceof IProcess) {
                        IProcess process = (IProcess)eventSource;
                        launch = process.getLaunch();
                    } else if (eventSource instanceof IDebugTarget) {
                        IDebugTarget debugTarget = (IDebugTarget)eventSource;
                        launch = debugTarget.getLaunch();
                    }
                    if (launch == null) continue block3;
                    this.afterProcess(launch);
                    this.cleanup(launch);
                }
            }
        }
    }

    public String getVMArguments(ILaunchConfiguration configuration) throws CoreException {
        String args = super.getVMArguments(configuration);
        IJavaProject javaProject = this.getJavaProject(configuration);
        String vmArgs = javaProject.getProject().getPersistentProperty(GreenPepperEclipsePlugin.VM_ARGS);
        return StringUtil.isEmpty((String)vmArgs) ? args : String.valueOf(args) + " " + vmArgs;
    }

    public String getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
        try {
            StringBuilder sb = new StringBuilder();
            EclipseSpecification specification = null;
            IProgressMonitor monitor = (IProgressMonitor)launchedSpecs.get((Object)this.launch);
            specification = monitor != null ? (EclipseSpecification)monitor : EclipseSpecification.build(this.getJavaProject(configuration).getProject(), configuration);
            sb.append(this.fixtureFactoryClassOptions(configuration));
            sb.append(specification.asCmdLineOptions()).append(" ");
            sb.append(this.getAdditionalArgs(configuration));
            sb.append(super.getProgramArguments(configuration));
            return sb.toString();
        }
        catch (GreenPepperServerException e) {
            this.abort(GreenPepperMessages.getText(e.getId()), (Throwable)e, 4);
        }
        catch (Exception e) {
            this.abort(GreenPepperMessages.greenpepper_server_generalexeerror, (Throwable)e, 4);
        }
        return null;
    }

    public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
        String[] superEntries = super.getClasspath(configuration);
        String[] coreClasspath = this.getCoreClasspath(configuration);
        return this.mergeArrays(superEntries, coreClasspath);
    }

    private String[] mergeArrays(String[] firstArray, String[] secondArray) {
        ArrayList<String> mergedArray = new ArrayList<String>();
        for (String elem2 : firstArray) {
            mergedArray.add(elem2);
        }
        for (String elem2 : secondArray) {
            mergedArray.add(elem2);
        }
        return mergedArray.toArray(new String[mergedArray.size()]);
    }

    private void afterProcess(ILaunch launch) {
        EclipseSpecification specification = launchedSpecs.get((Object)launch);
        if (specification != null) {
            specification.executed();
        }
    }

    private void cleanup(ILaunch launch) {
        launchedSpecs.remove((Object)launch);
        if (launchedSpecs.isEmpty()) {
            DebugPlugin.getDefault().removeDebugEventListener((IDebugEventSetListener)this);
        }
    }

    private String[] getCoreClasspath(ILaunchConfiguration configuration) throws CoreException {
        FilenameFilter filter;
        File dir;
        String coreLocation;
        File[] files;
        IJavaProject javaProject = this.getJavaProject(configuration);
        if (javaProject == null) {
            this.abort(GreenPepperMessages.greenpepper_server_generalexeerror, null, 4);
        }
        if ((files = (dir = new File(coreLocation = javaProject.getProject().getPersistentProperty(GreenPepperEclipsePlugin.CORE))).listFiles(filter = new FilenameFilter(){

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        })) == null) {
            return new String[0];
        }
        ArrayList<String> path = new ArrayList<String>();
        for (File file : files) {
            path.add(file.getAbsolutePath());
        }
        return path.toArray(new String[path.size()]);
    }

    private String fixtureFactoryClassOptions(ILaunchConfiguration configuration) throws CoreException {
        IJavaProject javaProject = this.getJavaProject(configuration);
        if (javaProject == null) {
            this.abort(GreenPepperMessages.greenpepper_server_generalexeerror, null, 4);
        }
        StringBuilder sb = new StringBuilder("");
        String factoryClass = javaProject.getProject().getPersistentProperty(GreenPepperEclipsePlugin.FACTORY_CLASS);
        if (!StringUtil.isEmpty((String)factoryClass)) {
            sb.append("-f ").append(factoryClass);
            String factoryArgs = javaProject.getProject().getPersistentProperty(GreenPepperEclipsePlugin.FACTORY_ARGS);
            if (!StringUtil.isEmpty((String)factoryArgs)) {
                sb.append(";").append(factoryArgs);
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    private String getAdditionalArgs(ILaunchConfiguration configuration) throws CoreException {
        IJavaProject javaProject = this.getJavaProject(configuration);
        String additionalArgs = javaProject.getProject().getPersistentProperty(GreenPepperEclipsePlugin.ADDITIONAL_ARGS);
        return StringUtil.isEmpty((String)additionalArgs) ? "" : String.valueOf(additionalArgs) + " ";
    }

}

