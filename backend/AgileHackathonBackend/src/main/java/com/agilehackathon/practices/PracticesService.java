package com.agilehackathon.practices;


import com.agilehackathon.login.CustomerDao;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("practices")
public class PracticesService {

    public static final int STATUS_OK = 200;
    public static final int STATUS_FORBIDDEN = 403;

    private final CustomerDao customerDao;
    private final PracticesDao practicesDao;

    public PracticesService(CustomerDao customerDao, PracticesDao practicesDao) {
        this.customerDao = customerDao;
        this.practicesDao = practicesDao;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Practice> getAll(@PathParam("username") String username, @Context HttpServletResponse response) throws IOException {
        System.out.println("Attempting to login customer with username: [" + username + "]");

        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if(customerRegistered){
            System.out.println("Access to practice service successful");
            return practicesDao.findAllPractices();
        } else {
            System.out.println("Access to practice service failed");
            response.setStatus(403);
            response.getOutputStream().close();
            return new ArrayList<>();
        }
    }

    private Response response(int status) {
        return Response.status(status).build();
    }


}
