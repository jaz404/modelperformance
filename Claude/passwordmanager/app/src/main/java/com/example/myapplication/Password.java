package com.example.myapplication;


public class Password {
    private int id;
    private String accountName;
    private String username;
    private String password;

    public Password(String accountName, String username, String password) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
    }

    public Password(int id, String accountName, String username, String password) {
        this.id = id;
        this.accountName = accountName;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}