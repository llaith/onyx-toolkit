/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.meta.predicate;


import org.llaith.onyx.toolkit.pattern.meta.MetadataContainer;
import org.llaith.onyx.toolkit.pattern.meta.Tags;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 *
 */
public class WithTag<T extends MetadataContainer> implements Predicate<T> {

    public static <X extends MetadataContainer> WithTag<X> withTag(final String tag) {
        return new WithTag<>(tag);
    }

    private final String tag;

    public WithTag(final String tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@Nullable final T input) {
        return (input != null) &&
                input.hasMetadata(Tags.class) &&
                input.metadataOf(Tags.class).containsTag(this.tag);
    }

}
