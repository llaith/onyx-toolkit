/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.llaith.onyx.toolkit.fn.ExcecutionUtil.rethrow;
import static org.llaith.onyx.toolkit.lang.Guard.notNull;
import static org.llaith.onyx.toolkit.reflection.AnnotationUtil.fieldsMarkedWith;

/**
 * Consider using Guava's Objects.toStringHelper instead. This is the very lazy
 * option, but has some value in cases where the trade-off (performance vs
 * forgetting to change the tostring) is worth it.
 */
public class PersistentIdentities {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Inherited
    public @interface PersistentIdentity {
    }

    public static boolean equals(final Object us, final Object them) {

        // because we should be calling this from an object, we *expect* the lhs!
        return EqualsBuilder.reflectionEquals(
                equivalenceFields(us),
                equivalenceFields(them));

    }

    public static boolean equals(final Object us, final Set<Object> theirs) {

        // because we should be calling this from an object, we *expect* the lhs!
        return EqualsBuilder.reflectionEquals(
                equivalenceFields(us),
                theirs);

    }

    public static int hashCode(final Object us) {

        // because we should be calling this from an object, we *expect* the lhs!
        return HashCodeBuilder.reflectionHashCode(equivalenceFields(us));

    }

    public static Set<Object> equivalenceFields(final Object target) {

        // check class is also marked properly
        if (!notNull(target).getClass().isAnnotationPresent(PersistentIdentity.class))
            throw new UncheckedException(String.format(
                    "Class [%s] does not use persistent-identity equivalence!",
                    target));

        final Set<Object> fields = new HashSet<>();

        for (final Field f : fieldsMarkedWith(notNull(target).getClass(), PersistentIdentity.class)) {

            rethrow(() -> fields.add(f.get(target)));

        }

        return fields;

    }

    @PersistentIdentity
    public static class BasePersistentIdentity {

        @Override
        public boolean equals(final Object o) {

            return PersistentIdentities.equals(this, o);

        }

        @Override
        public int hashCode() {

            return PersistentIdentities.hashCode(this);

        }

    }

}
