/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.display.memo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Each of these is a paragraph (with a body text break between them), whereas
 * each separate String in the texts var is equivalent to ending with a <br/>
 */
public class ParagraphBlock extends Block {

    private final List<String> texts = new ArrayList<>();

    public ParagraphBlock() {
        super();
    }

    public ParagraphBlock(final String title) {
        super(title);
    }

    public ParagraphBlock(final String title, String... texts) {
        super(title);

        if (texts != null) this.texts.addAll(Arrays.asList(texts));
    }

    public List<String> texts() {
        return Collections.unmodifiableList(texts);
    }

    public ParagraphBlock withText(final String text) {
        this.texts.add(text);
        return this;
    }

    public String addText(final String text) {
        this.texts.add(text);
        return text;
    }

    public void addTexts(final List<String> texts) {
        this.texts.addAll(texts);
    }

}
