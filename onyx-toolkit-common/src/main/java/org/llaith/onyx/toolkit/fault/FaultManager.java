/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Allows multiple errors/exceptions, useful when compiling
 * Allows warnings to be ignored.
 * <p>
 * Rules:
 * 1) Only one validation per location
 * 2) Skips a validation if the subset of the location has already failed.
 */
public class FaultManager {

    private static final Logger logger = LoggerFactory.getLogger(Fault.class);

    // to help some logic it's neater if we get an exception
    public static class ErrorException extends Exception {

        public ErrorException() {
            super();
        }
    }

    private final Map<FaultLocation,Set<SuppressionToken>> suppressions = new HashMap<>();

    private final List<Fault> errors = new LinkedList<>();
    private final List<Fault> warnings = new LinkedList<>();
    private final Set<FaultLocation> locations = new HashSet<>();

    public FaultManager() {
        this(new ArrayList<>());
    }

    public FaultManager(final Collection<FaultSuppression> suppressions) {
        this.suppressions.putAll(this.indexSuppressions(suppressions));
    }

    private Map<FaultLocation,Set<SuppressionToken>> indexSuppressions(final Collection<FaultSuppression> suppressions) {
        final Map<FaultLocation,Set<SuppressionToken>> m = new HashMap<>();
        for (FaultSuppression suppression : suppressions) {
            if (!m.containsKey(suppression.location())) m.put(suppression.location(), new HashSet<>());
            m.get(suppression.location()).add(suppression.token());
        }
        return m;
    }

    public boolean addFault(final Fault fault) {
        if (this.isLocationOk(fault.location())) {
            return this.storeFault(this.markSuppressed(fault));
        }
        return false;
    }

    public void addFaultAndThrow(final Fault fault) throws FaultManager.ErrorException {
        if (!this.addFault(fault)) throw new FaultManager.ErrorException();
    }

    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }


    public List<Fault> warnings() {
        return new ArrayList<>(this.warnings);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }


    public List<Fault> errors() {
        return new ArrayList<>(this.errors);
    }

    public boolean isLocationOk(final FaultLocation location) {
        for (FaultLocation chk : this.locations) {
            if (chk.includes(location)) return false;
        }
        return true;
    }

    public <X extends FaultCollector> X collectFaults(final X collector) {

        return this.collectFaults(
                collector,
                (o) -> true);

    }

    public <X extends FaultCollector> X collectFaults(final X collector, final Predicate<Fault> predicate) {

        for (final Fault fault : this.errors) {
            if (predicate.test(fault)) collector.addFault(fault);
        }

        return collector;

    }

    private Fault markSuppressed(final Fault fault) {
        return fault.withSuppression(
                this.suppressions.containsKey(fault.location()) &&
                        this.suppressions.get(fault.location()).contains(fault.suppressionToken()));
    }

    private boolean storeFault(final Fault fault) {
        if (fault.isSuppressed()) {
            this.warnings.add(fault);

            return true;
        }

        this.locations.add(fault.location());
        this.errors.add(fault);

        return false;
    }

}
