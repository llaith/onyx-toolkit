package org.llaith.onyx.toolkit.support.elastic.geo;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.llaith.onyx.toolkit.results.ResultList;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GeoRadiusSearchBuilder {

    public static QuerySetter query(final GeoRadiusRequest request) {

        return new Holder(request);

    }

    public interface QuerySetter {

        FieldSetter withQuery(SearchRequestBuilder requestBuilder);

    }

    public interface FieldSetter {

        OrderSetter onField(String field);

    }

    public interface OrderSetter {

        Executor orderBy(SortOrder order);

    }

    public interface Executor {

        <X> ResultList<X> execute(Function<SearchHit, X> fn);

    }

    private static class Holder implements FieldSetter, OrderSetter, QuerySetter, Executor {

        private final GeoRadiusRequest request;

        private SearchRequestBuilder searchRequestBuilder;

        private String field;

        private SortOrder order;

        Holder(final GeoRadiusRequest request) {
            this.request = request;
        }

        @Override
        public FieldSetter withQuery(final SearchRequestBuilder requestBuilder) {

            this.searchRequestBuilder = requestBuilder;

            return this;
        }

        @Override
        public OrderSetter onField(final String field) {

            this.field = field;

            return this;

        }

        @Override
        public Executor orderBy(final SortOrder order) {

            this.order = order;

            return this;
        }

        @Override
        public <X> ResultList<X> execute(Function<SearchHit, X> fn) {

            this.searchRequestBuilder.setQuery(this.constructQuery(field))
                                     .addSort(this.constructSort(field, order));

            return new ResultList<>(StreamSupport
                    .stream(
                            this.searchRequestBuilder
                                    .execute()
                                    .actionGet()
                                    .getHits().spliterator(),
                            false)
                    .map(fn)
                    .collect(Collectors.toList()));

        }


        private QueryBuilder constructQuery(final String fieldName) {

            GeoPoint point = request.getGeoPoint();

            return QueryBuilders
                    .geoDistanceQuery(fieldName)
                    .point(point.lat(), point.lon())
                    .distance(request.getRadius(), request.getUnit());
        }

        private GeoDistanceSortBuilder constructSort(final String fieldName, final SortOrder order) {

            return SortBuilders
                    .geoDistanceSort(fieldName)
                    .unit(request.getUnit())
                    .points(request.getGeoPoint())
                    .order(order)
                    .geoDistance(GeoDistance.ARC);

        }

    }

}
