package org.llaith.onyx.toolkit.stage;

import org.llaith.onyx.toolkit.output.status.StatusReporter;

import java.util.function.Function;

/**
 *
 */
public interface StatusReporterFactory extends Function<Context,StatusReporter> {

    // for neatness elsewhere
    
}
