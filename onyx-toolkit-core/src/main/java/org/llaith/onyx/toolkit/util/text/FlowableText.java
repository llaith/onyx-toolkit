/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.text;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.llaith.onyx.toolkit.util.lang.StringUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Replace leading tabs with spaces, updateLine is extremely variable with them.
 */
public class FlowableText {

    private final int maxWidth;

    private final LinkedList<String> lines = new LinkedList<>();

    public FlowableText(final int maxWidth) {
        this.maxWidth = maxWidth < 1 ? 240 : maxWidth;
    }

    public void append(final int indent, final String text) {
        this.newline(indent, this.lines.removeLast() + text);
    }

    public void newline(final int indentChars, final String text) {

        // first line with passed in indent
        // any overflows with passed in indent + mode.
        // change indent to tabs and store tabspace?

        for (final String line : StringUtil.reflowTextToLines(text, Math.max(maxWidth - indentChars, 1))) {
            this.lines.add(Strings.repeat(" ", indentChars) + line);
        }

    }

    public void newline() {
        this.lines.add("");
    }

    public List<String> asLines() {
        return new LinkedList<>(this.lines);
    }

    public String[] asLineArray() {
        return this.lines.toArray(new String[this.lines.size()]);
    }

    public String asString() {
        return Joiner.on("\n").join(this.lines);
    }

}
