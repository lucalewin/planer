package dev.lucalewin.planer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import java.util.List;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.SwipeRefreshLayout;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.iserv.IservPlanRow;
import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;
import dev.lucalewin.planer.iserv.web_scraping.TaskRunner;
import dev.lucalewin.planer.util.Tuple;

public class MainActivity extends BaseThemeActivity {

    private static final TaskRunner runner = new TaskRunner();

    private Context mContext;

    private ToolbarLayout toolbarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TableLayout planerTableToday;
    private TableLayout planerTableTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            List<IservPlanRow> affectedDataToday = IservWebScraper.getAffectedData(this, IservPlan.TODAY);
            List<IservPlanRow> affectedDataTomorrow = IservWebScraper.getAffectedData(this, IservPlan.TOMORROW);

            return Tuple.of(affectedDataToday, affectedDataTomorrow);
        }, (TaskRunner.Callback<Tuple<List<IservPlanRow>, List<IservPlanRow>>>) (result) -> {

            // Todo: filter out for the user irrelevant rows

            planerTableToday.removeViews(1, planerTableToday.getChildCount() - 1);
            planerTableTomorrow.removeViews(1, planerTableTomorrow.getChildCount() - 1);

            List<IservPlanRow> affectedDataToday = result.getFirst();
            List<IservPlanRow> affectedDataTomorrow = result.getSecond();

            affectedDataToday.forEach(row -> planerTableToday.addView(row.toTableRow(MainActivity.this)));
            affectedDataTomorrow.forEach(row -> planerTableTomorrow.addView(row.toTableRow(MainActivity.this)));

            swipeRefreshLayout.setRefreshing(false);
        });
    }

}