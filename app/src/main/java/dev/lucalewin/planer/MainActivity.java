package dev.lucalewin.planer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.SwipeRefreshLayout;
import dev.lucalewin.planer.settings.IservAccountSettingsActivity;
import dev.lucalewin.planer.settings.MainSettingsActivity;
import dev.lucalewin.planer.util.Device;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.iserv.Iserv;
import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.util.TaskRunner;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.version.UpdateManager;
import dev.lucalewin.planer.views.TipsCardView;

public class MainActivity extends BaseThemeActivity {

    private ToolbarLayout toolbarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MaterialTextView currentDayPlanerLabel;
    private MaterialTextView nextDayPlanerLabel;

    private MaterialCardView currentPlanerContainer;
    private MaterialCardView nextPlanerContainer;

    private TipsCardView tipCardIservAccount;
    private TipsCardView tipCardClass;
    private TipsCardView tipCardError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarLayout = findViewById(R.id.main_toolbar_layout);

        toolbarLayout.inflateToolbarMenu(R.menu.main);
        toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.settings_menu_item) {
                startActivity(new Intent().setClass(this, MainSettingsActivity.class));
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
        swipeRefreshLayout.seslSetRefreshOnce(false);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        currentDayPlanerLabel = findViewById(R.id.label_planer_current_day);
        nextDayPlanerLabel = findViewById(R.id.label_planer_next_day);

        currentPlanerContainer = findViewById(R.id.planer_current_container);
        nextPlanerContainer = findViewById(R.id.planer_next_container);

        tipCardIservAccount = findViewById(R.id.add_iserv_account_tip_card);
        tipCardIservAccount.setOnClickListener(view -> startActivity(new Intent().setClass(this, IservAccountSettingsActivity.class)));

        tipCardClass = findViewById(R.id.tip_card_set_class);
        tipCardClass.setOnClickListener(view -> startActivity(new Intent().setClass(this, MainSettingsActivity.class)));

        tipCardError = findViewById(R.id.tip_card_error);


        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    private void onRefresh() {
        // refresh async
        new Thread(() -> {
            // check if
            // - connection is available
            // - iserv account details a given
            // - class is specified
            // - user is logged in
            if (!areSettingsValid()) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }

            // load plans
            final IservPlan p0 = Iserv.getCurrentPlan(this);
            final IservPlan p1 = Iserv.getNextPlan(this);

            // update ui
            updateUI(p0, p1);

            // stop refreshing
            swipeRefreshLayout.setRefreshing(false);
        }).start();
    }

    private boolean areSettingsValid() {
        // check if device has an internet connection
        if (!Device.hasInternetAccess(this)) {
            Toast.makeText(this, getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
            return false;
        }

        // check if iserv account data is available
        if (!Iserv.isIservAccountSpecified(this)) {
            // show tip card
            // make user set their iserv account
            tipCardIservAccount.setVisibility(View.VISIBLE);
            return false;
        }

        // check if class is specified
        if (!Iserv.isClassSpecified(this)) {
            // show tip card
            // make user set their class (+ courses)
            tipCardClass.setVisibility(View.VISIBLE);
            return false;
        }

        // check if user is already logged in
        if (!Iserv.isUserLoggedIn()) {
            boolean loggedIn = Iserv.login(this);

            if (!loggedIn) {
                // login failed --> display error + stop loading
                tipCardError.setVisibility(View.VISIBLE);
                tipCardError.setTitle("Error");
                tipCardError.setMessage("Could not login to iserv\nCheck your iserv account again");
                return false;
            }
        }

        return true;
    }

    private void updateUI(IservPlan p0, IservPlan p1) {
        new Handler(Looper.getMainLooper()).post(() -> {
            final String clazz = Preferences.getSharedPreferences(MainActivity.this).getString("class", null);

            if (p0 != null) {
                if (LocalDateTime.now().getDayOfWeek() != p0.getDay()) {
                    currentDayPlanerLabel.setText(LocalDateTime.now().getDayOfWeek().plus(1) == p0.getDay()
                            ? getResources().getString(R.string.tomorrow)
                            : p0.getDay().getDisplayName(TextStyle.FULL, Locale.getDefault()));
                }

                TableLayout today = p0.initTable(this, clazz);
                currentPlanerContainer.removeAllViews();
                currentPlanerContainer.addView(today);
            }

            if (p1 != null) {
                if (LocalDateTime.now().getDayOfWeek() != p1.getDay()) {
                    nextDayPlanerLabel.setText(LocalDateTime.now().getDayOfWeek().plus(1) == p1.getDay()
                            ? getResources().getString(R.string.tomorrow)
                            : p1.getDay().getDisplayName(TextStyle.FULL, Locale.getDefault()));
                }

                TableLayout tomorrow = p1.initTable(this, clazz);
                nextPlanerContainer.removeAllViews();
                nextPlanerContainer.addView(tomorrow);
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            toolbarLayout.setSubtitle(String.format(getString(R.string.iserv_synced_at), LocalDateTime.now().format(dateTimeFormatter)));
        });
    }
}