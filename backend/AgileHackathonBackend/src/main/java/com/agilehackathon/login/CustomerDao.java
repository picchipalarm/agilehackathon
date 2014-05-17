package com.agilehackathon.login;

import com.agilehackathon.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerDao {

    Map<String , Customer> registeredUsers = new HashMap<>();

    public CustomerDao() {
        registeredUsers.put("aram", new Customer("aram", "0207"));
        registeredUsers.put("mattia", new Customer("mattia", "0208"));
        registeredUsers.put("hendrik", new Customer("hendrik", "0209"));
    }

    public boolean isCustomerRegistered(String validUserName) {
        return registeredUsers.containsKey(validUserName);
    }
}
