/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.crash;


import org.crsh.cli.Command;
import org.crsh.cli.Named;
import org.crsh.cli.impl.lang.CommandFactory;
import org.crsh.cli.impl.lang.Util;
import org.crsh.console.jline.JLineProcessor;
import org.crsh.console.jline.Terminal;
import org.crsh.console.jline.TerminalFactory;
import org.crsh.console.jline.console.ConsoleReader;
import org.crsh.console.jline.internal.Configuration;
import org.crsh.shell.Shell;
import org.crsh.shell.ShellFactory;
import org.crsh.standalone.Bootstrap;
import org.crsh.util.InterruptHandler;
import org.crsh.util.Utils;
import org.crsh.vfs.FS;
import org.crsh.vfs.spi.file.FileMountFactory;
import org.crsh.vfs.spi.url.ClassPathMountFactory;
import org.fusesource.jansi.AnsiConsole;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.lang.BooleanUtil;
import org.llaith.onyx.toolkit.util.thread.RuntimeUtil;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;


@Named("shell")
public class CommandShell {

    private final boolean interactive;

    private final Properties properties;


    public CommandShell(final boolean interactive) {

        this(interactive, new Properties());

    }

    public CommandShell(final boolean interactive, final Properties properties) {

        this.interactive = interactive;

        this.properties = Guard.notNull(properties);

    }

    private void runLocalShell(final Bootstrap bootstrap) throws IOException {

        // build the terminal
        final Terminal term = RuntimeUtil.lifecycle(
                TerminalFactory.create(),
                Terminal::restore);

        // Use AnsiConsole only if term doesn't support Ansi
        final PrintStream out = term.isAnsiSupported() ?
                new PrintStream(
                        new BufferedOutputStream(term.wrapOutIfNeeded(new FileOutputStream(FileDescriptor.out)), 16384),
                        false,
                        Configuration.getEncoding()) :
                AnsiConsole.out;

        // build streams
        final FileInputStream in = new FileInputStream(FileDescriptor.in);
        final ConsoleReader reader = new ConsoleReader(null, in, out, term);

        // build the shell
        final Shell shell = bootstrap
                .getContext()
                .getPlugin(ShellFactory.class)
                .create(null);

        // use some bits of jline
        final JLineProcessor processor = new JLineProcessor(
                term.isAnsiSupported(),
                shell,
                reader,
                out);

        // install interupt handler
        new InterruptHandler(processor::interrupt).install();

        // install thread
        final Thread thread = new Thread(processor);
        thread.setDaemon(true);
        thread.start();

        // wait for termination
        try {
            processor.closed();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        Runtime.getRuntime().exit(0);

    }

    @Command
    public void main() throws Exception {

        final FS.Builder builder = new FS.Builder()
                .register("file", new FileMountFactory(Utils.getCurrentDirectory()))
                .register("classpath", new ClassPathMountFactory(Thread.currentThread().getContextClassLoader()));

        final Bootstrap bootstrap = new Bootstrap(
                Thread.currentThread().getContextClassLoader(),
                builder.mount("classpath:/crash/").build(),
                builder.mount("classpath:/crash/commands/").build());

        bootstrap.setConfig(properties);

        bootstrap.bootstrap();

        RuntimeUtil.lifecycle(bootstrap, Bootstrap::shutdown);

        if (BooleanUtil.is(interactive)) {

            this.runLocalShell(bootstrap);

        }

    }

    public void run() throws Exception {

        CommandFactory.DEFAULT.create(CommandShell.class)
                              .matcher()
                              .parse("")
                              .invoke(Util.wrap(this));

    }

    public static void main(String[] args) throws Exception {

        new CommandShell(true).run();

    }

}


