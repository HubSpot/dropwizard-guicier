package com.hubspot.dropwizard.guicier.objects;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.glassfish.jersey.server.ExtendedUriInfo;

@Provider
public class ContextInjectedFilter implements ContainerResponseFilter {

  public static final String RESPONSE_HEADER = "x-context-injected-filter";

  private final ComponentInvocationCounter counter;

  @Context
  private ExtendedUriInfo extendedUriInfo;

  @Inject
  public ContextInjectedFilter(ComponentInvocationCounter counter) {
    this.counter = counter;
  }

  @Override
  public void filter(
    ContainerRequestContext requestContext,
    ContainerResponseContext responseContext
  ) throws IOException {
    checkNotNull(extendedUriInfo, "@Context injected ExtendedUriInfo null");

    counter.inc(getClass().getSimpleName());
    responseContext.getHeaders().putSingle(RESPONSE_HEADER, "true");
  }
}
