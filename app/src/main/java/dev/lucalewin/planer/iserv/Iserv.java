package dev.lucalewin.planer.iserv;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.settings.IservAccountSettingsActivity;

public class Iserv {

    private static boolean userLoggedIn = false;

    private Iserv() { }

    public static boolean login(Context context) {
        try {
            return userLoggedIn = IservWebScraper.login(context);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return userLoggedIn = false;
    }

    public static boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public static boolean isIservAccountSpecified(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        String baseURL = preferences.getString("base_url", null);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);

        return baseURL != null && username != null && password != null &&
                !baseURL.equals("") && !username.equals("") && !password.equals("");
    }

    public static boolean isClassSpecified(Context context) {
        final String clazz = Preferences.getSharedPreferences(context)
                .getString("class", null);
        return clazz != null && !clazz.equals("");
    }

    public static IservPlan getCurrentPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, 0);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IservPlan getNextPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, 1);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // setter
}
