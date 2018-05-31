package org.llaith.onyx.toolkit.support.fusefs;

import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.stackTraceToLines;

/**
 *
 */
public final class FuseWrapper extends FuseStubFS {

    private static final Logger logger = LoggerFactory.getLogger(FuseWrapper.class);

    private final FuseStubFS delegate;

    public FuseWrapper(FuseStubFS delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getFSName() {
        return "fusefs" + ThreadLocalRandom.current().nextInt();
    }

    @Override
    public void mount(Path mountPoint, boolean blocking, boolean debug, String[] fuseOpts) {
        super.mount(mountPoint, blocking, debug, fuseOpts);
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi) {
        return wrap("readdir", path, () -> delegate.readdir(path, buf, filter, offset, fi));
    }

    @Override
    public int readlink(String path, Pointer buf, long size) {
        return wrap("readlink", path, () -> delegate.readlink(path, buf, size));
    }

    @Override
    public int getattr(String path, FileStat stat) {
        return wrap("getattr", path, () -> delegate.getattr(path, stat));
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        return wrap("open", path, () -> delegate.open(path, fi));
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        return wrap("read", path, () -> delegate.read(path, buf, size, offset, fi));
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        return wrap("release", path, () -> delegate.release(path, fi));
    }

    @Override
    public void umount() {
        super.umount();
    }

    @Override
    public void destroy(Pointer initResult) {
        wrap("destroy", () -> delegate.destroy(initResult));
    }

    private int wrap(final String action, final String path, Supplier<Integer> supplier) {

        try {

            return supplier.get();

        } catch (Throwable e) {

            logger.error(String.format("%s had an uncaught exception for path: %s", action, path));

            for (final String line : stackTraceToLines(e)) logger.error(line.replaceAll("\t", "    "));

            return -ErrorCodes.ENOSYS();

        }

    }

    private void wrap(final String action, Runnable runnable) {

        try {

            runnable.run();

        } catch (Throwable e) {

            logger.error(String.format("%s had an uncaught exception", action));

            for (final String line : stackTraceToLines(e)) logger.error(line.replaceAll("\t", "    "));

        }

    }

}
