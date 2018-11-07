package org.llaith.onyx.toolkit.pattern.display.status;

import org.llaith.onyx.toolkit.io.console.TextDevice;
import org.llaith.onyx.toolkit.io.console.TextDevices;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;

/**
 *
 */
public class ConsoleStatusReporter implements StatusReporter {

    private final Function<String,String> formatter;

    private final TextDevice console = TextDevices.defaultTextDevice();

    private Threshold minThreshold = Threshold.SUMMARY;

    private final Emitter realEmitter = new Emitter() {

        @Override
        public Emitter updateLine(final String msg) {

            // simply outputs the line - with the formatter
            console.printf(notNull(msg).replaceAll("%", "%%"));

            return this;

        }

        @Override
        public Emitter newLine(final String msg) {

            console.printf("\n" + formatter.apply(notNull(msg).replaceAll("%", "%%")));

            return this;

        }

        @Override
        public Emitter replaceLine(final String msg) {

            console.printf("\r" + formatter.apply(notNull(msg).replaceAll("%", "%%")));

            return this;

        }

        @Override
        public Emitter clearLine() {

            // simply updateLine with a line reset at start
            return this.replaceLine("");

        }

        @Override
        public Emitter emptyLine() {

            // and simply updateLine with a newline at the start
            return this.newLine("");

        }

    };

    private final Emitter dummyEmitter = new Emitter() {

        @Override
        public Emitter updateLine(final String msg) {
            return this;
        }

        @Override
        public Emitter newLine(final String msg) {
            return this;
        }

        @Override
        public Emitter replaceLine(final String msg) {
            return this;
        }

        @Override
        public Emitter clearLine() {
            return this;
        }

        @Override
        public Emitter emptyLine() {
            return this;
        }

    };

    public ConsoleStatusReporter() {

        this(Function.identity());

    }

    public ConsoleStatusReporter(final Function<String,String> formatter) {

        this.formatter = notNull(formatter);

    }

    public ConsoleStatusReporter minThreshold(final Threshold minThreshold) {

        this.minThreshold = notNull(minThreshold);

        return this;

    }

    @Override
    public Emitter outputAt(final Threshold threshold) {

        return (threshold.ordinal() > this.minThreshold.ordinal()) ?
                realEmitter :
                dummyEmitter;

    }

    @Override
    public void outputIfAt(final Threshold threshold, final Consumer<Emitter> consumer) {

        if (threshold.ordinal() > this.minThreshold.ordinal()) {

            consumer.accept(this.realEmitter);

        }

    }

    public static void main(String[] args) {

        // this shows the replace clears the existing line
        new ConsoleStatusReporter()
                .outputIfAt(Threshold.WARN, emitter -> emitter
                        .emptyLine()
                        .updateLine("TEST/0000000000")
                        .newLine("TEST/")
                        .replaceLine("1234567890")
                        .replaceLine("TEST/0000000000")
                        .replaceLine("TEST/11111")
                        .updateLine("11111"));

        new ConsoleStatusReporter()
                .outputAt(Threshold.WARN)
                .emptyLine()
                .updateLine("TEST/0000000000")
                .newLine("TEST/")
                .replaceLine("1234567890")
                .replaceLine("TEST/0000000000")
                .replaceLine("TEST/11111")
                .updateLine("11111");

    }

}
