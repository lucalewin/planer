package dev.lucalewin.planer.settings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Locale;

import de.dlyt.yanndroid.oneui.layout.PreferenceFragment;
import de.dlyt.yanndroid.oneui.preference.EditTextPreference;
import de.dlyt.yanndroid.oneui.preference.ListPreference;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.preferences.language.LanguageUtil;
import dev.lucalewin.planer.preferences.view.BadgePreferenceScreen;

public class MainSettingsFragment extends PreferenceFragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        setPreferencesFromResource(R.xml.preferences, s);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireView().setBackgroundColor(getResources().getColor(de.dlyt.yanndroid.oneui.R.color.item_background_color));

        sharedPreferences = Preferences.getSharedPreferences(requireContext());
        assert sharedPreferences != null;
        editor = sharedPreferences.edit();

        ListPreference languagePreference = findPreference("language_selector");
        EditTextPreference classPreference = findPreference("class");
        BadgePreferenceScreen appInfoPreference = findPreference("about_app");
//        MultiSelectListPreference coursesPreference = findPreference("courses");

        assert languagePreference != null;
        assert classPreference    != null;
        assert appInfoPreference  != null;

        // init languagePreference
        String locale = sharedPreferences.getString("language", Locale.getDefault().getLanguage());
        String[] locales = getResources().getStringArray(R.array.languages);
        String[] languages = Arrays.stream(locales).map(s -> new Locale(s).getDisplayLanguage()).toArray(String[]::new);

        languagePreference.setSummary(new Locale(locale).getDisplayLanguage());
        languagePreference.setEntries(languages);
        languagePreference.setValueIndex(Arrays.asList(languages).indexOf(new Locale(locale).getDisplayLanguage()));
        languagePreference.setOnPreferenceChangeListener((preference, lang) -> {
            sharedPreferences.edit().putString("language", (String) lang).apply();
            LanguageUtil.setLanguage(requireContext(), (String) lang);
            languagePreference.setSummary(new Locale((String) lang).getDisplayLanguage());
            return true;
        });

        // init classPreference
        String currentClass = sharedPreferences.getString("class", "");
        classPreference.setText(currentClass);
        classPreference.setSummary(currentClass);
        classPreference.setOnPreferenceChangeListener((preference, clazz) -> {
            editor.putString("class", (String) clazz).commit();
            classPreference.setSummary((String) clazz);
            return true;
        });

        if (!sharedPreferences.getBoolean("latestVersionInstalled", true)) {
            appInfoPreference.setBadge(BadgePreferenceScreen.N_BADGE);
        }

//        boolean updateAvailable = UpdateManager.getInstance(requireContext()).isLatestVersionInstalled();

//        CheckBoxPreference checkBoxPreference = findPreference("checkbox");
//        assert checkBoxPreference != null;
//        checkBoxPreference.setWidgetLayoutResource(R.layout.badge_layout);

        // init coursesPreference
        // FIXME
//        Set<String> courses = sharedPreferences.getStringSet("courses", Collections.emptySet());
//        coursesPreference.setEntries(courses.toArray(new String[] {}));
    }
}
