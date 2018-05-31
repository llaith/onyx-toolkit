/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.collection;

import java.util.Arrays;
import java.util.function.Function;

/**
 *
 */
public class ArrayUtil {
    
    public static <T> T head(final T[] arr) {
        
        if ( (arr != null) && (arr.length > 0)) return arr[0];
        
        return null;
        
    }

    public static <T> T[] tail(final T[] arr) {

        if ( (arr != null) && (arr.length > 0)) 
            return Arrays.copyOfRange(arr, 1, arr.length);

        return null;

    }

    public static <T, S> Function<T[],S> processCorrectSizeLines(final int expectedSize, final Function<T[],S> delegate) {

        return arr -> {

            if (arr == null || arr.length == 0) {

                System.out.println("Skipping, blank-row.");

                return null;

            }

            if (arr.length < expectedSize) {

                System.out.printf(
                        "Skipping, not enough columns (%s/%s cols found): %s%n",
                        arr.length,
                        expectedSize,
                        Arrays.toString(arr));

                return null;
            }

            return delegate.apply(arr);

        };

    }

}
