package dev.lucalewin.planer.iserv;

import android.content.Context;
import android.content.SharedPreferences;

import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;
import dev.lucalewin.planer.preferences.Preferences;
import dev.lucalewin.planer.settings.IservAccountSettingsActivity;

/**
 * This class acts as an interface to all Iserv functionalities
 *
 * @author Luca Lewin
 * @since Planer v1.4.2
 * @version 1.0
 */
public class Iserv {

    private static boolean userLoggedIn = false;

    /**
     * All methods in this class are static
     * Therefore this class has not to be instantiated
     */
    private Iserv() { }

    /**
     * @see IservWebScraper#login(Context)
     * @param context the context of the current activity
     * @return true if login attempt was successful, otherwise false
     */
    public static boolean login(Context context) {
        return userLoggedIn = IservWebScraper.login(context);
    }

    /**
     * @see Iserv#login(Context)
     * @return true if the user is logged in otherwise false
     */
    public static boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    /**
     *
     * @param context the context of the current activity
     * @return true if the user has added his iserv credentials
     */
    public static boolean isIservAccountSpecified(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        final String baseURL = preferences.getString("base_url", null);
        final String username = preferences.getString("username", null);
        final String password = preferences.getString("password", null);

        return baseURL != null && username != null && password != null &&
                !baseURL.equals("") && !username.equals("") && !password.equals("");
    }

    /**
     *
     * @param context the context of the current activity
     * @return true if the user has added a class in the settings, otherwise false
     */
    public static boolean isClassSpecified(Context context) {
        final String clazz = Preferences.getSharedPreferences(context)
                .getString("class", null);
        return clazz != null && !clazz.equals("");
    }

    /**
     *
     * @param context the context of the current activity
     * @return the current plan from the iserv substitution plan
     */
    public static IservPlan getCurrentPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, IservWebScraper.TODAY);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param context the context of the current activity
     * @return the next plan from the iserv substitution plan
     */
    public static IservPlan getNextPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, IservWebScraper.TOMORROW);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
