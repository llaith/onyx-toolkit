/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.settings;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Properties;

/**
 *
 */
public interface SettingsBuilder extends Settings {

    SettingsBuilder set(String key, Object value);

    SettingsBuilder setIfNotNull(String key, Object value);

    SettingsBuilder from(File file);

    SettingsBuilder from(Path path);

    SettingsBuilder from(Properties properties);

    SettingsBuilder fromAllFiles(Collection<File> files); // prefixes because of erasure!

    SettingsBuilder fromAllPaths(Collection<Path> paths); // prefixes because of erasure!

    SettingsBuilder fromAllProperties(Collection<Properties> properties); // prefixes because of erasure!

}
