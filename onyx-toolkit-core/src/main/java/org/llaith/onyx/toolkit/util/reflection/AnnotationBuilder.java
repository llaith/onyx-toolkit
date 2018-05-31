package org.llaith.onyx.toolkit.util.reflection;

/**
 *
 */
/*
 * Copyright (c) 2016.
 */

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AnnotationBuilder<T extends Annotation> {


    public static <X extends Annotation> AnnotationBuilder<X> buildAnnotation(final Class<X> annClass) {
        return new AnnotationBuilder<>(annClass);
    }

    public static class AnnotationParam<T extends Annotation> {

        private final AnnotationBuilder<T> holder;
        private final String key;

        private AnnotationParam(final AnnotationBuilder<T> holder, final String key) {
            this.holder = holder;
            this.key = key;
        }

        public AnnotationBuilder<T> of(final Object val) {
            this.holder.values.put(this.key, val);
            return this.holder;
        }
    }

    private final Class<T> klass;
    private final Map<String,Object> values;

    private AnnotationBuilder(final Class<T> klass) {
        this(klass, new HashMap<>());
    }

    private AnnotationBuilder(final Class<T> klass, final Map<String,Object> values) {
        this.klass = klass;
        this.values = values;
    }


    public AnnotationBuilder.AnnotationParam<T> with(final String key) {
        return new AnnotationBuilder.AnnotationParam<>(this, key);
    }


    public T create() {
        return AnnotationUtil.newAnnotation(this.klass, this.values);
    }

}
