package dev.lucalewin.planer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import dev.lucalewin.planer.BuildConfig;
import dev.lucalewin.planer.R;
import dev.lucalewin.planer.settings.base.SettingsBaseActivity;
import dev.lucalewin.planer.validation.DomainValidator;

public class IservAccountSettingsActivity extends SettingsBaseActivity {

    public static final String ISERV_SP_NAME = BuildConfig.APPLICATION_ID + ".iserv_account";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ToolbarLayout toolbarLayout;

//    private EditText editTextIservBaseUrl;
    private TextInputEditText editTextIservBaseUrl;
    private TextInputLayout textInputLayoutIservBaseUrl;
    private EditText editTextIservUsername;
    private EditText editTextIservPassword;

    private Button btnCancel;
    private Button btnSave;

    private boolean btnSaveEnabled;
    private boolean btnCancelEnabled;

    private String base_url;
    private String username;
    private String password;

    private final DomainValidator mDomainValidator = new DomainValidator();

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
        textInputLayoutIservBaseUrl = findViewById(R.id.iserv_base_url_text_input_layout);
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
            editor.putString("base_url", base_url = Objects.requireNonNull(editTextIservBaseUrl.getText()).toString());
            editor.putString("username", username = editTextIservUsername.getText().toString());
            editor.putString("password", password = editTextIservPassword.getText().toString());
            editor.apply();
            finish();
        });
    }

    private void onAnyTextChanged() {
        // validate inputs
        if (!mDomainValidator.isValid(Objects.requireNonNull(editTextIservBaseUrl.getText()).toString())) {
            // show error
            textInputLayoutIservBaseUrl.setError(getString(R.string.error_malformed_url));

            disableSaveButton();
            return;
        }

        textInputLayoutIservBaseUrl.setError(null);

        if (base_url.equals(editTextIservBaseUrl.getText().toString())
                && username.equals(editTextIservUsername.getText().toString())
                && password.equals(editTextIservPassword.getText().toString())) {
            // no value was actually changed
            disableSaveButton();
            return;
        }

        if (editTextIservBaseUrl.getText().toString().equals("")
                || editTextIservUsername.getText().toString().equals("")
                || editTextIservPassword.getText().toString().equals("")) {
            // all fields are required
            disableSaveButton();
            return;
        }

        enableSaveButton();
    }

    private void enableSaveButton() {
        if (btnSave.isEnabled())
            return;

        btnSave.setEnabled(true);
        btnSave.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color));
    }

    private void disableSaveButton() {
        if (!btnSave.isEnabled())
            return;

        btnSave.setEnabled(false);
        btnSave.setTextColor(this.getColor(de.dlyt.yanndroid.oneui.R.color.sesl_edit_text_color_disabled));
    }
}