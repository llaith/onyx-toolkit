package org.llaith.onyx.toolkit.support.elastic.geo;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;

public class GeoRadiusRequest {

    private String latLon;

    private double radius;

    private DistanceUnit unit;

    private GeoPoint geoPoint;

    public GeoRadiusRequest(final String latLon, final double radius, final DistanceUnit unit) {

        this.latLon = latLon;

        this.radius = radius;

        this.unit = unit;

        this.geoPoint = GeoPoint.parseFromLatLon(this.latLon);

    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getLatLon() {
        return latLon;
    }

    public void setLatLon(String latLon) {
        this.latLon = latLon;
    }

    public DistanceUnit getUnit() {
        return unit;
    }

    public void setUnit(DistanceUnit unit) {
        this.unit = unit;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
