package dev.lucalewin.planer.settings.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.util.SeslMisc;

import de.dlyt.yanndroid.oneui.preference.HorizontalRadioPreference;
import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.SwitchPreference;
import de.dlyt.yanndroid.oneui.utils.ThemeUtil;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.settings.PreferenceSettingsActivity;
import dev.lucalewin.planer.settings.base.SettingsBaseFragment;

public class PreferencesSettingsFragment extends SettingsBaseFragment implements Preference.OnPreferenceChangeListener {

    private PreferenceSettingsActivity mActivity;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getActivity() instanceof PreferenceSettingsActivity)
            mActivity = ((PreferenceSettingsActivity) getActivity());
        mContext = getContext();
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        setPreferencesFromResource(R.xml.preferences, s);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        String currentDarkMode = String.valueOf(ThemeUtil.getDarkMode(mContext));
        HorizontalRadioPreference darkModePref = (HorizontalRadioPreference) findPreference("dark_mode");

        switch (preference.getKey()) {
            case "dark_mode":
                if (currentDarkMode != newValue) {
                    ThemeUtil.setDarkMode(mActivity, ((String) newValue).equals("0") ? ThemeUtil.DARK_MODE_DISABLED : ThemeUtil.DARK_MODE_ENABLED);
                }
                return true;
            case "dark_mode_auto":
                if ((boolean) newValue) {
                    darkModePref.setEnabled(false);
                    ThemeUtil.setDarkMode(mActivity, ThemeUtil.DARK_MODE_AUTO);
                } else {
                    darkModePref.setEnabled(true);
                }
                return true;
        }

        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int darkMode = ThemeUtil.getDarkMode(mContext);

        HorizontalRadioPreference darkModePref = (HorizontalRadioPreference) findPreference("dark_mode");
        assert darkModePref != null;
        darkModePref.setOnPreferenceChangeListener(this);
        darkModePref.setDividerEnabled(false);
        darkModePref.setTouchEffectEnabled(false);
        darkModePref.setEnabled(darkMode != ThemeUtil.DARK_MODE_AUTO);
        darkModePref.setValue(SeslMisc.isLightTheme(mContext) ? "0" : "1");

        SwitchPreference autoDarkModePref = (SwitchPreference) findPreference("dark_mode_auto");
        assert autoDarkModePref != null;
        autoDarkModePref.setOnPreferenceChangeListener(this);
        autoDarkModePref.setChecked(darkMode == ThemeUtil.DARK_MODE_AUTO);
    }
}
