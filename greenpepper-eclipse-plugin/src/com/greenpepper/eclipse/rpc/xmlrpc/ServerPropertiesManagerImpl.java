/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.ServerPropertiesManager
 *  org.eclipse.jface.preference.IPreferenceStore
 */
package com.greenpepper.eclipse.rpc.xmlrpc;

import com.greenpepper.eclipse.preferences.GreenPepperPreferencePage;
import com.greenpepper.server.ServerPropertiesManager;
import org.eclipse.jface.preference.IPreferenceStore;

public class ServerPropertiesManagerImpl
implements ServerPropertiesManager {
    public static String IDENTIFIER = "WORKSPACE";
    private final IPreferenceStore preferenceStore;

    public ServerPropertiesManagerImpl() {
        GreenPepperPreferencePage gppage = new GreenPepperPreferencePage();
        this.preferenceStore = gppage.getPreferenceStore();
    }

    public String getProperty(String key, String identifier) {
        return this.preferenceStore.getString(key);
    }

    public void setProperty(String key, String value, String identifier) {
    }
}

