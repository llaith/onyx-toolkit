package org.llaith.onyx.toolkit.support.core.json;

/**
 *
 */
public interface JsonMapper {

    String toJson(Object o);

    <X> X fromJson(Class<X> klass, String s);
}
