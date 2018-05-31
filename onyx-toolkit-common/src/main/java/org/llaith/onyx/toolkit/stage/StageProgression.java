/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.stage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class StageProgression implements Iterable<StageCompletion> {

    private final List<StageCompletion> completions = new ArrayList<>();

    public void addCompletion(final StageCompletion completion) {

        completions.add(completion);

    }

    public StageCompletion last() {

        return this.completions.get(this.completions.size() - 1);

    }

    public boolean isEmpty() {

        return this.completions.isEmpty();

    }

    @Override
    @Nonnull
    public Iterator<StageCompletion> iterator() {

        return this.completions.iterator();
        
    }

}
