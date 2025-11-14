package com.hubspot.dropwizard.guicier.injection;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.inject.InjectionManager;

/**
 * This links the active InjectionManager to the Guice InjectionManagerProvider,
 * to the active jakarta.ws.rs.core.Application, for use in the JerseyGuicierModule.
 */
@Provider
public class InjectionManagerProviderFeature implements Feature {

  private final InjectionManager injectionManager;

  @Inject
  public InjectionManagerProviderFeature(InjectionManager injectionManager) {
    this.injectionManager = injectionManager;
  }

  @Override
  public boolean configure(FeatureContext context) {
    Application application = injectionManager.getInstance(Application.class);
    InjectionManagerProvider.set(application, injectionManager);

    return true;
  }
}
