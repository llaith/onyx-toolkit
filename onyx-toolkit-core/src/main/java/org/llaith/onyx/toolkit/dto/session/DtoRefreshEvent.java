/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.session;

import org.llaith.onyx.toolkit.dto.Dto;

/**
 * An event which represents a request to synchronise another DtoObject
 * from the source's state.
 *
 */
public class DtoRefreshEvent {

    private final Dto source;

    public DtoRefreshEvent(final Dto source) {
        this.source = source;
    }

    /**
     * Retrieves the source object that is to be used as the master
     * for the synchronization.
     *
     * @return the source object
     */
    public Dto source() {
        return source;
    }

}
