package org.llaith.onyx.toolkit.filesystem;

/**
 *
 */
public class PathContext {

    public static PathContext newPathContext(final String separator, final String device, final String mount, final PathInfo info) {

        return new PathContext(separator, device, mount, info);

    }

    private final String separator;

    private final String device;

    private final String mount;

    private final PathInfo info;

    private PathContext(final String separator, final String device, final String mount, final PathInfo info) {

        this.separator = separator;
        this.device = device;
        this.mount = mount;
        this.info = info;

    }

    public String device() {

        return device;

    }

    public String mount() {

        return mount;

    }
    
    public String separator() {
        
        return this.separator;
        
    }

    public PathInfo info() {

        return info;

    }

    public String toDeviceRelativePath() {

        return this.info
                .asString()
                .replaceFirst(this.mount, "/");

    }

    public String toDevicePath() {

        return this.info
                .asString()
                .replaceFirst(this.mount, this.device);

    }

}
