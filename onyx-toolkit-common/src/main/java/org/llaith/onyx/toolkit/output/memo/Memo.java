/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.output.memo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Memo {

    private final List<Section> sections = new ArrayList<>();

    public Memo(Section... sections) {
        if (sections != null) {
            this.sections.addAll(Arrays.asList(sections));
        }
    }

    public List<Section> sections() {
        return Collections.unmodifiableList(sections);
    }

    public Memo withSection(final Section section) {
        this.sections.add(section);
        return this;
    }

    public <X extends Section> X addSection(final X section) {
        this.sections.add(section);
        return section;
    }

    public void addSections(final List<Section> sections) {
        this.sections.addAll(sections);
    }

}
