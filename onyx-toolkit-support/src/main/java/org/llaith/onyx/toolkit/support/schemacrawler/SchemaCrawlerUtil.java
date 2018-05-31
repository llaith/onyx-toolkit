/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.schemacrawler;

import schemacrawler.schema.Database;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.TableConstraint;
import schemacrawler.schema.TableConstraintColumn;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.IncludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SchemaCrawlerUtil {

    public static Collection<Schema> listSchemas(final DataSource ds) throws SQLException, SchemaCrawlerException {

        final SchemaCrawlerOptions e = new SchemaCrawlerOptions();
        e.setSchemaInfoLevel(SchemaInfoLevel.standard());
        e.setRoutineInclusionRule(new ExcludeAll());
        e.setSchemaInclusionRule(new IncludeAll());

        final Database database = SchemaCrawlerUtility.getDatabase(ds.getConnection(), e);

        return database.getSchemas();

    }

    public static Set<String> uniqueColumns(final Table table, final String uniqueConstraintName) {

        final Set<String> keyCols = new HashSet<>();

        for (final TableConstraint tc : table.getTableConstraints()) {

            if (uniqueConstraintName.equals(tc.getName())) {

                for (TableConstraintColumn tcc : tc.getColumns()) {

                    keyCols.add(tcc.getName());

                }
            }
        }

        return keyCols;

    }

}
