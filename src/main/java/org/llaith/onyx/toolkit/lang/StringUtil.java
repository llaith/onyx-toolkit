/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * I prefer my own to the ones in the other libs.
 */
public final class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    // Use this in pref to String.format when you don't want a new Formatter() created each time.
    private static final Formatter formatter = new Formatter();

    private StringUtil() {
    }

    public static boolean notBlankOrNull(@Nullable final String s) {
        return !isBlankOrNull(s);
    }

    public static boolean isBlankOrNull(@Nullable final String s) {
        return s == null || s.trim()
                             .length() < 1;
    }

    public static boolean notEmptyOrNull(@Nullable final String s) {
        return !isEmptyOrNull(s);
    }

    public static boolean isEmptyOrNull(@Nullable final String s) {
        return s == null || s.length() < 1;
    }

    public static boolean isEquals(final String s1, final String s2) {
        return Guard.nullToBlank(s1).equals(s2);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "squid:S2201"}) // i know what i'm doing
    public static boolean isValidInteger(final String s) {

        if (isBlankOrNull(s)) return false;

        try {

            Integer.parseInt(s);

        } catch (NumberFormatException e) {

            return false;

        }

        return true;

    }

    public static int toInt(final String s, final String msg) {

        try {

            return Integer.parseInt(Guard.notBlankOrNull(s));

        } catch (IllegalArgumentException e) {

            logger.warn("Cuaght exception:", e);

            throw new NumberFormatException(msg);

        }

    }

    public static boolean isUppercase(@Nullable final String s) {

        // done this way which is less efficient because it cannot bail-out early, 
        // but avoids problems with non-english charsets and numbers and punctuation being false for isUpperCase(char).
        return s != null && (s.toUpperCase().equals(s));

    }

    public static boolean isLowercase(@Nullable final String s) {

        // done this way which is less efficient because it cannot bail-out early, 
        // but avoids problems with non-english charsets and numbers and punctuation being false for isUpperCase(char).
        return s != null && (s.toLowerCase().equals(s));

    }

    @Nonnull
    public static String uppercaseFirstLetter(@Nonnull final String s) {
        return Guard.notNull(s)
                    .substring(0, 1)
                    .toUpperCase() + s.substring(1);
    }

    @Nonnull
    public static String lowercaseFirstLetter(@Nonnull final String s) {
        return Guard.notNull(s)
                    .substring(0, 1)
                    .toLowerCase() + s.substring(1);
    }

    @Nonnull
    public static String dropLastLetter(@Nonnull final String s) {
        return Guard.notNull(s)
                    .substring(0, s.length() - 1);
    }

    @Nonnull
    public static String pluralize(@Nonnull final String s) {
        if (s.endsWith("y")) return s.substring(0, s.length() - 1) + "ies"; // propert(y)[ies]
        if (s.endsWith("s")) return s.substring(0, s.length()) + "es"; // Process[es]
        else return dropLastLetter(s);
    }

    @Nonnull
    public static String depluralize(@Nonnull final String s) {
        if (s.endsWith("ies")) return s.substring(0, s.length() - 3) + "y"; // propert(ies)[y]
        if (s.endsWith("ses")) return s.substring(0, s.length() - 2); // Process(es)
        else return dropLastLetter(s);
    }

    @Nullable
    public static String propertyizeMethodName(@Nonnull final String s) {
        if (s.startsWith("get")) return s.substring(3);
        if (s.startsWith("set")) return s.substring(3);
        if (s.startsWith("is")) return s.substring(2);
        return null;
    }

    public static boolean isValidJavaIdentifier(@Nonnull final String str) {

        if (Guard.blankToNull(str) == null) return false;

        final char[] arr = str.toCharArray();
        if (!Character.isJavaIdentifierStart(arr[0])) return false;

        for (int i = 1; i < arr.length; i++) {
            if (!Character.isJavaIdentifierPart(arr[i])) return false;
        }

        return true;
    }

    public static int countLeadingSpaces(final String line) {
        int count = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') count++;
            else break;
        }

        return count;
    }

    public static int countLeadingWhitepace(final String line) {
        int count = 0;

        for (int i = 0; i < line.length(); i++) {
            if (Character.isWhitespace(line.charAt(i))) count++;
            else break;
        }

        return count;
    }

    /**
     * Replaced my own util with this. Use directly except where compatibility is required.
     */
    @Nonnull
    public static List<String> splitAtLength(@Nonnull final String s, final int charLimit) {
        return Lists.newArrayList(Splitter.fixedLength(charLimit)
                                          .split(s));
    }

    public static String[] splitIntoLines(final String s) {
        return splitIntoLines(s, false);
    }

    public static String[] splitIntoLines(final String s, final boolean skipBlankLines) {
        return skipBlankLines ?
                s.split("[\\r\\n]+") :
                s.split("\\r?\\n");
    }

    public static List<String> wrap(final String text, final int maxLength) {

        final int max = maxLength < 1 ? 1024 : maxLength;
        final int length = Guard.notNull(text)
                                .length();

        final List<String> lines = new LinkedList<>();

        int pos = 0;
        while ((length - pos) > max) {

            if (text.charAt(pos) == ' ') {
                pos++;
                continue;
            }

            int nextBreak = text.lastIndexOf(' ', max + pos);

            if (nextBreak >= pos) {
                lines.add(text.substring(pos, nextBreak));
                pos = nextBreak + 1;
            } else { // long words get split at length
                lines.add(text.substring(pos, max + pos));
                pos += max;
            }
        }

        // left over
        lines.add(text.substring(pos));

        return lines;
    }

    public static List<String> reflowTextToLines(final String text, final int maxLength) {

        final List<String> lines = new LinkedList<>();

        for (final String line : StringUtil.splitIntoLines(text)) {
            lines.addAll(wrap(line, maxLength));
        }

        return lines;
    }

    public static String reflowText(final String text, final int maxLength) {
        return Joiner.on("\n")
                     .join(reflowTextToLines(text, maxLength));
    }

    // a bit wasteful, throws away a matcher, use the InterpolatedString for better memory use
    public static String interpolate(final String template, Map<String,Object> params) {

        return interpolate(
                Pattern.compile("\\$\\{([^}]*)\\}")
                       .matcher(template),
                params);

    }

    public static <V> String interpolate(final Matcher matcher, Map<String,V> params) {

        final Map<String,V> replacements = Guard.notNull(params);

        final StringBuffer sb = new StringBuffer(); // api needs a stringbuffer!
        while (matcher.find()) {
            final Object value = replacements.get(matcher.group(1));
            if (value != null) matcher.appendReplacement(sb, value.toString());
        }
        matcher.appendTail(sb);

        return sb.toString();

    }

    public static List<String> extractParams(final Matcher matcher) {

        final List<String> found = new ArrayList<>();

        while (matcher.find()) {

            found.add(matcher.group(1));

        }

        return found;

    }

    public static String randomAlphaString(final Random rnd, final int len) {

        return randomString(
                rnd,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                len);

    }

    public static String randomAlphaNumericString(final Random rnd, final int len) {

        return randomString(
                rnd,
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                len);

    }

    /*
     * From here: http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     *
     * SecureRandom passed because it's expensive and the holder should hold it (or declare inline if
     * they don't care).
     */
    public static String randomString(final Random rnd, final String charset, final int len) {

        final StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) sb.append(charset.charAt(rnd.nextInt(charset.length())));

        return sb.toString();
    }

    /*
     * From here: http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     *
     * This works by choosing 130 bits from a cryptographically secure random bit generator, and encoding them in
     * base-32. 128 bits is considered to be cryptographically strong, but each digit in a base 32 number can encode
     * 5 bits, so 128 is rounded up to the next multiple of 5. This encoding is compact and efficient, with 5 random
     * bits per character. Compare this to a random UUID, which only has 3.4 bits per character in standard layout,
     * and only 122 random bits in total.
     */
    public static String sessionId(final SecureRandom rnd) {
        return new BigInteger(130, rnd).toString(32);
    }

    /*
     * These are here because I use them all the time and it's unwieldy using them in the raw form
     */
    public static String snakeCaseToCamelCase(final String in) {

        return CaseFormat.LOWER_UNDERSCORE.to(
                CaseFormat.LOWER_CAMEL,
                in);

    }

    public static String camelCaseToSnakeCase(final String in) {

        return CaseFormat.LOWER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE,
                in);

    }

    public static String toSingleLine(final String in) {

        return in
                .replaceAll("\n", " ")
                .replaceAll("\t", " ");

    }

    public static String[] tokenize(final String s) {

        if (s == null) return null;

        return s.split("\\s+");

    }

    public static boolean equalsIgnoreCase(final String str1, final String str2) {

        return equalsIgnoreCase(str1, str2, Locale.getDefault());

    }

    @SuppressWarnings("squid:S1698") // i know what i'm doing
    public static boolean equalsIgnoreCase(final String str1, final String str2, final Locale locale) {

        if (str1 == null || str2 == null) return str1 == str2;

        return str1 == str2 || str1.length() == str2.length() && str1.toLowerCase(locale).equals(str2.toLowerCase(locale));

    }

    public static String fromInputStream(final InputStream in) {

        return fromInputStream(in, StandardCharsets.UTF_8);

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String fromInputStream(final InputStream in, Charset charSet) {

        byte[] bytes = new byte[0];

        try {
            int n = in.available();
            bytes = new byte[n];
            in.read(bytes, 0, n);

        } catch (IOException e) {
            logger.error("Caught exception: ", e);
        }

        return new String(bytes, charSet);

    }

    // Todo, move to utils
    public static int countMatches(String str, String sub) {

        if (StringUtil.isBlankOrNull(str) || StringUtil.isBlankOrNull(sub)) return 0;

        int count = 0;
        int idx = 0;

        while ((idx = str.indexOf(sub, idx)) != -1) {

            count++;

            idx += sub.length();

        }

        return count;

    }

    // Todo, move to utils
    public static String[] splitColumns(String str, String token, int cols) {

        if (StringUtil.isBlankOrNull(str) || StringUtil.isBlankOrNull(token)) return null;

        final String[] splits = new String[cols];

        int count = 0;
        int idx = 0;
        int last = 0;

        while ((idx = str.indexOf(token, idx)) != -1) {

            splits[count] = str.substring(last, idx);

            last = idx + 1;
            idx += token.length();

            count++;

            if (count == cols - 1) {
                splits[count] = str.substring(idx); // append the rest
                break;
            }

        }

        if (count < cols - 1) return null; // not enough tokens

        return splits;

    }


    public static String unquoteString(final String in) {

        boolean ok = (in != null) && (in.length() >= 2) && in.startsWith("\"") && in.endsWith("\"");

        if (!ok) return in;

        return in.substring(1, in.length() - 1);

    }

    public static String encodeUtf8(final String text) {

        try {

            return URLEncoder.encode(text, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public static String decodeUtf8(final String text) {

        try {

            return URLDecoder.decode(text, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public static String replaceTabsWithSpaces(final String text, final int indentChars) {

        final String tabspace = Strings.repeat(" ", indentChars);

        return Guard.notNull(text).replaceAll("\\t", tabspace);

    }

    // although this class will fill up in future, this method exists to remind me of the most
    // efficient (non-array-copy) and best way to do it.
    public static StringBuilder chop(final StringBuilder sb, final int delChars) {
        sb.setLength(Math.max(sb.length() - delChars, 0));
        return sb;
    }

}
