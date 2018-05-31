/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

/**
 * An interface that means the object in question supports change tracking when
 * used with the rest of the Dto package.
 * <p/>
 * Note: It's important that implementing interfaces do not use equivalence
 * (the.equals())) as that breaks stuff. Each individual Dto instance is equal
 * only to itself (because of the change tracking) and they communicate to other
 * instances that 'match' themselves via binding and notifications. The
 * DtoCollection add/remove tracking also relies on this. The existing
 * implementations enforce this, but extenders will have to bear that in mind.
 */
public interface ChangeTracked {

    /**
     * A dirty object is one with pending changes to the current values
     *
     * @return true if the object is dirty, false otherwise.
     */
    boolean isDirty();

    /**
     * A stale object is one with updated original values.
     *
     * @return true if the object is dirty, false otherwise.
     */
    boolean isStale();

    /**
     * A conflicted object is one that is both (isDirty() == true) and
     * (isStale == true), meaning it has been updated from both sides and
     * the current dirty value has been overwritten by the new original
     * value and the conflict flag set.
     *
     * @return true if isStale() && isDirty()
     */
    boolean isConflicted();

    /**
     * Accept any changed current values. They will be copied to the original
     * values, and any flags isDirty(), isStale(), isConflicted() flags reset.
     * If used with db entities, this should therefore be done after the new
     * record has been successfully saved.
     *
     */
    void acceptChanges();

    /**
     * Cancel any changed current values. They will be replaced by the objects
     * original values, and any flags isDirty(), isStale(), isConflicted() flags
     * will be reset.
     *
     */
    void cancelChanges();

}
