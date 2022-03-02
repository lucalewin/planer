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

        AboutPage about_page = findViewById(R.id.about_page);

        about_page.setUpdateState(AboutPage.LOADING);

        UpdateManager updater = UpdateManager.getInstance(this);

        new TaskRunner().executeAsync(updater::isLatestVersionInstalled, (result) -> {
            if (result) {
                about_page.setUpdateState(AboutPage.NO_UPDATE);
            } else {
                about_page.setUpdateState(AboutPage.UPDATE_AVAILABLE);
                about_page.setUpdateButtonOnClickListener(view -> updater.installLatestVersion());
            }
        });

        // TODO
//        ((MaterialButton) findViewById(R.id.about_btn1)).setOnClickListener(v -> about_page.setUpdateState(AboutPage.LOADING));
//        ((MaterialButton) findViewById(R.id.about_btn2)).setOnClickListener(v -> about_page.setUpdateState(AboutPage.NO_UPDATE));
//        ((MaterialButton) findViewById(R.id.about_btn3)).setOnClickListener(v -> about_page.setUpdateState(AboutPage.UPDATE_AVAILABLE));
//        ((MaterialButton) findViewById(R.id.about_btn4)).setOnClickListener(v -> about_page.setUpdateState(AboutPage.NOT_UPDATEABLE));
//        ((MaterialButton) findViewById(R.id.about_btn5)).setOnClickListener(v -> about_page.setUpdateState(AboutPage.NO_CONNECTION));
    }
}