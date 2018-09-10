/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection;


import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * From here: http://stackoverflow.com/questions/835416/how-to-copy-properties-from-one-java-bean-to-another
 */
public class BeanUtil {

    public static void copyProperties(Object fromObj, Object toObj) {

        try {

            final BeanInfo fromBean = Introspector.getBeanInfo(fromObj.getClass());
            final BeanInfo toBean = Introspector.getBeanInfo(toObj.getClass());

            final List<PropertyDescriptor> fromPds = Arrays.asList(fromBean.getPropertyDescriptors());

            for (PropertyDescriptor toPd : toBean.getPropertyDescriptors()) {

                final PropertyDescriptor fromPd = fromPds.get(fromPds.indexOf(toPd));

                if (fromPd.getDisplayName().equals(toPd.getDisplayName())
                        && !fromPd.getDisplayName().equals("class")) {

                    if (toPd.getWriteMethod() != null) toPd.getWriteMethod().invoke(
                            toObj,
                            fromPd.getReadMethod().invoke(fromObj));
                }

            }
        } catch (IntrospectionException e) {

            throw UncheckedException.wrap(String.format(
                    "Failed to copy properties from: %s to: %s.",
                    fromObj,
                    toObj), e);

        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
