/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 *
 */
public class DtoValuesTest {

    @Test
    public void testRelations() throws Exception {

        final Dto inner = toDto(this.newContact1());
        final Dto outer = toDto(this.newContact2()).setThis("partner", inner);

        assumeThat(inner.isDirty(),is(true));
        assertThat(outer.isDirty(),is(true));

        outer.acceptChanges();

        assumeThat(inner.isDirty(),is(false));
        assertThat(outer.isDirty(),is(false));

        inner.set("email", "newemail@email.com");

        assumeThat(inner.isDirty(),is(true));
        assertThat(outer.isDirty(),is(true));

    }

    @Test
    public void testCycles() throws Exception {

        /*
         * TODO: Test fails. No cycle detection at the moment. Not clear how to
         * add it either! Needs to also be added to DtoCollections too. Perhaps
         * it can be added to the DtoValue instead of the DtoObject/Collection?
         *
         * What you would be saying is that a given DtoValue would only pass a
         * command down once per 'run'. The question is how do you reset it per
         * run? You'd have to cache the value also to avoid the return vals
         * being screwy.
         */

        final Dto inner = toDto(this.newContact1());
        final Dto outer = toDto(this.newContact2());

        outer.set("partner",inner);
        inner.set("partner",outer);

        outer.acceptChanges();

        assumeThat(inner.isDirty(),is(false));
        assertThat(outer.isDirty(),is(false));

        inner.set("email","newemail@email.com");

        assumeThat(inner.isDirty(),is(true));
        assertThat(outer.isDirty(),is(true));

    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact1() throws Exception {
        return new ContactData("LUK03", "Name", "luk@address.com", "555-3234", true, 10, null, null);
    }

    private ContactData newContact2() throws Exception {
        return new ContactData("LAN04", "Name", "lan@address.com", "555-4234", true, 5, null, null);
    }

}
