/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.fault.ext;

import org.llaith.onyx.toolkit.pattern.fault.Fault;
import org.llaith.onyx.toolkit.pattern.fault.FaultManager;

/**
 *
 */
public class SysoutFaultRenderer implements FaultRenderer {

    @Override
    public void display(final FaultManager faults) {

        if (faults.hasErrors()) {
            System.err.println("Errors ("+faults.errors().size()+"):");
            for (final Fault fault : faults.errors()) {
                System.err.println(fault.location() + ":" + fault.message());
            }
        }

        if (faults.hasWarnings()) {
            System.err.println("Warnings ("+faults.warnings().size()+"):");
            for (final Fault fault : faults.warnings()) {
                System.err.println(fault.location() + ":" + fault.message());
            }
        }

    }

}
