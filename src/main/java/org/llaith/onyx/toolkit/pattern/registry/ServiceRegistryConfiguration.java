/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.registry;

/**
 *
 */
public interface ServiceRegistryConfiguration {

    // Throwing a runtime exception allows implementers of this interface to
    // wait until a dependent implementation has been initialised. It will be
    // retried until all implementations are throwing these exceptions or have
    // succeeded. Note in this case the preceeding is for suppliers.
    void execute(ServiceRegistryBuilder builder);

}
