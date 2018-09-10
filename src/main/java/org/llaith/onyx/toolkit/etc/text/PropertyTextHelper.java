package org.llaith.onyx.toolkit.etc.text;

/**
 *
 */
public class PropertyTextHelper {

    private final int justify;

    private final String ansiProperty;
    private final String ansiValue;
    private final String ansiNote;

    public PropertyTextHelper(final int justify, final String ansiProperty, final String ansiValue, final String ansiNote) {
        this.justify = justify;
        this.ansiProperty = ansiProperty;
        this.ansiValue = ansiValue;
        this.ansiNote = ansiNote;
    }

    public String line(final String property, final Object value) {

        return line(property, value, "");

    }

    public String line(final String property, final Object value, final String note) {

        return String.format(ansiProperty + "%" + justify + "s: " + ansiValue + "%s" + ansiNote + " %s",
                             property,
                             value,
                             note);

    }

}
