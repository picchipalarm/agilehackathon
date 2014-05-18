package com.agilehackathon.twillio;// You may want to be more specific in your imports

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Twilio {

//    public static boolean SEND_REAL_SMS = false;
    public static boolean SEND_REAL_SMS = true;

    // Find your Account Sid and Token at twilio.com/user/account
//    public static final String ACCOUNT_SID = "AC3374424cfb4a941d959201092993ea0b";
//    public static final String AUTH_TOKEN = "0c0cbfdc8acbccf645de1436edb9bd9f";

    public static final String ACCOUNT_SID = "ACb6a829d4dd6b65e389e46db3845b9bb3";
    public static final String AUTH_TOKEN = "d511cd102e6d4276adcff9852692a454";

    public static void main(String[] args) throws TwilioRestException {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build the parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "+447473154763"));
        params.add(new BasicNameValuePair("From", "+15005550006"));
        params.add(new BasicNameValuePair("Body", "test2"));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message message = messageFactory.create(params);
        System.out.println(message.getSid());
    }

    public void sendSms(String phonenumber, String text) throws TwilioRestException {
        if(!SEND_REAL_SMS) {
            System.out.println("Here is where would send an sms to " + phonenumber + " saying:\n" + text);
            return;
        }

        System.out.println("Sending sms to " + phonenumber + " saying:\n" + text);
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build the parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("From", "+15005550006"));
        params.add(new BasicNameValuePair("From", "+441212851878"));
        params.add(new BasicNameValuePair("To", phonenumber));
        params.add(new BasicNameValuePair("Body", text));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        messageFactory.create(params);

        System.out.println("Sent sms to " + phonenumber + " saying:\n" + text);
    }
}