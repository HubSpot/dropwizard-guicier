package com.hubspot.dropwizard.guicier;

import javax.servlet.ServletConfig;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Configuration;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ExtendedUriInfo;

import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.squarespace.jersey2.guice.JerseyModule;

/**
 * This supplements the bindings provided in {@link com.squarespace.jersey2.guice.JerseyGuiceModule}.
 */
public class JerseyGuicierModule extends JerseyModule {

  @Override
  protected void configure() {}

  @Provides
  public Configuration providesConfiguration(ServiceLocator serviceLocator) {
    return serviceLocator.getService(Configuration.class);
  }

  @Provides
  @RequestScoped
  public ContainerRequestContext providesContainerRequestContext(ServiceLocator serviceLocator) {
    return serviceLocator.getService(ContainerRequestContext.class);
  }

  @Provides
  @RequestScoped
  public ExtendedUriInfo providesExtendedUriInfo(ServiceLocator serviceLocator) {
    return serviceLocator.getService(ExtendedUriInfo.class);
  }

  @Provides
  public ResourceContext providesResourceContext(ServiceLocator serviceLocator) {
    return serviceLocator.getService(ResourceContext.class);
  }

  @Provides
  public ServletConfig providesServletConfig(ServiceLocator serviceLocator) {
    return serviceLocator.getService(ServletConfig.class);
  }
}
