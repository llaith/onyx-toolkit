package org.llaith.onyx.toolkit.geo.uk;


import org.llaith.onyx.toolkit.geo.GeoPoint;

import javax.validation.constraints.NotNull;

/**
 * http://webarchive.nationalarchives.gov.uk/20160105160709/http://www.ons.gov.uk/ons/guide-method/geography/beginner-s-guide/postal/index.html
 */
public class GeoLocatedUkPostcode {

    @NotNull
    private UkPostcode.Type type;

    @NotNull
    private String area; // EH (124 of)

    @NotNull
    private String district; // EH1 (3114 of)

    private String sector; // EH1 2 (12,381 of)

    private String postcode; // EH1 234 (~1.75 mil of)

    @NotNull
    private GeoPoint geopoint;

    public GeoLocatedUkPostcode() {
        super();
    }

    public GeoLocatedUkPostcode(UkPostcode info, GeoPoint point) {
        this(info.type, info.area, info.district, info.sector, info.postcode, point);
    }

    public GeoLocatedUkPostcode(UkPostcode.Type type, String area, String district, String sector, String postcode, GeoPoint point) {
        this.area = area;
        this.district = district;
        this.sector = sector;
        this.postcode = postcode;
        this.type = type;
        this.geopoint = point;
    }

    public String getArea() {
        return area;
    }

    public void setArea(final String area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(final String sector) {
        this.sector = sector;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public UkPostcode.Type getType() {
        return type;
    }

    public void setType(UkPostcode.Type type) {
        this.type = type;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "GeoLocatedUkPostcode{" +
                "postcode=" + postcode +
                ", type=" + type +
                ", point=" + geopoint;
    }
}
