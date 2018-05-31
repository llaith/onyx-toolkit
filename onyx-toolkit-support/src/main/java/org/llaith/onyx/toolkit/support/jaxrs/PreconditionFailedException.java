package org.llaith.onyx.toolkit.support.jaxrs;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * A runtime exception indicating that an access to a resource requested by
 * a client has been {@link javax.ws.rs.core.Response.Status#FORBIDDEN forbidden}
 * by the server.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public class PreconditionFailedException extends ClientErrorException {

    /**
     * Construct a new "forbidden" exception.
     */
    public PreconditionFailedException() {
        super(Response.Status.PRECONDITION_FAILED);
    }

    /**
     * Construct a new "forbidden" exception.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     */
    public PreconditionFailedException(String message) {
        super(message, Response.Status.PRECONDITION_FAILED);
    }

    /**
     * Construct a new "forbidden" exception.
     *
     * @param cause the underlying cause of the exception.
     */
    public PreconditionFailedException(Throwable cause) {
        super(Response.Status.PRECONDITION_FAILED, cause);
    }

    /**
     * Construct a new "forbidden" exception.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the underlying cause of the exception.
     */
    public PreconditionFailedException(String message, Throwable cause) {
        super(message, Response.Status.PRECONDITION_FAILED, cause);
    }

}
