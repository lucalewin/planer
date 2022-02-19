package dev.lucalewin.planer.util;

import java.time.DayOfWeek;

public class DayOfWeekUtil {

    public static DayOfWeek fromGermanDayName(String germanDayName) {
        DayOfWeek day = null;
        switch (germanDayName) {
            case "Montag":
                day = DayOfWeek.MONDAY;
                break;
            case "Dienstag":
                day = DayOfWeek.TUESDAY;
                break;
            case "Mittwoch":
                day = DayOfWeek.WEDNESDAY;
                break;
            case "Donnerstag":
                day = DayOfWeek.THURSDAY;
                break;
            case "Freitag":
                day = DayOfWeek.FRIDAY;
                break;
            case "Samstag":
                day = DayOfWeek.SATURDAY;
                break;
            case "Sonntag":
                day = DayOfWeek.SUNDAY;
                break;
        }
        return day;
    }
}
