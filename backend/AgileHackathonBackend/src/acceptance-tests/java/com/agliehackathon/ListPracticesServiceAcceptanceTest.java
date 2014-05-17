package com.agliehackathon;

import com.agilehackathon.practices.Practice;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpecRunner.class)
public class ListPracticesServiceAcceptanceTest implements WithTestState {
    private static final String REGISTERED_USER = "mattia";
    private static final String UNREGISTERED_USER = "noone";
    public static final String URI = "http://localhost:8080/agilehackathon/rest/practices/";
    private static final Practice PRACTICE_1 = null;
    private static final Practice PRACTICE_2 = null;
    private TestState yatspec = new TestState();
    private HttpResponse httpResponse;
    private String username;
    private String responseContent;

    @Test
    public void practicesReturns200WhenRegisteredUsernameGiven() throws Exception {

        givenARegisteredUsername();

        whenThePracticesServiceIsCalled();

        thenTheServiceRespondsWithStatus(200);
    }

    @Test
    public void practicesReturns403WhenUnregisteredUsernameGiven() throws Exception {

        givenAUnregisteredUsername();

        whenThePracticesServiceIsCalled();

        thenTheServiceRespondsWithStatus(403);
    }

    @Test
    public void practicesReturnsAllPractices() throws Exception {

        givenARegisteredUsername();
        givenPractices(PRACTICE_1, PRACTICE_2);

        whenThePracticesServiceIsCalled();

        thenTheResponseContains(PRACTICE_1, PRACTICE_2);
    }

    private void givenPractices(Practice... practices) {

    }

    private void thenTheResponseContains(Practice... practices) throws JSONException {
        String expectedResponse = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Dr. Quinn Surgery\",\n" +
                "    \"address\": \"Hoxton\",\n" +
                "    \"location\": \"https://www.google.co.uk/maps/place/Hoxton+Square/@51.5273296,-0.0808067,17z/data=!3m1!4b1!4m2!3m1!1s0x48761cbadbc045ff:0x54292b8ccb0589c2\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"Doctor Dolittle\",\n" +
                "    \"address\": \"Old Street\",\n" +
                "    \"location\": \"https://www.google.co.uk/maps/place/Old+St/@51.5254642,-0.0879389,17z/data=!3m1!4b1!4m2!3m1!1s0x48761ca8abba80d9:0xd6cf02f1c545d61e\"\n" +
                "  }\n" +
                "]";
        yatspec.log("Expected Response", expectedResponse);
        yatspec.log("Actual Response", responseContent);
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }


    private void givenAUnregisteredUsername() {
        username = UNREGISTERED_USER;
        yatspec.log("username", username);
    }

    private void givenARegisteredUsername() {
        username = REGISTERED_USER;
        yatspec.log("username", username);
    }

    private void whenThePracticesServiceIsCalled() throws IOException {
        yatspec.log("URI", URI + username);
        Response response = Request.Get(URI + username)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
        httpResponse = response.returnResponse();
        responseContent = EntityUtils.toString(httpResponse.getEntity());
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
