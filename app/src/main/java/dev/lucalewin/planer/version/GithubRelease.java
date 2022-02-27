package dev.lucalewin.planer.version;

import com.vdurmont.semver4j.Semver;

public class GithubRelease {

    public String url;
    public String tag_name;
    public String name;
    public GithubReleaseAsset[] assets;

    public Semver getVersion() {
        return new Semver(tag_name);
    }

}
