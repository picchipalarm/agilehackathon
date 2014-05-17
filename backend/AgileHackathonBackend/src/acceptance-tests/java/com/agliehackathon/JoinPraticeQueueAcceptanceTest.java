package com.agliehackathon;

import com.agilehackathon.practices.Practice;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpecRunner.class)
public class JoinPraticeQueueAcceptanceTest implements WithTestState {

    private static final String REGISTERED_USER = "mattia";
    private static final String UNREGISTERED_USER = "noone";
    public static final String URI = "http://localhost:8080/agilehackathon/rest/practices/{practiceId}/joinQueue/{username}";
    private static final Practice PRACTICE_1 = null;
    private static final Practice PRACTICE_2 = null;
    private static final String USER_1 = "aram";
    private static final String USER_2 = "mattia";
    private TestState yatspec = new TestState();
    private HttpResponse httpResponse;
    private String username;
    private String responseContent;

    @Test
    public void returns403WhenUsernameNotRegistered() throws Exception {
        givenAUnregisteredUsername();

        whenTheJoinPracticeQueueServiceIsCalled();

        thenTheServiceRespondsWithStatus(403);

    }
    @Test
    public void returns500WhenUsernameAlreadyInQueue() throws Exception {

        givenARegisteredUsername(USER_1);

        whenTheJoinPracticeQueueServiceIsCalled();
        whenTheJoinPracticeQueueServiceIsCalled();

        thenTheServiceRespondsWithStatus(500);

    }

    @Test
    public void returnsQueuePositionAndPracticeWhenJoiningSuccessfully() throws Exception {
        givenARegisteredUsername(USER_1);

        whenTheJoinPracticeQueueServiceIsCalled();

        thenTheResponseContainsPracticeAndPosition(PRACTICE_1, 1);

    }
    @Test

    public void multipleUsersCanJoinQueueSuccessfully() throws Exception {
        givenARegisteredUsername(USER_1);

        whenTheJoinPracticeQueueServiceIsCalled();

        thenTheResponseContainsPracticeAndPosition(PRACTICE_1, 1);

        givenARegisteredUsername(USER_2);

        whenTheJoinPracticeQueueServiceIsCalled();

        thenTheResponseContainsPracticeAndPosition(PRACTICE_1, 2);
    }

    private void thenTheResponseContainsPracticeAndPosition(Practice practice, int position) {

        String expectedResponse =
                "  {\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Dr. Quinn Surgery\",\n" +
                "    \"address\": \"Hoxton\",\n" +
                "    \"location\": \"https://www.google.co.uk/maps/place/Hoxton+Square/@51.5273296,-0.0808067,17z/data=!3m1!4b1!4m2!3m1!1s0x48761cbadbc045ff:0x54292b8ccb0589c2\"\n" +
                "  }";
        yatspec.log("Expected Response", expectedResponse);
        yatspec.log("Actual Response", responseContent);
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }


    private void thenTheResponseContains(Practice practice1) {

    }

    @Override
    public TestState testState() {
        return yatspec;
    }

    private void givenAUnregisteredUsername() {
        username = UNREGISTERED_USER;
        yatspec.log("username", username);
    }

    private void givenARegisteredUsername(String user) {
        username = user;
        yatspec.log("username", username);
    }

    private void whenTheJoinPracticeQueueServiceIsCalled() throws IOException {
        String url = StringUtils.replace(URI, "{practiceId}", "1");
        url = StringUtils.replace(url, "{username}", "aram");
        yatspec.log("URI", url);
        Response response = Request.Get(url)
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
}
