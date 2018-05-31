package org.llaith.onyx.toolkit.util.exception;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 *
 */
public class FnUtil {

    public static RunData wrapRun() {

        return new RunData();

    }

    public static class RunData {

        private Runnable perform;
        private Consumer<Void> onSuccess;
        private Consumer<Exception> onException;

        public RunData perform(final Runnable perform) {

            Guard.isNull(this.perform);

            this.perform = Guard.notNull(perform);

            return this;

        }

        public RunData onSuccess(final Consumer<Void> onSuccess) {

            Guard.isNull(this.onSuccess);

            this.onSuccess = Guard.notNull(onSuccess);

            return this;

        }

        public RunData onException(final Consumer<Exception> onException) {

            Guard.isNull(this.onException);

            this.onException = Guard.notNull(onException);

            return this;

        }

        public void run() {

            FnUtil.run(this);

        }


    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static void run(final RunData runData) {

        Guard.notNull(runData);

        Guard.notNull(runData.perform);

        try {

            runData.perform.run();

            if (runData.onSuccess != null) runData.onSuccess.accept(null);

        } catch (Exception e) {

            if (runData.onException != null) runData.onException.accept(e);

            throw UncheckedException.wrap(e);

        }

    }

    public static <X> CallData<X> wrapCall() {

        return new CallData<>();

    }

    public static class CallData<T> {

        private Callable<T> perform;
        private Consumer<T> onSuccess;
        private Consumer<Exception> onException;

        public CallData<T> perform(final Callable<T> perform) {

            Guard.isNull(this.perform);

            this.perform = Guard.notNull(perform);

            return this;

        }

        public CallData<T> onSuccess(final Consumer<T> onSuccess) {

            Guard.isNull(this.onSuccess);

            this.onSuccess = Guard.notNull(onSuccess);

            return this;

        }

        public CallData<T> onException(final Consumer<Exception> onException) {

            Guard.isNull(this.onException);

            this.onException = Guard.notNull(onException);

            return this;

        }

        public T call() {

            return FnUtil.call(this);

        }

    }

    /* note, if you want to suppress the exception, wrap in a suppress call also */
    public static <X> X call(final CallData<X> callData) {

        Guard.notNull(callData);

        Guard.notNull(callData.perform);

        try {

            final X result = callData.perform.call();

            if (callData.onSuccess != null) callData.onSuccess.accept(result);

            return result;

        } catch (Exception e) {

            if (callData.onException != null) callData.onException.accept(e);

            throw UncheckedException.wrap(e);

        }

    }

    public static void main(String[] args) {

//        final String s = FnUtil.call(new CallData<String>() {{
//            call = () -> "Hello";
//            onSuccess = System.out::println;
//            onException = Throwable::printStackTrace;
//        }});
//

        final String s = FnUtil.<String>wrapCall()
                .perform(() -> "Hello")
                .onSuccess(System.out::println)
                .onException(Throwable::printStackTrace)
                .call();

        FnUtil.wrapCall()
              .perform(() -> "Hello")
              .onSuccess(System.out::println)
              .onException(Throwable::printStackTrace)
              .call();

        FnUtil.wrapRun()
              .perform(() -> {int a = 1;})
              .onSuccess(System.out::println)
              .onException(Throwable::printStackTrace)
              .run();

    }

}
