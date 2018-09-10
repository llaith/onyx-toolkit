/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public interface Environment {

    String getVariable(String key);

    class OsEnvironment implements Environment {

        @Override
        public String getVariable(final String key) {

            return System.getenv(key);

        }

    }

    class MockEnvironment implements Environment {

        private final Map<String,String> vars = new HashMap<>();

        public MockEnvironment(final Map<String,String> vars) {

            this.vars.putAll(vars);

        }

        @Override
        public String getVariable(final String key) {

            return this.vars.get(key);

        }

    }

}
