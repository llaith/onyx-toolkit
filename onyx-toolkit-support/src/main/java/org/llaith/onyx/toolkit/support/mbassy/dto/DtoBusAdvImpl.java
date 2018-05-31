/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.mbassy.dto;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import net.engio.mbassy.listener.Listener;
import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.session.DtoBus;
import org.llaith.onyx.toolkit.dto.session.DtoRefreshEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the DtoBus using MBassadors MessageBus.
 * https://github.com/bennidi/mbassador
 * This is a faster and more feature rich EventBus implementation,
 * Considered experimental as yet untested in an actual application.
 * If this one is needed, the jar should be included manually, as
 * it's currently scoped as provided.
 */
public class DtoBusAdvImpl implements DtoBus {

    @Listener
    public static class Adapter {

        private final Dto dto;

        public Adapter(final Dto dto) {
            this.dto = dto;
        }

        @Handler(delivery = Invoke.Synchronously)
        public void onRefresh(final DtoRefreshEvent dtoRefreshEvent) {
            dto.onRefresh(dtoRefreshEvent);
        }

    }

    private final MBassador<DtoRefreshEvent> messageBus;

    private final Map<Dto,Adapter> registered = new HashMap<>();

    public DtoBusAdvImpl() {

        this.messageBus = new MBassador<>(
                new BusConfiguration()
                        .addFeature(Feature.SyncPubSub.Default())
                        .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                        .addFeature(Feature.AsynchronousMessageDispatch.Default()));

    }

    @Override
    public void register(final Dto o) {
        final Adapter adapter = new Adapter(o);
        this.registered.put(o, adapter);
        this.messageBus.subscribe(adapter);
    }

    @Override
    public void unregister(final Dto o) {
        final Adapter adapter = this.registered.get(o);
        if (adapter != null) {
            this.messageBus.subscribe(adapter);
            this.registered.remove(o);
        }
    }

    @Override
    public void post(final DtoRefreshEvent event) {
        this.messageBus.post(event).now();
    }

    @Override
    public int count() {
        return this.registered.size();
    }

}
