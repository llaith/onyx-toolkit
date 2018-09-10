package org.llaith.onyx.toolkit.io.filesystem;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.llaith.onyx.toolkit.fn.ExcecutionUtil.rethrowOrReturn;

/**
 *
 */
public class PathInfolizer {

    // warning, it is a feature to return the normalised path from this.
    // bear it in mind, the fromPath() absolutes and normalises the path
    // passed in.
    private final LoadingCache<Path,PathInfo> infos = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .recordStats()
            .build(new CacheLoader<Path,PathInfo>() {
                public PathInfo load(@Nonnull final Path path) {
                    return PathInfo.fromPath(path);
                }
            });

    public PathInfo infolize(@Nonnull final Path path) {

        return rethrowOrReturn(() -> this.infos.get(path));

    }

    public PathInfo infolize(@Nonnull final String path) {

        return rethrowOrReturn(() -> this.infos.get(Paths.get(path)));

    }

    public CacheStats stats() {

        return this.infos.stats();

    }

}
