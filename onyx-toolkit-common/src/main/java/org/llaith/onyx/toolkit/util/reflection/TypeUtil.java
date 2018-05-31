/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;

import com.google.common.reflect.TypeToken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * Used to cheat around the TypeToken not allowing a cast down to <T>, which of course
 * they do for a very good reason (safety) it's just that, basically, I value prettiness
 * above true type safety! ;)
 */
public class TypeUtil {

    /*
     * NOTE: Important. When you use this in a selector, which is the initial use case, it doesn't
     * really restrict it past the raw class (which is determined at runtime), it just helps with
     * the compiler signature casts. For that reason, it's probably better to use the TypeToken
     * directly and I will be adding that to the repository when the FluentIterable implements a
     * filter based on TypeTokens.
     */
    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    @Nonnull
    public static <X> Class<X> coerce(@Nonnull final TypeToken<X> token) {

        // convert from <?> to 'raw' to <T>
        final Class rawClazz = token.getRawType();

        final Class<X> ret = rawClazz;

        return ret;

    }

    /**
     * Exists to make a nicer error message and consume the passed in class
     * param in generic methods.
     */
    @Nullable
    public static <X> X cast(@Nonnull Class<X> klass, @Nullable final Object o) {

        if (o == null) return null;

        if (klass.isAssignableFrom(o.getClass())) return klass.cast(o);

        throw new ClassCastException(format(
                "Expected value to have a class of :%s but it was: %s",
                klass.getName(),
                o.getClass().getName()));

    }

}
