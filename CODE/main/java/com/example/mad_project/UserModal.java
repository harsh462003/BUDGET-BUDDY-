package com.example.mad_project;

public class UserModal {
    private String Name;
    private String username;
    private String password;

    public String getName() {
        return Name;
    }

    public String getusername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    ;

    public UserModal(String Name, String username, String password) {
        this.Name = Name;
        this.username = username;
        this.password = password;
    }
}