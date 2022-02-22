package dev.lucalewin.planer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ToolbarLayout toolbarLayout = findViewById(R.id.contact_toolbar_layout);
        toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
    }

}