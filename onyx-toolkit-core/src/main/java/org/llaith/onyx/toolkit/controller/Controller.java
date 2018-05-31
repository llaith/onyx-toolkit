/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.controller;

/**
 * Represents a controller that is managed via a stack.
 */
public interface Controller {

    /**
     * Called when the controller has been pushed onto the stack. In a view controller
     * scenario, it would correspond to a display() event.
     *
     * @return itself.
     */
    public Controller activate();

    /**
     * Called when the controller has been pushed down the stack away from the top
     * position. In a view controller scenario, it would correspond to a hide() event
     * received when a user navigates forward away from the current controller.
     *
     * @return itself.
     */
    public Controller deactivate();

    /**
     * Called immediately the controller is requested to clean up before being popped off the stack.
     * If the controller cannot allow itself to be popped off the stack. It is expected that the
     * controller would have made a change in the state of the application to allow whatever condition
     * is stopping the dispose action from completing to be resolved. In a view controller situation,
     * this could correspond to an unsaved form and the controller would be expected to be requesting
     * a dialog to resolve the situation.
     *
     * @return itself.
     *
     * @throws ControllerDisposeAbortException if the controller cannot allow itself to be popped off
     * the stack.
     */
    public Controller willDispose() throws ControllerDisposeAbortException;

    /**
     * Called when the controller is about to be popped off the stack. At this point it is inevitable that
     * the controller will be popped off the stack, so the controller should clean up accordingly.
     *
     * The progression for dispose is as follows: 1) willDispose() 2) deactivate() 3) dispose()
     *
     * @return itself.
     */
    public Controller dispose();

}
