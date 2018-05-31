/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.controller;

/**
 * A controller that sends this exception is stopping the current
 * unwinding of the stack. The thrower will receive an additional
 * activate() immediately after this exception is processed. The
 * sender should use that activate() as a trigger (set a flag before
 * throwing the exception) to display (possibly involving the push()
 * of a controller that can resolve the condition that caused the
 * dispose() to be aborted. This delayed processing is to allow the
 * controllerStack to be reset before receiving any other pushes
 * to help resolve the condition.
 */
public class ControllerDisposeAbortException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new exception that will abort the current requested pop.
     */
    public ControllerDisposeAbortException() {
        super("Controller pop has been aborted");
    }

}
