/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.ext;

import com.google.common.base.Supplier;
import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.DtoCollection;
import org.llaith.onyx.toolkit.dto.DtoField;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.reflection.PojoModel;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * WARNING, build then as simple minimal mappings of the fields, required and defaults
 * used only for primitives and defaults, not as validation.
 * <p/>
 * BEWARE: compile time constants: https://community.oracle.com/message/9047977
 * <p/>
 * Specifically out of scope is collections and nested dtos! Support can be added later but many questions
 * remain as the most useful way to do it. At the moment support can be added by the composer.
 */
public class PojoDtoFactory {

    public static Supplier<Dto> newSupplier(final Class<?> klass) {
        return () -> new PojoDtoFactory().newFrom(klass);
    }

    public static Supplier<Dto> newSupplier(final Class<?> klass, final Supplier<Set<DtoField>> extraFields) {
        return () -> new PojoDtoFactory().newFrom(
                klass,
                extraFields.get());
    }

    public static Supplier<Dto> newSupplier(final PojoModel model) {
        return () -> new PojoDtoFactory().newFrom(model);
    }

    public static Supplier<Dto> newSupplier(final PojoModel model, final Supplier<Set<DtoField>> extraFields) {
        return () -> new PojoDtoFactory().newFrom(
                model,
                extraFields.get());
    }

    public Dto newFrom(final Class<?> pojo) {
        return this.newFrom(new PojoModel(pojo));
    }

    public Dto newFrom(final Class<?> pojo, final Set<DtoField> extraFields) {
        // WARNING, EXTREMELY DANGEROUS to pass extra fields, make sure they are Supplier<>ed not shared!
        return this.newFrom(
                new PojoModel(pojo),
                extraFields);
    }

    public Dto newFrom(final PojoModel model) {
        return this.newFrom(model,null);
    }

    public Dto newFrom(final PojoModel model, final Set<DtoField> extraFields) {

        // start with the fields
        final Set<DtoField> fields = new HashSet<>();

        // add any extra fields
        if (extraFields != null) fields.addAll(extraFields);

        // load up all the simple fields
        for (final Field field : Guard.notNull(model).fields()) {

            final String name = field.getName();

            if (!model.isNested(name)) fields.add(new DtoField(
                    name,
                    field.getType(),
                    model.isRequired(name),
                    model.isImmutable(name),
                    model.isIdentity(name)));

        }

        // get into the nested models
        for (final Map.Entry<String,PojoModel> entry : model.getNestedModels().entrySet()) {

            final String name = entry.getKey();

            fields.add(new DtoField(
                    name,
                    Dto.class,
                    model.isRequired(name),
                    model.isImmutable(name),
                    model.isIdentity(name)));

        }

        // get into the nested collections
        for (final Map.Entry<String,PojoModel> entry : model.getNestedCollections().entrySet()) {

            final String name = entry.getKey();

            fields.add(new DtoField(
                    name,
                    DtoCollection.class,
                    model.isRequired(name),
                    model.isImmutable(name),
                    model.isIdentity(name)));

        }

        return new Dto(
                model.type().getName(),
                fields);

    }

}
