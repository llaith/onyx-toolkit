package org.llaith.onyx.toolkit.support.fusefs;

import org.llaith.onyx.toolkit.support.fusefs.spi.FuseDirectoryHandler;
import org.llaith.onyx.toolkit.support.fusefs.spi.FuseFileHandler;
import org.llaith.onyx.toolkit.support.fusefs.spi.FusePathHandler;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class FusePath {

    private final String lookup;

    private final boolean exists;

    private final Object info;

    private final Path actual;

    private final FusePathHandler pathHandler;

    private final FuseDirectoryHandler dirHandler;

    private final FuseFileHandler fileHandler;


    public FusePath(final String lookup, final boolean exists, final FusePathHandler handler) {

        this(lookup, exists, null, null, handler);

    }

    public FusePath(final String lookup, final boolean exists, final Object info, final Path actual, final FusePathHandler handler) {

        this.lookup = notNull(lookup);

        this.exists = exists;

        this.info = info; // optional

        this.actual = actual; // optional

        this.pathHandler = handler;

        if (handler != null) {

            if (handler instanceof FuseDirectoryHandler) {

                this.dirHandler = (FuseDirectoryHandler)handler;

                this.fileHandler = null;

            } else if (handler instanceof FuseFileHandler) {

                this.fileHandler = (FuseFileHandler)handler;

                this.dirHandler = null;

            } else throw new IllegalArgumentException("Unknown path-handler type: " + handler);

        } else {

            this.dirHandler = null;

            this.fileHandler = null;

        }

    }

    public String getLookup() {
        return lookup;
    }

    public boolean isExists() {
        return exists;
    }

    public Object getInfo() {
        return info;
    }

    public Path getActual() {
        return actual;
    }

    public boolean isActualExists() {

        return Files.exists(actual);

    }

    public <X> X getInfoAs(final Class<X> klass) {

        if (notNull(klass).isAssignableFrom(this.info.getClass())) return klass.cast(this.info);

        return null;

    }

    public <X> X expectInfoAs(final Class<X> klass) {

        return Guard.notNull(getInfoAs(klass));

    }

    public FusePathHandler getPathHandler() {

        return this.pathHandler;

    }

    public <X extends FusePathHandler> X getPathHandlerAs(final Class<X> klass) {

        if (notNull(klass).isAssignableFrom(this.pathHandler.getClass())) return klass.cast(this.pathHandler);

        return null;

    }

    public <X extends FusePathHandler> X expectPathHandlerAs(final Class<X> klass) {

        return Guard.notNull(getPathHandlerAs(klass));

    }

    public FuseDirectoryHandler getDirHandler() {

        return dirHandler;

    }

    public FuseFileHandler getFileHandler() {

        return fileHandler;

    }

    public boolean isDirectory() {

        return this.dirHandler != null;

    }

    public boolean isRegularFile() {

        return this.dirHandler != null;

    }

}
