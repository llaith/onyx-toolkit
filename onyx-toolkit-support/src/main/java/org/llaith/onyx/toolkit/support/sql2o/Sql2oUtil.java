package org.llaith.onyx.toolkit.support.sql2o;

import org.sql2o.Query;
import org.llaith.onyx.toolkit.util.reflection.FieldAccessUtil;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

import java.sql.Statement;

/**
 *
 */
public class Sql2oUtil {
    
    // Remember to use a transaction. See Sql2oLazyLoadBulkCommand for details.
    @SuppressWarnings("squid:S2095") // special case, using reflection to fix bug in library
    public static Query setFetchSize(final Query query, final int batchSize) {

        final Statement statement = FieldAccessUtil.fieldGet(query, Statement.class, "statement");

        ExceptionUtil.rethrow(() -> statement.setFetchSize(batchSize));

        return query;

    }

}
