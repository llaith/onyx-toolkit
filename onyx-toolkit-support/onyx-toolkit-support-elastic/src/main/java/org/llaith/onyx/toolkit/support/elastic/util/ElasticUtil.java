package org.llaith.onyx.toolkit.support.elastic.util;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *
 */
public class ElasticUtil {

    public static <X> List<X> query(SearchRequestBuilder request, final Function<SearchHit,X> fn) {

        return StreamSupport.stream(request.execute()
                                           .actionGet()
                                           .getHits().spliterator(), false)
                            .map(fn)
                            .collect(Collectors.toList());

    }

}
