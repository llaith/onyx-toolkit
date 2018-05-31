/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.output.status;

import org.llaith.onyx.toolkit.stage.Context;
import org.llaith.onyx.toolkit.stage.Stage;
import org.llaith.onyx.toolkit.stage.ext.ConsoleLoggingStageListener;
import org.llaith.onyx.toolkit.util.thread.ThreadUtil;
import org.llaith.onyx.toolkit.output.progress.ProgressControl;
import org.llaith.onyx.toolkit.output.progress.ProgressTextRenderer;

import static java.lang.String.format;
import static org.llaith.onyx.toolkit.stage.ext.ContextAwareConsoleFormatter.consoleStatusReporter;
import static org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold.SUMMARY;
import static org.llaith.onyx.toolkit.output.status.StatusReporter.Threshold.VERBOSE;

/**
 * Simple logger which does not support updating existing messages.
 */
public class SimpleStatusLoggerTest {

    public static void main(String[] args) {

        // which link you have to the token doesnt matter, as long as you
        // remove them in order!

        final ProgressTextRenderer progressRenderer = new ProgressTextRenderer(true);

        final Stage root = new Stage(
                "Root Level",
                consoleStatusReporter(true, VERBOSE),
                new ConsoleLoggingStageListener());

        final int total = 100;
        final int longsleep = 100;
        final int shortsleep = 50;

        // this would be one thing
        final Stage step1 = root.startNestedStage("Step 1");
        step1.execute(context -> context.toCache(
                "totalbar",
                new ProgressControl(true, 20).setTotal(total * 2)));

        // this would be another one
        final Stage step2a = step1.startNestedStage("Step 2a");
        step2a.execute(context -> {

            context.reportAt(SUMMARY).newLine("First Dir").emptyLine();

            ProgressControl progress1 = context.fromCache("totalbar", ProgressControl.class);
            ProgressControl progress2 = new ProgressControl(true, 20).setTotal(total);

            runProgress(total, longsleep, context, progressRenderer, progress1, progress2);

        });

        step1.completeNestedStage(step2a, true);

        // this would be another one
        final Stage step2b = step1.startNestedStage("Step 2b");
        step2b.execute(context -> {

            context.reportIfAt(SUMMARY, emitter -> emitter.newLine("Second Dir:").emptyLine());

            ProgressControl progress1 = context.fromCache("totalbar", ProgressControl.class);
            ProgressControl progress3 = new ProgressControl(true, 20).setTotal(total);

            runProgress(total, shortsleep, context, progressRenderer, progress1, progress3);

        });

        step1.completeNestedStage(step2b, false);

        root.completeNestedStage(step1, true);

        root.completeStage(true);


//        token1.markNestedOk(token3);
//
//        final Memo memo1 = new Memo(new Section(0, "Hello", new ParagraphBlock("WTF! Hello!")));
//
//        MessageToken message1 = token2.postMessage(StatusReporter.Threshold.WARN);
//        message1.update(memo1);
//
//        status.markNestedOk(token2);
//
//        status.markNestedFailed(token1);
//
//        new
//
//                TextMemoRenderer(10, 4).
//
//                                               render(memo1);

    }

    private static void runProgress(
            final int total, final int sleep, final Context context,
            final ProgressTextRenderer progressRenderer,
            final ProgressControl progress1, final ProgressControl progress2) {

        for (int i = 0; i < total; i++) {

            ThreadUtil.sleep(sleep);

            progress1.incrementCount(1);

            progress2.incrementCount(1);

            progress1.update();

            progress2.update();

            context.reportIfAt(SUMMARY, emitter -> emitter.replaceLine(format(
                    "Total %s | Directory %s",
                    progressRenderer.render(progress1),
                    progressRenderer.render(progress2))));

        }


    }

}
