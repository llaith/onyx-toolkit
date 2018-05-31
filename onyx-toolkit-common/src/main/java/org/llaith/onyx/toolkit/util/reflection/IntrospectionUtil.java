/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;


import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * NOT YET USED
 */
public class IntrospectionUtil {

    public <X> Map<String,Object> objectToMap(final X pojo, final Map<String,Object> map) {

        try {

            final BeanInfo info = Introspector.getBeanInfo(pojo.getClass());
            for (final PropertyDescriptor pd : info.getPropertyDescriptors()) {
                final Method reader = pd.getReadMethod();
                if (reader != null) map.put(
                        pd.getName(),
                        reader.invoke(pojo));
            }

            return map;

        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw UncheckedException.wrap(e);
        }

    }

}
