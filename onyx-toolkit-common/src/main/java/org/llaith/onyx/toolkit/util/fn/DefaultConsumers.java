package org.llaith.onyx.toolkit.util.fn;

import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 *
 */
public class DefaultConsumers {

    @Nonnull
    public static <X> Consumer<X> defaultConsumerIfNull(@Nullable final Consumer<X> consumer) {

        return (consumer == null) ?
                (o) -> {} :
                consumer;

    }

    @Nonnull
    public static <X extends Exception> Consumer<X> defaultRethrowIfNull(@Nullable final Consumer<X> consumer) {

        return (consumer == null) ?
                (e) -> {throw UncheckedException.wrap(e);} :
                consumer;

    }

}
