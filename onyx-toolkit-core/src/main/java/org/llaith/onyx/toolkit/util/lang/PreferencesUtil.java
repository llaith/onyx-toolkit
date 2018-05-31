/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * https://stackoverflow.com/questions/15004954/java-setting-preferences-backingstore-directory#17815931
 */
public class PreferencesUtil {

    private static final String BACKING_STORE_AVAIL = "BackingStoreAvail";

    private static boolean backingStoreAvailable() {
        
        final Preferences prefs = Preferences.userRoot().node("<temporary>");
        
        try {
        
            boolean oldValue = prefs.getBoolean(BACKING_STORE_AVAIL, false);
            prefs.putBoolean(BACKING_STORE_AVAIL, !oldValue);
            prefs.flush();
        
        } catch(BackingStoreException e) {
            return false;
        }
        
        return true;
        
    }
    
}
