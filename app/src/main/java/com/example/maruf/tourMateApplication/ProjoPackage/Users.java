package com.example.maruf.tourMateApplication.ProjoPackage;

public class Users {
    public String userEmail;
    public String userPassword;

    public Users(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}

