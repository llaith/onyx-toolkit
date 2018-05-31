/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.session;

import org.llaith.onyx.toolkit.dto.Dto;

/**
 * Used to abstract between different event buses, eg, Guava and Otto (android).
 * Remember, we are only abstracting enough for Dto's to use, just basic events,
 * not Otto's producers for example.
 * <p/>
 * Although the specific implementation of this is abstracted, you will need
 * to make sure the correct Subscription annotations are present on the
 * listener. Currently only the Guavas EventBus is supported, but to support
 * another EventBus, subclasses of the DtoObject could use a common base-class
 * that adds another EventBus implementations subscription mechanism.
 */
public interface DtoBus {

    /**
     * Registers the passed in param as a consumer of DtoRefreshEvents.
     *
     * @param o the DtoObject to be registered.
     */
    void register(Dto o);

    /**
     * Unregisters the passed in param as a consumer of DtoRefreshEvents.
     *
     * @param o the DtoObject to be unregistered.
     */
    void unregister(Dto o);

    /**
     * Posts a DtoRefreshEvent to all registered subscribers.
     *
     * @param event
     */
    void post(DtoRefreshEvent event);


    /**
     * Returns the count of how many dtos are currently registered.
     *
     */
    int count();
}
