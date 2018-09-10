/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.command;

/**
 *
 */
public interface CommandExecutor<C> {

    <X extends Command<C>> X execute(X command);

}
