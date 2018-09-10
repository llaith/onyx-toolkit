/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection.pojo;

import org.llaith.onyx.toolkit.lang.Guard;
import org.llaith.onyx.toolkit.reflection.GenericsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class PojoModel {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface BaseClass {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Required {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Ignore {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Immutable {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Identity {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface NestedModel {

    }

    private static Logger log = LoggerFactory.getLogger(PojoModel.class);

    private final Class<?> type;
    private final Map<String,Field> fields = new HashMap<>();
    private final Map<String,PojoModel> nestedModels = new HashMap<>();
    private final Map<String,PojoModel> nestedCollections = new HashMap<>();
    private final Set<String> required = new HashSet<>();
    private final Set<String> immutable = new HashSet<>();
    private final Set<String> identity = new HashSet<>();

    private final Map<Class<?>,PojoModel> cache;

    public PojoModel(final Object o) {

        this(Guard.notNull(o).getClass());

    }

    public PojoModel(final Class<?> type) {

        this(
                type,
                new HashMap<>());

    }

    private PojoModel(final Class<?> type, final Map<Class<?>,PojoModel> cache) {

        this.type = Guard.notNull(type);

        this.cache = Guard.notNull(cache);

        this.pullFields();

    }

    private void pullFields() {

        // hunt until the base class
        Class<?> srch = this.type;
        while (true) {

            for (final Field field : srch.getDeclaredFields()) {

                if (field.isSynthetic()) continue;

                if (field.isAnnotationPresent(Ignore.class)) continue;

                int modifiers = field.getModifiers();

                if (Modifier.isStatic(modifiers)) continue;
                if (Modifier.isPrivate(modifiers)) log.debug(
                        "(INFO) Use of private attributes will require slower reflection access: '" + field.getName());
                if (Modifier.isFinal(modifiers)) {
                    this.immutable.add(field.getName());
                    log.debug(
                            "(WARNING) Final field may exhibit extreme strangeness (references to it may have been inlined!) if it is initialised via a compile-time expression instead of ctor call: '" + field.getName());
                }

                // don't overwrite descendants
                if (!this.fields.containsKey(field.getName())) fields.put(
                        field.getName(),
                        field);

                if (field.isAnnotationPresent(Required.class)) this.required.add(field.getName());
                if (field.isAnnotationPresent(Immutable.class)) this.immutable.add(field.getName());
                if (field.isAnnotationPresent(Identity.class)) identity.add(field.getName());

                // rip nested types
                if (field.isAnnotationPresent(NestedModel.class)) {

                    final Class<?> fieldType = field.getType();

                    if (Collection.class.isAssignableFrom(fieldType)) {

                        final Class<?> collectionType = GenericsUtil.concreteElementTypeOfCollection(field);

                        this.nestedCollections.put(
                                field.getName(),
                                this.cache.containsKey(collectionType) ?
                                        this.cache.get(collectionType) :
                                        this.newNestedClassModel(collectionType));

                    } else {

                        this.nestedModels.put(
                                field.getName(),
                                this.cache.containsKey(fieldType) ?
                                        this.cache.get(fieldType) :
                                        this.newNestedClassModel(fieldType));

                    }

                }

            }

            srch = srch.getSuperclass();

            if (srch.isAnnotationPresent(BaseClass.class)) break;
            if (srch.isAnnotationPresent(Ignore.class)) break;
            if (srch == Object.class) break;
        }

    }

    private PojoModel newNestedClassModel(final Class<?> subType) {

        // we've got a recursive request
        if (subType.equals(this.type)) {

            this.cache.put(
                    this.type,
                    this);

            return this;

        }

        final PojoModel model = new PojoModel(subType, this.cache);

        this.cache.put(
                model.type(),
                model);

        return model;

    }

    public Class<?> type() {
        return type;
    }

    public Set<Field> fields() {
        return new HashSet<>(fields.values());
    }

    public Field fieldFor(final String name) {
        return this.fields.get(name);
    }

    public Map<String,PojoModel> getNestedModels() {
        return new HashMap<>(this.nestedModels);
    }

    public Map<String,PojoModel> getNestedCollections() {
        return new HashMap<>(this.nestedCollections);
    }

    public Set<String> identity() {
        return new HashSet<>(identity);
    }

    public boolean isNested(final String fieldname) {
        return this.nestedModels.containsKey(fieldname) || this.nestedCollections.containsKey(fieldname);
    }

    public boolean isRequired(final String fieldname) {
        return this.required.contains(fieldname);
    }

    public boolean isImmutable(final String fieldname) {
        return this.immutable.contains(fieldname);
    }

    public boolean isIdentity(final String fieldname) {
        return this.identity.contains(fieldname);
    }

}
