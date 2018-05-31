/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;


import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 *
 */
public class DtoRelease3PrimitivesTest {

    @Test
    public void testSupportPrimitives() throws Exception {

        final Dto dto = new DtoBuilder("DUMMY")
                .addField(new DtoField(
                        "prim",
                        int.class,
                        false,
                        false,
                        false))
                .addField(new DtoField(
                        "wrap",
                        Integer.class,
                        false,
                        false,
                        false))
                .build();

        dto.set("prim", 1);
        dto.set("wrap", 1);

        assertTrue(
                "Unboxing failure",
                dto.get("prim", int.class) == 1);

        assertTrue(
                "Unboxing failure",
                dto.get("wrap", int.class) == 1);

    }

    @Test
    public void testSupportWrappers() throws Exception {

        final Dto dto = new DtoBuilder("DUMMY")
                .addField(new DtoField(
                        "prim",
                        int.class,
                        false,
                        false,
                        false))
                .addField(new DtoField(
                        "wrap",
                        Integer.class,
                        false,
                        false,
                        false))
                .build();

        dto.set("prim", 1);
        dto.set("wrap", 1);

        assertThat(
                dto.get("prim", Integer.class),
                is(equalTo(1)));

        assertThat(
                dto.get("wrap", Integer.class),
                is(equalTo(1)));

    }
}
