package com.hubspot.dropwizard.guicier.objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Path("/jersey-context")
@Produces(APPLICATION_JSON)
public class JerseyContextResource {
  private final Injector injector;

  @Inject
  public JerseyContextResource(Injector injector) {
    this.injector = injector;
  }

  @GET
  @Path("/is-resolvable-by-guice")
  public boolean isResolvableByGuice(@QueryParam("className") String className) throws ClassNotFoundException {
    Class<?> clazz = Class.forName(className);
    try {
      return injector.getInstance(clazz) != null;
    } catch (ConfigurationException e) {
      return false;
    }
  }
}
