/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 *
 */
public class RuntimeUtil {

    private static final Logger Log = LoggerFactory.getLogger(RuntimeUtil.class);

    public static <X extends Closeable> X onShutdownClose(final X closeable) {

        Runtime.getRuntime()
               .addShutdownHook(new Thread(() -> {
                   try {
                       if (closeable != null) closeable.close();
                   } catch (Exception e) {
                       Log.error("Ingoring error on shutdown of closeable:" + closeable.toString(), e);
                   }
               }));

        return closeable;
    }

    public interface Terminator<X> {

        void terminate(X x) throws Exception;
    }

    public interface Initialiser<X> {

        void init(X x) throws Exception;

    }

    public interface Factory<X> {

        X create() throws Exception;

    }

    public static <X> X lifecycle(final X x, final Terminator<X> terminator) {

        Runtime.getRuntime()
               .addShutdownHook(new Thread(() -> {
                   try {
                       terminator.terminate(x);
                   } catch (Exception e) {
                       Log.error("Ingoring error on shutdown.", e);
                   }
               }));

        return x;
    }

    public static <X> X lifecycle(final X x, final Initialiser<X> initialiser, final Terminator<X> terminator) {

        try {

            initialiser.init(x);

            if (x == null) return null;

            return lifecycle(x, terminator);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <X> X lifecycle(final Factory<X> factory, final Terminator<X> terminator) {

        try {

            final X x = factory.create();

            if (x == null) return null;

            return lifecycle(x, terminator);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
