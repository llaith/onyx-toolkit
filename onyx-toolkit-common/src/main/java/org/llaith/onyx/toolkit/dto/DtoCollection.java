/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.llaith.onyx.toolkit.meta.MetadataContainer;
import org.llaith.onyx.toolkit.meta.MetadataDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * Why not a TransactionalList? A List cannot practically track it's additions
 * and deletions, a set can. Sets and Maps are 'pure' collections. A list, in
 * this context, can be thought of as an ordered view of a set. A view which is
 * recreated on each change that is derived from the set of currentValues plus a
 * Comparator.
 *
 * Equivalence: It's equivalent if it's current currentValues are equal. It's the
 * responsibility of code that knows it is DtoObject to check it's
 * isStale() etc. In all ways possible, it should act as though it is the
 * 'current' Set<Dto>.
 *
 * It is a deliberate restriction that the DtoCollections only take DtoObjects
 * rather than anything. It's to ensure that 'isStale()' etc messages are passed
 * down properly. It would be ok for collections of primitives, but not of any
 * other objects. For that reason, it's safer to keep it to DtoObject's, and have
 * primitive wrapping dtos. It's should be very rare in any case, as these are
 * designed to present database entities and they will have and id as well as
 * some data fields in any case.
 *
 * Design: Inspired by the idea that we only need collections and a lua-style
 * 'table' object which can be either a map or an 'object'. Originally these
 * were based on Sets and Maps, but that had to change because the interfaces
 * are unsafe for the TransactionalAspect. Particularly concepts like 'retain'
 * are meaningless, and maps really act like two sets together with undefined
 * semantics for the tracking the changes of the keys.
 *
 * NEW: in future, model Map as Collection of DtoEntry<String,V extends Dto<?>.
 * Dto's as keys is far more complex, and it's not the way the DTOs should be used.
 *
 * Also description that the Collection is unordered.
 *
 */
public class DtoCollection implements ChangeTracked, MetadataContainer, Iterable<Dto> {

    private final MetadataDelegate metadatas = new MetadataDelegate();

    private final Set<Dto> current = new HashSet<>();
    private final Set<Dto> original = new HashSet<>();

    private final Set<Dto> dirtyDeletions = new HashSet<>();
    private final Set<Dto> dirtyAdditions = new HashSet<>();
    private final Set<Dto> staleDeletions = new HashSet<>();
    private final Set<Dto> staleAdditions = new HashSet<>();

    public DtoCollection() {
        super();
    }

    public DtoCollection(final Collection<Dto> collection) {
        this.current.addAll(collection);
        this.original.addAll(collection);
    }

    public int size() {
        return this.current.size();
    }

    public boolean isEmpty() {
        return this.current.isEmpty();
    }

    public boolean contains(final Dto dto) {
        return this.current.contains(dto);
    }

    public DtoCollection add(final Dto dto) {

        this.current.add(dto);

        if (this.dirtyDeletions.contains(dto)) this.dirtyDeletions.remove(dto);
        else this.dirtyAdditions.add(dto);

        return this;
    }

    public DtoCollection addAll(final Collection<Dto> it) {

        for (Dto dto : it) {
            this.add(dto);
        }

        return this;

    }

    public DtoCollection remove(final Dto dto) {

        this.current.remove(dto);

        if (this.dirtyAdditions.contains(dto)) this.dirtyAdditions.remove(dto);
        else this.dirtyDeletions.add(dto);

        return this;
    }

    public DtoCollection removeAll(final Collection<Dto> it) {

        for (Dto dto : it) {
            this.remove(dto);
        }

        return this;
    }

    public DtoCollection clear() {

        this.removeAll(new HashSet<>(this.current));

        return this;
    }

    public DtoCollection set(final Collection<Dto> it) {
        // the clear/addAll do all necessary tracking.
        this.clear();

        this.addAll(it);

        return this;
    }

    public void reset(final Collection<Dto> it) {

        this.staleAdditions.clear();
        for (final Dto item : it) {
            if (!this.original.contains(item)) {
                this.original.add(item);
                if (this.dirtyAdditions.contains(item)) this.dirtyAdditions.remove(item);
                else this.staleAdditions.add(item);
            }
        }

        this.staleDeletions.clear();
        for (final Dto item : this.original) {
            if (!it.contains(item)) {
                this.original.remove(item);
                if (this.dirtyDeletions.contains(item)) this.dirtyDeletions.remove(item);
                else this.staleDeletions.add(item);
            }
        }

        this.current.clear();
        this.current.addAll(this.original);

    }

    public Set<Dto> getAll() {
        return new HashSet<>(current);
    }

    public List<Dto> getAllAsList() {
        return new ArrayList<>(current);
    }

    public Set<Dto> getDirtyDeletions() {
        return new HashSet<>(dirtyDeletions);
    }

    public Set<Dto> getDirtyAdditions() {
        return new HashSet<>(dirtyAdditions);
    }

    public Set<Dto> getStaleDeletions() {
        return new HashSet<>(staleDeletions);
    }

    public Set<Dto> getStaleAdditions() {
        return new HashSet<>(staleAdditions);
    }

    @Override
    public boolean isDirty() {

        if (this.hasDirtyCollections()) return true;

        for (final Dto dto : this.current) {
            if (dto.isDirty()) return true;
        }

        return false;
    }

    @Override
    public boolean isStale() {

        if (this.hasStaleCollections()) return true;

        for (final Dto dto: this.current) {
            if (dto.isStale()) return true;
        }

        return false;
    }

    @Override
    public boolean isConflicted() {
        return this.isDirty() && this.isStale();
    }

    @Override
    public void acceptChanges() {
        for (final Dto dto : this.current) {
            dto.acceptChanges();
        }

        this.original.clear();
        this.original.addAll(this.current);

        this.clearFlags();

    }

    @Override
    public void cancelChanges() {

        for (final Dto dto : this.current) {
            dto.cancelChanges();
        }

        this.current.clear();
        this.current.addAll(this.original);

        this.clearFlags();

    }

    @Override
    public <X> X addMetadata(X metadata) {
        return this.metadatas.addMetadata(metadata);
    }

    @Override
    public <X> X replaceMetadata(X metadata) {
        return this.metadatas.replaceMetadata(metadata);
    }

    @Override
    public <X> X metadataOf(Class<X> metadataClass) {
        return this.metadatas.metadataOf(metadataClass);
    }

    @Override
    public boolean hasMetadata(Class<?> metadataClass) {
        return this.metadatas.hasMetadata(metadataClass);
    }

    @Override
    public <X> X removeMetadata(Class<X> metadataClass) {
        return this.metadatas.removeMetadata(metadataClass);
    }

    @Override
    public Iterator<Dto> iterator() {
        return this.current.iterator();
    }

    @Override
    public final boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    private boolean hasDirtyCollections() {
        return (this.dirtyAdditions.size() + this.dirtyDeletions.size()) > 0;
    }

    private boolean hasStaleCollections() {
        return (this.staleAdditions.size() + this.staleDeletions.size()) > 0;
    }

    private void clearFlags() {
        this.dirtyAdditions.clear();
        this.dirtyDeletions.clear();
        this.staleAdditions.clear();
        this.staleDeletions.clear();
    }

}
