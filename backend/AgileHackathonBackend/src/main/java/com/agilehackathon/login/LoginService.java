package com.agilehackathon.login;


import com.agilehackathon.model.Customer;
import com.agilehackathon.twillio.Twilio;
import com.twilio.sdk.TwilioRestException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("login")
public class LoginService {

    public static final int STATUS_OK = 200;
    public static final int STATUS_FORBIDDEN = 403;

    private final CustomerDao customerDao;
    private Twilio twilio;

    private static final String LOGIN_SUCCESSFUL =
            "Quanda says: Logged in! And here's your first badge\n" +
                    "            ,,,         ,,,\n" +
                    "          ;\"   ^;     ;'   \",\n" +
                    "          ;    s$$$$$$$s     ;\n" +
                    "          ,  ss$$$$$$$$$$s  ,'\n" +
                    "          ;s$$$$$$$$$$$$$$$\n" +
                    "          $$$$$$$$$$$$$$$$$$\n" +
                    "         $$$$P\"\"Y$$$Y\"\"W$$$$$\n" +
                    "         $$$$  p\"$$$\"q  $$$$$\n" +
                    "         $$$$  .$$$$$.  $$$$\n" +
                    "          $$DcaU$$$$$$$$$$\n" +
                    "by          \"Y$$$\"*\"$$$Y\"    aka\n" +
                    "dcau            \"$b.$$\"     Panda";
 private static final String LOGIN_SUCCESSFUL_2 =
            "Quanda says: Logged in! And here's your first badge\n" +
                    "◖㈠ ω ㈠◗";

    public LoginService(CustomerDao customerDao, Twilio twilio) {
        this.customerDao = customerDao;
        this.twilio = twilio;
    }

    @GET
    @Path("{username}")
    public Response loginWithUsername(@PathParam("username") String username) throws TwilioRestException {
        System.out.println("Attempting to login customer with username: [" + username + "]");

        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if(customerRegistered){
            System.out.println("Login successful");
            sendLoggedIn(username);
            return response(STATUS_OK);
        } else {
            System.out.println("Login failed");
            return response(STATUS_FORBIDDEN);
        }
    }

    private Response response(int status) {
        return Response.status(status).build();
    }

    private void sendLoggedIn(String username) throws TwilioRestException {
        Customer customer = customerDao.findCustomerByUsername(username);
        twilio.sendSms(customer.getPhoneNumber(), LOGIN_SUCCESSFUL_2);
    }

}
