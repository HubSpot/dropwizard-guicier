package com.hubspot.dropwizard.guicier.bridge;

import com.google.inject.Injector;
import java.util.Set;
import javax.servlet.ServletContext;
import org.glassfish.jersey.inject.hk2.ImmediateHk2InjectionManager;
import org.glassfish.jersey.internal.inject.Bindings;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.spi.ComponentProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceComponentProvider implements ComponentProvider {

  private static final Logger LOG = LoggerFactory.getLogger(GuiceComponentProvider.class);

  private volatile Injector guiceInjector;

  @Override
  public void initialize(InjectionManager injectionManager) {
    InjectionManagerProvider.set(injectionManager);

    ServletContext sc = injectionManager.getInstance(ServletContext.class);
    if (sc != null) {
      guiceInjector = (Injector) sc.getAttribute(Injector.class.getName());
    }

    if (guiceInjector == null) {
      LOG.error("Failed to lookup guice injector from servlet context");
      return;
    }

    // initialize HK2 guice-bridge
    ImmediateHk2InjectionManager hk2InjectionManager =
      (ImmediateHk2InjectionManager) injectionManager;
    GuiceBridge
      .getGuiceBridge()
      .initializeGuiceBridge(hk2InjectionManager.getServiceLocator());
    GuiceIntoHK2Bridge guiceBridge = injectionManager.getInstance(
      GuiceIntoHK2Bridge.class
    );
    guiceBridge.bridgeGuiceInjector(guiceInjector);

    injectionManager.register(
      Bindings.injectionResolver(new GuiceInjectionResolver(guiceInjector))
    );
    injectionManager.register(Bindings.service(guiceInjector).to(Injector.class));

    LOG.debug("Guice Component Provider initialized");
  }

  @Override
  public boolean bind(Class<?> component, Set<Class<?>> providerContracts) {
    return false;
  }

  @Override
  public void done() {}
}
