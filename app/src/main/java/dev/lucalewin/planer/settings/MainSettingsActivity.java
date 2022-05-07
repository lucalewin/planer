package dev.lucalewin.planer.settings;

import android.os.Bundle;

import dev.lucalewin.planer.R;
import dev.lucalewin.planer.settings.base.SettingsBaseActivity;
import dev.lucalewin.planer.settings.fragments.MainSettingsFragment;

public class MainSettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MainSettingsFragment())
                .commit();
    }

}