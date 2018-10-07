/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.settings;

import com.google.common.reflect.TypeToken;

import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public interface Settings {

    boolean has(String key);

    <X> X get(Class<X> klass, String key);

    <X> X get(Class<X> klass, String key, X defaultValue);

    <X> Optional<X> getOptional(Class<X> klass, String key);

    <X> X get(TypeToken<X> token, String key);

    <X> X get(TypeToken<X> token, String key, X defaultValue);

    <X> Optional<X> getOptional(TypeToken<X> token, String key);

    <X> X assemble(Function<Settings,X> assembler);

    <X> X assemble(Class<? extends Function<Settings,X>> assemblerClass);

}
