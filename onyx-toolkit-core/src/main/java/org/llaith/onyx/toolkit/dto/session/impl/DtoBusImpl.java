/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.session.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.session.DtoBus;
import org.llaith.onyx.toolkit.dto.session.DtoRefreshEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the DtoBus using Guava's EventBus.
 * The registering DtoObjects need to have their listening
 * methods marked with the com.google.common.eventbus.Subscribe
 * annotation.
 */
public class DtoBusImpl implements DtoBus {

    private final EventBus eventBus = new EventBus();
    private final Map<Dto,Adapter> registered = new HashMap<>();

    public static class Adapter {

        private final Dto dto;

        public Adapter(final Dto dto) {
            this.dto = dto;
        }

        @Subscribe
        public void onRefresh(final DtoRefreshEvent dtoRefreshEvent) {
            dto.onRefresh(dtoRefreshEvent);
        }

    }

    @Override
    public void register(final Dto o) {
        final Adapter adapter = new Adapter(o);
        this.registered.put(o, adapter);
        this.eventBus.register(adapter);
    }

    @Override
    public void unregister(final Dto o) {
        final Adapter adapter = this.registered.get(o);
        if (adapter != null) {
            this.eventBus.unregister(adapter);
            this.registered.remove(o);
        }
    }

    @Override
    public void post(final DtoRefreshEvent event) {
        this.eventBus.post(event);
    }

    @Override
    public int count() {
        return this.registered.size();
    }

}
