/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.display.memo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Section {

    private final int level;
    private final String heading;

    private final List<Block> blocks = new ArrayList<>();

    public Section(final int level) {
        this(level,(Block[])null);
    }

    public Section(final int level, final Block... blocks) {
        this(level,null,blocks);
    }

    public Section(final int level, final String heading, final Block... blocks) {
        this.level = level;

        this.heading = heading; // ok as null

        if (blocks != null) this.blocks.addAll(Arrays.asList(blocks));
    }

    public int level() {
        return level;
    }

    public boolean hasHeading() {
        return heading != null;
    }

    public String heading() {
        return heading;
    }

    public boolean hasBlocks() {
        return !this.blocks.isEmpty();
    }

    public List<Block> blocks() {
        return Collections.unmodifiableList(blocks);
    }

    public Section withBlock(final Block block) {
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
