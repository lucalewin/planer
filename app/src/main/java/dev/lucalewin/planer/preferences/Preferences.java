package dev.lucalewin.planer.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import dev.lucalewin.planer.BuildConfig;

public class Preferences {

    public static final String SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID + ".preferences";

    @NonNull
    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

}
