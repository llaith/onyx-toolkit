/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.settings;

import com.google.common.reflect.TypeToken;
import org.llaith.onyx.toolkit.lang.PropertiesUtil;
import org.llaith.onyx.toolkit.reflection.InstanceUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

/**
 * Designed to be reloaded at runtime. Holds only properties, if objects are required, use a provider.
 */
public class SettingsImpl implements SettingsBuilder {

    private final Settings parentSettings;

    private final Map<String,Object> properties = new HashMap<>();

    public SettingsImpl() {

        this(null);

    }

    public SettingsImpl(final Settings parentSettings) {

        this.parentSettings = parentSettings;

    }

    @Override
    public boolean has(final String key) {

        // we don't just want to know there is a key, we want it to be valid
        return this.properties.containsKey(key);

    }

    @Override
    public <X> X get(final Class<X> klass, final String key) {

        return this.get(TypeToken.of(klass), key);

    }

    @Override
    public <X> X get(final Class<X> klass, final String key, final X defaultValue) {

        return this.get(TypeToken.of(klass), key, defaultValue);

    }

    @Override
    public <X> Optional<X> getOptional(final Class<X> klass, final String key) {

        return this.getOptional(TypeToken.of(klass), key);

    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(final TypeToken<X> token, final String key) {

        final Object val = this.properties.containsKey(key) ?
                this.properties.get(key) :
                (this.parentSettings != null) ?
                        this.parentSettings.get(token, key) :
                        null;

        if (val == null) throw new IllegalStateException("Cannot find required property with key: " + key);

        if (!token.isSupertypeOf(val.getClass())) throw new IllegalStateException(String.format(
                "Stored property with key: %s is type of: %s, not: %s as requested.",
                key,
                val.getClass().getName(),
                token.getRawType().getName()));

        return (X)val;

    }

    @Override
    public <X> X get(final TypeToken<X> token, final String key, final X defaultValue) {

        return this.has(key) ?
                this.get(token, key) :
                defaultValue;

    }

    @Override
    public <X> Optional<X> getOptional(final TypeToken<X> token, final String key) {

        return this.has(key) ?
                Optional.of(this.get(token, key)) :
                Optional.empty();

    }

    @Override
    public <X> X assemble(final Function<Settings,X> assembler) {

        return assembler.apply(this);

    }

    @Override
    public <X> X assemble(final Class<? extends Function<Settings,X>> assemblerClass) {

        return assemble(InstanceUtil.newInstance(assemblerClass));

    }

    @Override
    public SettingsBuilder setIfNotNull(final String key, final Object value) {

        if (value != null) return this.set(key, value);

        return this;

    }

    @Override
    public SettingsBuilder set(final String key, final Object value) {

        if (value == null) throw new IllegalStateException(String.format(
                "It is not possible to store the null value for '%s'. Use setIfNotNull() and test when using with has() instead.",
                key));

        if (this.properties.containsKey(key)) throw new IllegalStateException(String.format(
                "Cannot overwrite existing property: '%s' containing value '%s' with new value '%s'.",
                key,
                this.properties.get(key),
                value));

        this.properties.put(key, value);

        return this;

    }

    @Override
    public SettingsBuilder from(final File file) {

        final Map<String,String> props = PropertiesUtil.fromFileToMap(file);

        for (Map.Entry<String,String> entry : props.entrySet()) {

            this.set(
                    entry.getKey(),
                    entry.getValue());

        }

        return this;

    }

    @Override
    public SettingsBuilder from(final Path file) {
        return from(file.toFile());
    }

    @Override
    public SettingsBuilder from(final Properties properties) {

        final Map<String,String> props = PropertiesUtil.toMap(properties);

        for (Map.Entry<String,String> entry : props.entrySet()) {

            this.set(
                    entry.getKey(),
                    entry.getValue());

        }

        return this;

    }

    @Override
    public SettingsBuilder fromAllFiles(final Collection<File> files) {

        files.forEach(this::from);

        return this;

    }

    @Override
    public SettingsBuilder fromAllPaths(final Collection<Path> paths) {

        paths.forEach(this::from);

        return this;

    }

    @Override
    public SettingsBuilder fromAllProperties(final Collection<Properties> properties) {

        properties.forEach(this::from);

        return this;

    }

}
