package dev.lucalewin.planer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.SwipeRefreshLayout;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;
import dev.lucalewin.planer.util.TaskRunner;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.util.Tuple;
import dev.lucalewin.planer.version.UpdateManager;
import dev.lucalewin.planer.views.TipsCardView;

public class MainActivity extends BaseThemeActivity {

    private static final TaskRunner runner = new TaskRunner();

    private ToolbarLayout toolbarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MaterialTextView currentDayPlanerLabel;
    private MaterialTextView nextDayPlanerLabel;

    private MaterialCardView currentPlanerContainer;
    private MaterialCardView nextPlanerContainer;

    private TipsCardView tipCardIservAccount;
    private TipsCardView tipCardClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarLayout = findViewById(R.id.main_toolbar_layout);

        toolbarLayout.inflateToolbarMenu(R.menu.main);
        toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.settings_menu_item) {
                startActivity(new Intent().setClass(this, SettingsActivity.class));
            } else {
                return false;
            }
            return true;
        });

        UpdateManager updater = UpdateManager.getInstance(this);

        new TaskRunner().executeAsync(updater::isLatestVersionInstalled, result -> {
            if (!result) {
                toolbarLayout.getToolbarMenu().findItem(R.id.settings_menu_item).setBadge(ToolbarLayout.N_BADGE);
            }
            Preferences.getSharedPreferences(this).edit().putBoolean("latestVersionInstalled", result).apply();
        });

        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
        swipeRefreshLayout.seslSetRefreshOnce(true);
        swipeRefreshLayout.setOnRefreshListener(this::loadIservData);

        currentDayPlanerLabel = findViewById(R.id.label_planer_current_day);
        nextDayPlanerLabel = findViewById(R.id.label_planer_next_day);

        currentPlanerContainer = findViewById(R.id.planer_current_container);
        nextPlanerContainer = findViewById(R.id.planer_next_container);

        tipCardIservAccount = findViewById(R.id.add_iserv_account_tip_card);
        tipCardIservAccount.setOnClickListener(view -> startActivity(new Intent().setClass(this, IservAccountSettingsActivity.class)));

        tipCardClass = findViewById(R.id.tip_card_set_class);
        tipCardClass.setOnClickListener(view -> startActivity(new Intent().setClass(this, SettingsActivity.class)));

        loadIservData();
    }

    private boolean isIservAccountSpecified() {
        SharedPreferences preferences = getSharedPreferences(IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        String baseURL = preferences.getString("base_url", null);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);

        System.out.println(baseURL + username + password);

        return baseURL != null && username != null && password != null && !baseURL.equals("") && !username.equals("") && !password.equals("");
    }

    private boolean isClassSpecified() {
        final String clazz = Preferences.getSharedPreferences(MainActivity.this).getString("class", null);
        return clazz != null && !clazz.equals("");
    }

    private void loadIservData() {

        tipCardIservAccount.setVisibility(View.GONE);
        tipCardClass.setVisibility(View.GONE);

        if (!isIservAccountSpecified()) {
            // show tip card
            // make user set their iserv account
            tipCardIservAccount.setVisibility(View.VISIBLE);
            return;
        }

        if (!isClassSpecified()) {
            // show tip card
            // make user set their class (+ courses)
            tipCardClass.setVisibility(View.VISIBLE);
            return;
        }

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

            final String clazz = Preferences.getSharedPreferences(MainActivity.this).getString("class", null);

            if (clazz == null) {
                // this should never be reached, but safety first :)
                return;
            }

            TableLayout today = currentPlan.initTable(this, clazz);
            currentPlanerContainer.removeAllViews();
            currentPlanerContainer.addView(today);

            TableLayout tomorrow = nextPlan.initTable(this, clazz);
            nextPlanerContainer.removeAllViews();
            nextPlanerContainer.addView(tomorrow);

            swipeRefreshLayout.setRefreshing(false);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            toolbarLayout.setSubtitle(String.format(getString(R.string.iserv_synced_at), LocalDateTime.now().format(dateTimeFormatter)));
        });
    }

}