/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.session;


import com.google.common.base.Supplier;
import org.llaith.onyx.toolkit.dto.DtoField;
import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.DtoCollection;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * <P>
 * The DtoSession is used to place built (identified) dto instances to be synchronised with each other.
 * The synchronization occurs on the acceptChanges/cancelChanges invocation, which means it is good practice
 * to call cancelChanges even if you plan to discard the dto, although this is not strictly necessary.
 * </P>
 * <pre>
 *     SomeDto dto1 = session.addDto(dtoBuilder.build());
 *     SomeDto dto2 = session.addDto(dtoBuilder.build());
 *
 *     dto1.setSomeField(value);
 *
 *     Asset.Equals(dto1.getSomeField(),dto2.getSomeField());
 * </pre>
 *
 * IMPORTANT NOTE: because adding dtos needs to be recursive, and i don;t want to reference count, we
 * instead leave them until the session is cleared(). The usage should reflect the fact that the
 * session should be cleared or popped altogether periodically.
 *
 * For this reason the root layer has been changed to not accept objects after construction as they would
 * never be cleared.
 *
 */
public class DtoSession {

    private final Supplier<DtoBus> busFactory;

    private final HashMap<Object,DtoBus> buses = new HashMap<>();

    private final Set<Dto> root = new HashSet<>();

    private final Deque<Set<Dto>> stack;
    private final Deque<Object> bookmarks;

    /**
     * Constructs a new DtoSession.
     *
     * @param busFactory a Supplier for creating new EventBus instances for
     *                   each identified Dto.
     */
    public DtoSession(final Supplier<DtoBus> busFactory) {
        this.busFactory = busFactory;

        this.stack = new LinkedList<>();
        this.bookmarks = new LinkedList<>();

        this.stack.add(this.root);
    }

    /**
     * Constructs a new DtoSession with the passed in objects in the root session.
     *
     * @param busFactory a Supplier for creating new EventBus instances for
     *                   each identified Dto.
     */
    public DtoSession(final Supplier<DtoBus> busFactory, final Collection<Dto> rootContents) {
        this(busFactory);

        for (final Dto dto: rootContents) {
            this.registerDto(dto);
        }

    }

    /**
     * Pushes another layer for DtoObject instances onto the stack. It is presumed
     * that each layer represents some sort of 'view' of an application. It is
     * more convenient to discard an entire layer of DtoObjects en-mass then to
     * worry about discarding and un-registering them one by one.
     *
     * @param bookmark the identifier used as the bookmark in order to pop
     *                 back to that point when finished.
     */
    public Object push(final Object bookmark) {
        this.bookmarks.push(bookmark);

        this.stack.push(new HashSet<>());

        return bookmark;
    }

    /**
     * Rewind (pop) the stack back to the given bookmark, un-registering all DtoObjects
     * stored in the popped layers from their EventBuses. If the bookmark cannot be found,
     * the stack is unwound all the way and a new black layer is reset onto the bottom.
     *
     * @param bookmark the identifier that marks the point at which we want to
     *                 rewind the stack to.
     */
    public Object pop(final Object bookmark) {
        while (true) {
            if ((this.bookmarks.isEmpty()) || (this.bookmarks.peek().equals(bookmark))) break;
            this.bookmarks.pop();
            this.stack.pop().clear();
        }

        if (this.stack.size() < 1) this.stack.add(this.root); // maybe clear it? or ditch the idea?

        return bookmark;
    }

    /**
     * Add a DtoObject to the session. A reference to the DtoObject will be stored in the
     * current layer and the DtoObject will be registered against the correct EventBus. If
     * it is the first dto with a given Identity, a new EventBus will be created for it.
     *
     * @param dto the DtoObject to add to the current layer.
     * @param <X> The type of the DtoObject
     * @return the dto object that was passed in (for chaining).
     */
    public <X extends Dto> X addDto(final X dto) {

        if (this.stack.peek() == this.root) throw new IllegalStateException("Cannot add instances to the root session.");

        this.registerDto(dto);

        return dto;
    }

    private void registerDto(final Dto dto) {

        if (dto.isNew()) dto.acceptChanges(); //throw new IllegalStateException("Cannot add new instances to a session.");

        if (this.stack.peek().contains(dto)) return; // short circuit recursive cycles

        this.addDtoToBus(dto);

        this.addDtoToSession(dto);

        this.addNestedToSession(dto);

        this.addChildrenToSession(dto);

    }

    /**
     * Clears all the current layers dtos.
     */
    public void clear() {

        // to avoid leaking through the bus
        for (final Dto dto : this.stack.peek()) {
            this.removeDtoFromBus(dto);
        }

        this.stack.peek().clear();
    }

    private void addNestedToSession(final Dto dto) {

        for (final DtoField field : dto.fields().values()) {
            if (Dto.class.isAssignableFrom(field.type())) {
                // what about the original/new/etc values? do we leave them behind in the session?
                if (dto.has(field.name())) this.registerDto((Dto)dto.get(field.name()));
            }
        }

    }

    private void addChildrenToSession(final Dto dto) {

        for (final DtoField field : dto.fields().values()) {
            if (DtoCollection.class.isAssignableFrom(field.type())) {
                // what about the original/new/etc values? do we leave them behind in the session?
                if (dto.has(field.name())) {
                    for (final Dto nested : (DtoCollection)dto.get(field.name())) {
                        this.registerDto(nested);
                    }
                }
            }
        }

    }

    private void addDtoToBus(final Dto dto) {
        if (!this.buses.containsKey(dto.identity())) this.buses.put(
                dto.identity(),
                this.busFactory.get());

        dto.register(this.buses.get(dto.identity()));
    }

    private void removeDtoFromBus(final Dto dto) {

        final DtoBus bus = this.buses.get(dto.identity());

        if (bus != null) {

            dto.unregister(bus);

            if (bus.count() < 1) this.buses.remove(dto.identity());
        }
    }

    private void addDtoToSession(final Dto dto) {
        this.stack.peek().add(dto);
    }

}
