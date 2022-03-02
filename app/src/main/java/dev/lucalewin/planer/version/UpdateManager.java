package dev.lucalewin.planer.version;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;

import dev.lucalewin.planer.BuildConfig;

public class UpdateManager {

    private final Context context;
    private final String user;
    private final String repo;

    private GithubRelease[] releases;

    public static UpdateManager getInstance(Context context) {
        return new UpdateManager(context, "lucalewin", "planer");
    }

    public UpdateManager(Context context, String user, String repo) {
        this.context = context;
        this.user = user;
        this.repo = repo;
    }

    public GithubRelease[] getGithubReleases() {
        AtomicReference<String> res = new AtomicReference<>("");
        StringRequest req = new StringRequest("https://api.github.com/repos/" + user + "/" + repo + "/releases", res::set, error -> res.set("error"));
        Volley.newRequestQueue(context).add(req);
        //noinspection StatementWithEmptyBody
        while (res.get().equals(""));
        return new Gson().fromJson(res.get(), GithubRelease[].class);
    }

    public boolean isLatestVersionInstalled() {
        releases = getGithubReleases();
        for (GithubRelease release : releases) {
            if (release.getVersion().isGreaterThan(BuildConfig.VERSION_NAME)) {
                return false;
            }
        }
        return true;
    }

    public void installLatestVersion() {
        if (releases != null && releases.length > 0) {
            GithubRelease newest = releases[0];
            for (int i = 1; i < releases.length; i++) {
                if (newest.getVersion().isLowerThan(releases[i].getVersion())) {
                    newest = releases[i];
                }
            }

            // TODO: download apk automatically and start the install intent
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newest.html_url)));
        }
    }
}
