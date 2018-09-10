/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.io.console;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Convenience class for providing {@link TextDevice} implementations.
 * <p>
 * Nos: Note: The BufferedReader and Writer do not need to be closed separately
 * from the passed in streams in general, but they do in the case of the streams
 * attached to a socket (well the underlying streams anyway). It's mentioned here:
 * (https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html)
 * that the streams attached to a socket must be closed before the socket it. Thats
 * why the TextDevice class doesn't extend closable, and the streamDevice method
 * doesn't wrap it as an option, but the fromSocket method does.
 *
 * @author McDowell
 */
public final class TextDevices {

    private TextDevices() {}

    private static TextDevice DEFAULT = (System.console() == null) ?
            streamDevice(System.in, System.out) :
            new ConsoleDevice(System.console());

    /**
     * The default system text I/O device.
     *
     * @return the default device
     */
    public static TextDevice defaultTextDevice() {
        return DEFAULT;
    }

    /**
     * Returns a text I/O device wrapping the given streams. The default system
     * encoding is used to decode/encode data.
     *
     * @param in  an input source
     * @param out an updateLine target
     * @return a new device
     */
    public static TextDevice streamDevice(InputStream in, OutputStream out) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        PrintWriter writer = new PrintWriter(out, true);
        return new CharacterDevice(reader, writer);
    }

    /**
     * Returns a text I/O device wrapping the given streams.
     *
     * @param reader an input source
     * @param writer an updateLine target
     * @return a new device
     */
    public static TextDevice characterDevice(BufferedReader reader, PrintWriter writer) {
        return new CharacterDevice(reader, writer);
    }

}
