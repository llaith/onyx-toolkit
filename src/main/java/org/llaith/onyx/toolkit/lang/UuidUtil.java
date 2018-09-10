package org.llaith.onyx.toolkit.lang;

import java.math.BigInteger;
import java.util.UUID;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;

/**
 *
 */
public class UuidUtil {

    public static String uuid() {

        return uuid(UUID.randomUUID());

    }

    public static String uuid(final UUID uuid) {

        return notNull(uuid)
                .toString()
                .toLowerCase();

    }

    public static String squishedUuid(final UUID uuid) {

        return uuid(uuid).replaceAll("-", "");

    }

    public static String squishedUuid() {

        return squishedUuid(UUID.randomUUID());

    }

    public static UUID unsquishUuid(final String squished) {

        final BigInteger bi1 = new BigInteger(squished.substring(0, 16), 16);
        final BigInteger bi2 = new BigInteger(squished.substring(16, 32), 16);

        return new UUID(bi1.longValue(), bi2.longValue());

    }

    public static String uuidUrn() {

        return "urn:uuid:" + uuid();

    }

    public static String uuidUrn(final UUID uuid) {

        return "urn:uuid:" + uuid(uuid);

    }


}
