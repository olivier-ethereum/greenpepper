/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
 *  org.eclipse.jface.preference.IPreferenceStore
 */
package com.greenpepper.eclipse.preferences;

import com.greenpepper.eclipse.GreenPepperEclipsePlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer
extends AbstractPreferenceInitializer {
    public void initializeDefaultPreferences() {
        IPreferenceStore store = GreenPepperEclipsePlugin.getDefault().getPreferenceStore();
        store.setDefault("GREENPEPPER_URL", "http://localhost:8080/greenpepper-server");
        store.setDefault("GREENPEPPER_HANDLER", "greenpepper1");
    }
}

