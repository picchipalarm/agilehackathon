package com.agilehackathon.practices;

import com.agilehackathon.model.Practice;

/**
 * Created by AhmedAr on 17/05/14.
 */
public class PracticeCustomerQueueDao {
    public int joinQueue(Practice practice, String username) {
        return 0;
    }

    public static class CustomerAlreadyJoinedException extends RuntimeException {}
}
