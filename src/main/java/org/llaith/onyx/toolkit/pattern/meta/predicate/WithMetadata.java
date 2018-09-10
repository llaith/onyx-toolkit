/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.meta.predicate;


import org.llaith.onyx.toolkit.pattern.meta.MetadataContainer;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 *
 */
public class WithMetadata<T extends MetadataContainer> implements Predicate<T> {

    public static <X extends MetadataContainer> WithMetadata<X> withMetadata(final Class<?> metadataClass) {
        return new WithMetadata<>(metadataClass);
    }

    private final Class<?> metadataClass;

    public WithMetadata(final Class<?> metadataClass) {
        this.metadataClass = metadataClass;
    }

    @Override
    public boolean test(@Nullable final T input) {
        return (input != null) && input.hasMetadata(this.metadataClass);
    }

}
