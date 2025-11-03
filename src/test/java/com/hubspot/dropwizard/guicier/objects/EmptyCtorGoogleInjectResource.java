package com.hubspot.dropwizard.guicier.objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/empty-ctor-google-inject")
@Produces(APPLICATION_JSON)
public class EmptyCtorGoogleInjectResource {

  @Inject
  public EmptyCtorGoogleInjectResource() {}

  @GET
  public String hello() {
    return "world";
  }
}
