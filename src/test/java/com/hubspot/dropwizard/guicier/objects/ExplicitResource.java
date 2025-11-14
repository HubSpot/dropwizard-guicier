package com.hubspot.dropwizard.guicier.objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/explicit")
@Produces(APPLICATION_JSON)
public class ExplicitResource {

  private final ExplicitDAO dao;

  @Inject
  public ExplicitResource(ExplicitDAO dao) {
    this.dao = dao;
  }

  @GET
  @Path("/message")
  public String getMessage() {
    return dao.getMessage();
  }

  public ExplicitDAO getDAO() {
    return dao;
  }
}
