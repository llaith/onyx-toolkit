/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.console;

import java.io.PrintWriter;
import java.io.Reader;

/**
 * Abstraction representing a text input/updateLine device.
 *
 * @author McDowell
 */
public interface TextDevice {

    TextDevice printf(String fmt, Object... params) throws ConsoleException;

    String readLine() throws ConsoleException;

    char[] readPassword() throws ConsoleException;

    Reader reader() throws ConsoleException;

    TextDevice writeLine(String line) throws ConsoleException;

    PrintWriter writer() throws ConsoleException;
    
}
