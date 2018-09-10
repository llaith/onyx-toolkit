/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.meta.predicate;

import org.llaith.onyx.toolkit.lang.Guard;
import org.llaith.onyx.toolkit.pattern.meta.MetadataContainer;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 *
 */
public class WithAnnotation<T> implements Predicate<T> {

    public interface AnnotationFinder<T> {

        Annotation find(Class<? extends Annotation> annClass, T t);
    }

    public static class ClassFinder implements WithAnnotation.AnnotationFinder<Class<?>> {

        @Override
        public Annotation find(final Class<? extends Annotation> annClass, final Class<?> target) {
            return target.getAnnotation(annClass);
        }
    }

    public static class ModelFinder<T extends MetadataContainer> implements WithAnnotation.AnnotationFinder<T> {

        @Override
        public Annotation find(final Class<? extends Annotation> annClass, final T target) {
            return target.metadataOf(annClass);
        }
    }

    @SafeVarargs
    public static <X> WithAnnotation<X> withMetadata(
            final WithAnnotation.AnnotationFinder<X> finder,
            final Class<? extends Annotation>... annClass) {
        return new WithAnnotation<>(finder, annClass);
    }

    private final List<Class<? extends Annotation>> annClasses;
    private final WithAnnotation.AnnotationFinder<T> finder;

    @SafeVarargs
    public WithAnnotation(final WithAnnotation.AnnotationFinder<T> finder, final Class<? extends Annotation>... annClasses) {
        this(finder, asList(annClasses));
    }

    public WithAnnotation(final WithAnnotation.AnnotationFinder<T> finder, final List<Class<? extends Annotation>> annClasses) {
        this.finder = finder;
        this.annClasses = Guard.notNullOrEmpty(annClasses);
    }

    @Override
    public boolean test(@Nullable final T input) {
        for (final Class<? extends Annotation> annClass : this.annClasses) {
            final Annotation ann = this.finder.find(annClass, input);
            if (ann != null) return true;
        }
        return false;
    }

}
