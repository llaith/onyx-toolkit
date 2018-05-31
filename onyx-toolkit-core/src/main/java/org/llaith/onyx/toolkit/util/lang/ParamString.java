/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Replaces ParamString. Bit weird but allows consistent static and non-static usage efficiently as possible with
 * similar phrasing.
 */
public class ParamString {

    public static ImmutableMap.Builder<String,Object> paramsOf() {
        return ImmutableMap.builder();
    }

    // these are convenience functions, not really necessary, but help typing issues with pre-v8 type detection.
    public static Map<String,Object> paramsOf(String k0, Object v0) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        return map;
    }

    public static Map<String,Object> paramsOf(String k0, Object v0, String k1, Object v1) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }


    public static Map<String,Object> paramsOf(String k0, Object v0, String k1, Object v1, String k2, Object v2) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @SuppressWarnings("squid:S00107")
    public static Map<String,Object> paramsOf(
            String k0, Object v0, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @SuppressWarnings("squid:S00107")
    public static Map<String,Object> paramsOf(
            String k0, Object v0, String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4,
            Object v4) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @SuppressWarnings("squid:S00107")
    public static Map<String,Object> paramsOf(
            String k0, Object v0, String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4,
            Object v4, String k5, Object v5) {
        Map<String,Object> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    private final String template;
    private final Pattern pattern;

    public ParamString(final String template) {

        this.template = template;

        this.pattern = Pattern.compile("\\$\\{([^}]*)\\}");

    }
    
    public List<String> extractParams() {

        return StringUtil.extractParams(
                this.pattern.matcher(this.template));
        
    }

    public String resolveWith(final Map<String,Object> params) {

        return StringUtil.interpolate(
                this.pattern.matcher(this.template),
                params);

    }

    @SuppressWarnings("squid:S106")
    public static void main(String[] args) {

        final String s = new ParamString("The ${speed} brown ${animal} jumped over the lazy dog.")
                                 .resolveWith(paramsOf(
                                         "speed", "quick",
                                         "animal", "fox"));


        System.out.println("Result: " + s);

        final ParamString ps2 = new ParamString("The ${speed} brown ${animal} ate up the lazy dog.");
        String s2 = ps2.resolveWith(paramsOf(
                "speed", "slow",
                "animal", "bear"));

        System.out.println("Result: " + s2);


        final ParamString test = new ParamString("INSERT INTO ${table} (${cols}) " +
                        "SELECT ${vars} " +
                        "WHERE NOT EXISTS (SELECT 1 FROM ${checktable} WHERE 1 = 1 AND ${wheres})");

        System.out.println("Found: "+test.extractParams());

    }

}
