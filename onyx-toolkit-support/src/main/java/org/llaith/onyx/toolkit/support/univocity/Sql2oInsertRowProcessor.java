package org.llaith.onyx.toolkit.support.univocity;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import org.slf4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
public class Sql2oInsertRowProcessor implements RowProcessor {

    private static final Logger logger = getLogger(Sql2oInsertRowProcessor.class);

    public interface InsertProcessor {

        boolean buildInsert(Query query, String[] strings) throws Exception;

    }

    private final Sql2o sql2o;

    private final String sql;

    private final InsertProcessor processor;

    private final int batchSize;

    private Connection connection;

    private Query insert;

    private int currentRow = 0;


    public Sql2oInsertRowProcessor(final Sql2o sql2o, final String sql, final InsertProcessor processor, final int batchSize) {

        this.sql2o = sql2o;

        this.sql = sql;

        this.processor = processor;

        this.batchSize = batchSize;

    }

    @Override
    public void processStarted(final ParsingContext context) {

        try {

            this.connection = this.sql2o.beginTransaction();

            this.insert = this.connection.createQuery(this.sql);

        } catch (RuntimeException e) {

            this.closeQuietlyAndRethrow(e);

        }

    }

    @Override
    public void rowProcessed(final String[] row, final ParsingContext context) {

        try {

            boolean added = this.processor.buildInsert(this.insert, row);

            if (!added) return;

            insert.addToBatch();

            this.currentRow++;

            if (this.currentRow % this.batchSize == 0) {

                logger.info("Batch upload row: " + currentRow);

                insert.executeBatch();

            }

        } catch (Exception e) {

            final SQLException sqlException = ExceptionUtil.findException(SQLException.class, e);

            if (sqlException != null) ExceptionUtil.outputSqlException(sqlException);

            this.closeQuietlyAndRethrow(e);

        }

    }

    @Override
    public void processEnded(final ParsingContext context) {

        try {

            // get the last batch
            insert.executeBatch();

            logger.info("Batch upload row: " + currentRow);

            this.connection.commit();

        } catch (RuntimeException e) {

            this.closeQuietlyAndRethrow(e);

        } finally {

            this.closeQuietly();

        }

    }

    private void closeQuietly() {

        this.closeQuietlyAndRethrow(null);

    }

    private void closeQuietlyAndRethrow(final Exception e) {

        RuntimeException returnE = (e != null) ?
                UncheckedException.wrap(e) :
                null;

        if (this.insert != null) {
            try {

                this.insert.close();

            } catch (RuntimeException e2) {

                if (returnE == null) returnE = e2;
                else returnE.addSuppressed(e2);

            }
        }

        if (this.connection != null) {
            try {

                this.connection.close();

            } catch (RuntimeException e2) {

                if (returnE == null) returnE = e2;
                else returnE.addSuppressed(e2);

            }

        }

        if (returnE != null) throw returnE;

    }

}
