/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.io.ObjectStreamClass;
import java.nio.ByteBuffer;

/**
 * A collection of misc checksums.
 */
public class ChecksumUtil {

    public static String calcCcittChecksumAsString(byte[] bytes) {

        return Integer.toHexString(calcCcittChecksum(bytes));

    }

    /**
     * Implementation of a CRC16-CCITT Checksum.
     * 1 + x + x^5 + x^12 + x^16 is irreducible polynomial.
     */
    public static int calcCcittChecksum(byte[] bytes) {
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }

        crc &= 0xffff;

        return crc;
    }

    // only way can think for now.
    public static int calc16bitChecksumForClass(final Class<?> klass) {

        // step 1, get built-in serializedUID
        final ObjectStreamClass c = ObjectStreamClass.lookup(klass);

        // step 2, turn into byte array
        final byte[] bytes = ByteBuffer.allocate(8).putLong(c.getSerialVersionUID()).array();

        // step 3, add to crc32 (int sized)
        return ChecksumUtil.calcCcittChecksum(bytes);

    }

    // only way can think for now.
    public static int calc16bitChecksumForClasses(final Class<?>[] klasses) {

        if (klasses == null) return 0;

        final ByteBuffer buffer = ByteBuffer.allocate(8 * klasses.length);

        for (Class<?> klass : klasses) {

            // step 1, get built-in serializedUID
            final ObjectStreamClass c = ObjectStreamClass.lookup(klass);

            // step 2, turn into byte array
            buffer.putLong(c.getSerialVersionUID());

        }

        // step 3, add to crc32 (int sized)
        return ChecksumUtil.calcCcittChecksum(buffer.array());

    }

}
