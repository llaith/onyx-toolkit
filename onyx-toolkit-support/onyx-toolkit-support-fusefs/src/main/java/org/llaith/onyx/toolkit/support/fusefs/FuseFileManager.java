package org.llaith.onyx.toolkit.support.fusefs;

import jnr.ffi.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static ru.serce.jnrfuse.ErrorCodes.EFAULT;
import static ru.serce.jnrfuse.ErrorCodes.EIO;
import static ru.serce.jnrfuse.ErrorCodes.EISDIR;
import static ru.serce.jnrfuse.ErrorCodes.ENOENT;
import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.rethrowOrReturn;

/**
 *
 */
public class FuseFileManager {

    private static final Logger logger = LoggerFactory.getLogger(FuseFileManager.class);

    private static final int MAX_OPEN_FILES = Integer.MAX_VALUE;

    private final Map<Long,RandomAccessFile> openFiles = new HashMap<>();

    private final AtomicLong nextOpenFileId = new AtomicLong(0);

    public int open(final FusePath lookup,
                    final FuseFileInfo fi) {

        final Path actual = lookup.getActual();

        // if it's missing, return that its missing
        if (!Files.exists(actual)) {

            logger.error("Path {} does not exist", lookup);

            return -ENOENT();

        }

        if (!Files.isRegularFile(actual)) {

            logger.error("Path {} is a directory", lookup);

            return -EISDIR();

        }

        // open the actual file
        synchronized (openFiles) {

            if (openFiles.size() == MAX_OPEN_FILES) {

                logger.error("Cannot open {}: too many open files", lookup.getLookup());

                return -ErrorCodes.EMFILE();

            }

            // open the actual file
            final RandomAccessFile file = rethrowOrReturn(() -> new RandomAccessFile(actual.toFile(), "r"));

            // test the file was opened
            if (file == null) {

                logger.info("Cannot open {}: file does not exist", lookup.getLookup());

                return -ErrorCodes.ENOENT();

            }

            // increment the new number
            final long id = nextOpenFileId.getAndIncrement();

            // add it to the cache
            openFiles.put(id, file);

            // assign the id
            fi.fh.set(id);

        }

        return 0;


    }

    public int read(
            final FusePath lookup,
            final Pointer buf,
            final long size,
            final long offset,
            final FuseFileInfo fi) {

        final int sz = (int)size;

        RandomAccessFile file;

        synchronized (this.openFiles) {

            file = this.openFiles.get(fi.fh.get());

            if (file == null) {

                logger.error("Cannot find descriptor for {} in table", lookup.getLookup());

                return -ErrorCodes.EBADFD();

            }

        }

        int rd = 0;
        int nread = 0;

        try {

            file.seek(offset);

            final byte[] dest = new byte[sz];

            while (rd >= 0 && nread < size) {

                rd = file.read(dest, nread, sz - nread);

                if (rd >= 0) nread += rd;

            }

            if (nread == -1) nread = 0; // EOF 

            else if (nread > 0) buf.put(0, dest, 0, nread);

        } catch (IOException e) {

            logger.error("IOException while reading from {}.", lookup.getLookup(), e);

            return -EIO();

        } catch (Throwable e) {

            logger.error("Unexpected exception on {}", lookup.getLookup(), e);

            return -EFAULT();

        }

        return nread;

    }

    public int release(final FusePath lookup,
                       final FuseFileInfo fi) {

        // get the file id
        final long id = fi.fh.get();

        // access the open files map
        synchronized (this.openFiles) {

            // lookup the file entry
            final RandomAccessFile file = this.openFiles.remove(id);

            // check it worked
            if (file == null) {

                logger.error("Cannot find file descriptor for {}", lookup.getLookup());

                return -ErrorCodes.EBADFD();

            }

            // try to close the actual file
            try {

                file.close();

            } catch (IOException e) {

                logger.error("Failed to close {}", lookup.getLookup(), e);

            }

        }

        return 0;

    }

}
