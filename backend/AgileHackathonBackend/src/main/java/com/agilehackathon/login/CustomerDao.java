package com.agilehackathon.login;

import java.util.HashMap;
import java.util.Map;

public class CustomerDao {

    Map<String , Customer> registeredUsers = new HashMap<>();

    public CustomerDao() {
        registeredUsers.put("aram", new Customer("aram", "0207"));
        registeredUsers.put("mattia", new Customer("mattia", "0208"));
    }

    public boolean isCustomerRegistered(String validUserName) {
        return registeredUsers.containsKey(validUserName);
    }
}
