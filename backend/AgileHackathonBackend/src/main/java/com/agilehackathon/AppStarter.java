package com.agilehackathon;

import com.agilehackathon.configuration.WebServer;
import org.eclipse.jetty.server.Server;

public class AppStarter {

    public static void main(String[] args) throws Exception {
        new WebServer(new Server()).start();
    }

}
