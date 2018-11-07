package org.llaith.onyx.toolkit.pattern.display.status;

import java.util.function.Consumer;

/**
 *
 */
public interface StatusReporter {

    /**
     * Not the same as code loggers! Suited to runtime use. Verboses and summaries
     * are specialisations of infos, and fatals are a specialisation of error.
     */
    enum Threshold {

        DEBUG, VERBOSE, SUMMARY, WARN, ERROR, FATAL

    }

    interface Emitter {

        Emitter updateLine(String msg);

        Emitter newLine(String msg);

        Emitter replaceLine(String msg);

        Emitter clearLine();

        Emitter emptyLine();

    }

    /**
     * Note that different thresholds could go to different emitters (like on a web page), or (commonsly on consoles)
     * some thresholds are ignored (like standard console logger behaviour).
     */
    Emitter outputAt(Threshold threshold);

    /**
     * This form will not call the consumer if the threshold is lower than the minimum threshold, if such functionality
     * is supported by the target reporter (it usually is). This is only useful if the cost of creating the output is
     * high, else the other form is easier to use.
     */
    void outputIfAt(Threshold threshold, Consumer<Emitter> consumer);


}
