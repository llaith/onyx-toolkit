package org.llaith.onyx.toolkit.lang;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 *
 */
public class NumberUtil {

    public static int adjustInteger(final int actual, final int dflt, final int min, final int max) {

        // set the default batch size if not passed
        final int requestedSize = actual > 0 ?
                actual :
                dflt;

        // then clamp it and use this instead
        return Ints.constrainToRange(requestedSize, min, max);

    }

    public static long adjustLong(final long actual, final long dflt, final long min, final long max) {

        // set the default batch size if not passed
        final long requestedSize = actual > 0 ?
                actual :
                dflt;

        // then clamp it and use this instead
        return Longs.constrainToRange(requestedSize, min, max);

    }

    public static double percent(final long count, final long total) {

        final double rawPercent = (100.0 / total) * count;

        return ((rawPercent == 100.0) && (count != total)) ?
                rawPercent - 1 :
                rawPercent;

    }

}
