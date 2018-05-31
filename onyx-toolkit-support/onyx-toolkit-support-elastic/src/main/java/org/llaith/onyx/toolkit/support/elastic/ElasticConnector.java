/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.elastic;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;

import java.util.function.Function;


/**
 *
 */
public class ElasticConnector {

    public int performBatch(
            final Client client,
            final Function<Client,BulkProcessor> processorFactory,
            final ElasticBulkCommand command) {

        try (final ElasticClientSession elastic = new ElasticClientSession(client)) {

            return elastic.open(processorFactory).perform(command);

        }

    }

}
