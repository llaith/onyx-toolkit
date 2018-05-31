package org.llaith.onyx.toolkit.support.fusefs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.llaith.onyx.toolkit.support.fusefs.spi.FusePathCacheHandler;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.rethrowOrReturn;
import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class FusePathCache {

    private final FusePathCacheHandler lookupPathHandler;

    // we primarily rely on this for thread safety
    private final LoadingCache<String,FusePath> paths = CacheBuilder
            .newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            //.removalListener(MY_LISTENER)
            .build(
                    new CacheLoader<String,FusePath>() {
                        public FusePath load(@Nonnull String path) {
                            return lookupPathHandler.lookupPath(path, paths);
                        }
                    });

    public FusePathCache(final FusePathCacheHandler lookupPathHandler) {

        this.lookupPathHandler = notNull(lookupPathHandler);

    }

    public FusePath lookup(final String path) {

        return rethrowOrReturn(() -> this.paths.get(path));

    }

    public void cache(final String path, final FusePath lookup) {

        this.paths.put(path, lookup);

    }

}
