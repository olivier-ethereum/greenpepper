/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.resource.ImageDescriptor
 *  org.eclipse.osgi.util.NLS
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.ui.plugin.AbstractUIPlugin
 */
package com.greenpepper.eclipse;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GreenPepperImages
extends NLS {
    private static final Map<String, Image> imageCache = new HashMap<String, Image>();
    public static final String ICON_PATH = "icons/";
    public static final String ERROR = "icons/error.gif";
    public static final String EXCEPTION = "icons/exception.gif";
    public static final String PROJECT = "icons/project.gif";
    public static final String COUNTER_ERROR = "icons/redpepper_12.gif";
    public static final String COUNTER_SUCCESS = "icons/greenpepper_12.gif";
    public static final String COUNTER_FAILURES = "icons/greenpepper_12.gif";
    public static final String REPOSITORYVIEW_NON_EXECUTABLE_DOCUMENT_IMAGE = "icons/greypepper_16.gif";
    public static final String REPOSITORYVIEW_EXECUTABLE_DOCUMENT_IMAGE = "icons/greenpepper_16.gif";
    public static final String REPOSITORYVIEW_ERROR_DOCUMENT_IMAGE = "icons/redpepper_16.gif";
    public static final String REPOSITORYVIEW_FIXTURE = "icons/fixture.gif";
    public static final String FIXTURE = "icons/new_fixture.png";
    public static final String IMPLEMENTED_VERSION_SPECIFICATION_ICON = "icons/implemented.png";
    public static final String IMPLEMENTED_VERSION_SPECIFICATION_DISABLE_ICON = "icons/implemented_disabled.png";
    public static final String WORKING_VERSION_SPECIFICATION_ICON = "icons/working.png";
    public static final String WORKING_VERSION_SPECIFICATION_DISABLE_ICON = "icons/working_disabled.png";
    public static final String REPOSITORYVIEW_EXECUTE_BRANCH_ICON = "icons/branch_execute_enabled.png";
    public static final String REPOSITORYVIEW_EXECUTE_BRANCH_DISABLE_ICON = "icons/branch_execute_disabled.png";
    public static final String REPOSITORYVIEW_EXECUTE_DOCUMENT_ICON = "icons/document_execute_enabled.png";
    public static final String REPOSITORYVIEW_EXECUTE_DOCUMENT_DISABLE_ICON = "icons/document_execute_disabled.png";
    public static final String REPOSITORYVIEW_SHOW_DOCUMENT_ICON = "icons/document_view_enabled.png";
    public static final String REPOSITORYVIEW_SHOW_DOCUMENT_DISABLE_ICON = "icons/document_view_disabled.png";
    public static final String REPOSITORYVIEW_REMOTE_DOCUMENT_ICON = "icons/remotedocument_view_enabled.png";
    public static final String REPOSITORYVIEW_REMOTE_DOCUMENT_DISABLE_ICON = "icons/remotedocument_view_disabled.png";
    public static final String REPOSITORYVIEW_PROPERTIES_ICON = "icons/document_view_disabled.png";
    public static final String TAG_IMPLEMENTED_ICON = "icons/tagimplemented.png";
    public static final String TAG_IMPLEMENTED_DISABLE_ICON = "icons/tagimplemented_disabled.png";
    public static final String REPOSITORYVIEW_REFRESH_TREE_ICON = "icons/refresh_tree.gif";

    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin((String)"com.greenpepper.eclipse", (String)path);
    }

    public static Image createImage(String path) {
        Image image = imageCache.get(path);
        if (image == null) {
            image = GreenPepperImages.getImageDescriptor(path).createImage(true);
            imageCache.put(path, image);
        }
        return image;
    }
}

