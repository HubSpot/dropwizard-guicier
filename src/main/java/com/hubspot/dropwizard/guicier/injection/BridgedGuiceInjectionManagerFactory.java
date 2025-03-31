package com.hubspot.dropwizard.guicier.injection;

import java.util.Optional;

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

import com.google.inject.Injector;

@Priority(15)
@ConstrainedTo(RuntimeType.SERVER)
public class BridgedGuiceInjectionManagerFactory implements InjectionManagerFactory {

  private static final Logger LOG = LoggerFactory.getLogger(
    BridgedGuiceInjectionManagerFactory.class
  );

  @Override
  public InjectionManager create(Object parent) {
    ImmediateHk2InjectionManager injectionManager =
      (ImmediateHk2InjectionManager) new Hk2InjectionManagerFactory()
        .create(getHk2Parent(parent));

    Optional<Injector> guiceInjectorMaybe = InjectorProvider.getMaybe();

    if (guiceInjectorMaybe.isEmpty()) {
      LOG.warn("No guice injector is set");
      return injectionManager;
    }

    Injector guiceInjector = guiceInjectorMaybe.get();

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
    return new BridgedGuiceInjectionManager(injectionManager, guiceInjector);
  }

  private static Object getHk2Parent(Object parent) {
    if (parent instanceof DelegatingInjectionManager) {
      DelegatingInjectionManager injectionManager = (DelegatingInjectionManager) parent;
      return injectionManager.getDelegate();
    }
    return parent;
  }
}
