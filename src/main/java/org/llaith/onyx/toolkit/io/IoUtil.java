/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.io;

import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.lang.Guard;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 */
public class IoUtil {

    public static void close(final AutoCloseable closeable) {

        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void close(final Closeable closeable) {

        close((AutoCloseable)closeable); // for coercion glitches in IDE

    }

    public static <X extends AutoCloseable> void consumeAndClose(@Nonnull final Supplier<X> supply, @Nonnull final Consumer<X> consume) {
        
        try (final X x = Guard.notNull(supply).get()) {
            
            Guard.notNull(consume).accept(x);
            
        } catch (Exception e) {
            
            throw UncheckedException.wrap(e);
            
        }
        
    }

    public static <X> Consumer<X> dummyConsumer() {
        
        return x -> {};
        
    }

    public static String readStringFromByteBuffer(final ByteBuffer buffer) {

        final byte[] bytes = new byte[buffer.remaining()];

        buffer.get(bytes);

        return new String(bytes);

    }

}
