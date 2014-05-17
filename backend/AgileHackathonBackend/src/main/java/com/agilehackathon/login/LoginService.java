package com.agilehackathon.login;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("login")
public class LoginService {

    public static final int STATUS_OK = 200;
    public static final int STATUS_FORBIDDEN = 403;

    private final CustomerDao customerDao;

    public LoginService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @GET
    @Path("{username}")
    public Response loginWithUsername(@PathParam("username") String username) {
        System.out.println("Attempting to login customer with username: [" + username + "]");

        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if(customerRegistered){
            System.out.println("Login successful");
            return response(STATUS_OK);
        } else {
            System.out.println("Login failed");
            return response(STATUS_FORBIDDEN);
        }
    }

    private Response response(int status) {
        return Response.status(status).build();
    }


}
