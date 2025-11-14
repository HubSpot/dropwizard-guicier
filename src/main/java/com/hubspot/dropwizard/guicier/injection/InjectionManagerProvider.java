package com.hubspot.dropwizard.guicier.injection;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.core.setup.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.jersey.internal.inject.InjectionManager;

@Singleton
public class InjectionManagerProvider implements Provider<InjectionManager> {

  private static final Map<Application, InjectionManager> INJECTION_MANAGERS_BY_APP =
    new HashMap<>();

  private final Application application;

  @Inject
  public InjectionManagerProvider(Environment environment) {
    this.application = environment.jersey().getResourceConfig();
  }

  public static void set(Application application, InjectionManager injectionManager) {
    INJECTION_MANAGERS_BY_APP.put(application, injectionManager);
  }

  @Override
  public InjectionManager get() {
    return checkNotNull(
      INJECTION_MANAGERS_BY_APP.get(application),
      "InjectionManager not set"
    );
  }
}
