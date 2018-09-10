package org.llaith.onyx.toolkit.pattern.results;

/**
 *
 */
public class StaleResultException extends UnexepctedResultException {

    public StaleResultException(final String msg) {

        super(msg);

    }

    public StaleResultException(final String msg, final Throwable e) {

        super(msg, e);

    }

}
