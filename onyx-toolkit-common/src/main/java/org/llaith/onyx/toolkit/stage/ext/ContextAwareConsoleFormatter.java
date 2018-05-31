package org.llaith.onyx.toolkit.stage.ext;

import org.llaith.onyx.toolkit.stage.Context;
import org.llaith.onyx.toolkit.stage.StatusReporterFactory;
import org.llaith.onyx.toolkit.util.text.AnsiCodes;
import org.llaith.onyx.toolkit.output.status.ConsoleStatusReporter;
import org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold;

import java.util.function.Function;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;
import static org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold.SUMMARY;
import static org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold.VERBOSE;

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
