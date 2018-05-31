/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.io;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 * This purely exists because I despise the IOException for files.
 */
public class FileUtil {

    public static String readFromPath(final String pathStr) {

        return readFromPath(new File(pathStr));

    }

    public static String readFromPath(final File path) {

        try {

            return Files.asCharSource(
                    path,
                    Charsets.UTF_8).read();

        } catch (IOException e) {
            throw UncheckedException.wrap(e);
        }

    }

    public static String readFromClasspath(final String path) {

        try {

            return Resources.toString(
                    Resources.getResource(path),
                    Charsets.UTF_8);

        } catch (IOException e) {
            throw UncheckedException.wrap(e);
        }

    }

    public static File absolutify(final File file) {

        return file
                .toPath()
                .toAbsolutePath()
                .toFile();

    }

    public static List<String> linesFrom(final File file) {

        return linesFrom(file, Charsets.UTF_8);

    }

    public static List<String> linesFrom(final File file, final Charset charset) {

        // purely exists to get rid of that checked exception!

        try {

            return Files.readLines(
                    file,
                    charset);

        } catch (IOException e) {

            throw new UncheckedException(String.format("Cannot read lines from: %s", e));

        }

    }

    public static long crcOf(final Path path, final int bytes) {

        try (final FileInputStream fis = new FileInputStream(path.toFile())) {

            final Checksum checksum = new CRC32();

            final byte[] buffer = new byte[bytes];

            final int read = fis.read(buffer);

            if (read < 1) return 0;

            checksum.update(buffer, 0, read);

            return checksum.getValue();

        } catch (IOException e) {

            throw UncheckedException.wrap(e);

        }

    }

    public static long crcOf(final Path path) {

        // warning, untested, test first

        try (final FileInputStream fis = new FileInputStream(path.toFile())) {

            final Checksum checksum = new CRC32();

            final byte[] buffer = new byte[1024 * 1024];

            int read;

            while ((read = fis.read(buffer)) > 0) {

                checksum.update(buffer, 0, read);

            }

            return checksum.getValue();

        } catch (IOException e) {

            throw UncheckedException.wrap(e);

        }

    }

    // https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int)(Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // to remind me how to do it properly!!!
    public static Path absolutePathOf(final Path path) {

        return notNull(path).toAbsolutePath().normalize();

    }

    public static List<File> allDirs(final File file) {

        final File[] files = notNull(file).listFiles(File::isDirectory);

        final List<File> subdirs = files != null ?
                new ArrayList<>(Arrays.asList(files)) :
                Collections.emptyList();

        final List<File> deepSubdirs = new ArrayList<>();

        for (final File subdir : subdirs) {
            deepSubdirs.addAll(allDirs(subdir));
        }

        subdirs.addAll(deepSubdirs);

        return subdirs;

    }

    // https://stackoverflow.com/questions/35988192/java-nio-most-concise-recursive-directory-delete
    public static void recursiveDeleteDirectory(final String dir) throws IOException {

        java.nio.file.Files.walk(Paths.get(dir))
                           .sorted(Comparator.reverseOrder())
                           .map(Path::toFile)
                           .peek(System.out::println)
                           .forEach(File::delete);

    }

    // just here to capture the standard approach and reduce noise
    public static Path normalisePathDeleteMe(final Path path) {

        return notNull(path).toAbsolutePath().normalize();

    }

    // just here to capture the standard approach and reduce noise
    public static String asString(final Path path) {

        return normalisePathDeleteMe(path).toString();

    }

    public static String[] splitPathToStrings(final Path path) {

        final Path[] splits = splitPath(path);

        final List<String> strings = new ArrayList<>();

        for (final Path split : splits) {

            strings.add(split.toString());

        }

        return strings.toArray(new String[strings.size()]);

    }

    public static Path[] splitPath(final Path path) {

        final List<Path> splits = new ArrayList<>();

        Path search = path.toAbsolutePath().normalize().getParent();

        while (search != null) {

            splits.add(search);

            search = search.getParent();

        }

        return splits.toArray(new Path[splits.size()]);

    }

    public static class ParsedFilename {

        public static ParsedFilename parseFilename(final String filename) {

            return new ParsedFilename(
                    filename.substring(0, filename.lastIndexOf("/")), // dirname
                    filename.substring(filename.lastIndexOf("/") + 1)); // filename

        }

        public final String dirname;

        public final String filename;

        private ParsedFilename(final String dirname, final String filename) {

            this.dirname = dirname;

            this.filename = filename;

        }

    }

}
