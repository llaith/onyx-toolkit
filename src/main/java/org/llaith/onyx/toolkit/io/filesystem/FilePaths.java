package org.llaith.onyx.toolkit.io.filesystem;

import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.fn.ExcecutionUtil;
import org.llaith.onyx.toolkit.lang.Guard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * Basically, an experiment with fluency substituting for dependent types.
 */
public class FilePaths {

    public static SomePath from(final Path path) {

        return new TheFilePath(path);

    }

    public static SomePath from(final File file) {

        return new TheFilePath(file);

    }

    public static SomePath from(final String string) {

        return new TheFilePath(string);

    }

    public static SomePath from(final URI uri) {

        return new TheFilePath(uri);

    }

    public static Directory directoryFrom(final Path path) {

        return new TheFilePath(path).asDirectory();

    }

    public static Directory directoryFrom(final File file) {

        return new TheFilePath(file).asDirectory();

    }

    public static Directory directoryFrom(final String string) {

        return new TheFilePath(string).asDirectory();

    }

    public static Directory directoryFrom(final URI uri) {

        return new TheFilePath(uri).asDirectory();

    }

    public static RegularFile regularFileFrom(final Path path) {

        return new TheFilePath(path).asRegularFile();

    }

    public static RegularFile regularFileFrom(final File file) {

        return new TheFilePath(file).asRegularFile();

    }

    public static RegularFile regularFileFrom(final String string) {

        return new TheFilePath(string).asRegularFile();

    }

    public static RegularFile regularFileFrom(final URI uri) {

        return new TheFilePath(uri).asRegularFile();

    }

    public interface FilePath {

        Path toPath();

        File toFile();

        String toString();

        URI toUri();

    }

    public interface SomePath extends FilePath {

        boolean exists();

        boolean isDirectory();

        boolean isRegularFile();

        Directory asDirectory();

        RegularFile asRegularFile();

        Directory createDirectory();

        RegularFile createRegularFile(boolean overwrite);

        SomePath toAbsolutePath();

    }

    public interface ExistingPath extends FilePath {

        Date creationDate();

        Date lastAccessDate();

        Date lastModifiedDate();

        PathInfo toPathInfo();

    }

    public interface Directory extends ExistingPath {

    }

    public interface RegularFile extends ExistingPath {

    }

    private static class TheFilePath implements SomePath, Directory, RegularFile {

        private final Path path;

        private File file;

        private String string;

        private URI uri;


        private TheFilePath(final Path path) {

            this.path = Guard.notNull(path);

        }

        private TheFilePath(final File file) {

            this.path = Guard.notNull(file).toPath();

            this.file = file;

        }

        private TheFilePath(final String string) {

            this.path = Paths.get(Guard.notNull(string));

            this.string = string;

        }

        private TheFilePath(final URI uri) {

            this.path = Paths.get(Guard.notNull(uri));

            this.uri = uri;

        }

        @Override
        public Path toPath() {

            return this.path;

        }

        @Override
        public File toFile() {

            if (this.file == null) this.file = this.path.toFile();

            return this.file;

        }

        @Override
        public String toString() {

            if (this.string == null) this.string = this.path.toString();

            return this.string;

        }

        @Override
        public URI toUri() {

            if (this.uri == null) this.uri = this.path.toUri();

            return this.uri;

        }

        @Override
        public boolean exists() {

            return Files.exists(this.path);

        }

        @Override
        public boolean isDirectory() {

            return Files.isDirectory(this.path);

        }

        @Override
        public boolean isRegularFile() {

            return Files.isRegularFile(this.path);

        }

        @Override
        public Directory asDirectory() {

            ExcecutionUtil.rethrow(() -> {

                if (!this.exists()) throw new IOException(String.format(
                        "The parameter path: [%s] must exist",
                        this.path));

                if (!this.isDirectory()) throw new IOException(String.format(
                        "The parameter path: [%s] is not a directory",
                        this.path));

            });

            return this;

        }

        @Override
        public RegularFile asRegularFile() {

            ExcecutionUtil.rethrow(() -> {

                if (!this.exists()) throw new IOException(String.format(
                        "The parameter path: [%s] must exist",
                        this.path));

                if (!this.isRegularFile()) throw new IOException(String.format(
                        "The parameter path: [%s] is not a regular-file",
                        this.path));

            });

            return this;

        }

        @Override
        public Directory createDirectory() {

            if (this.exists())
                throw new IllegalStateException("Cannot overwrite existing path as directory: " + this.toString());

            ExcecutionUtil.rethrow(() -> Files.createDirectories(this.path));

            return this;

        }

        @Override
        public RegularFile createRegularFile(final boolean overwrite) {

            if (this.exists()) {

                if (!overwrite) throw new UncheckedException("Cannot overwrite existing file: " + this.toString());

            } else {

                ExcecutionUtil.rethrow(() -> Files.createFile(this.path));

            }

            return this;

        }

        @Override
        public SomePath toAbsolutePath() {

            return new TheFilePath(this.path.toAbsolutePath().normalize());

        }

        @Override
        public Date creationDate() {

            return new Date(ExcecutionUtil.rethrowOrReturn(() -> Files
                    .readAttributes(this.toPath(), BasicFileAttributes.class)
                    .creationTime()
                    .toMillis()));

        }

        @Override
        public Date lastAccessDate() {

            return new Date(ExcecutionUtil.rethrowOrReturn(() -> Files
                    .readAttributes(this.toPath(), BasicFileAttributes.class)
                    .lastAccessTime()
                    .toMillis()));

        }

        @Override
        public Date lastModifiedDate() {

            return new Date(ExcecutionUtil.rethrowOrReturn(() -> Files
                    .readAttributes(this.toPath(), BasicFileAttributes.class)
                    .lastModifiedTime()
                    .toMillis()));

        }

        @Override
        public PathInfo toPathInfo() {

            return PathInfo.fromPath(this.path);

        }

    }

}
