/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import static org.llaith.onyx.toolkit.util.exception.ExceptionUtil.rethrowOrReturn;

/**
 * @author McDowell
 * @{link TextDevice} implementation wrapping character streams.
 */
class CharacterDevice implements TextDevice {

    private final BufferedReader reader;
    private final PrintWriter writer;

    public CharacterDevice() {
        this(
                new BufferedReader(new InputStreamReader(System.in)),
                new PrintWriter(rethrowOrReturn(() -> new OutputStreamWriter(System.out, "UTF-8"))));
    }

    public CharacterDevice(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public CharacterDevice printf(String fmt, Object... params)
            throws ConsoleException {
        writer.printf(fmt, params);
        return this;
    }

    @Override
    public String readLine() throws ConsoleException {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char[] readPassword() throws ConsoleException {
        return readLine().toCharArray();
    }

    @Override
    public Reader reader() throws ConsoleException {
        return reader;
    }

    @Override
    public TextDevice writeLine(final String line) throws ConsoleException {

        writer.println(line);

        writer.flush();

        return this;

    }

    @Override
    public PrintWriter writer() throws ConsoleException {
        return writer;
    }

}
