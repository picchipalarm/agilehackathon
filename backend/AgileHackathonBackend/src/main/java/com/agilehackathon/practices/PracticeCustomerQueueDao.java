package com.agilehackathon.practices;

import com.agilehackathon.model.Practice;
import com.agilehackathon.model.PracticeCustomerQueue;

import java.util.HashMap;
import java.util.Map;

public class PracticeCustomerQueueDao {

    Map<Integer, PracticeCustomerQueue> queueMap = new HashMap<>();

    public int joinQueue(Practice practice, String username) {

        PracticeCustomerQueue queue = findQueuebyPractice(practice);
        if(queue.hasCustomerAlreadyJoined(username)){
            throw new CustomerAlreadyJoinedException();
        }

        int customerPosition = queue.nextAvailablePosition();
        recordCustomerHasJoinedTheQueue(practice, username, customerPosition); // this would do the insert

        return customerPosition;
    }

    private void recordCustomerHasJoinedTheQueue(Practice practice, String username, int customerPosition) {
        PracticeCustomerQueue practiceCustomerQueue = queueMap.get(practice.getId());
        practiceCustomerQueue.addCustomerToQueue(practice, username, customerPosition);
    }

    public PracticeCustomerQueue findQueuebyPractice(Practice practice) {

        boolean queueExists = queueMap.containsKey(practice.getId());

        if(!queueExists){

            queueMap.put(practice.getId(), new PracticeCustomerQueue() );
        }

        return queueMap.get(practice.getId());
    }

    public void resetAllQueues() {
        queueMap = new HashMap<>();
    }

    public static class CustomerAlreadyJoinedException extends RuntimeException {}
}
