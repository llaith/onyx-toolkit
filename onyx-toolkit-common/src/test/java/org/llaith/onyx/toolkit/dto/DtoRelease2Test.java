/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.ext.DtoTransferAdapter;
import org.llaith.onyx.toolkit.dto.ext.PojoDtoFactory;
import org.llaith.onyx.toolkit.dto.instances.AnonData;
import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;
import org.llaith.onyx.toolkit.transferobject.ValuesTransfer;
import org.llaith.onyx.toolkit.transferobject.impl.PojoTransferAdapter;

import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * Some extra tests to changes in Release2.
 */
public class DtoRelease2Test {

    @Test
    public void twoIdentifiableInstancesShouldBeEqualIfSameId() {

        final Dto data1 = toIncompleteDto(newContact("nos")).acceptThis();
        final Dto data2 = toIncompleteDto(newContact("nos")).acceptThis();

        assumeFalse(data1.isDirty());
        assumeFalse(data2.isDirty());

        assertThat(data1.identity(), is(equalTo(data2.identity())));

    }

    @Test
    public void twoIdentifiableInstancesShouldNotBeEqualIfNotSameId() {

        final Dto data1 = toIncompleteDto(newContact("nos1")).acceptThis();
        final Dto data2 = toIncompleteDto(newContact("nos2")).acceptThis();

        assumeFalse(data1.isDirty());
        assumeFalse(data2.isDirty());

        assertThat(data1.identity(), is(not(equalTo(data2.identity()))));

    }

    @Test
    public void twoUnidentifiableInstancesShouldNotBeEqual() {

        final DtoTransferAdapter dtoAdapter = new DtoTransferAdapter(PojoDtoFactory.newSupplier(AnonData.class));

        final PojoTransferAdapter<AnonData> pojoAdapter = new PojoTransferAdapter<>(AnonData.class);

        final ValuesTransfer<AnonData,Dto> transferrer = new ValuesTransfer<>(pojoAdapter, dtoAdapter);

        final Dto data1 = transferrer.transfer(new AnonData("Name")).acceptThis();
        final Dto data2 = transferrer.transfer(new AnonData("Name")).acceptThis();

        assumeFalse(data1.isDirty());
        assumeFalse(data2.isDirty());

        assertThat(data1, is(not(equalTo(data2))));

    }

    @Test
    public void uninitializedFieldsAreNotMerged() {

        Dto first = toCompleteDto(newContact("id01")).acceptThis();
        // separate checks because expensive and not useful outside of tests
        assumeFalse(first.isNew());
        assumeFalse(first.isDirty());
        assumeFalse(first.isStale());
        assumeFalse(first.isConflicted());

        Dto second = toIncompleteDto(newContact("id01")).acceptThis();
        assumeFalse(second.isNew());
        assumeFalse(second.isDirty());
        assumeFalse(second.isStale());
        assumeFalse(second.isConflicted());

        // first has a value
        assumeThat(first.get("extra", String.class), is(equalTo("done")));

        // second has no value
        assumeThat(second.get("extra", String.class), is(nullValue()));

        // because its uninitialised
        assumeFalse(second.isValueInitialised("extra"));

        // then merge
        first.setValues(second); // how does this work? also do for reset!

        // and check the original value remains
        assertThat(first.get("extra", String.class), is(equalTo("done")));

        // then if we initialise to null
        second.set("extra", null);

        // it will be initialised
        assumeTrue(second.isValueInitialised("extra"));

        // and when we merge again
        first.setValues(second);

        // it will be null
        assertThat(first.get("extra", String.class), is(nullValue()));
    }

    @Test
    public void uninitializedFieldsAreNotMergedWhenReset() {

        Dto first = toCompleteDto(newContact("id01")).acceptThis();
        // separate checks because expensive and not useful outside of tests
        assumeFalse(first.isNew());
        assumeFalse(first.isDirty());
        assumeFalse(first.isStale());
        assumeFalse(first.isConflicted());

        Dto second = toIncompleteDto(newContact("id01")).acceptThis();
        assumeFalse(second.isNew());
        assumeFalse(second.isDirty());
        assumeFalse(second.isStale());
        assumeFalse(second.isConflicted());

        // first has a value
        assumeThat(first.get("extra", String.class), is(equalTo("done")));

        // second has no value
        assumeThat(second.get("extra", String.class), is(nullValue()));

        // because its uninitialised
        assumeFalse(second.isValueInitialised("extra"));

        // then merge
        first.resetValues(second); // how does this work? also do for reset!

        // and check the original value remains
        assertThat(first.get("extra", String.class), is(equalTo("done")));

        // then if we initialise to null
        second.set("extra", null);

        // it will be initialised
        assumeTrue(second.isValueInitialised("extra"));

        // and when we merge again
        first.resetValues(second);

        // it will be null
        assertThat(first.get("extra", String.class), is(nullValue()));
    }

    @Test
    public void uninitializedFieldsAreNotAddedToMapExport() {

        final Dto includedDto = toCompleteDto(newContact("id01")).acceptThis();
        final Map<String,Object> includedValues = includedDto.currentValues();
        final DtoField includedField = includedDto.dtoFieldFor("extra");
        assertTrue(includedValues.containsKey("extra"));

        final Dto missingDto = toIncompleteDto(newContact("id01")).acceptThis();
        final Map<String,Object> missingValues = missingDto.currentValues();
        final DtoField missingField = missingDto.dtoFieldFor("extra");
        assertFalse(missingValues.containsKey("extra"));

        assertNotSame(includedDto, missingDto);
        assertNotSame(includedField, missingField);
    }

    private Dto toCompleteDto(final ContactData contact) {

        return ContactDtoBuilder.extraBuilder()
                                .transfer(contact)
                                .setThis("extra", "done");

    }

    private Dto toIncompleteDto(final ContactData contact) {

        return ContactDtoBuilder.extraBuilder()
                .transfer(contact);

    }

    private ContactData newContact(final String id) {
        return new ContactData(id, "Name", "email@address.com", "555-1234", true, 40, null, null);
    }

}
