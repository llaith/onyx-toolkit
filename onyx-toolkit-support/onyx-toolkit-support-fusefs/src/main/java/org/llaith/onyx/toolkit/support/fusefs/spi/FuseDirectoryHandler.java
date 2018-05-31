package org.llaith.onyx.toolkit.support.fusefs.spi;

import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.struct.FuseContext;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import org.llaith.onyx.toolkit.support.fusefs.FusePath;
import org.llaith.onyx.toolkit.support.fusefs.FusePathCache;

/**
 *
 */
public interface FuseDirectoryHandler extends FusePathHandler {

    int readdir(
            FusePath lookup,
            FusePathCache paths,
            String path,
            Pointer buf,
            FuseFillDir filter,
            @off_t long offset,
            FuseFileInfo fi,
            FuseContext context);

}
