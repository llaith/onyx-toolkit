package org.llaith.onyx.toolkit.util.lang;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public final class DateUtil {

    private DateUtil() {}
    
    public static boolean isBetweenInclusive(LocalDate start, LocalDate end, LocalDate target) {

        return !target.isBefore(start) && !target.isAfter(end);

    }

    public static boolean isBetweenInclusive(Date start, Date end, Date target) {

        return !target.before(start) && !target.after(end);

    }

    public static boolean isToday(Date check) {

        final Date today = new Date();

        final Date startOfDay = toStartOfDay(today);

        final Date endOfDay = toEndOfDay(today);

        return isBetweenInclusive(startOfDay, endOfDay, check);

    }

    public static Date toStartOfDay(final Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);

        return cal.getTime();

    }

    public static Date toEndOfDay(final Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();

    }

}
