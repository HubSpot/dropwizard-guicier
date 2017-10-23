package com.hubspot.dropwizard.guicier.objects;

import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.glassfish.jersey.server.ExtendedUriInfo;

import com.google.common.collect.ImmutableSet;

public class HK2ContextBindings {
  // This list comes from https://stackoverflow.com/a/35868654
  public static final Set<Class<?>> SET = ImmutableSet.of(
    Application.class,
    javax.ws.rs.core.Configuration.class,
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
    ExtendedUriInfo.class);

  private HK2ContextBindings() {
    throw new AssertionError();
  }
}
