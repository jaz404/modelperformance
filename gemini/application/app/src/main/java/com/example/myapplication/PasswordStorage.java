package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PasswordStorage {
    private static final String SHARED_PREFS_NAME = "password_manager";
    private static final String PASSWORD_LIST_KEY = "password_list";

    private SharedPreferences sharedPreferences;

    public PasswordStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void savePassword(Password password) {
        List<Password> passwords = getPasswords();
        passwords.add(password);
        String json = new Gson().toJson(passwords);
        sharedPreferences.edit().putString(PASSWORD_LIST_KEY, json).apply();
    }

    public List<Password> getPasswords() {
        String json = sharedPreferences.getString(PASSWORD_LIST_KEY, "[]");
        Type type = new TypeToken<ArrayList<Password>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
}
