/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

/**
 * Generic command pattern. Will generally be matched to a 'processor', and the
 * results will be saved in the specific version of the command object rather
 * than simply returned as 'R', this allows multi-stage processing after
 * execution. The parametrisation of the exception is important because the
 * toolkit.Command is so low level that requiring a generic 'CommandException'
 * would negatively impact the understanding of the codebase.
 */
public interface Command<C> {

    void execute(C context);

}
