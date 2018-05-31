package org.llaith.onyx.toolkit.support.fusefs.spi;

import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseContext;
import org.llaith.onyx.toolkit.support.fusefs.FusePath;
import org.llaith.onyx.toolkit.support.fusefs.FusePathCache;

/**
 *
 */
public interface FusePathHandler {

    int getattr(
            FusePath lookup,
            FusePathCache paths,
            String path,
            FileStat stat,
            FuseContext context);

}
