package dev.lucalewin.planer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.SwipeRefreshLayout;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.iserv.IservPlanRow;
import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;
import dev.lucalewin.planer.iserv.web_scraping.TaskRunner;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.util.Tuple;

public class MainActivity extends BaseThemeActivity {

    private static final TaskRunner runner = new TaskRunner();

    private Context mContext;

    private ToolbarLayout toolbarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MaterialTextView currentDayPlanerLabel;
    private MaterialTextView nextDayPlanerLabel;

    private TableLayout planerTableToday;
    private TableLayout planerTableTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        LanguageUtil.init(this);

        mContext = this;
        toolbarLayout = findViewById(R.id.main_toolbar_layout);

        toolbarLayout.inflateToolbarMenu(R.menu.main);
        toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.settings_menu_item:
                    startActivity(new Intent().setClass(this, SettingsActivity.class));
                    break;
                default:
                    return false;
            }
            return true;
        });

        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
        swipeRefreshLayout.seslSetRefreshOnce(true);
        swipeRefreshLayout.setOnRefreshListener(this::loadIservData);

        currentDayPlanerLabel = findViewById(R.id.label_planer_current_day);
        nextDayPlanerLabel = findViewById(R.id.label_planer_next_day);

        planerTableToday = findViewById(R.id.planer_table_today);
        planerTableTomorrow = findViewById(R.id.planer_table_tomorrow);

        loadIservData();
    }

    private void loadIservData() {
        runner.executeAsync(() -> {
            swipeRefreshLayout.setRefreshing(true);
            IservWebScraper.login(this);

            // wait for WebScraper to login
            // noinspection StatementWithEmptyBody
            while (!IservWebScraper.isLoggedIn);

            final IservPlan affectedDataToday = IservWebScraper.getAffectedData(this, IservPlan.TODAY);
            final IservPlan affectedDataTomorrow = IservWebScraper.getAffectedData(this, IservPlan.TOMORROW);

            return Tuple.of(affectedDataToday, affectedDataTomorrow);
        }, (TaskRunner.Callback<Tuple<IservPlan, IservPlan>>) (result) -> {
            planerTableToday.removeViews(1, planerTableToday.getChildCount() - 1);
            planerTableTomorrow.removeViews(1, planerTableTomorrow.getChildCount() - 1);

            final IservPlan currentPlan = result.getFirst();
            final IservPlan nextPlan = result.getSecond();

            if (LocalDateTime.now().getDayOfWeek() != currentPlan.getDay()) {
                currentDayPlanerLabel.setText(
                        LocalDateTime.now().getDayOfWeek().plus(1) == currentPlan.getDay()
                                ? getResources().getString(R.string.tomorrow)
                                : currentPlan.getDay().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            }

            if (LocalDateTime.now().getDayOfWeek() != nextPlan.getDay()) {
                nextDayPlanerLabel.setText(
                        LocalDateTime.now().getDayOfWeek().plus(1) == nextPlan.getDay()
                                ? getResources().getString(R.string.tomorrow)
                                : nextPlan.getDay().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            }

            final Map<String, List<IservPlanRow>> affectedDataToday = currentPlan.getClasses();
            final Map<String, List<IservPlanRow>> affectedDataTomorrow = nextPlan.getClasses();

//            if (LocalDateTime.now().getDayOfWeek() == affectedDataToday.)

            final String clazz = Preferences.getSharedPreferences(MainActivity.this).getString("class", null);

            if (clazz == null) {
                // show tip card
                // make user set their class (+ courses)
                return;
            }

            final List<IservPlanRow> current = affectedDataToday.get(clazz);
            final List<IservPlanRow> next = affectedDataTomorrow.get(clazz);

            if (current != null) current.forEach(row -> planerTableToday.addView(row.toTableRow(MainActivity.this)));
            if (next != null) next.forEach(row -> planerTableTomorrow.addView(row.toTableRow(MainActivity.this)));

            swipeRefreshLayout.setRefreshing(false);
        });
    }

}