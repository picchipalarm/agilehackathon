package com.agilehackathon.configuration;

import org.glassfish.jersey.server.ResourceConfig;

public class RestResourceConfig {

   private final Object service;
   private final ResourceConfig resourceConfig = new ResourceConfig();

   public RestResourceConfig(final Object service) {
       this.service = service;
   }

   public void registerServices() {
       resourceConfig.register(service);
   }

   public ResourceConfig resourceConfig() {
       return resourceConfig;
   }
}