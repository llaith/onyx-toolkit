/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.meta.predicate;

import com.google.common.base.Objects;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.invoke.dsl.MethodHandler;
import org.llaith.onyx.toolkit.pattern.meta.MetadataContainer;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.llaith.onyx.toolkit.lang.Guard.notNullOrEmpty;

/**
 *
 */
public class WithAnnotationValue<T> implements Predicate<T> {

    public interface AnnotationFinder<T> {

        Annotation find(Class<? extends Annotation> annClass, T t);
    }

    public static class ClassFinder implements WithAnnotationValue.AnnotationFinder<Class<?>> {

        @Override
        public Annotation find(final Class<? extends Annotation> annClass, final Class<?> target) {
            return target.getAnnotation(annClass);
        }
    }

    public static class ModelFinder<T extends MetadataContainer> implements WithAnnotationValue.AnnotationFinder<T> {

        @Override
        public Annotation find(final Class<? extends Annotation> annClass, final T target) {
            return target.metadataOf(annClass);
        }
    }

    @SafeVarargs
    public static <X> WithAnnotationValue<X> withMetadata(
            final WithAnnotationValue.AnnotationFinder<X> finder,
            final String param,
            final Object value,
            final Class<? extends Annotation>... annClass) {
        return new WithAnnotationValue<>(finder, param, value, annClass);
    }

    private final List<Class<? extends Annotation>> annClasses;
    private final String param;
    private final Object value;
    private final WithAnnotationValue.AnnotationFinder<T> finder;

    @SafeVarargs
    public WithAnnotationValue(final WithAnnotationValue.AnnotationFinder<T> finder, final String param, final Object value, final Class<? extends Annotation>... annClasses) {
        this(finder, param, value, asList(annClasses));
    }

    public WithAnnotationValue(final WithAnnotationValue.AnnotationFinder<T> finder, final String param, final Object value, final List<Class<? extends Annotation>> annClasses) {
        this.param = param;
        this.value = value;
        this.finder = finder;
        this.annClasses = notNullOrEmpty(annClasses);
    }

    @Override
    public boolean test(@Nullable final T input) {
        for (final Class<? extends Annotation> annClass : this.annClasses) {
            final Annotation ann = this.finder.find(annClass, input);
            if (ann != null) {
                final MethodHandler method = new Mirror().on(ann).invoke().method(this.param);
                if (method != null) {
                    final Object o = method.withoutArgs();
                    return Objects.equal(o, this.value);
                }
            }
        }
        return false;
    }

}
