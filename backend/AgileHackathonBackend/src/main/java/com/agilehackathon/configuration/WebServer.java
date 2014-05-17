package com.agilehackathon.configuration;


import com.agilehackathon.login.CustomerDao;
import com.agilehackathon.login.LoginService;
import com.agilehackathon.status.StatusServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.IOException;

public class WebServer {

    private Server jetty;

    public WebServer(Server jetty) {
        this.jetty = jetty;
    }

    public void start() throws Exception {
        System.out.println("Starting....");
        setServerPort(8080);
        ServletContextHandler servletContextHandler = configureServlets("/");
        setServerHandlers(servletContextHandler);
        jetty.start();

    }

    private void setServerHandlers(final ServletContextHandler servletContextHandler) throws IOException {
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{servletHandler(servletContextHandler)});
        jetty.setHandler(handlers);
    }

    private void setServerPort(final int httpPort) {
        ServerConnector serverConnector = new ServerConnector(jetty);
        serverConnector.setPort(httpPort);
        jetty.addConnector(serverConnector);
    }


    private HandlerList servletHandler(final ServletContextHandler servletContextHandler) {
        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{servletContextHandler, new DefaultHandler()});
        return handlerList;
    }

    private ServletContextHandler configureServlets(final String servletContextPath) {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath(servletContextPath);

        configureStatusServlet(servletContextHandler);
        configureJersey(servletContextHandler);
        return servletContextHandler;
    }



    private void configureJersey(final ServletContextHandler servletContextHandler) {


        RestResourceConfig resourceConfig = new RestResourceConfig(new LoginService(new CustomerDao()));
        resourceConfig.registerServices();

        ServletHolder jersey = new ServletHolder(new ServletContainer(resourceConfig.resourceConfig()));
        jersey.setName("jersey service servlet");
        servletContextHandler.addServlet(jersey, "/agilehackathon/rest/*");
    }


    private void configureStatusServlet(final ServletContextHandler servletContextHandler) {
        servletContextHandler.addServlet(new ServletHolder(new StatusServlet()), "/agilehackathon/status");
    }

}
