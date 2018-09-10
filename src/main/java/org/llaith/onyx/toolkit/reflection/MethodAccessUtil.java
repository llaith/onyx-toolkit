package org.llaith.onyx.toolkit.reflection;

import com.google.common.base.Defaults;
import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.lang.Guard;

import java.lang.reflect.Method;

/**
 *
 */
public class MethodAccessUtil {

    public static Object callGetter(final Object target, final String methodName) {

        Class<?> klass = Guard.notNull(target.getClass());

        while (klass != null) {

            try {

                Method method = klass.getDeclaredMethod(methodName);

                method.setAccessible(true);

                final Object value = method.invoke(target);

                if ((method.getReturnType().isPrimitive()) && (value == null)) {
                    return Defaults.defaultValue(method.getReturnType());
                }

                return value;

            } catch (NoSuchMethodException e) {

                klass = klass.getSuperclass();

            } catch (Exception e) {

                throw UncheckedException.wrap(String.format(
                        "Cannot get value from method named: %s in object of: %s.",
                        methodName,
                        klass.getName()
                ), e);

            }

        }

        throw new UncheckedException(String.format(
                "Cannot find method named: %s in object of: %s.",
                methodName,
                target.getClass().getName()));

    }

    public static <X> X callGetter(final Object target, final Class<X> methodClass, final String methodName) {

        final Object val = callGetter(target, methodName);

        if (val == null) return null;

        if (methodClass.isAssignableFrom(val.getClass())) return methodClass.cast(val);

        throw new ClassCastException(String.format(
                "Class of the return type of the method named: %s in object of: %s is of type: %s but we expected a type of: %s.",
                methodName,
                target.getClass().getName(),
                val.getClass(),
                methodClass));


    }

}
