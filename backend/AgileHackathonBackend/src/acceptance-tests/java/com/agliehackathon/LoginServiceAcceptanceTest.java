package com.agliehackathon;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpecRunner.class)
public class LoginServiceAcceptanceTest implements WithTestState {
    private static final String REGISTERED_USER = "mattia";
    private static final String UNREGISTERED_USER = "noone";
    public static final String URI = "http://localhost:8080/agilehackathon/rest/login/";
    private TestState yatspec = new TestState();
    private HttpResponse httpResponse;
    private String username;

    @Test
    public void loginReturns200WhenRegisteredUsernameGiven() throws Exception {

        givenARegisteredUsername();

        whenTheLoginServiceIsCalled();

        thenTheServiceRespondsWithStatus(200);
    }

    @Test
    public void loginReturns403WhenUnregisteredUsernameGiven() throws Exception {

        givenAUnregisteredUsername();

        whenTheLoginServiceIsCalled();

        thenTheServiceRespondsWithStatus(403);
    }


    private void givenAUnregisteredUsername() {
        username = UNREGISTERED_USER;
        yatspec.log("username", username);
    }

    private void givenARegisteredUsername() {
        username = REGISTERED_USER;
        yatspec.log("username", username);
    }

    private void whenTheLoginServiceIsCalled() throws IOException {
        yatspec.log("URI", URI + username);
        httpResponse = Request.Get(URI + username)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnResponse();
    }

    private void thenTheServiceRespondsWithStatus(int status) {
        yatspec.log("httpResponse Code", httpResponse.getStatusLine().getStatusCode());
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(status);
    }

    @Override
    public TestState testState() {
        return yatspec;
    }
}
