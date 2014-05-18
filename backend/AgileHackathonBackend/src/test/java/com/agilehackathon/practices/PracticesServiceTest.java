package com.agilehackathon.practices;

import com.agilehackathon.login.CustomerDao;
import com.agilehackathon.model.Customer;
import com.agilehackathon.model.Practice;
import com.agilehackathon.twillio.Twilio;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.agilehackathon.practices.PracticeCustomerQueueDao.CustomerAlreadyJoinedException;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PracticesServiceTest {
    public static final String VALID_USER_NAME = "ValidUserName";
    public static final String INVALID_USER_NAME = "InValidUserName";
    private static final Integer PRACTICE_ID = 123;
    public static final int POSITION = 2;
    CustomerDao customerDao = mock(CustomerDao.class);
    PracticesDao practicesDao = mock(PracticesDao.class);
    PracticeCustomerQueueDao practiceCustomerQueueDao = mock(PracticeCustomerQueueDao.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
    Twilio twilio = mock(Twilio.class);
    PracticesService practicesService = new PracticesService(customerDao, practicesDao, practiceCustomerQueueDao, twilio);

    @Test
    public void respondsWith403WhenUsernameIsInvalid() throws Exception {

        when(customerDao.isCustomerRegistered(INVALID_USER_NAME)).thenReturn(false);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        practicesService.getAll(INVALID_USER_NAME, response);

        verify(servletOutputStream).close();
        verify(response).setStatus(403);
    }

    @Test
    public void returnsAllPractices() throws Exception {
        givenARegisteredUser();
        givenSomePractices("practice1", "practice2");

        List<Practice> practices = practicesService.getAll(VALID_USER_NAME, null);

        assertThat(practices).hasSize(2);
        assertThat(practices).onProperty("name").containsOnly("practice1", "practice2");
    }

    @Test
    public void respondsWith403WhenCallingJoinPracticeQueueWIthInvalidUsername() throws Exception {

        when(customerDao.isCustomerRegistered(INVALID_USER_NAME)).thenReturn(false);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        practicesService.joinPracticeQueue(INVALID_USER_NAME, PRACTICE_ID, response);

        verify(servletOutputStream).close();
        verify(response).setStatus(403);
    }


    @Test
    public void returnsThePracticesWithPosition() throws Exception {
        givenARegisteredUser();
        Practice practice1 = givenAPractice("practice1");

        when(practiceCustomerQueueDao.joinQueue(practice1, VALID_USER_NAME)).thenReturn(POSITION);

        PracticesService.JoinResponse joinResponse = practicesService.joinPracticeQueue(VALID_USER_NAME, PRACTICE_ID, null);

        assertThat(joinResponse.getPosition()).isEqualTo(POSITION);
        assertThat(joinResponse.getPractice().getId()).isEqualTo(practice1.getId());
        assertThat(joinResponse.getPractice().getAddress()).isEqualTo(practice1.getAddress());
        assertThat(joinResponse.getPractice().getLocation()).isEqualTo(practice1.getLocation());
        assertThat(joinResponse.getPractice().getName()).isEqualTo(practice1.getName());

    }

    @Test
    public void returns500WhenUserHasAlreadyJoinedTheQueue() throws Exception {
        givenARegisteredUser();
        Practice practice1 = givenAPractice("practice1");

        when(practiceCustomerQueueDao.joinQueue(practice1, VALID_USER_NAME)).thenThrow(new CustomerAlreadyJoinedException());
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        PracticesService.JoinResponse joinResponse = practicesService.joinPracticeQueue(VALID_USER_NAME, PRACTICE_ID, response);

        verify(servletOutputStream).close();
        verify(response).setStatus(500);
    }

    private void givenSomePractices(String... practicesNames) {
        List<Practice> practices = new ArrayList<>();
        for (String practiceName : practicesNames) {
            practices.add(new Practice(practiceName, 1, "a", "b"));
        }
        when(practicesDao.findAllPractices()).thenReturn(practices);
    }
    private Practice givenAPractice(String practiceName) {

        Practice practice = new Practice(practiceName, PRACTICE_ID, "a", "b");
        when(practicesDao.findPracticeById(PRACTICE_ID)).thenReturn(practice);
        return practice;
    }

    private void givenARegisteredUser() {
        when(customerDao.isCustomerRegistered(VALID_USER_NAME)).thenReturn(true);
        when(customerDao.findCustomerByUsername(VALID_USER_NAME)).thenReturn(new Customer(VALID_USER_NAME, "1234"));
    }
}
