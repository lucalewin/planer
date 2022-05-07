package dev.lucalewin.planer.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.settings.base.SettingsBaseActivity;

public class NotificationSettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        ToolbarLayout toolbarLayout = findViewById(R.id.notification_settings_toolbar_layout);
        toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
    }
}