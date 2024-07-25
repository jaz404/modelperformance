package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPasswordActivity extends AppCompatActivity {
    private EditText accountNameEditText, usernameEditText, passwordEditText;
    private PasswordStorage passwordStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        accountNameEditText = findViewById(R.id.account_name);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        passwordStorage = new PasswordStorage(this);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            String accountName = accountNameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            Password newPassword = new Password(accountName, username, password);
            passwordStorage.savePassword(newPassword);
            finish(); // Close the activity
        });
    }
}
