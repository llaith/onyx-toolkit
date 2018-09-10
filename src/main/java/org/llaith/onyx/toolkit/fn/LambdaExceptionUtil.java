package org.llaith.onyx.toolkit.fn;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * https://stackoverflow.com/questions/27644361/how-can-i-throw-checked-exceptions-from-inside-java-8-streams
 * <p>
 * This LambdaExceptionUtil helper class lets you use any checked exceptions in Java streams, like this:
 * <p>
 * Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
 * .map(rethrowFunction(Class::forName))
 * .collect(Collectors.toList());
 * Note Class::forName throws ClassNotFoundException, which is checked. The stream itself also throws ClassNotFoundException, and NOT some wrapping unchecked exception.
 * <p>
 * NOTE 1: The rethrow methods of the LambdaExceptionUtil class above may be used without fear, and are OK to use in any situation. A big thanks to user @PaoloC who helped solve the last problem: Now the compiler will ask you to add throw clauses and everything's as if you could throw checked exceptions natively on Java 8 streams.
 * <p>
 * NOTE 2: The uncheck methods of the LambdaExceptionUtil class above are bonus methods, and may be safely removed them from the class if you don't want to use them. If you do used them, do it with care, and not before understanding the following use cases, advantages/disadvantages and limitations:
 * <p>
 * • You may use the uncheck methods if you are calling a method which literally can never throw the exception that it declares. For example: new String(byteArr, "UTF-8") throws UnsupportedEncodingException, but UTF-8 is guaranteed by the Java spec to always be present. Here, the throws declaration is a nuisance and any solution to silence it with minimal boilerplate is welcome: String text = uncheck(() -> new String(byteArr, "UTF-8"));
 * <p>
 * • You may use the uncheck methods if you are implementing a strict interface where you don't have the option for adding a throws declaration, and yet throwing an exception is entirely appropriate. Wrapping an exception just to gain the privilege of throwing it results in a stacktrace with spurious exceptions which contribute no information about what actually went wrong. A good example is Runnable.run(), which does not throw any checked exceptions.
 * <p>
 * • In any case, if you decide to use the uncheck methods, be aware of these 2 consequences of throwing CHECKED exceptions without a throws clause: 1) The calling-code won't be able to catch it by name (if you try, the compiler will say: Exception is never thrown in body of corresponding try statement). It will bubble and probably be caught in the main program loop by some "catch Exception" or "catch Throwable", which may be what you want anyway. 2) It violates the principle of least surprise: it will no longer be enough to catch RuntimeException to be able to guarantee catching all possible exceptions. For this reason, I believe this should not be done in framework code, but only in business code that you completely control.
 * <p>
 * References:
 * http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html
 * http://www.mail-archive.com/javaposse@googlegroups.com/msg05984.html
 * Project Lombok annotation: @SneakyThrows
 * Brian Goetz opinion (against) here: How can I throw CHECKED exceptions from inside Java 8 streams?
 * https://softwareengineering.stackexchange.com/questions/225931/workaround-for-java-checked-exceptions?newreg=ddf0dd15e8174af8ba52e091cf85688e *
 */
public final class LambdaExceptionUtil {

    @FunctionalInterface
    public interface Consumer_WithExceptions<T, E extends Exception> {

        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface BiConsumer_WithExceptions<T, U, E extends Exception> {

        void accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface Function_WithExceptions<T, R, E extends Exception> {

        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface Supplier_WithExceptions<T, E extends Exception> {

        T get() throws E;
    }

    @FunctionalInterface
    public interface Runnable_WithExceptions<E extends Exception> {

        void run() throws E;
    }

    /**
     * .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println));
     */
    public static <T, E extends Exception> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T,E> consumer) {
        return t -> {
            try { consumer.accept(t); } catch (Exception exception) { throwAsUnchecked(exception); }
        };
    }

    public static <T, U, E extends Exception> BiConsumer<T,U> rethrowBiConsumer(BiConsumer_WithExceptions<T,U,E> biConsumer) {
        return (t, u) -> {
            try { biConsumer.accept(t, u); } catch (Exception exception) { throwAsUnchecked(exception); }
        };
    }

    /**
     * .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
     */
    public static <T, R, E extends Exception> Function<T,R> rethrowFunction(Function_WithExceptions<T,R,E> function) {
        return t -> {
            try { return function.apply(t); } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
     */
    public static <T, E extends Exception> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T,E> function) {
        return () -> {
            try { return function.get(); } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static void uncheck(Runnable_WithExceptions t) {
        try { t.run(); } catch (Exception exception) { throwAsUnchecked(exception); }
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static <R, E extends Exception> R uncheck(Supplier_WithExceptions<R,E> supplier) {
        try { return supplier.get(); } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * uncheck(Class::forName, "xxx");
     */
    public static <T, R, E extends Exception> R uncheck(Function_WithExceptions<T,R,E> function, T t) {
        try { return function.apply(t); } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E)exception;
    }

}