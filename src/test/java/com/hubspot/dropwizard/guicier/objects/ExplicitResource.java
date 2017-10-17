package com.hubspot.dropwizard.guicier.objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;

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
    public String getMessage () {
        return dao.getMessage();
    }

    public ExplicitDAO getDAO() {
        return dao;
    }

}
