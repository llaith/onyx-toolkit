package org.llaith.onyx.toolkit.stage.ext;

import org.llaith.onyx.toolkit.stage.Context;
import org.llaith.onyx.toolkit.stage.StageListener;

import static java.lang.String.format;
import static org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold.VERBOSE;

/**
 *
 */
public class ConsoleLoggingStageListener implements StageListener {

    @Override
    public void onStart(final Context context) {

        context.reportAt(VERBOSE).newLine("Start");

    }

    @Override
    public void onSuccess(final Context context) {

        context.reportAt(VERBOSE).newLine(format("Success %s", context.completedString()));

    }

    @Override
    public void onFailure(final Context context) {

        context.reportAt(VERBOSE).newLine(format("Failure %s", context.completedString()));

    }

}
