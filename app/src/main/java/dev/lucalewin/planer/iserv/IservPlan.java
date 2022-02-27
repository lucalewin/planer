package dev.lucalewin.planer.iserv;

import android.widget.TableRow;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class IservPlan {

    public static final int TODAY = 0;
    public static final int TOMORROW = 1;

    private final DayOfWeek day;
    private final Map<String, List<IservPlanRow>> classes;

    public IservPlan(DayOfWeek day, Map<String, List<IservPlanRow>> classes) {
        this.day = day;
        this.classes = classes;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public Map<String, List<IservPlanRow>> getClasses() {
        return classes;
    }
}
