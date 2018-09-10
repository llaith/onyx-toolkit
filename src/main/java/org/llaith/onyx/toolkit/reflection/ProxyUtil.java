/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection;

import java.lang.reflect.Proxy;

/**
 * Created by IntelliJ IDEA.
 * User: nos
 * Date: 15-Dec-2009
 * Time: 06:26:42
 * <p/>
 * Modified from:
 * http://www.ibm.com/developerworks/java/library/j-jtp08305.html
 */
public class ProxyUtil {

    public static <T> T proxyOf(final Class<T> iface, final T obj) {
        return iface.cast(
                Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                        new Class<?>[]{iface},
                                       (proxy, method, args) -> method.invoke(obj,args)));
    }
}
