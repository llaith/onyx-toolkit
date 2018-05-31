package org.llaith.onyx.toolkit.support.core.picolo;

import com.google.common.base.Throwables;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;

import static picocli.CommandLine.Help.Ansi.AUTO;
import static picocli.CommandLine.usage;

/**
 * Commands that are invoked must implement
 */
public class CommandLineUtil {

    public interface Executable extends Consumer<List<CommandLine>> {}

    public static abstract class BaseCommand {

        @Option(names = {"--version"}, help = true)
        protected boolean versionRequested;

        @Option(names = {"-h", "--help"}, help = true)
        protected boolean helpRequested;

        protected abstract String version();

    }

    public static void runCommand(final BaseCommand command, final PrintStream out, final String... args) {

        final CommandLine cli = new CommandLine(command); // validate command outside of try-catch

        try {

            final List<CommandLine> commands = cli.parse(args);

            final BaseCommand cmd = (BaseCommand)commands.get(0).getCommand();

            final boolean abort = (cmd.helpRequested) || (commands.size() < 2) || (cmd.versionRequested);

            if (abort) {

                if (cmd.versionRequested) System.out.printf("Amaranth Control, version: %s", cmd.version());
                else usage(cmd, System.err, AUTO);

                return;

            }

            //noinspection unchecked
            ((Executable)commands
                    .get(commands.size() - 1)
                    .getCommand())
                    .accept(commands);

        } catch (final Exception ex) {

            out.println("Execution failed with error: "+ex.getMessage());

            out.println("Stacktrace follows:");

            out.println(Throwables.getStackTraceAsString(ex));
            
            //cli.usage(out, AUTO);

        }

    }

}
