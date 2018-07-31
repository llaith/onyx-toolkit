/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 *
 */
public class DtoCollectionTest {

    @Test
    public void removingElementsThenAcceptingShrinksAndDirtiesTheCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();
        final Dto two = toDto(newChild2()).acceptThis();

        assumeThat(one.isDirty() || two.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(
                one,
                two)).remove(one);

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(1));
        assertThat(children.contains(two),is(true));

        children.acceptChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(1));
        assertThat(children.contains(two),is(true));
    }

    @Test
    public void addingElementsThenAcceptingExpandsAndDirtiesTheCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();
        final Dto two = toDto(newChild2()).acceptThis();

        assumeThat(one.isDirty() || two.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(one)).add(two);

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(2));
        assertThat(children.contains(two),is(true));

        children.acceptChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(2));
        assertThat(children.contains(two),is(true));
    }

    @Test
    public void removingElementThenCancellingDoesNotChangeOrDirtyCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();
        final Dto two = toDto(newChild2()).acceptThis();

        assumeThat(one.isDirty() || two.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(
                one,
                two)).remove(one);

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(1));
        assertThat(children.contains(two),is(true));

        children.cancelChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(2));
        assertThat(children.contains(two),is(true));
        assertThat(children.contains(one),is(true));
    }

    @Test
    public void addingElementThenCancellingDoesNotChangeOrDirtyCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();
        final Dto two = toDto(newChild2()).acceptThis();

        assumeThat(one.isDirty() || two.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(one)).add(two);

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(2));
        assertThat(children.contains(two),is(true));

        children.cancelChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(1));
        assertThat(children.contains(two),is(false));
    }

    @Test
    public void changingNestedDtoThenAcceptingDirtiesCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();

        assumeThat(one.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(one));

        one.setThis("email", "changed@email.com");

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(1));

        children.acceptChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(1));
        assertThat(one.get("email", String.class),is(equalTo("changed@email.com")));
    }

    @Test
    public void changingNestedDtoThenCancellingDoesNotChangeOrDirtyCollection() throws Exception {

        final Dto one = toDto(newChild1()).acceptThis();

        assumeThat(one.isDirty(),is(false));

        final DtoCollection children = new DtoCollection(Arrays.asList(one));

        one.set("email", "changed@email.com");

        assertThat(children.isDirty(),is(true));
        assertThat(children.size(),is(1));

        children.cancelChanges();

        assertThat(children.isDirty(),is(false));
        assertThat(children.size(),is(1));
        assertThat(one.get("email", String.class),is(equalTo("luk@address.com")));
    }

    @Test
    public void mutatingThenAcceptingNestedCollectionDirtiesParentDto() throws Exception {

        final Dto master = toDto(newContact1()).acceptThis();

        assumeThat(master.isDirty(),is(false));

        final Dto one = master.get("relations", DtoCollection.class).getAllAsList().get(0);

        one.set("email", "changed@email.com");

        assertThat(master.isDirty(),is(true));
        assertThat(master.acceptThis().isDirty(),is(false));
    }

    @Test
    public void mutatingThenCancellingNestedCollectionDoesNotDirtiesParentDto() throws Exception {

        final Dto master = toDto(newContact1()).acceptThis();

        assumeThat(master.isDirty(),is(false));

        final Dto one = master.get("relations", DtoCollection.class).getAllAsList().get(0);

        one.set("email", "changed@email.com");

        assertThat(master.isDirty(),is(true));
        assertThat(master.acceptThis().isDirty(),is(false));
    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact1() {
        return new ContactData(
                "LAU02",
                "Name",
                "lua@address.com",
                "555-2234",
                true,
                30,
                null,
                new HashSet<>(Arrays.asList(newChild1())));
    }

    private ContactData newChild1() {
        return new ContactData("LUK03", "Name", "luk@address.com", "555-3234", true, 10, null, null);
    }

    private ContactData newChild2() {
        return new ContactData("LAN04", "Name", "lan@address.com", "555-4234", true, 5, null, null);
    }

}
