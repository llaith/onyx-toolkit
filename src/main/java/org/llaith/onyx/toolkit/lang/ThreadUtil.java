/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 *
 */
public class ThreadUtil {

    public static void sleep(final int millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }

    }

    public static String generateThreadDump() {

        final StringBuilder dump = new StringBuilder();

        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);

        for (final ThreadInfo threadInfo : threadInfos) {

            dump.append('"')
                .append(threadInfo.getThreadName())
                .append("\" ");

            final Thread.State state = threadInfo.getThreadState();

            dump.append("\n   java.lang.Thread.State: ")
                .append(state);

            final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();

            for (final StackTraceElement stackTraceElement : stackTraceElements) {

                dump.append("\n        at ")
                    .append(stackTraceElement);

            }

            dump.append("\n\n");

        }

        return dump.toString();

    }

}
