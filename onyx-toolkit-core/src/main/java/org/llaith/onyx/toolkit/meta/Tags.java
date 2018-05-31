/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.meta;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Tags {

    private final Set<String> tags = new HashSet<>();

    public boolean containsTag(final String tag) {
        return this.tags.contains(tag);
    }

    public Tags addTag(final String tag) {
        this.tags.add(tag);
        return this;
    }

    public Tags removeTag(final String tag) {
        this.tags.remove(tag);
        return this;
    }

}
