package org.llaith.onyx.toolkit.support.core.univocity;

import com.google.common.base.Joiner;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
public class OutputFileRowProcessor implements RowProcessor, AutoCloseable {

    private static final Logger logger = getLogger(OutputFileRowProcessor.class);
    
    // in vars
    private final int batchSize;

    private final File outFile;

    // state
    private PrintWriter out;

    private final StringBuilder buf = new StringBuilder();

    private int currentRow = 0;

    // constants
    private final Joiner joiner = Joiner.on(",");


    public OutputFileRowProcessor(final File outFile, final int batchSize) {

        this.outFile = outFile;

        this.batchSize = batchSize;

    }

    @SuppressWarnings("squid:S2095") // false positive
    @Override
    public void processStarted(final ParsingContext parsingContext) {

        try {

            this.out = new PrintWriter(new FileWriter(this.outFile));

            this.currentRow = 0;

        } catch (IOException e) {

            if (this.out != null) this.out.close();

            throw new RuntimeException(e);

        }

    }

    @Override
    public void rowProcessed(final String[] strings, final ParsingContext parsingContext) {

        try {

            final String append = joiner.join(strings);

            if (append.isEmpty()) return;

            this.buf.append(append);

            this.currentRow++;

            if (this.currentRow % this.batchSize == 0) {

                this.out.print(this.buf);

                this.buf.delete(0, this.buf.length());

                logger.info("Batch upload row: " + currentRow);

            }

        } catch (final RuntimeException e) {

            this.out.close();

            throw e;

        }

    }

    @Override
    public void processEnded(final ParsingContext parsingContext) {

        this.out.print(this.buf);

        logger.info("Batch upload row: " + currentRow);

    }

    @Override
    public void close() throws Exception {

        this.out.close();

    }

}
