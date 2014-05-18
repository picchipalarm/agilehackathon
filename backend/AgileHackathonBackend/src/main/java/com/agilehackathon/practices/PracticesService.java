package com.agilehackathon.practices;


import com.agilehackathon.login.CustomerDao;
import com.agilehackathon.model.Customer;
import com.agilehackathon.model.Practice;
import com.agilehackathon.model.PracticeCustomerQueue;
import com.agilehackathon.twillio.Twilio;
import com.twilio.sdk.TwilioRestException;

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

import static com.agilehackathon.practices.PracticeCustomerQueueDao.CustomerAlreadyJoinedException;

@Path("practices")
public class PracticesService {

    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_ERROR = 500;
    private static final String QUEUE_JOINED_CONFIRMATION_TEXT =
            "Quanda thanks you for joining the queue! Here's your second badge\n" +
                    "░░░░░░░░▄██▄░░░░░░▄▄░░\n" +
                    "░░░░░░░▐███▀░░░░░▄███▌\n" +
                    "░░▄▀░░▄█▀▀░░░░░░░░▀██░\n" +
                    "░█░░░██░░░░░░░░░░░░░░░\n" +
                    "█▌░░▐██░░▄██▌░░▄▄▄░░░▄\n" +
                    "██░░▐██▄░▀█▀░░░▀██░░▐▌\n" +
                    "██▄░▐███▄▄░░▄▄▄░▀▀░▄██\n" +
                    "▐███▄██████▄░▀░▄█████▌\n" +
                    "▐████████████▀▀██████░\n" +
                    "░▐████▀██████░░█████░░\n" +
                    "░░░▀▀▀░░█████▌░████▀░░\n" +
                    "░░░░░░░░░▀▀███░▀▀▀░░░░ ";


    private final CustomerDao customerDao;
    private final PracticesDao practicesDao;
    private final PracticeCustomerQueueDao practiceCustomerQueueDao;
    private Twilio twilio;

    public PracticesService(CustomerDao customerDao, PracticesDao practicesDao, PracticeCustomerQueueDao practiceCustomerQueueDao, Twilio twilio) {
        this.customerDao = customerDao;
        this.practicesDao = practicesDao;
        this.practiceCustomerQueueDao = practiceCustomerQueueDao;
        this.twilio = twilio;
    }


    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Practice> getAll(@PathParam("username") String username, @Context HttpServletResponse response) throws IOException {
        System.out.println("Attempting to login customer with username: [" + username + "]");

        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if (customerRegistered) {
            System.out.println("Access to practice service list successful");
            response.addHeader("Access-Control-Allow-Origin", "*");
            return practicesDao.findAllPractices();
        } else {
            System.out.println("Access to practice service list failed");
            response.setStatus(STATUS_FORBIDDEN);
            response.getOutputStream().close();
            return new ArrayList<>();
        }
    }

    @GET
    @Path("{practiceId}/joinQueue/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public JoinResponse joinPracticeQueue(@PathParam("username") String username, @PathParam("practiceId") Integer practiceId, @Context HttpServletResponse response) throws IOException, TwilioRestException {
        boolean customerRegistered = customerDao.isCustomerRegistered(username);
        if (!customerRegistered) {
            System.out.println("Access to practice service join failed");
            return error(response, STATUS_FORBIDDEN);
        }

        Practice practice = practicesDao.findPracticeById(practiceId);
        // TODO what if the practice doesnt exist?

        try {
            int position = practiceCustomerQueueDao.joinQueue(practice, username);
            System.out.println("Access to practice service join successful");
            System.out.println("Position " + position + ", practice id " + practice.getId() + ", practice name " + practice.getName());
            sendConfirmationToCustomer(username);
            return new JoinResponse(practice, position);
        } catch (CustomerAlreadyJoinedException e) {
            System.out.println("fail see exception message CustomerAlreadyJoinedException");
            return error(response, STATUS_ERROR);
        }

    }

    @GET
    @Path("{practiceId}/status/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public QueueStatusResponse practiceStatusQueue(@PathParam("username") String username, @PathParam("practiceId") Integer practiceId, @Context HttpServletResponse response) throws IOException {
        Practice practice = practicesDao.findPracticeById(practiceId);
        PracticeCustomerQueue queuebyPractice = practiceCustomerQueueDao.findQueuebyPractice(practice);

        Integer serving = queuebyPractice.getServing();

        int customerPositionInQueue = queuebyPractice.getCustomerPositionInQueue(username);
        System.out.println("practiceStatusQueue for practice: " + practiceId + " position: " + customerPositionInQueue + ", serving: " + serving);
        return new QueueStatusResponse(customerPositionInQueue, serving);
    }

    @GET
    @Path("resetAllQueues")
    public Response resetAllQueues() {
        System.out.println("Resetting all queues...");
        practiceCustomerQueueDao.resetAllQueues();
        System.out.println("Reset all queues");
        return Response.status(200).build();
    }

    @GET
    @Path("{practiceId}/serveNextCustomer")
    public Response serveNextCustomer(@PathParam("practiceId") Integer practiceId) {
        System.out.println("serving next customer for practice " + practiceId);
        Practice practice = practicesDao.findPracticeById(practiceId);
        PracticeCustomerQueue queuebyPractice = practiceCustomerQueueDao.findQueuebyPractice(practice);
        int serving = queuebyPractice.serveCustomer();
        System.out.println("Serving customer " + serving);
        return Response.status(200).build();
    }

    @GET
    @Path("/doNotCallThis/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doNotCallThis(@PathParam("username") String username) throws IOException, TwilioRestException {
        Customer customer = customerDao.findCustomerByUsername(username);
        twilio.sendSms(customer.getPhoneNumber(), "Oh boy, you shouldn't have done that!\n" +
                "    |\\   /|\n" +
                "      \\|_|/\n" +
                "      /. .\\\n" +
                "     =\\_Y_/=\n" +
                "      {>o<}");
        return Response.status(200).build();
    }


    private JoinResponse error(HttpServletResponse response, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getOutputStream().close();
        return null;
    }

    private void sendConfirmationToCustomer(String username) throws TwilioRestException {
        Customer customer = customerDao.findCustomerByUsername(username);
        twilio.sendSms(customer.getPhoneNumber(), QUEUE_JOINED_CONFIRMATION_TEXT);
    }

    public class QueueStatusResponse {

        private int serving;
        private int position;

        public QueueStatusResponse(int position, int serving) {
            this.serving = serving;
            this.position = position;
        }

        public int getServing() {
            return serving;
        }

        public int getPosition() {
            return position;
        }
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
