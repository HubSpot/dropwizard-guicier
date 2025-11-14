package com.hubspot.dropwizard.guicier.objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

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
  public boolean isResolvableByGuice(@QueryParam("className") String className)
    throws ClassNotFoundException {
    Class<?> clazz = Class.forName(className);
    try {
      return injector.getInstance(clazz) != null;
    } catch (ConfigurationException e) {
      return false;
    }
  }
}
