/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.output.memo;

/**
 *
 */
public class TableBlock extends Block {

    private final String headers;
    private final String rowFormat;
    private final Object[][] rowData;

    public TableBlock(String headers, String rowFormat, Object[][] rowData) {
        super();
        this.headers = headers;
        this.rowFormat = rowFormat;
        this.rowData = rowData;
    }

    public TableBlock(String title, String headers, String rowFormat, Object[][] rowData) {
        super(title);
        this.headers = headers;
        this.rowFormat = rowFormat;
        this.rowData = rowData;
    }

    public String headers() {
        return headers;
    }

    public String rowFormat() {
        return rowFormat;
    }

    public Object[][] rowData() {
        return rowData;
    }

}
