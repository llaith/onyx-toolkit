/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.env;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MockEnvironment implements Environment {

    private final Map<String,String> vars = new HashMap<>();

    public MockEnvironment(final Map<String,String> vars) {

        this.vars.putAll(vars);

    }

    @Override
    public String getVariable(final String key) {

        return this.vars.get(key);
        
    }

}
