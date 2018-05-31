/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.elastic;

import org.elasticsearch.action.bulk.BulkProcessor;

/**
 *
 */
public interface ElasticBulkCommand {

    int process(BulkProcessor elastic);

}
