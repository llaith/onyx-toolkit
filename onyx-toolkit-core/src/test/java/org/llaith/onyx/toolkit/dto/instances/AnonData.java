/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.instances;

/**
 *
 */
@SuppressWarnings("unchecked")
public class AnonData {

    private String name;

    public AnonData() {
        super();
    }

    public AnonData(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AnonData{" +
                "name='" + name + '\'' +
                '}';
    }

}
