package dev.lucalewin.planer.util;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class DateUtil {

    /**
     *
     * @param day the string of the weekday
     * @param locale the locale of the weekday string
     * @return the corresponding DayOfWeek enum field
     */
    public static DayOfWeek parseDayOfWeek(String day, Locale locale) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("EEEE"))
                .toFormatter(locale);

        TemporalAccessor accessor = formatter.parse(day);
        return DayOfWeek.from(accessor);
    }

}
