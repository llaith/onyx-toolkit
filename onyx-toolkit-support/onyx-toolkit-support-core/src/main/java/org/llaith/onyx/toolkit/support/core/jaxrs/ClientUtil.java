/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.jaxrs;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class ClientUtil {

    public static Response validate(final Response response) {

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        return response;

    }

    public static void checkAndClose(final Response response) {

        validate(response).close();

    }

    public static <T> T entity(final Class<T> type, final Response response) {

        validate(response);

        final T entity = response.readEntity(type);

        response.close();

        return entity;

    }

    public static <T> T entity(final GenericType<T> type, final Response response) {

        validate(response);

        final T entity = response.readEntity(type);

        response.close();

        return entity;

    }

}
