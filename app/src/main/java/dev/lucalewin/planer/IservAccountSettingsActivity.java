package dev.lucalewin.planer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import dev.lucalewin.planer.BuildConfig;
import dev.lucalewin.planer.R;

public class IservAccountSettingsActivity extends AppCompatActivity {

    public static final String ISERV_SP_NAME = BuildConfig.APPLICATION_ID + ".iserv_account";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ToolbarLayout toolbarLayout;

    private EditText editTextIservBaseUrl;
    private EditText editTextIservUsername;
    private EditText editTextIservPassword;

    private Button btnCancel;
    private Button btnSave;

    private boolean btnSaveEnabled;
    private boolean btnCancelEnabled;

    private String base_url;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iserv_account_settings);

        // load shared preferences
        sharedPreferences = getSharedPreferences(ISERV_SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // set navigation actions
        toolbarLayout = findViewById(R.id.iserv_account_settings_toolbar_layout);
        toolbarLayout.setNavigationButtonOnClickListener(view -> finish());

        // get edit texts
        editTextIservBaseUrl = findViewById(R.id.iserv_base_url_text_input);
        editTextIservUsername = findViewById(R.id.iserv_username_text_input);
        editTextIservPassword = findViewById(R.id.iserv_password_text_input);

        // load saved preferences
        editTextIservBaseUrl.setText(base_url = sharedPreferences.getString("base_url", ""));
        editTextIservUsername.setText(username = sharedPreferences.getString("username", ""));
        editTextIservPassword.setText(password = sharedPreferences.getString("password", ""));

        // set onEditListener
        editTextIservBaseUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                onAnyTextChanged();
            }
        });
        editTextIservUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                onAnyTextChanged();
            }
        });
        editTextIservPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                onAnyTextChanged();
            }
        });

        // get buttons
        btnCancel = findViewById(R.id.iserv_acc_edit_cancel);
        btnSave = findViewById(R.id.iserv_acc_edit_save);

        btnSaveEnabled = btnSave.isEnabled();
        btnCancelEnabled = btnCancel.isEnabled();

        // set onClickListener
        btnCancel.setOnClickListener(view -> finish());
        btnSave.setOnClickListener(view -> {
            editor.putString("base_url", base_url = editTextIservBaseUrl.getText().toString());
            editor.putString("username", username = editTextIservUsername.getText().toString());
            editor.putString("password", password = editTextIservPassword.getText().toString());
            editor.apply();
            onBackPressed();
        });
    }

    private void onAnyTextChanged() {
        if (base_url.equals(editTextIservBaseUrl.getText().toString())
                && username.equals(editTextIservUsername.getText().toString())
                && password.equals(editTextIservPassword.getText().toString())) {
            // no value was actually changed
            disableSaveButton();
            enableCancelButton();
            return;
        }

        if (editTextIservBaseUrl.getText().toString().equals("")
                || editTextIservUsername.getText().toString().equals("")
                || editTextIservPassword.getText().toString().equals("")) {
            // all fields are required
            disableSaveButton();
            enableCancelButton();
            return;
        }

        enableSaveButton();
    }

    private void enableSaveButton() {
        if (btnSaveEnabled)
            return;

        btnSave.setEnabled(true);
        btnSave.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color));
    }

    private void disableSaveButton() {
        if (!btnSaveEnabled)
            return;

        btnSave.setEnabled(false);
        btnSave.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color_disabled));
    }

    private void enableCancelButton() {
        if (btnCancelEnabled)
            return;

        btnCancel.setEnabled(true);
        btnCancel.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color));
    }

    private void disableCancelButton() {
        if (!btnCancelEnabled)
            return;

        btnCancel.setEnabled(false);
        btnCancel.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color_disabled));
    }
}