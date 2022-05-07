package dev.lucalewin.planer.settings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.preference.EditTextPreference;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.preferences.view.BadgePreferenceScreen;
import dev.lucalewin.planer.settings.base.SettingsBaseFragment;

public class MainSettingsFragment extends SettingsBaseFragment {

    private SharedPreferences        mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private EditTextPreference    mClassPreference;
    private BadgePreferenceScreen mAboutAppPreference;

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        setPreferencesFromResource(R.xml.settings, s);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedPreferences       = Preferences.getSharedPreferences(requireContext());
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mClassPreference         = findPreference("class");
        mAboutAppPreference      = findPreference("about_app");

        setupClassPreference();
        setupAboutAppPreference();
    }

    private void setupClassPreference() {
        String currentClass = mSharedPreferences.getString("class", "");
        mClassPreference.setText(currentClass);
        mClassPreference.setSummary(currentClass);
        mClassPreference.setOnPreferenceChangeListener((preference, clazz) -> {
            mSharedPreferencesEditor.putString("class", (String) clazz).commit();
            mClassPreference.setSummary((String) clazz);
            return true;
        });
    }

    private void setupAboutAppPreference() {
        if (!mSharedPreferences.getBoolean("latestVersionInstalled", true)) {
            mAboutAppPreference.setBadge(BadgePreferenceScreen.N_BADGE);
        }
    }
}
