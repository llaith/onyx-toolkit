package org.llaith.onyx.toolkit.filesystem;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.rethrowOrReturn;
import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class PathContextualizer {

    private final FileSystemService fs;

    private final LoadingCache<Path,PathContext> contexts = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .recordStats()
            .build(new CacheLoader<Path,PathContext>() {
                public PathContext load(@Nonnull final Path path) {
                    return fs.contextualize(path);
                }
            });

    public PathContextualizer(final FileSystemService fs) {

        this.fs = notNull(fs);

    }

    public PathContext contextualise(@Nonnull final Path path) {

        return rethrowOrReturn(() -> this.contexts.get(notNull(path)));

    }

    public CacheStats stats() {

        return this.contexts.stats();

    }

}
