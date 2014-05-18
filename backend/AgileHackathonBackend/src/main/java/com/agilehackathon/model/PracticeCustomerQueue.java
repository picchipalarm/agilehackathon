package com.agilehackathon.model;

import java.util.LinkedList;
import java.util.Queue;

public class PracticeCustomerQueue {

    int queueSize;
    Queue<String> customersInQ = new LinkedList<String>();
    Integer serving;

    public PracticeCustomerQueue() {
        this.queueSize = 0;
        this.serving = 0;
    }

    public Integer getServing() {
        return serving;
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

    public int getCustomerPositionInQueue(String username) {
        int i = 1;
        for (String userInQ : customersInQ) {
            if(userInQ.equals(username)){
                return i;
            }
            i++;
        }
        return 0;
    }

    public int serveCustomer() {
        return ++serving;
    }
}
