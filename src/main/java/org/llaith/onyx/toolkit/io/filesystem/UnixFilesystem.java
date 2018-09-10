package org.llaith.onyx.toolkit.io.filesystem;

import java.nio.file.Path;

/**
 *
 */
public interface UnixFilesystem {

    interface Mounts {

        String mountForDevice(String device);

    }

    PathContext contextualize(Path path);

    int fileCount(Path path);

    int recursiveFileCount(Path path);

    Mounts diskMounts();

}
