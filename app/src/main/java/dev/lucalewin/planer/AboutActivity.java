package dev.lucalewin.planer;

import android.os.Bundle;

import de.dlyt.yanndroid.oneui.layout.AboutPage;
import dev.lucalewin.planer.base.BaseThemeActivity;
import dev.lucalewin.planer.util.TaskRunner;
import dev.lucalewin.planer.version.UpdateManager;

public class AboutActivity extends BaseThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final AboutPage about_page = findViewById(R.id.about_page);

        about_page.setUpdateState(AboutPage.LOADING);

        final UpdateManager updater = UpdateManager.getInstance(this);

        new TaskRunner().executeAsync(updater::isLatestVersionInstalled, (result) -> {
            if (result) {
                about_page.setUpdateState(AboutPage.NO_UPDATE);
            } else {
                about_page.setUpdateState(AboutPage.UPDATE_AVAILABLE);
                about_page.setUpdateButtonOnClickListener(view -> updater.installLatestVersion());
            }
        });
    }
}