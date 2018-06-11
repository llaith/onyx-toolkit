package org.llaith.onyx.toolkit.util.exception;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class FnUtil {

    public static class RunResult<T extends BaseData<T>> {

        Throwable thrown;

        public void rethrow() {

            if (this.thrown != null) throw UncheckedException.wrap(this.thrown);

        }

    }

    public static class CallResult<T extends BaseData<T>> {

        T result;

        Throwable thrown;

        public T rethrowOrResult() {

            if (this.thrown != null) throw UncheckedException.wrap(this.thrown);

            return result;

        }

        public T result() {

            return this.result;

        }

    }

    public static abstract class BaseData<T extends BaseData<T>> {

        Consumer<Exception> onException;
        Consumer<Void> onFinalize;

        protected abstract T getThis();

        public T onException(final Consumer<Exception> onException) {

            Guard.isNull(this.onException);

            this.onException = notNull(onException);

            return getThis();

        }

        public T onFinalize(final Consumer<Void> onFinalize) {

            Guard.isNull(this.onFinalize);

            this.onFinalize = notNull(onFinalize);

            return getThis();

        }

    }

    public static class RunData extends BaseData<RunData> {

        Runnable runnable;

        @Override
        protected RunData getThis() {
            return this;
        }

        RunData perform(final Runnable runnable) {

            Guard.isNull(this.runnable);

            this.runnable = notNull(runnable);

            return this;

        }

        void run() {

            FnUtil.run(this);

        }

    }

    public static class CallData<T> extends BaseData<CallData<T>> {

        Callable<T> callable;

        @Override
        protected CallData<T> getThis() {
            return null;
        }

        CallData<T> perform(final Callable<T> callable) {

            Guard.isNull(this.callable);

            this.callable = notNull(callable);

            return this;

        }

        T call() {

            return FnUtil.call(this);

        }

    }

    public static RunData wrapRun() {

        return new RunData();

    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static void run(final RunData runData) {

        notNull(notNull(runData).runnable);

        Exception thrown = null;

        try {

            runData.runnable.run();

        } catch (Exception e) {

            thrown = e;

            if (runData.onException != null) {

                try {

                    runData.onException.accept(e);

                } catch (Exception suppress) {

                    thrown.addSuppressed(e);

                }

            }

        } finally {

            if (runData.onFinalize != null) {

                try {

                    runData.onFinalize.accept(null);

                } catch (Exception e) {

                    if (thrown == null) thrown = e;

                    else thrown.addSuppressed(e);

                }

            }

        }

        if ((thrown != null) && (!runData.suppressThrow))
            throw UncheckedException.wrap(thrown);

    }

    public static <X> CallData<X> wrapCall() {

        return new CallData<>();

    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static <X> X call(final CallData<X> callData) {

        notNull(notNull(callData).callable);

        X result = null;

        Exception thrown = null;

        try {

            result = callData.callable.call();

        } catch (Exception e) {

            thrown = e;

            if (callData.onException != null) {

                try {

                    callData.onException.accept(e);

                } catch (Exception suppress) {

                    thrown.addSuppressed(e);

                }

            }

        } finally {

            if (callData.onFinalize != null) {

                try {

                    callData.onFinalize.accept(null);

                } catch (Exception e) {

                    if (thrown == null) thrown = e;

                    else thrown.addSuppressed(e);

                }

            }

        }

        if ((thrown != null) && (!callData.suppressThrow))
            throw UncheckedException.wrap(thrown);

        return result;

    }

    public static void main(String[] args) {

//        final String s = FnExUtil.call(new CallData<String>() {{
//            call = () -> "Hello";
//            onFinalize = System.out::println;
//            onException = Throwable::printStackTrace;
//        }});
//

        final String s = FnUtil.<String>wrapCall()
                .perform(() -> "Hello")
                .onFinalize(System.out::println)
                .onException(Throwable::printStackTrace)
                .call();

        FnUtil.wrapCall()
              .perform(() -> "Hello")
              .onFinalize(System.out::println)
              .onException(Throwable::printStackTrace)
              .call();

        FnUtil.wrapRun()
              .perform(() -> {int a = 1;})
              .onFinalize(System.out::println)
              .onException(Throwable::printStackTrace)
              .run();

    }

}
