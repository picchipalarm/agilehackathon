package com.agilehackathon.practices;

import com.agilehackathon.login.CustomerDao;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PracticesServiceTest {
    public static final String VALID_USER_NAME = "ValidUserName";
    public static final String INVALID_USER_NAME = "InValidUserName";
    CustomerDao customerDao = mock(CustomerDao.class);
    PracticesDao practicesDao = mock(PracticesDao.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
    PracticesService practicesService = new PracticesService(customerDao, practicesDao);

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

    private void givenSomePractices(String... practicesNames) {
        List<Practice> practices = new ArrayList<>();
        for (String practiceName : practicesNames) {
            practices.add(new Practice(practiceName, 1, "a", "b"));
        }
        when(practicesDao.findAllPractices()).thenReturn(practices);
    }

    private void givenARegisteredUser() {
        when(customerDao.isCustomerRegistered(VALID_USER_NAME)).thenReturn(true);
    }
}
