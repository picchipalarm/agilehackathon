package com.agilehackathon.practices;


import com.agilehackathon.login.CustomerDao;
import com.agilehackathon.model.Practice;

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

    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_ERROR = 500;

    private final CustomerDao customerDao;
    private final PracticesDao practicesDao;
    private final PracticeCustomerQueueDao practiceCustomerQueueDao;

    public PracticesService(CustomerDao customerDao, PracticesDao practicesDao, PracticeCustomerQueueDao practiceCustomerQueueDao) {
        this.customerDao = customerDao;
        this.practicesDao = practicesDao;
        this.practiceCustomerQueueDao = practiceCustomerQueueDao;
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
            response.setStatus(STATUS_FORBIDDEN);
            response.getOutputStream().close();
            return new ArrayList<>();
        }
    }

    private Response response(int status) {
        return Response.status(status).build();
    }

    @GET
    @Path("{practiceId}/joinQueue/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public JoinResponse joinPracticeQueue(@PathParam("username") String username, @PathParam("practiceId") Integer practiceId, @Context HttpServletResponse response) throws IOException {
        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if(!customerRegistered){
            System.out.println("Access to practice service failed");
            response.setStatus(STATUS_FORBIDDEN);
            response.getOutputStream().close();
            return null;
        }

        Practice practice = practicesDao.findPracticeById(practiceId);

        int position = 0;
        try {
            position = practiceCustomerQueueDao.joinQueue(practice, username);
        } catch (PracticeCustomerQueueDao.CustomerAlreadyJoinedException e) {
            System.out.println("fail see exception message");
            response.setStatus(STATUS_ERROR);
            response.getOutputStream().close();
            return null;
        }

        JoinResponse result = new JoinResponse(practice, position);
        return result;
    }

    public class JoinResponse {

        private Practice practice;
        private int position;

        public JoinResponse(Practice practice, int position) {
            this.practice = practice;
            this.position = position;
        }

        public Practice getPractice() {
            return practice;
        }

        public int getPosition() {
            return position;
        }
    }
}
