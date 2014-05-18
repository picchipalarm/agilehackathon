package com.agilehackathon.model;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PracticeCustomerQueueTest {

    PracticeCustomerQueue practiceCustomerQueue = new PracticeCustomerQueue();
    private Practice practice = mock(Practice.class);



    @Test
    public void initiallyNoOneIsBeingServed() throws Exception {
        assertThat(practiceCustomerQueue.getServing()).isEqualTo(0);
    }

    @Test
    public void theFirstCustomerToJoinIsNotBeingServed() throws Exception {
        practiceCustomerQueue.addCustomerToQueue(practice, "someuser", 1);

        assertThat(practiceCustomerQueue.getServing()).isEqualTo(0);
    }

    @Test
    public void theNextAvailablePositionGoesUpWhenCustomersJoinTheQueue() throws Exception {
        assertThat(practiceCustomerQueue.nextAvailablePosition()).isEqualTo(1);

        practiceCustomerQueue.addCustomerToQueue(practice, "someuser", 1);

        assertThat(practiceCustomerQueue.nextAvailablePosition()).isEqualTo(2);

        practiceCustomerQueue.addCustomerToQueue(practice, "someuser", 2);

        assertThat(practiceCustomerQueue.nextAvailablePosition()).isEqualTo(3);
    }

    @Test
    public void firstCustomerPositionIs1() throws Exception {
        practiceCustomerQueue.addCustomerToQueue(practice, "someuser", 1);

        assertThat(practiceCustomerQueue.getCustomerPositionInQueue("someuser")).isEqualTo(1);
    }

    @Test
    public void returnsCustomerPosition() throws Exception {
        practiceCustomerQueue.addCustomerToQueue(practice, "someuser1", 1);
        practiceCustomerQueue.addCustomerToQueue(practice, "someuser2", 2);
        practiceCustomerQueue.addCustomerToQueue(practice, "someuser3", 3);

        assertThat(practiceCustomerQueue.getCustomerPositionInQueue("someuser3")).isEqualTo(3);
    }

    @Test
    public void initiallyServesFirstCustomer() throws Exception {
        int servingCustomer = practiceCustomerQueue.serveCustomer();

        assertThat(servingCustomer).isEqualTo(1);
    }

    @Test
    public void servesNextCustomer() throws Exception {
        practiceCustomerQueue.serveCustomer();
        practiceCustomerQueue.serveCustomer();
        int servingCustomer = practiceCustomerQueue.serveCustomer();

        assertThat(servingCustomer).isEqualTo(3);
    }


}
