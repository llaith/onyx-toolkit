package org.llaith.onyx.toolkit.filesystem.impl;

import org.llaith.onyx.toolkit.filesystem.PathContext;
import org.llaith.onyx.toolkit.filesystem.PathInfo;
import org.llaith.onyx.toolkit.filesystem.FileSystemService;
import org.zeroturnaround.exec.ProcessExecutor;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;
import org.llaith.onyx.toolkit.util.lang.StringUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class LinuxFileSystemService implements FileSystemService {

    @Override
    public PathContext contextualize(final Path path) {

        final String dir = path.toAbsolutePath().normalize().toString();

        if (!Files.exists(path)) throw new UncheckedException("Path does not exist: " + dir);

        final String[] mount = evaluateMount(dir);

        final String device = evaluateDevice(mount[0], mount[1]);

        return PathContext.newPathContext(
                path.getFileSystem().getSeparator(),
                device,
                mount[1],
                PathInfo.fromPath(path));

    }

    @Override
    public int fileCount(final Path path) {

        // get the fir
        final String dir = path.toAbsolutePath().normalize().toString();

        // defer to bash
        final String result = this.execute("bash", "-c", String.format(
                "ls %s | wc -l",
                dir));

        // get the value from the line
        final String[] line = result.split("\\r?\\n");

        // get rid of the '.' dir
        return Integer.parseInt(line[0]) - 2;

    }

    @Override
    public int recursiveFileCount(final Path path) {

        // get the fir
        final String dir = path.toAbsolutePath().normalize().toString();

        // defer to bash
        final String result = this.execute("bash", "-c", String.format(
                "find %s | wc -l",
                dir));

        // get the value from the line
        final String[] line = result.split("\\r?\\n");

        // get rid of the '.' dir
        return Integer.parseInt(line[0]) - 1;

    }

    private int evaluateFileCount(final String dir) {

        // defer to bash
        final String result = this.execute("bash", "-c", String.format(
                "find %s | wc -l",
                dir));

        // get the value from the line
        final String[] line = result.split("\\r?\\n");

        // get rid of the '.' dir
        return Integer.parseInt(line[0]) - 1;

    }

    private String[] evaluateMount(final String dir) {

        return this.evaluateDf(execute("df", dir));

    }

    private String[] evaluateDf(final String df) {

        for (final String line : df.split("\\r?\\n")) {

            if (line.contains("No such file or directory"))
                throw new UncheckedException("Could not parse results of 'df':\n" + line);

            if (line.startsWith("Filesystem")) continue;

            final String[] words = line.split("\\s+");

            if (words.length < 6) continue;

            return new String[] {words[0], words[5]};

        }

        throw new UncheckedException("Could not parse results of 'df':\n" + df);

    }

    public String evaluateDevice(final String mapper, final String mount) {

        String blkid = evaluateBlkid(mapper);

        if (blkid == null) blkid = evaluateLsblk(mount);

        if (blkid == null) throw new UncheckedException(String.format(
                "Could not parse results for device: %s mounted at: %s%n",
                mapper,
                mount));

        return blkid;

    }

    private String evaluateBlkid(final String device) {

        final String blkid = this.execute("blkid");

        for (final String line : blkid.split("\\r?\\n")) {

            if (!line.startsWith(device)) continue;

            final String[] words = line.split("\\s+");

            if (words.length < 3) continue; // some lines have 3, some have 4, we want the 3rd

            if (words[1].startsWith("UUID=\""))
                return words[1].substring(6, words[1].length() - 1);

        }

        return null;

    }

    private String evaluateLsblk(final String mount) {

        final String blkid = this.execute("bash", "-c", "lsblk -f | tr --delete '├─' | awk '{print $4,$3}'");

        for (final String line : blkid.split("\\r?\\n")) {

            if (!line.startsWith(mount)) continue;

            final String[] words = line.split("\\s+");

            if (words.length < 2) continue; // some lines have 1, some have 2, we want the 2nd

            if (StringUtil.notBlankOrNull(words[1]))
                return words[1];


        }

        return null;

    }

    private String execute(final String... cmd) {

        try {

            return new ProcessExecutor()
                    .command(cmd)
                    .readOutput(true)
                    .timeout(5, TimeUnit.SECONDS)
                    .destroyOnExit()
                    .execute()
                    .outputUTF8();

        } catch (IOException | InterruptedException | TimeoutException e) {
            throw UncheckedException.wrap(e);
        }

    }

    @Override
    public Mounts diskMounts() {

        final String blkid = this.execute("bash", "-c", "lsblk -f | tr --delete '├─' | awk '{print $4,$3}'");

        final Map<String,String> mounts = new HashMap<>();

        for (final String line : blkid.split("\\r?\\n")) {

            final String[] words = line.split("\\s+");

            if (words.length < 2) continue;
            if (words[0].isEmpty()) continue;
            if (line.startsWith("UUID")) continue;
            if (line.startsWith("[SWAP]")) continue;

            mounts.put(words[1], words[0]);

        }

        return mounts::get;

    }

}
