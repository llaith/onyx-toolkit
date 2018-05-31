/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.transferobject;

/**
 * Note, there is nothing inherent about these that require them to be the same type to be transferred.
 *
 * It's perfectly ok for subsets to be transferred. There are parts of a solution where you might want to
 * restrict it, such as the domain stuff so as to make sure the changes are properly reflected, but thats
 * enforcable on a case by case basis.
 */
public class ValuesTransfer<E,I> {

    private final ValuesExportAdapter<E> from;
    private final ValuesImportAdapter<I> to;

    public ValuesTransfer(final ValuesExportAdapter<E> from, final ValuesImportAdapter<I> to) {
        this.from = from;
        this.to = to;
    }

    public I transfer(final E in) {
        return to.importValues(from.exportValues(in));
    }

}
