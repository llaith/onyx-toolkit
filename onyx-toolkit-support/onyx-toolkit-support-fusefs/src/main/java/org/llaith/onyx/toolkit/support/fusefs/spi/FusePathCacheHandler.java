package org.llaith.onyx.toolkit.support.fusefs.spi;

import com.google.common.cache.LoadingCache;
import org.llaith.onyx.toolkit.support.fusefs.FusePath;

/**
 *
 */
public interface FusePathCacheHandler {

    FusePath lookupPath(final String path, final LoadingCache<String,FusePath> paths);

}
