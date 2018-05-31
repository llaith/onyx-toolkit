package org.llaith.onyx.toolkit.output.progress;

import com.google.common.base.Strings;
import org.llaith.onyx.toolkit.util.text.AnsiCodes;

import java.text.DecimalFormat;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class ProgressTextRenderer {

    private final boolean colour;

    private final String template;

    private final DecimalFormat percentFormat;

    public ProgressTextRenderer(final boolean colour) {

        this(colour, new DecimalFormat("0"));

    }

    public ProgressTextRenderer(final boolean colour, final DecimalFormat percentFormat) {

        this.colour = colour;

        this.template = colour ?
                this.templateColourStr() :
                this.templateStr();

        this.percentFormat = notNull(percentFormat);

    }

    private String templateStr() {

        return "(%s/%s) [%s] %3s%%";

    }

    private String templateColourStr() {

        return AnsiCodes.WHITE +
                "(" +
                AnsiCodes.BLUE +
                "%s" +
                AnsiCodes.WHITE +
                "/" +
                AnsiCodes.BLUE +
                "%s" +
                AnsiCodes.WHITE +
                ")" +
                AnsiCodes.RESET +
                " " +
                AnsiCodes.WHITE +
                "[" +
                AnsiCodes.HIGH_INTENSITY +
                AnsiCodes.GREEN +
                "%s" +
                AnsiCodes.RESET +
                AnsiCodes.WHITE +
                "]" +
                AnsiCodes.RESET +
                " " +
                AnsiCodes.BLUE +
                "%3s" +
                AnsiCodes.WHITE +
                "%%" +
                AnsiCodes.RESET;

    }

    public DecimalFormat percentFormat() {

        return this.percentFormat;

    }

    public String render(final ProgressControl control) {

        final String bar = this.renderBar(control);

        final int length = String.valueOf(control.total()).length();
        
        return String.format(
                template,
                Strings.padStart(""+control.count(),length,' '),
                control.total(),
                bar,
                this.percentFormat.format(control.percent()));

    }

    private String renderBar(final ProgressControl control) {

        final StringBuilder bar = new StringBuilder();

        for (int i = 0; i < control.scale(); i++) {

            if (i < control.complete()) {
                bar.append("=");
            } else if (i == control.complete()) {
                if (this.colour) bar.append(AnsiCodes.YELLOW);
                bar.append(">");
                if (this.colour) bar.append(AnsiCodes.RESET);
            } else {
                bar.append(" ");
            }

        }

        return bar.toString();

    }

}
