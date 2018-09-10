package org.llaith.onyx.toolkit.io;

import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public class UriUtil {

    public static URI toUri(final String uri) {

        try {

            return new URI(uri);

        } catch (URISyntaxException e) {

            throw UncheckedException.wrap(e);

        }

    }

    public static URI toUri(final String base, final String resource) {

        try {

            return new URI(base).resolve(resource);

        } catch (URISyntaxException e) {

            throw UncheckedException.wrap(e);

        }

    }

    public static URI toUri(final String base, final URI resource) {

        try {

            return new URI(base).resolve(resource);

        } catch (URISyntaxException e) {

            throw UncheckedException.wrap(e);

        }

    }

}
