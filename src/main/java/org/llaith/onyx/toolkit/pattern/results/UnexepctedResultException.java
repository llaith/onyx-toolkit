package org.llaith.onyx.toolkit.pattern.results;

/**
 *
 */
public class UnexepctedResultException extends RuntimeException {

    public UnexepctedResultException(final String message) {
        super(message);
    }

    public UnexepctedResultException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
