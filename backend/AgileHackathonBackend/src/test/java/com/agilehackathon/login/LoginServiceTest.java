package com.agilehackathon.login;

import com.agilehackathon.twillio.Twilio;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    public static final String VALID_USER_NAME = "ValidUserName";
    public static final String INVALID_USER_NAME = "InValidUserName";
    CustomerDao customerDao = mock(CustomerDao.class);
    Twilio twilio = mock(Twilio.class);
    LoginService loginService = new LoginService(customerDao, twilio);


    @Test
    public void respondsWith200WhenUsernameIsValid() throws Exception {

        when(customerDao.isCustomerRegistered(VALID_USER_NAME)).thenReturn(true);
        Response response = loginService.loginWithUsername(VALID_USER_NAME);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void respondsWith403WhenUsernameIsInvalid() throws Exception {

        when(customerDao.isCustomerRegistered(INVALID_USER_NAME)).thenReturn(false);
        Response response = loginService.loginWithUsername(INVALID_USER_NAME);
        assertThat(response.getStatus()).isEqualTo(403);
    }

}
