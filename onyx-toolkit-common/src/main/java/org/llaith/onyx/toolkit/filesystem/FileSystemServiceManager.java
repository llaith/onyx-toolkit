package org.llaith.onyx.toolkit.filesystem;


import org.llaith.onyx.toolkit.filesystem.impl.LinuxFileSystemService;

/**
 *
 */
public class FileSystemServiceManager {

    public FileSystemService getFileSystemService() {

        // todo: eventually return different filesystems based on system properties
        return new LinuxFileSystemService();

    }

}
