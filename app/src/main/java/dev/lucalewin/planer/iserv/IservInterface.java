package dev.lucalewin.planer.iserv;

import android.content.Context;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import dev.lucalewin.planer.iserv.web_scraping.IservWebScraper;

public class IservInterface {

    // static fields

    private static final IservInterface instance = new IservInterface();

    // private fields

    private boolean userLoggedIn = false;

    // static methods

    public static IservInterface getInstance() {
        return instance;
    }

    // constructor

    private IservInterface() { }

    // public methods

    public boolean login(Context context) {
        try {
            return userLoggedIn = IservWebScraper.login(context);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return userLoggedIn = false;
    }

    // getter

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public IservPlan getCurrentPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, 0);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IservPlan getNextPlan(Context context) {
        try {
            return IservWebScraper.getAffectedData(context, 1);
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // setter
}
