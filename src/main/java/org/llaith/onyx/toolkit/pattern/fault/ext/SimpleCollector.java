/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.fault.ext;

import org.llaith.onyx.toolkit.pattern.fault.Fault;
import org.llaith.onyx.toolkit.pattern.fault.FaultCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * A very simple Collector that just takes a snapshot into 2 lists, one for warnings and
 * one for errors. Intended for simple compositions.
 */
public class SimpleCollector implements FaultCollector {

    private final List<Fault> errors = new ArrayList<>();
    private final List<Fault> warnings = new ArrayList<>();

    @Override
    public void addFault(final Fault fault) {
        if (fault.isSuppressed()) this.warnings.add(fault);
        else this.errors.add(fault);
    }

    public List<Fault> getErrors() {
        return new ArrayList<>(errors);
    }

    public List<Fault> getWarnings() {
        return new ArrayList<>(warnings);
    }

}
