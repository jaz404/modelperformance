package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView passwordListView;
    private PasswordStorage passwordStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordListView = findViewById(R.id.password_list);
        passwordStorage = new PasswordStorage(this);

        loadPasswords();
    }

    private void loadPasswords() {
        List<Password> passwords = passwordStorage.getPasswords();
        List<String> accountNames = new ArrayList<>();
        for (Password password : passwords) {
            accountNames.add(password.getAccountName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountNames);
        passwordListView.setAdapter(adapter);
    }

    public void addPassword(View view) {
        Intent intent = new Intent(this, AddPasswordActivity.class);
        startActivity(intent);
    }

    public void exportToExcel(View view) {
        // Implement export logic here
    }
}
