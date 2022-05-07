package dev.lucalewin.planer.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dlyt.yanndroid.oneui.utils.ThemeUtil;
import dev.lucalewin.planer.BuildConfig;

public class BaseThemeActivity extends AppCompatActivity {
    private static String SP_NAME = BuildConfig.APPLICATION_ID + "_preferences";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        setTheme(de.dlyt.yanndroid.oneui.R.style.OneUI4Theme);
        new ThemeUtil(this);

        super.onCreate(savedInstanceState);
    }
}
