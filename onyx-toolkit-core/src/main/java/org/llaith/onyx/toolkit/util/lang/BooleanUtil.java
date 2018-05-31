/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import javax.annotation.Nullable;

/**
 *
 */
public class BooleanUtil {

    public static boolean is(@Nullable Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    public static boolean isNot(@Nullable Boolean bool) {
        return !Boolean.TRUE.equals(bool); // nulls are false
    }

}
