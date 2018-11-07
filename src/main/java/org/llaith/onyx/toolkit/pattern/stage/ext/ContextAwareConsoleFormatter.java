package org.llaith.onyx.toolkit.pattern.stage.ext;

import org.llaith.onyx.toolkit.pattern.display.status.ConsoleStatusReporter;
import org.llaith.onyx.toolkit.pattern.display.status.StatusReporter.Threshold;
import org.llaith.onyx.toolkit.pattern.stage.Context;
import org.llaith.onyx.toolkit.pattern.stage.StatusReporterFactory;
import org.llaith.onyx.toolkit.lang.AnsiCodes;

import java.util.function.Function;

import static org.llaith.onyx.toolkit.pattern.display.status.StatusReporter.Threshold.SUMMARY;
import static org.llaith.onyx.toolkit.pattern.display.status.StatusReporter.Threshold.VERBOSE;
import static org.llaith.onyx.toolkit.lang.Guard.notNull;

/**
 *
 */
public class ContextAwareConsoleFormatter implements Function<String,String> {


    public static StatusReporterFactory consoleStatusReporter(final boolean colour, final boolean verbose) {

        return consoleStatusReporter(colour, verbose ? VERBOSE : SUMMARY);

    }

    public static StatusReporterFactory consoleStatusReporter(final boolean colour, final Threshold threshold) {

        return (context) ->
                new ConsoleStatusReporter(new ContextAwareConsoleFormatter(colour, context))
                        .minThreshold(threshold);

    }


    private static final String formatStr = "[ %s ] %s";

    private static final String formatColourStr = ""
            + AnsiCodes.WHITE
            + "["
            + AnsiCodes.MAGENTA
            + AnsiCodes.LOW_INTENSITY
            + " %s "
            + AnsiCodes.WHITE
            + "]"
            + AnsiCodes.RESET
            + " %s";

    private final boolean colour;

    private final Context context;

    public ContextAwareConsoleFormatter(final boolean colour, final Context context) {

        this.colour = colour;

        this.context = notNull(context);

    }

    @Override
    public String apply(final String s) {

        return String.format(
                this.colour ?
                        ContextAwareConsoleFormatter.formatColourStr :
                        ContextAwareConsoleFormatter.formatStr,
                context.breadcrumbs(),
                s);

    }
}
