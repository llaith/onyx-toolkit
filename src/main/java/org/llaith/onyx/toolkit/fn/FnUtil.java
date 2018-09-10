package org.llaith.onyx.toolkit.fn;

import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;

/**
 *
 */
public class FnUtil {

    public static class RunResult {

        public static RunResult Success() {
            return new RunResult(null);
        }

        public static RunResult Failure(final Exception thrown) {
            return new RunResult(thrown);
        }

        private final Exception thrown;

        private RunResult(final Exception thrown) {
            this.thrown = thrown;
        }

        public void rethrow() {

            if (this.thrown != null) throw UncheckedException.wrap(this.thrown);

        }

        public void returnOr(final Consumer<Exception> consumer) {

            if (this.thrown != null) notNull(consumer).accept(this.thrown);

        }

    }

    public static class CallResult<T> {

        public static <X> CallResult<X> Success(X result) {
            return new CallResult<>(result, null);
        }

        public static <X> CallResult<X> Failure(final Exception thrown) {
            return new CallResult<>(null, thrown);
        }

        private final T result;

        private final Exception thrown;

        private CallResult(final T result, final Exception thrown) {
            this.result = result;
            this.thrown = thrown;
        }

        public T rethrow() {

            if (this.thrown != null) throw UncheckedException.wrap(this.thrown);

            return result;

        }

        public T resultOr(final Consumer<Exception> consumer) {

            if (this.thrown != null) notNull(consumer).accept(this.thrown);

            return result;

        }

    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static RunResult run(final Runnable runnable) {

        try {

            notNull(runnable).run();

            return RunResult.Success();

        } catch (Exception e) {

            return RunResult.Failure(e);

        }

    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static <X> CallResult<X> call(final Callable<X> callable) {


        try {

            return CallResult.Success(notNull(callable).call());

        } catch (Exception e) {

            return CallResult.Failure(e);

        }

    }

    public static void main(String[] args) {

        final PrintStream out = System.out;

        run(() -> out.println("Run 1")).rethrow();

        run(() -> out.println("Run 2")).returnOr(UncheckedException::wrap);

        out.println(call(() -> "Call 1").rethrow());

        out.println(call(() -> "Call 2").resultOr(UncheckedException::wrap));

    }

}
