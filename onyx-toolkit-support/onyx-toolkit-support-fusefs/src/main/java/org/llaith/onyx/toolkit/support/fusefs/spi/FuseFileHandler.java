package org.llaith.onyx.toolkit.support.fusefs.spi;

import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import ru.serce.jnrfuse.struct.FuseContext;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import org.llaith.onyx.toolkit.support.fusefs.FusePath;
import org.llaith.onyx.toolkit.support.fusefs.FuseFileManager;
import org.llaith.onyx.toolkit.support.fusefs.FusePathCache;

/**
 *
 */
public interface FuseFileHandler extends FusePathHandler {

    int open(
            FusePath lookup,
            FusePathCache paths,
            FuseFileManager files,
            String path,
            FuseFileInfo fi,
            FuseContext context);

    int read(
            FusePath lookup,
            FusePathCache paths,
            FuseFileManager files,
            String path,
            Pointer buf,
            @size_t long size,
            @off_t long offset,
            FuseFileInfo fi,
            FuseContext context);

    int release(
            FusePath lookup,
            FusePathCache paths,
            FuseFileManager files,
            String path,
            FuseFileInfo fi,
            FuseContext context);

}
