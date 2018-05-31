/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.output.memo;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ListItem {

    private final String heading;

    private final List<Block> blocks = new ArrayList<>();

    public ListItem(final String heading, final Block... blocks) {
        this.heading = Guard.notNull(heading);

        if (blocks != null) this.blocks.addAll(Arrays.asList(blocks));
    }

    public String heading() {
        return this.heading;
    }

    public boolean hasBlocks() {
        return !this.blocks.isEmpty();
    }

    public List<Block> blocks() {
        return Collections.unmodifiableList(this.blocks);
    }

    public ListItem withBlock(final Block block) {
        this.blocks.add(block);
        return this;
    }

    public <X extends Block> X addBlock(final X block) {
        this.blocks.add(block);
        return block;
    }

    public void addBlocks(final List<Block> blocks) {
        this.blocks.addAll(blocks);
    }

}
