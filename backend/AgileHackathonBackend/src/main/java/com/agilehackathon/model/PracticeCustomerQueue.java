package com.agilehackathon.model;

import java.util.HashSet;
import java.util.Set;

public class PracticeCustomerQueue {

    int queueSize;
    Set<String> customersInQ = new HashSet<>();

    public PracticeCustomerQueue() {
        this.queueSize = 0;
    }

    public boolean hasCustomerAlreadyJoined(String username) {
        return customersInQ.contains(username);
    }

    public int nextAvailablePosition() {
        return queueSize + 1;
    }

    public void addCustomerToQueue(Practice practice, String username, int customerPosition) {
        queueSize = customerPosition;
        customersInQ.add(username);
    }
}
