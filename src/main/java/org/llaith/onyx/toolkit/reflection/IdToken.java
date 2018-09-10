/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection;


import com.google.common.base.MoreObjects;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.lang.Guard;

import java.lang.reflect.InvocationTargetException;


/**
 * NOTE:
 *         http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/
 *         Equals.html#Listing1
 *
 *         class gets a warning about superclass not overriding equals
 *
 *         I just discovered, it is a HUGE bug to have any non-transient fields
 *         excluded from equals. The JVM caches these 'value' classes and will
 *         often supply a cached one instead of the one you newed. The excluded
 *         fields will be from the cached one!
 *
 *         Actually, when the excluded fields are all based on the ones that are
 *         included - thats ok too.
 */
public class IdToken<K,T> {

    private final K id;
    private final TypeToken<T> target;

    public IdToken(final K id, final Class<T> target) {
        this(id,TypeToken.of(target)); // of() only works for non-generic types.
    }

    public IdToken(final K id, final TypeToken<T> target) {
        this.id = Guard.notNull(id);
        this.target = Guard.notNull(target);
    }

    public K id() {
        return this.id;
    }

    public TypeToken<T> target() {
        return target;
    }

    @SuppressWarnings("unchecked")
    public Class<T> targetClass() {
        //https://groups.google.com/forum/#!msg/guava-discuss/FJ486g9y2O0/7udWDbGBbvIJ
        return (Class<T>)target.getRawType();
    }

    public boolean isTargetAssignableFrom(Class<?> klass) {
        return this.target.getRawType().isAssignableFrom(klass);
    }

    public boolean isTargetAssignableFrom(TypeToken<?> token) {
        return this.target.isSupertypeOf(token);
    }

    public T castTarget(Object in) {
        final T cast = this.softCastTarget(in);
        if (cast == null) throw new ClassCastException(String.format("Cannot cast to: %s from: %s (%s).",
                this.targetClass().getName(),
                in.getClass().getName(),
                in));
        return cast;
    }

    public T softCastTarget(Object in) {
        if (in == null) return null;

        final boolean thisIsWrapper = Primitives.isWrapperType(this.target.getClass());
        final boolean thatIsWrapper = Primitives.isWrapperType(in.getClass());

        if (thisIsWrapper && !thatIsWrapper) {
            if (this.target.getRawType().isAssignableFrom(Primitives.wrap(in.getClass())))
                return Primitives.unwrap(this.targetClass()).cast(in);
            return null;
        }

        if (!thisIsWrapper && thatIsWrapper) {
            if (Primitives.wrap(this.target.getRawType()).isAssignableFrom(in.getClass()))
                return Primitives.wrap(this.targetClass()).cast(in);
            return null;
        }

        if (this.target.getRawType().isAssignableFrom(in.getClass())) return this.targetClass().cast(in);

        return null;
    }

    @SuppressWarnings("unchecked")
    public T newInstanceOfTarget() {
        try {
            return (T) this.target().getRawType().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw UncheckedException.wrap(e);
        }
    }

    @Override
    public final boolean equals(final Object o) {
        // These only ever work if you have the right instance. If you want to
        // look them up, then look them up by name from something that detects
        // conflicts, and pass the looked up one in. Otherwise we have to deal
        // with situations where we get similar but not exact matches. If you
        // want a token you can equals(), create a custom little class for that
        // use, don't use these.
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id",id)
                .add("target",target)
                .toString();
    }

}
