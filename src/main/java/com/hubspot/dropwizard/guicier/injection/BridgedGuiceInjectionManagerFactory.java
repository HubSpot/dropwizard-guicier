package com.hubspot.dropwizard.guicier.injection;

import com.google.inject.Injector;
import javax.annotation.Priority;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import org.glassfish.jersey.inject.hk2.Hk2InjectionManagerFactory;
import org.glassfish.jersey.inject.hk2.ImmediateHk2InjectionManager;
import org.glassfish.jersey.internal.inject.Bindings;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.InjectionManagerFactory;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(15)
@ConstrainedTo(RuntimeType.SERVER)
public class BridgedGuiceInjectionManagerFactory implements InjectionManagerFactory {

  private static final Logger LOG = LoggerFactory.getLogger(
    BridgedGuiceInjectionManagerFactory.class
  );

  @Override
  public InjectionManager create(Object parent) {
    ImmediateHk2InjectionManager injectionManager =
      (ImmediateHk2InjectionManager) new Hk2InjectionManagerFactory().create(parent);

    Injector guiceInjector = new InjectorProvider().get();

    if (guiceInjector == null) {
      throw new IllegalStateException(
        "Failed to lookup guice injector from servlet context"
      );
    }

    // initialize HK2 guice-bridge
    GuiceBridge
      .getGuiceBridge()
      .initializeGuiceBridge(injectionManager.getServiceLocator());
    GuiceIntoHK2Bridge guiceBridge = injectionManager.getInstance(
      GuiceIntoHK2Bridge.class
    );
    guiceBridge.bridgeGuiceInjector(guiceInjector);

    injectionManager.register(
      Bindings.injectionResolver(
        new GuiceInjectionResolver(guiceInjector, injectionManager.getServiceLocator())
      )
    );
    injectionManager.register(Bindings.service(guiceInjector).to(Injector.class));

    LOG.debug("Guice Component Provider initialized");
    return injectionManager;
  }
}
