package dev.lucalewin.planer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

public class PlanerAccount {

    private static final String SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID + ".planer_account";

    private final String clazz;
    private final Set<String> courses;

    public static PlanerAccount getInstance(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new PlanerAccount(sp.getString("class", null), sp.getStringSet("courses", Collections.emptySet()));
    }

    private PlanerAccount(String clazz, Set<String> courses) {
        this.clazz = clazz;
        this.courses = courses;
    }

    public String getClazz() {
        return clazz;
    }

    public Set<String> getCourses() {
        return courses;
    }
}
