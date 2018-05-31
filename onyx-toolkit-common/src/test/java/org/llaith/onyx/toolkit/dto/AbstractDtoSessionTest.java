/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;


import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;
import org.llaith.onyx.toolkit.dto.session.DtoSession;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 */
public class AbstractDtoSessionTest {

    protected DtoSession sessions;

    public void simplePropertyShouldSyncThroughSession() throws Exception {

        final Dto a = sessions.addDto(toDto(newContact1()).acceptThis());
        final Dto b = sessions.addDto(toDto(newContact1()).acceptThis());

        a.setThis("email", "none").acceptChanges();

        assertThat(b.get("email", String.class), is(equalTo("none")));
    }

    public void nestedPropertyShouldSyncThroughSession() throws Exception {

        final Dto a = sessions.addDto(toDto(newContact1()).acceptThis());
        final Dto b = sessions.addDto(toDto(newContact1()).acceptThis());

        a.get("partner", Dto.class).setThis("phone", "none").acceptChanges();

        assertThat(b.get("partner", Dto.class).get("phone", String.class), is(equalTo("none")));
    }

    public void nestedSetsShouldSyncThroughSession() throws Exception {

        final Dto a = sessions.addDto(toDto(newContact1()).acceptThis());
        final Dto b = sessions.addDto(toDto(newContact1()).acceptThis());

        a.get("partner", Dto.class).get("relations", DtoCollection.class).clear();
        a.acceptChanges();

        assertThat(b.get("partner", Dto.class).get("relations", DtoCollection.class).size(), is(equalTo(0)));
    }

    public void nestedSetsShouldSyncThroughSessionWithProperties() throws Exception {

        final Dto a = sessions.addDto(toDto(newContact1()).acceptThis());
        final Dto b = sessions.addDto(toDto(newContact1()).acceptThis());

        a.get("partner", Dto.class).get("relations", DtoCollection.class).getAllAsList().get(0).setThis("phone", "none")
                .acceptChanges();

        assertThat(
                b.get("partner", Dto.class).get("relations", DtoCollection.class).getAllAsList().get(0).get("phone", String.class),
                is(equalTo("none")));
    }

    public void builderInstancesAreAutoAcceptedWhenAddedToSessions() throws Exception {

        final Dto contact = toDto(newContact1());
        assertThat(contact.isNew(), is(true));

        this.sessions.addDto(contact);
        assertThat(contact.isNew(), is(false));

    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact1() throws Exception {

        return new ContactData(
                "NOS01",
                "Name",
                "nos@address.com",
                "555-1234",
                true,
                40,
                newContact2(),
                null);

    }

    private ContactData newContact2() throws Exception {

        return new ContactData(
                "LAU02",
                "Name",
                "lau@address.com",
                "555-2234",
                true,
                30,
                null,
                new HashSet<>(Arrays.asList(
                        newContact3())));

    }

    private ContactData newContact3() throws Exception {

        return new ContactData(
                "LAN04",
                "Name",
                "lan@address.com",
                "555-4234",
                true,
                5,
                null,
                null);

    }

}
