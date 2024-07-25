package com.example.myapplication;

public class Password {
    private String accountName;
    private String username;
    private String password;

    public Password(String accountName, String username, String password) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}