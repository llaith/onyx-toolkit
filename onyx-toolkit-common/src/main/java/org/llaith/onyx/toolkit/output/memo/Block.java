/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.output.memo;

/*
 * Note: a title on a text block is *not* the same as it's section heading (which
 * controls it's nesting and outline level). The title is more like a 'dictionary'
 * entry, group signifier, or sidebar title, and is completely optional.
 */
public abstract class Block {

    private final String title;

    public Block() {
        this(null);
    }

    public Block(final String title) {
        this.title = title;
    }

    public boolean hasTitle() {
        return this.title != null;
    }

    public String title() {
        return title;
    }

}
