package org.llaith.onyx.toolkit.support.fusefs;

import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseContext;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import org.llaith.onyx.toolkit.support.fusefs.spi.FusePathCacheHandler;
import org.llaith.onyx.toolkit.support.fusefs.spi.FuseDirectoryHandler;
import org.llaith.onyx.toolkit.support.fusefs.spi.FuseFileHandler;

import static ru.serce.jnrfuse.ErrorCodes.ENOENT;

/**
 * Notes:
 * http://fuse.996288.n3.nabble.com/Locking-open-files-in-FUSE-td3719.html
 */
public class FuseFilesystem extends FuseStubFS {

    private static final Logger logger = LoggerFactory.getLogger(FuseFilesystem.class);


    private final FusePathCache pathCache;

    private FuseFileManager fileManager;

    public FuseFilesystem(final FusePathCacheHandler lookupPathHandler) {

        this.pathCache = new FusePathCache(lookupPathHandler);

        this.fileManager = new FuseFileManager();

    }

    @Override
    public int readdir(final String path,
                       final Pointer buf,
                       final FuseFillDir filter,
                       final @off_t long offset,
                       final FuseFileInfo fi) {

        final FuseContext context = this.getContext();

        logger.debug("readdir: %s; by pid=%s uid=%s gid=%s", path, context.pid, context.uid, context.gid);

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);

        final FusePath lookup = this.pathCache.lookup(path);

        if (!lookup.isExists()) return -ENOENT();

        return lookup
                .expectPathHandlerAs(FuseDirectoryHandler.class)
                .readdir(lookup, this.pathCache, path, buf, filter, offset, fi, context);

    }

    @Override
    public int getattr(String path, FileStat stat) {

        final FuseContext context = this.getContext();

        logger.debug("getattr: %s; by pid=%s uid=%s gid=%s", path, context.pid, context.uid, context.gid);

        final FusePath lookup = this.pathCache.lookup(path);

        if (!lookup.isExists()) return -ENOENT();

        return lookup
                .getPathHandler()
                .getattr(lookup, this.pathCache, path, stat, context);

    }

    @Override
    public int open(String path, FuseFileInfo fi) {

        final FuseContext context = this.getContext();

        logger.debug("open: %s; by pid=%s uid=%s gid=%s", path, context.pid, context.uid, context.gid);

        final FusePath lookup = this.pathCache.lookup(path);

        if (!lookup.isExists()) return -ENOENT();

        return lookup
                .expectPathHandlerAs(FuseFileHandler.class)
                .open(lookup, this.pathCache, this.fileManager, path, fi, context);

    }

    @Override
    public int read(final String path, final Pointer buf, final long size, final long offset, final FuseFileInfo fi) {

        final FuseContext context = this.getContext();

        logger.debug("read: %s; by pid=%s uid=%s gid=%s", path, context.pid, context.uid, context.gid);

        final FusePath lookup = this.pathCache.lookup(path);

        if (!lookup.isExists()) return -ENOENT();

        return lookup
                .expectPathHandlerAs(FuseFileHandler.class)
                .read(lookup, this.pathCache, this.fileManager, path, buf, size, offset, fi, context);

    }

    @Override
    public int release(final String path, final FuseFileInfo fi) {

        final FuseContext context = this.getContext();

        logger.debug("release: %s; by pid=%s uid=%s gid=%s", path, context.pid, context.uid, context.gid);

        final FusePath lookup = this.pathCache.lookup(path);

        if (!lookup.isExists()) return -ENOENT();

        return lookup
                .expectPathHandlerAs(FuseFileHandler.class)
                .release(lookup, this.pathCache, this.fileManager, path, fi, context);

    }

}