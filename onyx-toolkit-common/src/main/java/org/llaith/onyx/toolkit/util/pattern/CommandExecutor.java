/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

/**
 *
 */
public interface CommandExecutor<C> {

    <X extends Command<C>> X execute(X command);

}
