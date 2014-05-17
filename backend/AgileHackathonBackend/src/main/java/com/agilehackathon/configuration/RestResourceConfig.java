package com.agilehackathon.configuration;

import com.agilehackathon.login.LoginService;
import org.glassfish.jersey.server.ResourceConfig;

public class RestResourceConfig {

   private final LoginService loginService;
   private final ResourceConfig resourceConfig = new ResourceConfig();

   public RestResourceConfig(final LoginService loginService) {
       this.loginService = loginService;
   }

   public void registerServices() {
       resourceConfig.register(loginService);
   }

   public ResourceConfig resourceConfig() {
       return resourceConfig;
   }
}