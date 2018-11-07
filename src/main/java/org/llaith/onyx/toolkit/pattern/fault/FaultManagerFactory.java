/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.fault;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class FaultManagerFactory {

    private final Set<FaultSuppression> suppressions = new HashSet<>();

    public FaultManagerFactory() {
        this(null);
    }

    public FaultManagerFactory(final Collection<FaultSuppression> suppressions) {
        if (suppressions != null) this.suppressions.addAll(suppressions);
    }

    public FaultManager newFaultManager() {
        return new FaultManager(this.suppressions);
    }

}
