package com.agilehackathon.login;

public class Customer {

    private String username;
    private String phoneNumber;

    public Customer(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

}
