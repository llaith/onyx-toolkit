/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.jline;


import jline.console.ConsoleReader;
import org.llaith.onyx.toolkit.console.ConsoleException;
import org.llaith.onyx.toolkit.console.TextDevice;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

/**
 * {@link TextDevice} implementation wrapping a {@link Console}.
 *
 * @author McDowell
 */
public class JLineConsoleDevice implements TextDevice {

    private final ConsoleReader console;
    private final PrintWriter out;
    private final Reader in;

    public JLineConsoleDevice() {
        this(ExceptionUtil.rethrowOrReturn(ConsoleReader::new));
    }

    public JLineConsoleDevice(final ConsoleReader console) {
        this.console = console;
        this.out = new PrintWriter(console.getOutput());
        this.in = new InputStreamReader(console.getInput());
    }

    public ConsoleReader getConsoleReader() {
        return this.console;
    }

    @Override
    public TextDevice printf(String fmt, Object... params) throws ConsoleException {
        this.out.format(fmt, params);
        return this;
    }

    @Override
    public Reader reader() throws ConsoleException {
        return this.in;
    }

    @Override
    public TextDevice writeLine(final String line) throws ConsoleException {

        this.out.println(line);

        this.out.flush();

        return this;

    }

    @Override
    public String readLine() throws ConsoleException {
        try {
            return this.console.readLine();
        } catch (IOException e) {
            throw new ConsoleException(e);
        }
    }

    @Override
    public char[] readPassword() throws ConsoleException {
        try {
            return console.readLine("*").toCharArray();
        } catch (IOException e) {
            throw new ConsoleException(e);
        }
    }

    @Override
    public PrintWriter writer() throws ConsoleException {
        return this.out;
    }

}