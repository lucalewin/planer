package dev.lucalewin.planer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ToolbarLayout toolbarLayout = findViewById(R.id.privacy_policy_toolbar_layout);
        toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
    }
}