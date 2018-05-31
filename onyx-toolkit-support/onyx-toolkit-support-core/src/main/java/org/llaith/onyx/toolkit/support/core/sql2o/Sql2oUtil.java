package org.llaith.onyx.toolkit.support.core.sql2o;

import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.reflection.FieldAccessUtil;
import org.sql2o.Query;

import java.sql.Statement;

/**
 *
 */
public class Sql2oUtil {

    // Remember to use a transaction. See Sql2oLazyLoadBulkCommand for details.
    @SuppressWarnings("squid:S2095") // special case, using reflection to fix bug in library
    public static Query setFetchSize(final Query query, final int batchSize) {

        final Statement statement = Guard.notNull(FieldAccessUtil.fieldGet(query, Statement.class, "statement"));

        ExceptionUtil.rethrow(() -> statement.setFetchSize(batchSize));

        return query;

    }

}
