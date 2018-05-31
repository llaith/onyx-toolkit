/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import org.junit.Test;
import org.llaith.onyx.toolkit.dto.ext.DtoTransferAdapter;
import org.llaith.onyx.toolkit.dto.ext.PojoDtoFactory;
import org.llaith.onyx.toolkit.transferobject.ValuesTransfer;
import org.llaith.onyx.toolkit.transferobject.impl.PojoTransferAdapter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Nesting is explicitly out of scope for the moment.
 */
public class DtoPojoRelease3Test {

    public static class Simple {
        public String name;
    }

    @Test
    public void testImportExportSimple() {


        final DtoTransferAdapter dtoAdapter = new DtoTransferAdapter(PojoDtoFactory.newSupplier(Simple.class));
        final PojoTransferAdapter<Simple> pojoAdapter = new PojoTransferAdapter<>(Simple.class);

        // get a pojo
        final Simple in = new Simple() {{name = "CHILD1";}};

        // turn into a dto
        final Dto dto = new ValuesTransfer<>(pojoAdapter, dtoAdapter)
                .transfer(in);

        // manipulate the dto
        dto
                .setThis("name", "ADJUSTED!")
                .acceptThis();

        // turn back to a pojo
        final Simple out = new ValuesTransfer<>(dtoAdapter, pojoAdapter)
                .transfer(dto);

        assertThat(
                out.name,
                is(equalTo("ADJUSTED!")));

    }

}
