package org.llaith.onyx.toolkit.util.text;

/**
 * https://github.com/dialex/JCDP
 * https://gist.github.com/nathan-fiscaletti/9dc252d30b51df7d710a
 */
public class AnsiCodes {

    public static final String	RESET				= "\u001B[0m";

    public static final String	HIGH_INTENSITY		= "\u001B[1m";
    public static final String	LOW_INTENSITY		= "\u001B[2m";

    public static final String	ITALIC				= "\u001B[3m";
    public static final String	UNDERLINE			= "\u001B[4m";
    public static final String	BLINK				= "\u001B[5m";
    public static final String	RAPID_BLINK			= "\u001B[6m";
    public static final String	REVERSE_VIDEO		= "\u001B[7m";
    public static final String	INVISIBLE_TEXT		= "\u001B[8m";

    public static final String	BLACK				= "\u001B[30m";
    public static final String	RED					= "\u001B[31m";
    public static final String	GREEN				= "\u001B[32m";
    public static final String	YELLOW				= "\u001B[33m";
    public static final String	BLUE				= "\u001B[34m";
    public static final String	MAGENTA				= "\u001B[35m";
    public static final String	CYAN				= "\u001B[36m";
    public static final String	WHITE				= "\u001B[37m";

    public static final String	BACKGROUND_BLACK	= "\u001B[40m";
    public static final String	BACKGROUND_RED		= "\u001B[41m";
    public static final String	BACKGROUND_GREEN	= "\u001B[42m";
    public static final String	BACKGROUND_YELLOW	= "\u001B[43m";
    public static final String	BACKGROUND_BLUE		= "\u001B[44m";
    public static final String	BACKGROUND_MAGENTA	= "\u001B[45m";
    public static final String	BACKGROUND_CYAN		= "\u001B[46m";
    public static final String	BACKGROUND_WHITE	= "\u001B[47m";

    public static void main(String[] args) {
        
        System.out.println(HIGH_INTENSITY+BLACK+"DkGrey;"+LOW_INTENSITY+WHITE+"LightGrey;"+HIGH_INTENSITY+WHITE+"White;");

        System.out.println(HIGH_INTENSITY+BLACK+BACKGROUND_GREEN+"DkGrey;"+LOW_INTENSITY+WHITE+BACKGROUND_GREEN+"LightGrey;"+HIGH_INTENSITY+WHITE+BACKGROUND_GREEN+"White;"+RESET);

        System.out.println(HIGH_INTENSITY+BLUE+BACKGROUND_WHITE+REVERSE_VIDEO+"This is a test"+RESET);
        System.out.println(HIGH_INTENSITY+BLUE+HIGH_INTENSITY+BACKGROUND_WHITE+REVERSE_VIDEO+"This is a test"+RESET);
        System.out.println(BLUE+BACKGROUND_WHITE+REVERSE_VIDEO+"This is a test"+RESET);
        System.out.println(BLUE+HIGH_INTENSITY+BACKGROUND_WHITE+REVERSE_VIDEO+"This is a test"+RESET);

    }

}
