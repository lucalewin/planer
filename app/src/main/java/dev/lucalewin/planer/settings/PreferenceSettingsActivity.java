package dev.lucalewin.planer.settings;

import android.os.Bundle;

import dev.lucalewin.planer.R;
import dev.lucalewin.planer.settings.base.SettingsBaseActivity;
import dev.lucalewin.planer.settings.fragments.PreferencesSettingsFragment;

public class PreferenceSettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbarLayout.setTitle(getString(R.string.preferences));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PreferencesSettingsFragment())
                .commit();
    }
}