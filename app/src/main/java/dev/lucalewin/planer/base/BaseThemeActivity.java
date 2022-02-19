package dev.lucalewin.planer.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.lucalewin.planer.BuildConfig;

public class BaseThemeActivity extends AppCompatActivity {
    private static String SP_NAME = BuildConfig.APPLICATION_ID + "_preferences";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }
}
