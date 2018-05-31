package org.llaith.onyx.toolkit.filesystem;

import java.nio.file.Path;

/**
 *
 */
public interface FileSystemService {

    interface Mounts {

        String mountForDevice(String device);

    }

    PathContext contextualize(Path path);

    int fileCount(Path path);

    int recursiveFileCount(Path path);

    Mounts diskMounts();

}
