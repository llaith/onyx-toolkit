/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.text;

import org.llaith.onyx.toolkit.util.lang.StringUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Cheap and nasty class, beware, it will build up and slow down.
 */
public class UniqueStrings {

    private final Random rnd = new Random();

    private Set<String> previous = new HashSet<>();

    public String newUniqueAlphaString(final int len) {

        String s;

        do {

            s = StringUtil.randomAlphaString(rnd, len);

        } while (!this.previous.contains(s));

        this.previous.add(s);

        return s;

    }

    public String newUniqueAlphaNumericString(final int len) {

        String s;

        do {

            s = StringUtil.randomAlphaNumericString(rnd, len);

        } while (!this.previous.contains(s));

        this.previous.add(s);

        return s;

    }

}
