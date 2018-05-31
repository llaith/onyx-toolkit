package org.llaith.onyx.toolkit.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GeoPoint {

    @JsonProperty
    private double lat;

    @JsonProperty
    private double lng;

    public GeoPoint setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public GeoPoint setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public static class GeoPointDeserializer extends JsonDeserializer<GeoPoint> {

        @Override
        public GeoPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            final String text = p.getText();

            try {

                final String[] coords = text.split(",");
                return new GeoPoint().setLat(Double.parseDouble(coords[0]))
                                     .setLng(Double.parseDouble(coords[1]));

            } catch (final RuntimeException e) {

                throw new IOException(String.format("Could not parse %s as Geopoint", text), e);

            }

        }
    }
    
}
