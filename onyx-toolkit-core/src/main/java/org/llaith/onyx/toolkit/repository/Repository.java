package org.llaith.onyx.toolkit.repository;

/**
 *
 */
public interface Repository<T> {

    <X extends T> RepositoryView<X> select(Class<X> klass);

}
