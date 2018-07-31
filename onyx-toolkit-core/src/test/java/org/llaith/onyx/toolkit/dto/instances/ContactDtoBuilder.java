/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.instances;


import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.DtoField;
import org.llaith.onyx.toolkit.dto.ext.DtoTransferAdapter;
import org.llaith.onyx.toolkit.dto.ext.PojoDtoFactory;
import org.llaith.onyx.toolkit.transferobject.ValuesTransfer;
import org.llaith.onyx.toolkit.transferobject.impl.PojoTransferAdapter;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class ContactDtoBuilder {

    private final static DtoTransferAdapter dtoAdapter = new DtoTransferAdapter(PojoDtoFactory.newSupplier(ContactData.class))
            .addSelfNestedAdapter("partner")
            .addSelfNestedAdapter("relations");

    private final static PojoTransferAdapter<ContactData> pojoAdapter = new PojoTransferAdapter<>(ContactData.class)
            .addSelfNestedAdapter("partner")
            .addSelfNestedAdapter("relations");

    private final static ValuesTransfer<ContactData,Dto> builder = new ValuesTransfer<>(pojoAdapter, dtoAdapter);

    private final static DtoTransferAdapter dtoExtraAdapter = new DtoTransferAdapter(
            PojoDtoFactory.newSupplier(
                    ContactData.class,
                    () -> {
                        return new HashSet<>(Arrays.asList(new DtoField(
                                "extra",
                                String.class,
                                false,
                                false,
                                false)));
                    }))
            .addSelfNestedAdapter("partner")
            .addSelfNestedAdapter("relations");

    private final static ValuesTransfer<ContactData,Dto> extraBuilder = new ValuesTransfer<>(pojoAdapter, dtoExtraAdapter);

    public static ValuesTransfer<ContactData,Dto> builder() {
        return builder;
    }

    public static ValuesTransfer<ContactData,Dto> extraBuilder() {
        return extraBuilder;
    }

}
