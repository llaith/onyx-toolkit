package org.llaith.onyx.toolkit.support.elastic.geo;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.llaith.onyx.toolkit.results.ResultList;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GeoBoundingBoxSearchBuilder {

    public static QuerySetter query(final GeoBoundingBoxRequest request) {

        return new Holder(request);

    }

    public interface QuerySetter {

        FieldSetter withQuery(SearchRequestBuilder requestBuilder);

    }

    public interface FieldSetter {

        Executor onField(String field);

    }

    public interface Executor {

        <X> ResultList<X> execute(final Function<SearchHit, X> fn);

    }


    private static class Holder implements QuerySetter, FieldSetter, Executor {

        private String field;

        private SearchRequestBuilder searchRequestBuilder;

        private final GeoBoundingBoxRequest request;

        Holder(final GeoBoundingBoxRequest request) {

            this.request = request;

        }


        @Override
        public Executor onField(final String field) {

            this.field = field;

            return this;

        }

        @Override
        public FieldSetter withQuery(final SearchRequestBuilder requestBuilder) {

            this.searchRequestBuilder = requestBuilder;
            
            return this;
        }

        @Override
        public <X> ResultList<X> execute(final Function<SearchHit, X> fn) {

            this.searchRequestBuilder.setQuery(
                    this.constructQuery());

            return new ResultList<>(StreamSupport.stream(
                    this.searchRequestBuilder
                            .execute()
                            .actionGet()
                            .getHits().spliterator(),
                    false).map(fn).collect(Collectors.toList()));

        }

        private QueryBuilder constructQuery() {

            return QueryBuilders.geoBoundingBoxQuery(field)
                                .topLeft(new GeoPoint(request.getNorthWestLanLon()))
                                .bottomRight(new GeoPoint(request.getSouthEastLanLon()));

        }

    }

}
