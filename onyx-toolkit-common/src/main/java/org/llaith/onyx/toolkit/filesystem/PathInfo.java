package org.llaith.onyx.toolkit.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.rethrowOrReturn;
import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class PathInfo {

    private final Path path;

    private final Map<String,Object> attrs;

    private int uid;
    private int gid;
    private int mode;
    private long rdev;
    private long size;
    private long dev;
    private long ino;
    private int nlink;
    private UserPrincipal owner;
    private GroupPrincipal group;
    private Set<PosixFilePermission> permissions;
    private FileTime creationTime;
    private FileTime ctime;
    private FileTime lastAccessTime;
    private FileTime lastModifiedTime;
    private boolean isDirectory;
    private boolean isOther;
    private boolean isRegularFile;
    private boolean isSymbolicLink;


    public static PathInfo fromPathString(final String pathString) {

        return new PathInfo(Paths.get(pathString));

    }

    public static PathInfo fromPath(final Path path) {

        return new PathInfo(path);

    }

    @SuppressWarnings("unchecked")
    private PathInfo(final Path path) {

        this.path = notNull(path).toAbsolutePath().normalize();

        this.attrs = rethrowOrReturn(() -> Files.readAttributes(
                path,
                "unix:*",
                LinkOption.NOFOLLOW_LINKS));

        this.uid = (int)this.attrs.get("uid");
        this.dev = (long)this.attrs.get("dev");
        this.mode = (int)this.attrs.get("mode");
        this.rdev = (long)this.attrs.get("rdev");
        this.nlink = (int)this.attrs.get("nlink");
        this.size = (long)this.attrs.get("size");
        this.ino = (long)this.attrs.get("ino");
        this.gid = (int)this.attrs.get("gid");
        this.owner = (UserPrincipal)this.attrs.get("owner");
        this.group = (GroupPrincipal)this.attrs.get("group");
        this.permissions = (Set<PosixFilePermission>)this.attrs.get("permissions");
        this.creationTime = (FileTime)this.attrs.get("creationTime");
        this.ctime = (FileTime)this.attrs.get("ctime");
        this.lastAccessTime = (FileTime)this.attrs.get("lastAccessTime");
        this.lastModifiedTime = (FileTime)this.attrs.get("lastModifiedTime");
        this.isDirectory = (boolean)this.attrs.get("isDirectory");
        this.isOther = (boolean)this.attrs.get("isOther");
        this.isRegularFile = (boolean)this.attrs.get("isRegularFile");
        this.isSymbolicLink = (boolean)this.attrs.get("isSymbolicLink");

    }

    public Path asPath() {

        return path;

    }

    public String asString() {

        return path.toString();

    }

    public long rdev() {
        return rdev;
    }

    public long size() {
        return size;
    }

    public int uid() {
        return uid;
    }

    public long dev() {
        return dev;
    }

    public int gid() {
        return gid;
    }

    public long ino() {
        return ino;
    }

    public int mode() {
        return mode;
    }

    public int nlink() {
        return nlink;
    }

    public UserPrincipal owner() {
        return owner;
    }

    public GroupPrincipal group() {
        return group;
    }

    public Set<PosixFilePermission> permissions() {
        return permissions;
    }

    public FileTime creationTime() {
        return creationTime;
    }

    public FileTime ctime() {
        return ctime;
    }

    public FileTime lastAccessTime() {
        return lastAccessTime;
    }

    public FileTime lastModifiedTime() {
        return lastModifiedTime;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isOther() {
        return isOther;
    }

    public boolean isRegularFile() {
        return isRegularFile;
    }

    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    public String usrName() {

        return this.owner.getName();

    }

    public String grpName() {

        return this.group.getName();

    }

    public String permissionsAsString() {

        return PosixFilePermissions.toString(this.permissions);

    }

    public String permissionsSummary() {
        return this.usrName() + ":" + this.grpName() + ":" + this.permissionsAsString();
    }

    public String securitySummary() {
        return this.uid + ":" + this.gid + ":" + this.mode;
    }

    public Date creationDate() {

        return new Date(this.creationTime.toInstant().toEpochMilli());

    }

    public Date changeDate() {

        return new Date(this.ctime.toInstant().toEpochMilli());

    }

    public Date lastAccessDate() {

        return new Date(this.lastAccessTime.toInstant().toEpochMilli());

    }

    public Date lastModifiedDate() {

        return new Date(this.lastModifiedTime.toInstant().toEpochMilli());

    }

    public static void main(String[] args) {

        try {

            final String testFile = "/home/nos/Development/Projects/Java/siwa/amaranth/postgres/schema/01_create_users.sql";

            final PathInfo info = PathInfo.fromPathString(testFile);

            final Map<String,Object> uattrs = new TreeMap<>(Files.readAttributes(
                    Paths.get(testFile),
                    "unix:*"));

            for (Map.Entry<String,Object> entry : uattrs.entrySet()) {

                System.out.printf("%s (%s): %s%n", entry.getKey(), entry.getValue().getClass().getName(), entry.getValue().toString());

            }

            //{creationTime=2017-07-20T18:37:36.692685Z, ctime=2017-07-29T18:10:11.369592Z, dev=64771, fileKey=(dev=fd03,ino=2133777), gid=1000, group=nos, ino=2133777, isDirectory=false, isOther=false, isRegularFile=true, isSymbolicLink=false, lastAccessTime=2017-12-05T22:27:24.38566Z, lastModifiedTime=2017-07-20T18:37:36.692685Z, mode=33204, nlink=1, owner=nos, permissions=[OWNER_READ, OWNER_WRITE, GROUP_WRITE, GROUP_READ, OTHERS_READ], rdev=0, size=107, uid=1000}

            //System.out.println(uattrs);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
