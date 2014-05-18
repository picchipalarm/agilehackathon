package com.agliehackathon;

import com.agilehackathon.model.Practice;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

@RunWith(SpecRunner.class)
public class PracticeQueueStatusAcceptanceTest implements WithTestState {

    public static final int POSITION_1 = 1;
    public static final int POSITION_2 = 2;
    public static final int SERVING_0 = 0;
    private TestState yatspec = new TestState();
    private static final String USER_1 = "aram";
    private static final String USER_2 = "mattia";
    public static final String JOIN_URL = "http://localhost:8080/agilehackathon/rest/practices/{practiceId}/joinQueue/{username}";
    public static final String STATUS_URL = "http://localhost:8080/agilehackathon/rest/practices/{practiceId}/status/{username}";
    private HttpResponse httpResponse;
    private static final Practice PRACTICE_1 = null;
    private String username;
    private String responseContent;

    @Before
    public void setUp() throws Exception {
        resetAllQueues();
    }

    @Test
    public void returnsStatusForAQueue() throws Exception {
        givenARegisteredUsername(USER_1);
        givenTheJoinPracticeQueueServiceIsCalled();

        whenThePracticeStatusQueueServiceIsCalled();

        thenTheResponseContainsPracticeAndServing(POSITION_1, SERVING_0);
    }

    @Test
    public void returnsStatusMultipleUsersForAQueue() throws Exception {
        givenThereAlreadyIsACustomerOnTheQueue();

        givenARegisteredUsername(USER_2);
        givenTheJoinPracticeQueueServiceIsCalled();

        whenThePracticeStatusQueueServiceIsCalled();

        thenTheResponseContainsPracticeAndServing(POSITION_2, SERVING_0);
    }

    private void givenThereAlreadyIsACustomerOnTheQueue() throws IOException {
        givenARegisteredUsername(USER_1);
        givenTheJoinPracticeQueueServiceIsCalled();
    }

    private void thenTheResponseContainsPracticeAndServing(int position, int serving) throws JSONException {

        String expectedResponse =
                "{\n" +
                        "  \"position\": " + position + "\n" +
                        ",  \"serving\": " + serving + "\n" +
                        "}";
        yatspec.log("Expected Response", expectedResponse);
        yatspec.log("Actual Response", responseContent);
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }

    private void whenThePracticeStatusQueueServiceIsCalled() throws IOException {
        String url = StringUtils.replace(STATUS_URL, "{practiceId}", "1");
        url = StringUtils.replace(url, "{username}", username);
        yatspec.log("STATUS_URL", url);
        Response response = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
        httpResponse = response.returnResponse();
        responseContent = EntityUtils.toString(httpResponse.getEntity());
    }

    private void givenTheJoinPracticeQueueServiceIsCalled() throws IOException {
        String url = StringUtils.replace(JOIN_URL, "{practiceId}", "1");
        url = StringUtils.replace(url, "{username}", username);
        yatspec.log("JOIN_URL", url);
        Response response = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
    }

    private void givenARegisteredUsername(String user) {
        username = user;
        yatspec.log("username", username);
    }


    private void resetAllQueues() throws IOException {
        Request.Get("http://localhost:8080/agilehackathon/rest/practices/resetAllQueues")
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
    }

    @Override
    public TestState testState() {
        return yatspec;
    }
}
