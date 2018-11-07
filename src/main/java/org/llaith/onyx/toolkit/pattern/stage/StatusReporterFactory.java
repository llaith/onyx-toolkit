package org.llaith.onyx.toolkit.pattern.stage;

import org.llaith.onyx.toolkit.pattern.display.status.StatusReporter;

import java.util.function.Function;

/**
 *
 */
public interface StatusReporterFactory extends Function<Context,StatusReporter> {

    // for neatness elsewhere
    
}
