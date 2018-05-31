/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.env;

/**
 *
 */
public class OsEnvironment implements Environment {

    @Override
    public String getVariable(final String key) {

        return System.getenv(key);

    }

}
