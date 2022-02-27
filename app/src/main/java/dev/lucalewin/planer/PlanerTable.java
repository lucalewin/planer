package dev.lucalewin.planer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.iserv.IservPlanRow;

public class PlanerTable {

    /**
     *
     * @param context the context of the current activity
     * @param clazz the class the user is in
     * @param plan the plan
     * @return a list of table rows
     */
    public static TableLayout initTable(Context context, String clazz, IservPlan plan) {
        @SuppressLint("InflateParams")
        TableLayout tableLayout = (TableLayout) LayoutInflater.from(context).inflate(R.layout.planer_table_layout, null, false);

        final Map<String, List<IservPlanRow>> affectedData = plan.getClasses();

        final List<IservPlanRow> rows = affectedData.get(clazz);

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
}
