/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.schemacrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schema.Database;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;
import org.llaith.onyx.toolkit.util.lang.Guard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class SchemaHolder {

    private static final Logger log = LoggerFactory.getLogger(SchemaHolder.class);


    private final Database database;


    public SchemaHolder(final DataSource dataSource, final boolean includeColumnTypes, final Set<String> schemaNames) {

        this(dataSource, includeColumnTypes, text -> Guard.notNull(schemaNames).contains(text));

    }

    public SchemaHolder(final DataSource dataSource, final boolean includeColumnTypes, final InclusionRule schemaInclusionRule) {

        Connection connection = null;

        try {

            log.info("Initialising SchemaCrawler");

            final SchemaInfoLevel level = SchemaInfoLevel.standard();
            level.setRetrieveColumnDataTypes(includeColumnTypes);

            final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            options.setSchemaInfoLevel(level);
            options.setRoutineInclusionRule(new ExcludeAll());
            options.setSchemaInclusionRule(schemaInclusionRule);


            log.info("Getting connection");
            connection = dataSource.getConnection();

            log.info("Getting database");
            this.database = SchemaCrawlerUtility.getDatabase(
                    connection,
                    options);

        } catch (SchemaCrawlerException | SQLException e) {
            throw UncheckedException.wrap(e);
        } finally {
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                log.warn("Supressing Exception: ", e);
            }
        }

    }

    public Table getTable(final String schemaName, final String tableName) {

        return Guard.notNull(database.getTable(Guard.notNull(database.getSchema(schemaName)), tableName));

    }

    public Collection<Table> getTables(final String schemaName) {

        return database.getTables(Guard.notNull(database.getSchema(schemaName)));

    }

    public Database getDatabase() {

        return database;

    }

}
