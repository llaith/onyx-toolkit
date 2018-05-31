package org.llaith.onyx.toolkit.support.json;

/**
 *
 */
public interface JsonMapper {

    String toJson(Object o);

    <X> X fromJson(Class<X> klass, String s);
}
