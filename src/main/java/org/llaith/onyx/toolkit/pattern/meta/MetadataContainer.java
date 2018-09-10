/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.meta;

/**
 * Distinction between add & replace because metadata is designed to be added
 * between modules that don't know of each other, and that can make for
 * unfortunate conflicts. Also no clear, only remove so that a module can only
 * remove metadata it knows about.
 */
public interface MetadataContainer {

    <T> T addMetadata(T metadata);

    <T> T replaceMetadata(T metadata);

    <T> T metadataOf(Class<T> metadataClass);

    boolean hasMetadata(Class<?> metadataClass);

    <T> T removeMetadata(Class<T> metadataClass);

}
