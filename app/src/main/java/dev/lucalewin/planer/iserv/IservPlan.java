package dev.lucalewin.planer.iserv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

import dev.lucalewin.planer.R;

public class IservPlan {

    public static final int TODAY = 0;
    public static final int TOMORROW = 1;

    private final DayOfWeek day;
    private final Map<String, List<IservPlanRow>> classes;

    public IservPlan(DayOfWeek day, Map<String, List<IservPlanRow>> classes) {
        this.day = day;
        this.classes = classes;
    }

    /**
     *
     * @param context the context of the current activity
     * @param clazz the class the user is in
     * @return a list of table rows
     */
    public TableLayout initTable(Context context, String clazz) {
        @SuppressLint("InflateParams")
        TableLayout tableLayout = (TableLayout) LayoutInflater.from(context).inflate(R.layout.planer_table_layout, null, false);

        final List<IservPlanRow> rows = classes.get(clazz.toUpperCase());

        if (rows != null) {
            rows.forEach(row -> tableLayout.addView(row.toTableRow(context)));
        } else {
            tableLayout.findViewById(R.id.planer_table_header).setVisibility(View.GONE);

            TextView notAffectedTextView = new TextView(context);
            notAffectedTextView.setText(context.getString(R.string.not_affected));
            tableLayout.addView(notAffectedTextView);
        }

        return tableLayout;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public Map<String, List<IservPlanRow>> getClasses() {
        return classes;
    }
}
