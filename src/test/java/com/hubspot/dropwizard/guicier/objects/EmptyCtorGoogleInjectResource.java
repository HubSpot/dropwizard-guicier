package com.hubspot.dropwizard.guicier.objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

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
