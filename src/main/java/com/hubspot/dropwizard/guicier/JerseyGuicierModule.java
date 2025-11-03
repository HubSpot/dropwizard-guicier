package com.hubspot.dropwizard.guicier;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.servlet.RequestScoped;
import com.hubspot.dropwizard.guicier.injection.InjectionManagerProvider;
import javax.servlet.ServletConfig;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ExtendedUriInfo;

/**
 * This supplements the bindings provided in {@link com.squarespace.jersey2.guice.JerseyGuiceModule}.
 */
public class JerseyGuicierModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(InjectionManager.class)
      .toProvider(InjectionManagerProvider.class)
      .in(Scopes.NO_SCOPE);
  }

  @Provides
  public Application providesApplication(InjectionManager injectionManager) {
    return injectionManager.getInstance(Application.class);
  }

  @Provides
  public Providers providesProviders(InjectionManager injectionManager) {
    return injectionManager.getInstance(Providers.class);
  }

  @Provides
  @RequestScoped
  public UriInfo providesUriInfo(InjectionManager injectionManager) {
    return injectionManager.getInstance(UriInfo.class);
  }

  @Provides
  @RequestScoped
  public HttpHeaders providesHttpHeaders(InjectionManager injectionManager) {
    return injectionManager.getInstance(HttpHeaders.class);
  }

  @Provides
  @RequestScoped
  public SecurityContext providesSecurityContext(InjectionManager injectionManager) {
    return injectionManager.getInstance(SecurityContext.class);
  }

  @Provides
  @RequestScoped
  public Request providesRequest(InjectionManager injectionManager) {
    return injectionManager.getInstance(Request.class);
  }

  @Provides
  public Configuration providesConfiguration(InjectionManager injectionManager) {
    return injectionManager.getInstance(Configuration.class);
  }

  @Provides
  @RequestScoped
  public ContainerRequestContext providesContainerRequestContext(
    InjectionManager injectionManager
  ) {
    return injectionManager.getInstance(ContainerRequestContext.class);
  }

  @Provides
  @RequestScoped
  public ExtendedUriInfo providesExtendedUriInfo(InjectionManager injectionManager) {
    return injectionManager.getInstance(ExtendedUriInfo.class);
  }

  @Provides
  public ResourceContext providesResourceContext(InjectionManager injectionManager) {
    return injectionManager.getInstance(ResourceContext.class);
  }

  @Provides
  public ServletConfig providesServletConfig(InjectionManager injectionManager) {
    return injectionManager.getInstance(ServletConfig.class);
  }
}
