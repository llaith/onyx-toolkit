/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.text;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class TextSnipper {

    private static final Logger log = LoggerFactory.getLogger(TextSnipper.class);

    private static final Splitter lineSplitter = Splitter.on(System.getProperty("line.separator"));

    private static final Joiner lineJoiner = Joiner.on(System.getProperty("line.separator"));

    // no longer using regexes
    private static final String SNIP_LINE_KEYWORD = "@@SNIP-LINE|";
    private static final String SNIP_START_KEYWORD = "@@SNIP-START|";
    private static final String SNIP_STOP_KEYWORD = "@@SNIP-STOP";
    private static final String SNIP_END_TAG = "@@";

    private final Set<String> deletes = new HashSet<>();
    private final Map<String,String> replaces = new HashMap<>();

    public TextSnipper deleteTag(final String tag) {
        this.deletes.add(tag);
        return this;
    }

    public TextSnipper replaceTag(final String tag, final String replacement) {
        this.deletes.add(tag); // simplifies the logic
        this.replaces.put(tag, replacement);
        return this;
    }

    public String snipTextFileToString(final File file) {

        return lineJoiner.join(this.snipTextFileToLines(file));

    }

    public List<String> snipTextFileToLines(final File file) {

        try {

            return this.snipLines(Files.readLines(file, Charsets.UTF_8));

        } catch (IOException e) {

            throw UncheckedException.wrap(e.getMessage(), e);

        }

    }

    public String snipText(final String string) {

        return lineJoiner.join(this.snipLines(lineSplitter.split(string)));

    }

    public List<String> snipLines(final Iterable<String> lines) {

        final List<String> out = new ArrayList<>();

        boolean snipping = false;

        for (final String line : lines) {

            if (snipping) {

                if (line.contains(SNIP_STOP_KEYWORD)) {
                    snipping = false;
                }

            } else {

                if (line.contains(SNIP_LINE_KEYWORD)) {

                    final String tag = this.getTag(SNIP_LINE_KEYWORD, line);

                    if (this.deletes.contains(tag)) {
                        if (this.replaces.containsKey(tag)) {
                            out.addAll(Lists.newArrayList(lineSplitter.split(this.replaces.get(tag))));
                        }
                    } else {
                        out.add(line);
                    }

                } else if (line.trim().startsWith(SNIP_START_KEYWORD)) {

                    final String tag = this.getTag(SNIP_START_KEYWORD, line);

                    if (this.deletes.contains(tag)) {

                        snipping = true;

                        if (this.replaces.containsKey(tag)) {
                            out.addAll(Lists.newArrayList(lineSplitter.split(this.replaces.get(tag))));
                        }

                    } else {
                        out.add(line);
                    }

                } else {
                    out.add(line);
                }

            }

        }

        return out;

    }

    private String getTag(final String tag, final String line) {

        Guard.notBlankOrNull(line);

        int start = line.indexOf(tag) + tag.length();
        int end = line.lastIndexOf(SNIP_END_TAG);
        if (start != -1 && end != -1) return line.substring(start, end);

        return null;

    }

    public static void main(String[] args) {

        final TextSnipper snipper = new TextSnipper()
                .deleteTag("delete")
                .replaceTag("replace", "[REPLACEMENT TEXT]");

        String text = snipper.snipText("This is a \n delete this line -- @@SNIP-LINE|delete@@ so there\n@@SNIP-START|replace@@\n1\n2\n3\n@@SNIP-STOP\nand the last line");

        System.out.println(text);

    }

}





