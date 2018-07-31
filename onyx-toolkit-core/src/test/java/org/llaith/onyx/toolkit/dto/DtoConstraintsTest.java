/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeThat;


/**
 * Test the basic change behaviour and builder transitions.
 */
public class DtoConstraintsTest {

    @Test(expected = IllegalStateException.class)
    public void clearingRequiredValuesWillErrorInIdentifiedMode() throws Exception {
        toDto(newContact())
                .acceptThis()
                .setThis("email", null)
                .acceptChanges();
    }

    @Test
    public void clearingRequiredValuesIsOkWhenNew() throws Exception {
        assertThat(
                toDto(newContact()).setThis("email", null).get("email", String.class),
                is(nullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void identifiedInstancesDontAllowContantsToChange() throws Exception {
        toDto(newContact())
                .acceptThis()
                .setThis("name", "New Name")
                .acceptChanges();
    }

    @Test
    public void builderInstancesDoAllowConstantsToChange() throws Exception {
        assertThat(
                toDto(newContact()).setThis("name", "New Name").get("name", String.class),
                is(equalTo("New Name")));
    }

    @Test
    public void changingCurrentValuesWorksAsExpected() throws Exception {
        assumeThat(
                toDto(newContact()).setThis("name", "New Name").get("name", String.class),
                is(equalTo("New Name")));
    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact() throws Exception {
        return new ContactData("NOS01", "Name", "email@address.com", "555-1234", true, 40, null, null);
    }

}
