/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.io;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 */
public class UriEncodeUtil {

    public static final String UTF_8 = "UTF-8";

    /**
     * perform URL encoding
     * <p>
     * TODO: this whole thing is kinda crappy.
     *
     * @return
     */
    private static String encodeUrlPart(final String part) throws UnsupportedEncodingException {
        //we need to go through and check part by part that a section does not need encoding

        int pos = 0;
        for (int i = 0; i < part.length(); ++i) {
            char c = part.charAt(i);
            if (c == '/') {
                if (pos != i) {
                    String original = part.substring(pos, i - 1);
                    String encoded = URLEncoder.encode(original, UTF_8);
                    if (!encoded.equals(original)) {
                        return realEncode(part, pos);
                    }
                }
                pos = i + 1;
            } else if (c == ' ') {
                return realEncode(part, pos);
            }
        }
        if (pos != part.length()) {
            String original = part.substring(pos);
            String encoded = URLEncoder.encode(original, UTF_8);
            if (!encoded.equals(original)) {
                return realEncode(part, pos);
            }
        }
        return part;
    }

    private static String realEncode(String part, int startPos) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(part.substring(0, startPos));
        int pos = startPos;
        for (int i = startPos; i < part.length(); ++i) {
            char c = part.charAt(i);
            if (c == '/') {
                if (pos != i) {
                    String original = part.substring(pos, i - 1);
                    String encoded = URLEncoder.encode(original, UTF_8);
                    sb.append(encoded);
                    sb.append('/');
                    pos = i + 1;
                }
            }
        }

        String original = part.substring(pos);
        String encoded = URLEncoder.encode(original, UTF_8);
        sb.append(encoded);
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            System.out.println("Value: "+encodeUrlPart(":b/"));
            System.out.println("Value: "+URLEncoder.encode(":b/", UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
}
