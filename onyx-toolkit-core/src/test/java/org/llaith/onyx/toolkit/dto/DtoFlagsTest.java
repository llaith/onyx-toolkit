/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.instances.ContactData;
import org.llaith.onyx.toolkit.dto.instances.ContactDtoBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;


/**
 * Test the basic change behaviour and builder transitions.
 */
public class DtoFlagsTest {

    @Test
    public void settingValuesOnNewFlagsAsDirty() throws Exception {
        assertTrue(toDto(newContact())
                .setThis("email", "newemail@address.com")
                .isDirty());
    }

    @Test
    public void newInstancesStayDirtyWithAnyEdit() throws Exception {
        final Dto target = toDto(newContact());

        assumeTrue(target
                .setThis("email","newemail@address.com")
                .isDirty());

        // because it was never accepted, original is still null
        assertTrue(target
                .setThis("email","email@address.com")
                .isDirty());
    }

    @Test
    public void settingNewValuesOnExistingFlagsItAsDirty() throws Exception {
        assertTrue(toDto(newContact())
                .acceptThis()
                .setThis("email", "newemail@address.com")
                .isDirty());
    }

    @Test
    public void existingInstancesUnflagAsDirtyIfSetBackToOldValue() throws Exception {
        final Dto target = toDto(newContact()).acceptThis();

        assumeTrue(target
                .setThis("email","newemail@address.com")
                .isDirty());

        assertFalse(target
                .setThis("email","email@address.com")
                .isDirty());
    }

    @Test
    public void settingNewValuesDoesNotFlagAsStale() throws Exception {
        assertFalse(toDto(newContact())
                .acceptThis()
                .setThis("email","newemail@address.com")
                .isStale());
    }

    @Test
    public void resettingNewValuesDoesNotFlagAsDirty() throws Exception {
        assertFalse(toDto(newContact())
                .acceptThis()
                .resetThis("email", "newemail@address.com")
                .isDirty());
    }

    @Test
    public void resettingNewValuesFlagsItAsStale() throws Exception {
        assertTrue(toDto(newContact())
                .acceptThis()
                .resetThis("email", "newemail@address.com")
                .isStale());
    }

    @Test
    public void resettingNewValuesSameAsOriginalUnflagsItAsStale() throws Exception {

        final Dto target = toDto(newContact()).acceptThis();

        assumeTrue(target
                .resetThis("email", "newemail@address.com")
                .isStale());

        assertTrue(target
                .resetThis("email", "email@address.com")
                .isStale());
    }

    @Test
    public void settingAndResettingWillFlagAsConflicted() throws Exception {
        assertTrue(toDto(newContact())
                .acceptThis()
                .setThis("email", "newemail@address.com")
                .resetThis("email", "neweremail@address.com")
                .isStale());
    }

    @Test
    public void acceptingConflictedInstancesWillResolveConflict() throws Exception {
        assertFalse(toDto(newContact())
                .acceptThis()
                .setThis("email", "newemail@address.com")
                .resetThis("email", "neweremail@address.com")
                .acceptThis()
                .isStale());
    }

    @Test
    public void cancellingConflictedInstancesWillResolveConflict() throws Exception {
        assertFalse(toDto(newContact())
                .acceptThis()
                .setThis("email", "newemail@address.com")
                .resetThis("email", "neweremail@address.com")
                .cancelThis()
                .isStale());
    }

    private Dto toDto(final ContactData contact) {

        return ContactDtoBuilder.builder()
                                .transfer(contact);

    }

    private ContactData newContact() throws Exception {
        return new ContactData("NOS01", "Name", "email@address.com", "555-1234", true, 40, null, null);
    }

}
