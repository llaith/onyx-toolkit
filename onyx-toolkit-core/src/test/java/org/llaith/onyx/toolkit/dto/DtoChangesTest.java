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
public class DtoChangesTest {

    @Test
    public void acceptingChangedInstancesDoesNothingWeird() throws Exception {
        assertThat(
                toDto(newContact())
                        .setThis("email", "newemail@address.com")
                        .acceptThis()
                        .get("email", String.class),
                is(equalTo("newemail@address.com")));
    }

    @Test
    public void cancellingAcceptedChangesDoesNothing() throws Exception {
        assertThat(
                toDto(newContact())
                        .setThis("email", "newemail@address.com")
                        .acceptThis()
                        .cancelThis()
                        .get("email", String.class),
                is(equalTo("newemail@address.com")));
    }

    @Test
    public void cancellingRollsBackToLastAcceptedChanges() throws Exception {
        assertThat(
                toDto(newContact())
                        .setThis("email", "newemail@address.com")
                        .acceptThis()
                        .setThis("email", "neweremail@address.com")
                        .cancelThis()
                        .get("email", String.class),
                is(equalTo("newemail@address.com")));
    }

    @Test
    public void theFirstAcceptFlagsTheObjectAsNotNew() throws Exception {
        assumeThat(
                toDto(newContact()).isNew(),
                is(true));

        assertThat(
                toDto(newContact()).acceptThis().isNew(),
                is(false));
    }

    @Test
    public void aCancelOnNewInstanceIsOkButClearsIt() throws Exception {

        final Dto target = toDto(this.newContact());

        assumeThat(
                target.isNew(),
                is(true));

        assertThat(
                target.setThis("name","joe").get("name",String.class),
                is(equalTo("joe")));

        assumeThat(
                target.cancelThis().isNew(),
                is(true));

        assertThat(
                target.get("name"),
                is(nullValue()));
    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact() throws Exception {

        return new ContactData(
                "NOS01",
                "Name",
                "email@address.com",
                "555-1234",
                true,
                40,
                null,
                null);

    }

}
