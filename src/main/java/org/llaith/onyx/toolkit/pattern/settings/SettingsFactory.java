/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class SettingsFactory {

    public static Settings fromProperties(final Properties properties) {

        return new SettingsFactory()
                .configureWith(builder -> builder.from(properties))
                .construct();

    }

    public static Settings fromProperties(final File file) {

        return new SettingsFactory()
                .configureWith(builder -> builder.from(file))
                .construct();

    }

    private final List<SettingsConfiguration> configurations = new ArrayList<>();

    private final Settings parentSettings;

    public SettingsFactory() {

        this(null);

    }

    public SettingsFactory(final Settings parentSettings) {

        this.parentSettings = parentSettings;

    }

    public SettingsFactory configureWith(final SettingsConfiguration configuration) {

        this.configurations.add(configuration);

        return this;

    }

    public Settings construct() {

        // create the new registry
        final SettingsImpl environment = this.parentSettings != null ?
                new SettingsImpl(this.parentSettings) :
                new SettingsImpl();

        // loop over and apply configurations
        for (final SettingsConfiguration configuration : this.configurations) {

            configuration.execute(environment);

        }

        // done
        return environment;

    }

}
