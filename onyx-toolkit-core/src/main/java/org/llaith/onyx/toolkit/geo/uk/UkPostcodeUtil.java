package org.llaith.onyx.toolkit.geo.uk;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class UkPostcodeUtil {

    // http://stackoverflow.com/questions/164979/uk-postcode-regex-comprehensive
    private static final String UK_POSTCODE_VAIDATE_PATTERN = "^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$";

    // http://radderz.me.uk/2013/06/postcode-grouping-regular-expression-for-royal-mail-line-listing-reports/
    private static final String UK_POSTCODE_SPLIT_PATTERN = "([A-Z]{1,2})([0-9]{1,2}[A-Z]?)[ ]?([0-9])([A-Z]{2})";

    // outcodes are also known as 'postcode districts'
    private static final String UK_OUTCODE_VAIDATE_PATTERN = "^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y])))))$";

    private static final String UK_OUTCODE_SPLIT_PATTERN = "([A-Z]{1,2})([0-9]{1,2}[A-Z]?)";

    public static String validateUkPostcode(final String postcode) {

        final Matcher matcher = Pattern.compile(UK_POSTCODE_VAIDATE_PATTERN)
                                       .matcher(postcode.trim());

        if (!matcher.matches()) return null;

        return postcode.toUpperCase(Locale.ENGLISH).replaceAll("\\s", "");

    }

    public static UkPostcode validateAndSplitUkPostcode(final String postcode) {

        final String validated = (postcode != null) ?
                validateUkPostcode(postcode) :
                null;

        if (validated == null) return null;

        final Matcher matcher = Pattern.compile(UK_POSTCODE_SPLIT_PATTERN)
                                       .matcher(validated);

        if (!matcher.matches()) return null;

        return new UkPostcode(
                matcher.group(1),
                matcher.group(1) + matcher.group(2),
                matcher.group(1) + matcher.group(2) + " " + matcher.group(3),
                new StringBuilder(validated).insert(validated.length() - 3, " ").toString());

    }

    public static String validateUkOutcode(final String postcode) {

        final Matcher matcher = Pattern.compile(UK_OUTCODE_VAIDATE_PATTERN)
                                       .matcher(postcode.trim());

        if (!matcher.matches()) return null;

        return postcode.toUpperCase(Locale.ENGLISH).replaceAll("\\s", ""); // for trailing ones

    }

    public static UkPostcode validateAndSplitUkOutcode(final String postcode) {

        final String validated = (postcode != null) ?
                validateUkOutcode(postcode) :
                null;

        if (validated == null) return null;

        final Matcher matcher = Pattern.compile(UK_OUTCODE_SPLIT_PATTERN)
                                       .matcher(validated);

        if (!matcher.matches()) return null;

        return new UkPostcode(
                matcher.group(1),
                matcher.group(1) + matcher.group(2));

    }

}
