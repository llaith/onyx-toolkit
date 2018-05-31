/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import org.slf4j.Logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 *
 */
public class TimingUtil {
    
    public interface ConsoleAdapter {
        void writeLine(String msg);
    }  

    public static void timeStartup(final Logger logger, final String name, final Runnable runnable) {

        timeStartupEx(null, Guard.notNull(logger), Guard.notNull(name), Guard.notNull(runnable));

    }

    public static void timeStartup(final ConsoleAdapter console, final String name, final Runnable runnable) {

        timeStartupEx(Guard.notNull(console), null, Guard.notNull(name), Guard.notNull(runnable));

    }

    public static void  timeStartup(final ConsoleAdapter console, final Logger logger, final String name, final Runnable runnable) {

        timeStartupEx(Guard.notNull(console), Guard.notNull(logger), Guard.notNull(name), Guard.notNull(runnable));

    }

    // You can use Guava's stopwatch, but this has the init/stop time also
    private static void timeStartupEx(final ConsoleAdapter console, final Logger logger, final String name, final Runnable runnable) {

        final Date start = new Date();

        try {

            final String msgOn = String.format(
                    "Starting %s at :%s.",
                    name,
                    DateFormat.getDateTimeInstance().format(start));

            if (console != null) console.writeLine(msgOn);

            if (logger != null) logger.info(msgOn);

            runnable.run();

            final Date done = new Date();

            final double elapsed = (done.getTime() - start.getTime()) / 1000d;

            final String msgUp = String.format(
                    "%s started at: %s (%s seconds).",
                    name,
                    DateFormat.getDateTimeInstance().format(done),
                    elapsed);

            if (console != null) console.writeLine(msgUp);

            if (logger != null) logger.info(msgUp);

        } catch (RuntimeException e) {

            final Date failed = new Date();

            final double elapsed = (failed.getTime() - start.getTime()) / 1000d;

            final String msgFail = String.format(
                    "%s could not be started at: %s (%s seconds) because: %s.",
                    name,
                    failed,
                    elapsed,
                    e.getMessage());

            if (console != null) console.writeLine(msgFail);

            if (logger != null) logger.error(msgFail, e);

            throw e;

        }

    }

    public static <V> V timeStartup(final Logger logger, final String name, final Callable<V> callable) {

        return timeStartupEx(null, Guard.notNull(logger), Guard.notNull(name), Guard.notNull(callable));

    }

    public static <V> V timeStartup(final ConsoleAdapter console, final String name, final Callable<V> callable) {

        return timeStartupEx(Guard.notNull(console), null, Guard.notNull(name), Guard.notNull(callable));

    }

    public static <V> V timeStartup(final ConsoleAdapter console, final Logger logger, final String name, final Callable<V> callable) {

        return timeStartupEx(Guard.notNull(console), Guard.notNull(logger), Guard.notNull(name), Guard.notNull(callable));

    }

    // You can use Guava's stopwatch, but this has the init/stop time also
    private static <V> V timeStartupEx(final ConsoleAdapter console, final Logger logger, final String name, final Callable<V> callable) {

        final Date start = new Date();

        try {
            
            final String msgOn = String.format(
                    "Starting %s at :%s.",
                    name,
                    DateFormat.getDateTimeInstance().format(start));

            if (console != null) console.writeLine(msgOn);

            if (logger != null) logger.info(msgOn);
            
            V v = callable.call();

            final Date done = new Date();

            final double elapsed = (done.getTime() - start.getTime()) / 1000d;

            final String msgUp = String.format(
                    "%s started at: %s (%s seconds).",
                    name,
                    DateFormat.getDateTimeInstance().format(done),
                    elapsed);

            if (console != null) console.writeLine(msgUp);

            if (logger != null) logger.info(msgUp);

            return v;

        } catch (RuntimeException e) {

            final Date failed = new Date();

            final double elapsed = (failed.getTime() - start.getTime()) / 1000d;

            final String msgFail = String.format(
                    "%s could not be started at: %s (%s seconds) because: %s.",
                    name,
                    failed,
                    elapsed,
                    e.getMessage());

            if (console != null) console.writeLine(msgFail);

            if (logger != null) logger.error(msgFail, e);

            throw e;

        } catch (Exception e) {
            
            throw new RuntimeException(e);

        }

    }

}
