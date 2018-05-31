/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.elastic;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.llaith.onyx.toolkit.util.lang.TypedAutoCloseable;

import java.io.IOException;
import java.util.function.Function;

/**
 *
 */
public class ElasticClientSession implements TypedAutoCloseable<IOException> {

    private Client client;

    public ElasticClientSession(final Client client) {
        this.client = client;
    }

    public ElasticBulkSession open(final Function<Client,BulkProcessor> processorFactory) {
        return new ElasticBulkSession(processorFactory.apply(client));
    }

    @Override
    public void close() {
        this.client.close();
    }

}
