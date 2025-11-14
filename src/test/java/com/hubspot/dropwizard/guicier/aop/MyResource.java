package com.hubspot.dropwizard.guicier.aop;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(MyResource.PATH)
public class MyResource {

  public static final String PATH = "/aop-rsrc";

  public static final String RESPONSE = "Hello, World!";

  @Inject
  public MyResource() {}

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @MyAnnotation
  public String sayHello() {
    return RESPONSE;
  }
}
