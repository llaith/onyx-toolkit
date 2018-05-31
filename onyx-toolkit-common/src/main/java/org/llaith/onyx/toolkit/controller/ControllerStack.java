/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.controller;


import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Contains other nested controllers, possibly view controllers, and
 * possibly representing a complex view such as portals or windows.
 * <p/>
 * If using for view controllers, attach an instance of this as a root. This
 * will could represent something like (a) the desktop, (b) the login window,
 * or (c) the main window of a normal app.
 */
public class ControllerStack<T extends Controller> {

    private Deque<T> controllerStack = new ArrayDeque<>();

    private Deque<ControllerStack<T>> controllerStacks = new ArrayDeque<>();

    /**
     * Constructs a new ControllerStack instance. A new instance always
     * contains a valid controller.
     *
     * @param controller the controller to add as the current top of the
     *                   stack.
     */
    public ControllerStack(final T controller) {
        this.push(Guard.notNull(controller));
    }

    /**
     * @return the controller that is the current top of the stack.
     */
    public T top() {
        return this.controllerStack.peek();
    }

    /**
     * Push a new controller onto this stack. The added controller
     * will receive an activate().
     *
     * @param controller the controller to be added.
     * @return the passed controller param.
     */
    public final T push(final T controller) {
        this.controllerStack.push(controller);

        controller.activate();

        return controller;
    }

    /**
     * Push a new controller into a sub-stack as the first element. If
     * you want access to the passed in controller it will be available
     * with the peek() command of the returned sub-stack.
     *
     * @param controller the controller to push onto the top of a new sub-stack which
     *                   will be pushed onto the top of the nested sub-stacks.
     * @return the newly created nested sub-stack. This will be the current top of the
     * nested stacks, and the passed in controller will be the current top of that.
     */
    public ControllerStack<T> pushBranch(final T controller) {
        final ControllerStack<T> sub = new ControllerStack<>(controller); // activated by this.
        this.controllerStacks.addFirst(sub);
        return sub;
    }

    /**
     * Attempt to pop the current controller off the stack, starting with the nested
     * sub-stacks first. All controllers will be sent a willDispose() call which they
     * can respond to by throwing an exception that will stop the unwinding at that
     * point.
     * @throws ControllerDisposeAbortException if the rewind process was aborted.
     */
    public void pop() throws ControllerDisposeAbortException{
        try {
            this.controllerStack.peek().willDispose();
            this.popAllSiblings();
            this.closeAndPop();
        } finally {
            this.controllerStack.peek().activate();
        }
    }

    /**
     * Forcibly pop the current controller off the stack, starting with and including
     * all of it's nested substacks first.
     */
    public void forcePop() {
        this.forcePopAllSiblings();
        this.closeAndPop();
        this.controllerStack.peek().activate();
    }

    /**
     * Pops the controllers off the stack until the passed in controller is the current head.
     * This is commonly used as some kind of 'breadcrumb' control. Remember that nested stacks
     * will have their own set of controllers and will not be rewound by this method.
     *
     * @param controller the controller to rewind the stack until it is the top.
     * @return the controller that was rewound to, or null if it could not be found and the stack
     * is now empty.
     * @throws ControllerDisposeAbortException if the rewind process was aborted.
     */
    public T rewindTo(final T controller) throws ControllerDisposeAbortException {
        for (Iterator<T> it = this.controllerStack.descendingIterator(); it.hasNext();) {
            if (it.next() == controller) return controller;
            this.pop(); // will activate and deactivate each controller in turn
        }
        return null;
    }

    /**
     * As rewindTo except that the popped controllers are not given a chance to
     * abort the rewind.
     *
     * @param controller the controller to rewind the stack until it is the top.
     */
    public T forceRewindTo(final T controller) {
        for (Iterator<T> it = this.controllerStack.descendingIterator(); it.hasNext();) {
            if (it.next() == controller) return controller;
            this.forcePop(); // will activate and deactivate each controller in turn
        }
        return null;
    }

    /**
     * Rewinds all and clears the stack.
     *
     * @return self instance.
     * @throws ControllerDisposeAbortException if the rewind process was aborted.
     */
    public ControllerStack<T> rewindAll() throws ControllerDisposeAbortException {
        this.rewindTo(null);
        return this;
    }

    /**
     * As rewindAll except that the popped controllers are not given a chance to
     * abort the rewind.
     *
     * @return self instance.
     * @throws ControllerDisposeAbortException if the rewind process was aborted.
     */
    public ControllerStack<T> forceRewindAll() {
        this.forceRewindTo(null);
        return this;
    }


    private void closeAndPop() {
        this.controllerStack.peek().deactivate();
        this.controllerStack.peek().dispose();
        this.controllerStack.pop();
    }

    private void popAllSiblings() throws ControllerDisposeAbortException {
        for (Iterator<ControllerStack<T>> it = this.controllerStacks.descendingIterator(); it.hasNext();) {
            it.next().rewindAll();
        }
    }

    private void forcePopAllSiblings() {
        for (Iterator<ControllerStack<T>> it = this.controllerStacks.descendingIterator(); it.hasNext();) {
            it.next().forceRewindAll();
        }
    }

}
