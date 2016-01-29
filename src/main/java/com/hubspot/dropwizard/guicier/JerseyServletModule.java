package com.hubspot.dropwizard.guicier;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.inject.Singleton;

public class JerseyServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    ServiceLocator locator = new ServiceLocatorDecorator(BootstrapUtils.newServiceLocator()) {

      @Override
      public void shutdown() {
        // don't shutdown, remove once jersey2-guice supports Jersey 2.21
      }
    };

    install(new BootstrapModule(locator));

    bind(HK2Linker.class);
  }

  @Singleton
  public static class HK2Linker {

    @Inject
    public HK2Linker(Injector injector, ServiceLocator locator) {
      BootstrapUtils.link(locator, injector);
      BootstrapUtils.install(locator);
    }
  }
}
