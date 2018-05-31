package org.llaith.onyx.toolkit.util.io;

import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 * Warning, untested
 */
public class FileLoadUtil {

    public static void load(final Path path, final Consumer<MappedByteBuffer> consumer) {

        if (!Files.isRegularFile(path)) throw new IllegalArgumentException("Path must be a regular file");
        
        try (final RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {

            final FileChannel in = raf.getChannel();

            final MappedByteBuffer buffer = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());

            notNull(consumer).accept(buffer);

        } catch (IOException e) {

            throw ExceptionUtil.wrapException(e);

        }
        
    }

    public static void load(final Path path, final long offset, final long size, final Consumer<MappedByteBuffer> consumer) {

        if (!Files.exists(path)) throw new IllegalArgumentException("Path must exist");

        if (!Files.isRegularFile(path)) throw new IllegalArgumentException("Path must be a regular file");

        try (final RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {

            notNull(consumer)
                    .accept(raf.getChannel()
                               .map(FileChannel.MapMode.READ_ONLY, offset, size));

        } catch (IOException e) {

            throw ExceptionUtil.wrapException(e);

        }

    }

}
