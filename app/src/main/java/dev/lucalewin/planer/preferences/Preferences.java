package dev.lucalewin.planer.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import dev.lucalewin.planer.BuildConfig;

public class Preferences {

    public static final String SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID + ".preferences";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

}
