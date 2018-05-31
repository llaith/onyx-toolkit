/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;


import org.llaith.onyx.toolkit.meta.MetadataContainer;
import org.llaith.onyx.toolkit.meta.MetadataDelegate;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.reflection.Primitive;

/**
 * DtoValue holds the set of values and flags that represent the current
 * and certain elements of past state for a field in a DtoObject.
 * <p/>
 * Warning:
 * Due to the use of safeNull values, a getValue == null may not be
 * the same as hasCurrent/hasOriginal() which will check the nullValue.
 * When determining the instances logical state, it makes more sense to
 * avoid the comparison to null. The comparison to null should only be
 * used when determining how to display the value.
 * Note: Could not think of a better way to do this, so forced to do a nasty
 * hack to deal with cycles without passing any objects down the interface
 * (yuck). This means it's a tad fragile.
 */
public class DtoField implements ChangeTracked {

    private final MetadataDelegate metadata = new MetadataDelegate();

    private final String name;
    private final Class<?> type;
    private final boolean required;
    private final boolean immutable;
    private final boolean identity;

    private boolean built = false;
    private boolean initialized = false;

    private Object lastValue;
    private Object originalValue;
    private Object currentValue;
    private Object discardValue;


    public DtoField(final String name, final Class<?> type, final boolean required, final boolean immutable, final boolean identity) {

        this.name = Guard.notNull(name);
        this.type = Guard.notNull(type);

        this.required = required;
        this.immutable = immutable;
        this.identity = identity;

    }

    /**
     * @return the name of this value.
     */
    public String name() {
        // convenience function
        return this.name;
    }

    /**
     * @return the DtoField definition used in this instance.
     */
    public Class<?> type() {
        return this.type;
    }

    /**
     *
     * @return if this field is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     *
     * @return if this field is immutable/final
     */
    public boolean isImmutable() {
        return immutable;
    }

    /**
     *
     * @return if this field is an identity field
     */
    public boolean isIdentity() {
        return identity;
    }

    /**
     * @return true if this value has been initialized.
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * @return true if this instances current value is not null
     * or the nullValue.
     */
    public boolean hasCurrent() {
        return this.currentValue != null;
    }


    /**
     * Returns the current value. The current value is the last value
     * of the field that was set or (in some cases) reset.
     *
     * @return the current value.
     */
    public Object currentValue() {
        return this.currentValue;
    }


    /**
     * @return true if this instances original value is not null
     * or the nullValue.
     */
    public boolean hasOriginal() {
        return this.originalValue != null;
    }


    /**
     * Returns the original value. The original value is the last value
     * of the field that was reset.
     *
     * @return the original value.
     */
    public Object originalValue() {
        return this.originalValue;
    }


    /**
     * Returns the last value. The last value is the previous value
     * of that was reset if the last reset altered the original value.
     *
     * @return the last (previous original) value.
     */
    public Object lastValue() {
        return this.lastValue;
    }


    /**
     * Set a new current value.
     *
     * @param value the new current value
     */
    public DtoField setValue(final Object value) {

        // check type first
        this.checkValueType(value);

        // first we initialise
        this.initialized = true;

        // if its still current, nothing to do.
        if (value == this.currentValue) return this;

        // and safe-set it.
        this.setCurrent(value);

        return this;
    }


    /**
     * Changes the 'original' value, and also the 'current' value
     * if the passed in value is different from the previous value.
     * If the value is not different, it leaves the current value
     * which may be dirty alone. If it did change the value, it
     * will also toggle the field as stale. This may result in the
     * field responding true to isConflicted(). If it
     *
     * @param value the new 'original' value
     * @return the passed in value param
     */
    public DtoField resetValue(Object value) {

        // check type first
        this.checkValueType(value);

        // first we initialise
        this.initialized = true;

        // if it hasn't changed from before, nothing to do.
        if (value == this.originalValue) return this;

        // save a new last value
        this.lastValue = this.originalValue;

        // then we set it as the new the original value
        this.setOriginal(value);

        // and if it's a new original value, we update the current
        if (this.isStale()) {
            // it's now conflicted
            this.discardValue = this.currentValue;
            // and overwritten
            this.setCurrent(value);
        }

        return this;
    }

    /*
     *  Inherited.
     */
    @Override
    public boolean isDirty() {
        return (this.currentValue != this.originalValue) || (this.hasNesting() && this.nestedDto().isDirty());
    }


    /*
     *  Inherited.
     */
    @Override
    public boolean isStale() {
        return (this.originalValue != this.lastValue) || (this.hasNesting() && this.nestedDto().isStale());
    }


    /*
     *  Inherited.
     */
    @Override
    public boolean isConflicted() {
        return ((this.discardValue != null) && (this.discardValue != this.currentValue)) ||
                (this.hasNesting() && this.nestedDto().isConflicted());
    }


    /*
     *  Inherited.
     */
    @Override
    public void acceptChanges() {

        this.verify(this.currentValue);

        this.setOriginal(this.resetState(this.currentValue));

        if (this.hasNesting()) this.nestedDto().acceptChanges();

    }


    /*
     *  Inherited.
     */
    @Override
    public void cancelChanges() {
        this.setCurrent(this.resetState(this.originalValue));

        if (this.hasNesting()) this.nestedDto().cancelChanges();

    }


    /*
     *  Gets the metadata container.
     */
    public MetadataContainer metadata() {
        return this.metadata;
    }


    /*
     *  Inherited.
     */
    @Override
    public final int hashCode() {
        // don't override
        return super.hashCode();
    }


    /*
     *  Inherited.
     */
    @Override
    public final boolean equals(Object obj) {
        // don't override
        return super.equals(obj);
    }


    /*
     *  Check the field is storable in the associated slot if added via
     *  the object methods. Tries casting via the generic type.
     */
    private Object checkValueType(Object value) {
        if (value == null) return null;

        if (this.type.isAssignableFrom(value.getClass())) return this.type.cast(value); // allow subclasses

        if (this.type.isPrimitive()) {
            Primitive<?> p = Primitive.primitiveFor(this.type);
            if (p != null && p.wrapperClass().isAssignableFrom(value.getClass())) return p.wrapperClass().cast(value);
        }

        throw new ClassCastException(String.format(
                "DtoValue: %s accepts: %s but received: %s.",
                this.name,
                this.type.getName(),
                value.getClass().getName()));

    }

    /*
     *  Check the field is storable in the associated slot if added via
     *  the object methods. Tries casting via the generic type. This is not
     *  the same as validation, which is outside the Dto system, and this
     *  is not exposed directly, it is called via the acceptChanges.
     */
    public void verify(Object value) {

        if ((value == null) && this.required)
            throw new IllegalStateException(String.format(
                    "Field: %s is not optional.",
                    this.name));

        if (this.built) {

            if (this.immutable && (value != this.originalValue))
                throw new IllegalStateException(String.format(
                        "Field: %s is constant.",
                        this.name));

        } else {
            this.built = true;
        }

    }

    /*
     * Sets the 'current' value using the nullValue if required.
     * Note the 'discard' is exclusively set from this value so
     * needs no special handling for nullValues.
     */
    private void setCurrent(Object value) {
        this.currentValue = value;
    }


    /*
     * Sets the 'original' value using the nullValue if required.
     * Note the 'lost' is exclusively set from this value so
     * needs no special handling for nullValues.
     */
    private void setOriginal(final Object value) {
        this.originalValue = value;
    }


    /**
     * Resets the internal state common to acceptChanges and
     * cancelChanges.
     *
     * @param value the current or original value as required.
     */
    private Object resetState(final Object value) {
        this.lastValue = value;
        this.discardValue = null;

        return value;
    }

    /*
     * Nesting is true if the current value is a Dto.
     */
    private boolean hasNesting() {
        return this.currentValue instanceof ChangeTracked;
    }


    /*
     * If the current value is a nested Dto, return it cast to Dto.
     */
    private ChangeTracked nestedDto() {
        if (this.currentValue instanceof ChangeTracked) return ChangeTracked.class.cast(this.currentValue);
        return null;
    }

}
