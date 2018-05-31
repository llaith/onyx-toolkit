/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.meta;

import org.llaith.onyx.toolkit.util.exception.UncheckedException;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MetadataDelegate implements MetadataContainer {

    private final Map<String, Object> metadatas = new HashMap<>();

    @Override
    public boolean hasMetadata(final Class<?> metadataClass) {
        return this.metadatas.containsKey(Guard.notNull(metadataClass).getName());
    }

    @Override
    public <X> X metadataOf(final Class<X> metadataClass) {
        if (Annotation.class.isAssignableFrom(metadataClass))
            return metadataClass.cast(this.metadatas.get(metadataClass.getName()));

        return metadataClass.cast(this.metadatas.get(Guard.notNull(metadataClass).getName()));
    }

    @Override
    public <T> T addMetadata(T metadata) {
        if (this.hasMetadata(Guard.notNull(metadata).getClass())) throw new UncheckedException(String.format(
                "Metadata for class: %s has already been stored.",
                metadata.getClass().getName()));

        return this.putMetadata(metadata);
    }

    @Override
    public <T> T replaceMetadata(final T metadata) {
        if (!this.hasMetadata(Guard.notNull(metadata).getClass())) throw new UncheckedException(String.format(
                "Metadata for class: %s has not previously been stored.",
                metadata.getClass().getName()));

        return this.putMetadata(metadata);
    }

    @Override
    public <T> T removeMetadata(final Class<T> metadataClass) {
        return metadataClass.cast(this.metadatas.remove(Guard.notNull(metadataClass).getName()));
    }

    private <T> T putMetadata(final T metadata) {
        if (metadata instanceof Annotation) {
            this.metadatas.put(((Annotation) metadata).annotationType().getName(),Guard.notNull(metadata));
        } else {
            // This should be able to work. Annotation is an interface, so we can castTarget. Without this, I can't create Proxy annotations as metadata!
            // else if (Proxy.class.isAssignableFrom(metadata.getClass())) throw new IllegalArgumentException("Cannot work with java.lang.reflect.Proxy classes.");
            this.metadatas.put(metadata.getClass().getName(),Guard.notNull(metadata));
        }

        return metadata;
    }

    @SuppressWarnings("unused")
    private <T> Class<T> checkNoProxy(final Class<T> metadataClass) {
        if (Proxy.class.isAssignableFrom(Guard.notNull(metadataClass))) throw new IllegalArgumentException(
                "Cannot work with Proxy classes.");

        return metadataClass;
    }

}
