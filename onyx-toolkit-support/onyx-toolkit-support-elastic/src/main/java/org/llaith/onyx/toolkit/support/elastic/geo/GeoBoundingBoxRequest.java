package org.llaith.onyx.toolkit.support.elastic.geo;

public class GeoBoundingBoxRequest {

    private String northWestLanLon;
    private String southEastLanLon;

    public GeoBoundingBoxRequest(String northWestLanLon, String southEastLanLon) {
        this.northWestLanLon = northWestLanLon;
        this.southEastLanLon = southEastLanLon;
    }

    public String getNorthWestLanLon() {
        return northWestLanLon;
    }

    public void setNorthWestLanLon(String northWestLanLon) {
        this.northWestLanLon = northWestLanLon;
    }

    public String getSouthEastLanLon() {
        return southEastLanLon;
    }

    public void setSouthEastLanLon(String southEastLanLon) {
        this.southEastLanLon = southEastLanLon;
    }

}
