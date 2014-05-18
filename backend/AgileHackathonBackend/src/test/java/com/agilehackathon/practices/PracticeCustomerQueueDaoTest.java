package com.agilehackathon.practices;

import com.agilehackathon.model.Practice;
import com.agilehackathon.model.PracticeCustomerQueue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.agilehackathon.practices.PracticeCustomerQueueDao.CustomerAlreadyJoinedException;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PracticeCustomerQueueDaoTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    public static final String VALID_USERNAME = "username";
    PracticeCustomerQueueDao practiceCustomerQueueDao = new PracticeCustomerQueueDao();
    Practice practice = mock(Practice.class);

    @Test
    public void theFirstCustomerJoinsTheQueueInPosition1() throws Exception {
        givenNoCustomersAreInTheQueue();

        Integer queuePosition = practiceCustomerQueueDao.joinQueue(practice, VALID_USERNAME);

        assertThat(queuePosition).isEqualTo(1);
    }

    @Test
    public void customersJoinTheQueueInIncrementalPositions() throws Exception {
        givenCustomersInTheQueue(5);

        Integer queuePosition = practiceCustomerQueueDao.joinQueue(practice, VALID_USERNAME);

        assertThat(queuePosition).isEqualTo(6);
    }

    @Test
    public void customerCantJoinTheSameQueueAgain() throws Exception {
        givenNoCustomersAreInTheQueue();

        practiceCustomerQueueDao.joinQueue(practice, VALID_USERNAME);
        exception.expect(CustomerAlreadyJoinedException.class);
        practiceCustomerQueueDao.joinQueue(practice, VALID_USERNAME);


    }

    private void givenCustomersInTheQueue(int numberOfCustomersInQueue) {
        PracticeCustomerQueue practiceCustomerQueue = new PracticeCustomerQueue();
        practiceCustomerQueueDao.queueMap.put(practice.getId(), practiceCustomerQueue);
        for (int i = 1; i <= numberOfCustomersInQueue; i++) {
            practiceCustomerQueue.addCustomerToQueue(practice, "user" + i, i);
        }

    }

    private void givenNoCustomersAreInTheQueue() {
        practiceCustomerQueueDao.queueMap.remove(practice.getId());
    }

}
