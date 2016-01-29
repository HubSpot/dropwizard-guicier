package com.hubspot.dropwizard.guicier;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;

public class JerseyServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    ServiceLocator locator = new ServiceLocatorDecorator(BootstrapUtils.newServiceLocator());

    install(new BootstrapModule(locator));

    bind(HK2Linker.class);
  }

  public static class HK2Linker {

    @Inject
    public HK2Linker(Injector injector,ServiceLocator locator) {
      BootstrapUtils.link(locator, injector);
      BootstrapUtils.install(locator);
    }
  }
}
