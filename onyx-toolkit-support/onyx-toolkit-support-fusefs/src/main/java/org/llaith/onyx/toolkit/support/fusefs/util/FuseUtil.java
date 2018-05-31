package org.llaith.onyx.toolkit.support.fusefs.util;

import ru.serce.jnrfuse.struct.Timespec;

import java.time.Instant;

/**
 *
 */
public class FuseUtil {

    public static void fillTime(Instant instant, Timespec timespec) {

        timespec.tv_sec.set(instant.getEpochSecond());
        timespec.tv_nsec.set(instant.getNano());
        
    }

}
