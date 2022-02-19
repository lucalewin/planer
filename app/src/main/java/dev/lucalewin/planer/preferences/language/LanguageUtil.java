package dev.lucalewin.planer.preferences.language;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

import dev.lucalewin.planer.preferences.Preferences;

public class LanguageUtil {

    public static void init(Context context) {
        String locale = Preferences.getSharedPreferences(context).getString("language", Locale.getDefault().getLanguage());
        setLanguage(context, locale);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void setLanguage(Context context, String language) {
        assert language != null;

        Locale locale = new Locale(language);

        if (language.equals("") || language.equals("System")) {
            // Todo
        } else {
            Locale.setDefault(locale);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale);
        }

        updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

}
