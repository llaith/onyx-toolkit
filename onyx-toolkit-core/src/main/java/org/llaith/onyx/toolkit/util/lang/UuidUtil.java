package org.llaith.onyx.toolkit.util.lang;

import java.util.UUID;

/**
 *
 */
public class UuidUtil {

    public static String uuid() {

        return UUID
                .randomUUID()
                .toString()
                .toLowerCase();

    }

    public static String externalId() {

        return uuid().replaceAll("-", "");

    }

}
