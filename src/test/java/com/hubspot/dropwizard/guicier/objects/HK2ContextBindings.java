package com.hubspot.dropwizard.guicier.objects;

import com.google.common.collect.ImmutableSet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Providers;
import java.util.Set;
import org.glassfish.jersey.server.ExtendedUriInfo;

public class HK2ContextBindings {

  // This list comes from https://stackoverflow.com/a/35868654
  public static final Set<Class<?>> SET = ImmutableSet.of(
    Application.class,
    jakarta.ws.rs.core.Configuration.class,
    ContainerRequestContext.class,
    HttpHeaders.class,
    HttpServletRequest.class,
    HttpServletResponse.class,
    Providers.class,
    Request.class,
    ResourceContext.class,
    SecurityContext.class,
    ServletConfig.class,
    ServletContext.class,
    UriInfo.class,
    // Jersey-specific
    ExtendedUriInfo.class
  );

  private HK2ContextBindings() {
    throw new AssertionError();
  }
}
