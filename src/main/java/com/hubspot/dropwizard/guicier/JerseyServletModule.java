package com.hubspot.dropwizard.guicier;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.inject.Singleton;

public class JerseyServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    install(new JerseyGuiceModule(JerseyGuiceUtils.newServiceLocator()));

    bind(HK2Installer.class);
  }

  @Singleton
  public static class HK2Installer {

    /**
     * Keep the unused ServiceLocator param, this ensures that the
     * {@link com.squarespace.jersey2.guice.JerseyGuiceModule.ServiceLocatorProvider} has been invoked
     * before we call{@link JerseyGuiceUtils#install(Injector)} (the provider calls
     * {@link JerseyGuiceUtils#link(ServiceLocator, Injector)})
     */
    @Inject
    public HK2Installer(Injector injector, ServiceLocator locator) {
      JerseyGuiceUtils.install(injector);
    }
  }
}
