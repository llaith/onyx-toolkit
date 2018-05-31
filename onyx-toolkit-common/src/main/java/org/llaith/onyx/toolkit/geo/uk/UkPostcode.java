package org.llaith.onyx.toolkit.geo.uk;

/**
 *
 */
public class UkPostcode {

    public enum Type {
        OUTCODE, POSTCODE
    }

    public final Type type;
    public final String area; // EH (124 of)
    public final String district; // EH1 (3114 of)
    public final String sector; // EH1 2 (12,381 of)
    public final String postcode; // EH1 234 (~1.75 mil of)

    public UkPostcode(final String area, final String district, final String sector, final String postcode) {
        this.type = Type.POSTCODE;
        this.area = area;
        this.district = district;
        this.sector = sector;
        this.postcode = postcode;
    }

    public UkPostcode(final String area, final String district) {
        this.type = Type.OUTCODE;
        this.area = area;
        this.district = district;
        this.sector = null;
        this.postcode = null;
    }

    @Override
    public String toString() {
        return "UkPostcode{" +
                "area='" + area + '\'' +
                ", district='" + district + '\'' +
                ", sector='" + sector + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
