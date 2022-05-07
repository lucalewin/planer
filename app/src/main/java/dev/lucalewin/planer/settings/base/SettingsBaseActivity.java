package dev.lucalewin.planer.settings.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.base.BaseThemeActivity;

public class SettingsBaseActivity extends BaseThemeActivity {

    protected ToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        mToolbarLayout = findViewById(R.id.settings_toolbar_layout);
        mToolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
    }
}
