package org.llaith.onyx.toolkit.io.filesystem;


import org.llaith.onyx.toolkit.io.filesystem.impl.LinuxFilesystem;

/**
 *
 */
public class UnixFilesystemManager {

    public UnixFilesystem getFilesystem() {

        // todo: eventually return different filesystems based on system properties
        return new LinuxFilesystem();

    }

}
