package com.agilehackathon.login;

import com.agilehackathon.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerDao {

    Map<String , Customer> registeredUsers = new HashMap<>();

    public CustomerDao() {
        registeredUsers.put("aram", new Customer("aram", "123"));
        registeredUsers.put("mattia", new Customer("mattia", "456"));
        registeredUsers.put("hendrik", new Customer("hendrik", "789"));
    }

    public boolean isCustomerRegistered(String validUserName) {
        return registeredUsers.containsKey(validUserName);
    }

    public Customer findCustomerByUsername(String username) {
        return registeredUsers.get(username);
    }
}
