package dev.lucalewin.planer.settings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

import de.dlyt.yanndroid.oneui.layout.PreferenceFragment;
import de.dlyt.yanndroid.oneui.preference.ListPreference;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.preferences.language.LanguageUtil;

public class MainSettingsFragment extends PreferenceFragment {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        setPreferencesFromResource(R.xml.preferences, s);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireView().setBackgroundColor(getResources().getColor(de.dlyt.yanndroid.oneui.R.color.item_background_color));

        sharedPreferences = Preferences.getSharedPreferences(requireContext());

        ListPreference language = findPreference("language_selector");
        assert language != null;

        assert sharedPreferences != null;
        String locale = sharedPreferences.getString("language", "English");
        String[] languages = getResources().getStringArray(R.array.languages);

        language.setSummary(locale);
        language.setEntries(languages);
        language.setValueIndex(Arrays.asList(languages).indexOf(locale));
        language.setOnPreferenceChangeListener((preference, lang) -> {
            sharedPreferences.edit().putString("language", (String) lang).apply();
            LanguageUtil.setLanguage(requireContext(), (String) lang);
            language.setSummary((String) lang);
            return true;
        });
    }

}
