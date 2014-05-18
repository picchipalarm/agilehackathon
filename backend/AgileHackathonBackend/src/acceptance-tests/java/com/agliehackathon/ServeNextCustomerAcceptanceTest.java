package com.agliehackathon;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

@RunWith(SpecRunner.class)
public class ServeNextCustomerAcceptanceTest implements WithTestState {

    private static final Integer PRACTICE_ID = 1;
    private static final String USERNAME_1 = "aram";
    private static final String USERNAME_2 = "mattia";
    private static final String USERNAME_3 = "hendrik";
    private TestState yatspec = new TestState();
    private HttpResponse httpResponse;
    private String responseContent;
    private int statusCode;

    @Test
    public void returns200AfterServingACustomer() throws Exception {
        givenACustomerInQueueForAPractice();

        whenTheNextCustomerIsServed();

        thenTheServiceReturns200();
    }

    @Test
    public void customerIsNotInTheQueueAnymoreAfterHavingBeenServed() throws Exception {
        givenMultipleCustomersAreInQueueForAPractice();

        whenTheNextCustomerIsServed();

        thenThePracticeIsServiceNextCustomer();
    }

    private void givenACustomerInQueueForAPractice() throws IOException {
        customerJoinsQueue(USERNAME_1);
    }

    private void givenMultipleCustomersAreInQueueForAPractice() throws IOException {
        customerJoinsQueue(USERNAME_1);
        customerJoinsQueue(USERNAME_2);
        customerJoinsQueue(USERNAME_3);
    }

    private void customerJoinsQueue(String username) throws IOException {
        String url = "http://localhost:8080/agilehackathon/rest/practices/" + PRACTICE_ID + "/joinQueue/" + username;
        Response response = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
        httpResponse = response.returnResponse();
        responseContent = EntityUtils.toString(httpResponse.getEntity());
        yatspec.log("Join Queue Response For Customer " + username, responseContent);
    }

    private void whenTheNextCustomerIsServed() throws IOException {
        String url = "http://localhost:8080/agilehackathon/rest/practices/" + PRACTICE_ID + "/serveNextCustomer";
        Response response = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
        httpResponse = response.returnResponse();
        responseContent = EntityUtils.toString(httpResponse.getEntity());
        statusCode = httpResponse.getStatusLine().getStatusCode();
        yatspec.log("Serve Next Customer Response Code", statusCode);
        yatspec.log("Serve Next Customer Full Response", responseContent);
    }

    private void thenTheServiceReturns200() {
        assertThat(statusCode).isEqualTo(200);
    }

    private void thenThePracticeIsServiceNextCustomer() throws IOException {
        String url = "http://localhost:8080/agilehackathon/rest/practices/" + PRACTICE_ID + "/status/" + USERNAME_1;
        Response response = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute();
        httpResponse = response.returnResponse();
        responseContent = EntityUtils.toString(httpResponse.getEntity());
        statusCode = httpResponse.getStatusLine().getStatusCode();
        yatspec.log("Practice Queue Status - status code", statusCode);
        yatspec.log("Practice Queue Status - Full Response", responseContent);

        assertThat(responseContent).contains("serving: 2");
    }

    @Override
    public TestState testState() {
        return yatspec;
    }

}
