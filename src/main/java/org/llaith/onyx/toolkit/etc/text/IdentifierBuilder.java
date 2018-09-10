/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.text;

import org.llaith.onyx.toolkit.reflection.ClassStructureUtil;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IdentifierBuilder {

    private final Mode mode;
    private final boolean avoidVowelEnds;
    private final int chars; // it's important this isn't passed in.

    private final Map<String,String> abbrevs = new HashMap<>();

    public enum Mode {
        NONE, CUT, COMPRESS
    }

    public IdentifierBuilder(final Mode mode, final boolean avoidVowelEnds, final int chars) {
        super();
        this.mode = mode;
        this.avoidVowelEnds = avoidVowelEnds;
        this.chars = chars;
    }

    public IdentifierBuilder(final Mode mode, final boolean avoidVowelEnds, final Map<String,String> abbrevs, final int chars) {
        this(mode, avoidVowelEnds, chars);
        this.abbrevs.putAll(abbrevs);
    }

    public String identToDbStyle(final Class<?> klass) {
        return identToDbStyle(ClassStructureUtil.unqualifyClass(klass));
    }

    public String identToDbStyle(final String ident) {
        final StringBuilder buf = new StringBuilder();

        for (final char c : ident.toCharArray()) {
            if (Character.isUpperCase(c)) {
                buf.append("_").append(Character.toLowerCase(c));
            } else {
                buf.append(c);
            }
        }
        if (buf.charAt(0) == '_') buf.deleteCharAt(0);

        return shrink(buf.toString());
    }

    private String shrink(final String ident) {
        final StringBuilder buf = new StringBuilder();

        for (final String word : ident.split("_")) {
            if (!shrinkWord(word, this.chars, buf)) buf.append(word);
            buf.append("_");
        }
        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }

    private boolean shrinkWord(final String word, final int chars, final StringBuilder buf) {
        if (!this.shrinkWordByAbbrev(word, buf)) {
            if (this.mode == Mode.NONE) return false;
            else if (this.mode == Mode.COMPRESS) return this.shrinkWordByCompress(word, chars, buf);
            else if (this.mode == Mode.CUT) return this.shrinkWordByCut(word, chars, buf);
        }
        return false;
    }

    private boolean shrinkWordByAbbrev(final String word, final StringBuilder buf) {
        if (this.abbrevs.containsKey(word)) {
            buf.append(this.abbrevs.get(word));
            return true;
        }
        return false;
    }

    private boolean shrinkWordByCut(final String word, final int chars, final StringBuilder buf) {
        if (chars < 1) return false;

        int counter = 0;
        for (final char c : word.toCharArray()) {
            counter++;
            if (counter < chars) {
                buf.append(c);
            } else if (counter == chars) {
                if (this.avoidVowelEnds && isVowel(c) && word.length() != chars) {
                    counter--; // pretend we didn't see it
                } else buf.append(c);
            } else if ((counter > chars)) {
                return true;
            }
        }

        return true;
    }

    private boolean shrinkWordByCompress(final String word, final int chars, final StringBuilder buf) {
        if (chars < 1) return false;

        int counter = 0;
        for (final char c : word.toCharArray()) {
            counter++;
            if (counter == 1) {
                buf.append(c); // always put first char
            } else if (isVowel(c)) {
                counter--; // pretend we didn't see it
            } else if ((counter < chars)) {
                buf.append(c);
            } else if ((counter == chars)) {
                if (this.avoidVowelEnds && isVowel(c) && word.length() != chars) {
                    counter--; // pretend we didn't see it
                } else buf.append(c);
            } else if ((counter > chars)) {
                return true;
            }
        }

        return true;
    }

    private boolean isVowel(final char c) {
        return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
    }

}
