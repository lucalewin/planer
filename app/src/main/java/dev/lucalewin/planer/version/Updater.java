package dev.lucalewin.planer.version;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import dev.lucalewin.planer.BuildConfig;

public class Updater {

    private final Context context;
    private final String user;
    private final String repo;

    public Updater(Context context, String user, String repo) {
        this.context = context;
        this.user = user;
        this.repo = repo;
    }

    public GithubRelease[] getGithubReleases() {
        AtomicReference<String> res = new AtomicReference<>("");
        StringRequest req = new StringRequest("", res::set, error -> res.set("error"));

        Volley.newRequestQueue(context);

        while (res.get().equals(""));

        Gson gson = new Gson();

        return gson.fromJson(res.get(), GithubRelease[].class);
    }

    public boolean isLatestVersionInstalled() {
        GithubRelease[] releases = getGithubReleases();

        for (GithubRelease release : releases) {
            if (release.getVersion().isGreaterThan(BuildConfig.VERSION_NAME)) {
                return true;
            }
        }

        return false;
    }

    public void installLatestVersion() {

    }

//    private static String getVersionNamesFromGithubReleases() {
//        return "";
//    }
//
//    public static boolean isLatestVersionInstalled() {
//        return false;
//    }
//
//    public static String getNewestVersionName() {
//        return "";
//    }
//
//    public static void downloadAndInstallNewestVersion() {
//        downloadNewestVersion();
//        installNewestVersion();
//    }
//
//    private static void downloadNewestVersion() {
//        String newestVersionName = getNewestVersionName();
//    }
//
//    private static void installNewestVersion() {
//
//    }

}
