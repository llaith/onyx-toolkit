/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.meta;

/**
 *
 */
public interface TagContainer {

    boolean containsTag(String tag);

    String addTag(String tag);

    String removeTag(String tag);

}
